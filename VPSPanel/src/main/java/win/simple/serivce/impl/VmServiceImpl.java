package win.simple.serivce.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import win.simple.*;
import win.simple.dao.ProductDao;
import win.simple.dao.UserDao;
import win.simple.dao.VmDao;
import win.simple.entity.*;
import win.simple.serivce.VmService;
import win.simple.util.Tools;
import win.simple.util.thread.CloneStoragectl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VmServiceImpl implements VmService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private VmDao vmDao;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public StateEntity power(String token, String vmUUID, int type) {
        StateEntity stateEntity = new StateEntity();
        if(isVmSubordinate(token, vmUUID)) {
            // 检测是不是在克隆磁盘
            CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
            if(null == cloneStoragectl) {
                stateEntity.setState(200);
                stateEntity.setMsg("ok");
                VmPower vmPower = new VmPower();
                switch (type) {
                    case 1:
                        stateEntity.setData(vmPower.startVM(vmUUID));
                        break;
                    case 2:
                        stateEntity.setData(vmPower.powerOff(vmUUID));
                        break;
                    case 3:
                        stateEntity.setData(vmPower.sleepVM(vmUUID));
                        break;
                    default:

                }
                return stateEntity;
            }
        }
        stateEntity.setState(400);
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity portMapperList(String token, String vmUUID) {
        StateEntity stateEntity = new StateEntity();
        if(isVmSubordinate(token, vmUUID)) {
            VmInfo vmInfo = new VmInfo();
            JSONObject VmInfoJSON = new JSONObject(vmInfo.showVmInfo(vmUUID));

            // 查询所有的映射端口
            List<PortMapperEntity> portMapperEntityList = new ArrayList<>();
            Iterator<String> iterator = VmInfoJSON.keys();
            while(iterator.hasNext()) {
                String key = iterator.next();
                // 检查是否存在Forwarding
                if(key.contains("Forwarding")) {
                    String forwarding = VmInfoJSON.getString(key);
                    String[] forwardingInfos = forwarding.split(",");
                    if(forwardingInfos.length >= 6) {
                        PortMapperEntity portMapperEntity = new PortMapperEntity();
                        portMapperEntity.setName(forwardingInfos[0]);
                        portMapperEntity.setAgreement(forwardingInfos[1]);
                        portMapperEntity.setExternalPort(forwardingInfos[3]);
                        portMapperEntity.setIntranetPort(forwardingInfos[5]);
                        portMapperEntityList.add(portMapperEntity);
                    }
                }
            }
            stateEntity.setState(200);
            stateEntity.setMsg("ok");
            stateEntity.setData(portMapperEntityList);
            return stateEntity;
        }
        stateEntity.setState(400);
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity addPortMapper(String token, String vmUUID, String name, int agreement, int intranetPort, int externalPort) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(400);
        if(isVmSubordinate(token, vmUUID)) {
            // 查询该外网端口是否被占用
            Integer occupyPortUserId = vmDao.selectOccupyPort(externalPort);
            if(null == occupyPortUserId) {
                Integer natPort = productDao.getNatPort(vmUUID);
                if(null != natPort && natPort > 0) {
                    Integer portRangeMin = vmDao.portrangemin();
                    Integer portRangeMax = vmDao.portrangemax();
                    if(null != portRangeMin && null != portRangeMax) {
                        if(externalPort >= portRangeMin && externalPort <= portRangeMax) {
                            String randomName = Tools.getRandomString(4);
                            if(isVmRunning(vmUUID)) {
                                VmControl vmControl = new VmControl();
                                vmControl.addPortMapper(vmUUID, randomName + "_" + name, agreement, intranetPort, externalPort);
                            } else {
                                VmModify vmModify = new VmModify();
                                vmModify.addPortMapper(vmUUID, randomName + "_" + name, agreement, intranetPort, externalPort);
                            }

                            String username = (String) redisTemplate.opsForValue().get(token);
                            Integer userid = userDao.isExistenceTwo(username);
                            vmDao.addOccupyPort(externalPort, userid, randomName + "_" + name);

                            productDao.setNatPort(vmUUID, natPort - 1);

                            stateEntity.setState(200);
                            stateEntity.setMsg("ok");
                            return stateEntity;
                        } else {
                            stateEntity.setMsg("创建失败，你创建的外网端口范围必须在" + portRangeMin + "~" + portRangeMax + "之间");
                        }
                    } else {
                        stateEntity.setMsg("端口创建失败，请联系管理员");
                    }
                } else {
                    stateEntity.setMsg("你无法创建更多的端口了");
                }
            } else {
                stateEntity.setMsg("该外网端口已被占用，请换个外网端口再试");
            }
        } else {
            stateEntity.setMsg("你没有权限执行此操作");
        }
        return stateEntity;
    }

    @Override
    public StateEntity deletePortMapper(String token, String vmUUID, String name) {
        StateEntity stateEntity = new StateEntity();
        if(isVmSubordinate(token, vmUUID)) {
            Integer natPort = productDao.getNatPort(vmUUID);
            if(null != natPort) {
                if(isVmRunning(vmUUID)) {
                    VmControl vmControl = new VmControl();
                    vmControl.deletePortMapper(vmUUID, name);
                } else {
                    VmModify vmModify = new VmModify();
                    vmModify.deletePortMapper(vmUUID, name);
                }
                vmDao.deleteOccupyPort(name);
                productDao.setNatPort(vmUUID, natPort + 1);
            }
            stateEntity.setState(200);
            stateEntity.setMsg("ok");
            return stateEntity;
        }
        stateEntity.setState(400);
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity osList(String token, String vmUUID) {
        StateEntity stateEntity = new StateEntity();
        if(isVmSubordinate(token, vmUUID)) {
            stateEntity.setState(200);
            stateEntity.setMsg("ok");
            stateEntity.setData(vmDao.osList());
            return stateEntity;
        }
        stateEntity.setState(400);
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity changeOs(String token, String vmUUID, int osId) {
        StateEntity stateEntity = new StateEntity();
        if(isVmSubordinate(token, vmUUID)) {
            if(!isVmRunning(vmUUID)) {
                OsEntity osEntity = vmDao.selectos(osId);
                CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
                if(null == cloneStoragectl) {
                    if(null != osEntity) {
                        VmInfo vmInfo = new VmInfo();
                        JSONObject VmInfoJSON = new JSONObject(vmInfo.showVmInfo(vmUUID));
                        String vmName = VmInfoJSON.getString("name");

                        VmStoragectl vmStoragectl = new VmStoragectl();
                        vmStoragectl.cancelStorageattach(vmUUID, "sata", 0);
                        vmStoragectl.deleteStorageattach(vmDao.dataPath() + "VirtualBox VMs\\" + vmName + "\\" + vmName + ".vdi");

                        VmModify vmModify = new VmModify();
                        vmModify.setOsType(vmUUID, osEntity.getOstype());

                        addClone(vmUUID, osEntity.getOsvdi(), vmDao.dataPath() + "VirtualBox VMs\\" + vmName + "\\" + vmName + ".vdi");

                        stateEntity.setState(200);
                        stateEntity.setMsg("ok");
                        return stateEntity;
                    } else {
                        stateEntity.setMsg("没有找到此系统");
                    }
                } else {
                    stateEntity.setMsg("正在创建系统中，无法执行此操作");
                }
            } else {
                stateEntity.setMsg("请关闭服务器后在重装系统");
            }
        } else {
            stateEntity.setMsg("你没有权限执行此操作");
        }
        stateEntity.setState(400);
        return stateEntity;
    }


    /**
     * 判断该虚拟机是否属于该用户
     * @param token
     * @param vmUUID
     * @return
     */
    public boolean isVmSubordinate(String token, String vmUUID) {
        String username = (String) redisTemplate.opsForValue().get(token);
        if(null != username) {
            Integer inspectUserId = userDao.isExistenceTwo(username);
            if (null != inspectUserId) {
                Integer requestUserId = productDao.vmOwner(vmUUID);
                // 检查请求的用户ID和虚拟机所属者的ID是否一致
                if(null != requestUserId && requestUserId.equals(inspectUserId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查指定虚拟机是否运行
     * @param vmUUID
     * @return
     */
    public boolean isVmRunning(String vmUUID) {
        VmInfo vmInfo = new VmInfo();
        JSONObject runningVmsJsonObject = new JSONObject(vmInfo.runningVms());
        JSONArray runningVmsJsonArray = runningVmsJsonObject.getJSONArray("runningvms");
        boolean isRunning = false;
        if(null != runningVmsJsonArray && runningVmsJsonArray.length() > 0) {
            for(int i = 0; i < runningVmsJsonArray.length(); i++) {
                String inspectUUID = runningVmsJsonArray.getJSONObject(i).getString("uuid");
                if(vmUUID.equals(inspectUUID)) {
                    isRunning = true;
                    break;
                }
            }
        }
        if(isRunning) {
            return true;
        }
        return false;
    }

    @Override
    public VmConfigureEntity getVmInfo(int configureid) {
        VmConfigureEntity vmConfigureEntity = productDao.configure(configureid);
        if(null != vmConfigureEntity) {
            VmInfo vmInfo = new VmInfo();
            String vmUuid = vmConfigureEntity.getVmuuid();

            JSONObject VmInfoJson = new JSONObject(vmInfo.showVmInfo(vmUuid));
            vmConfigureEntity.setCpu(Integer.parseInt(VmInfoJson.getString("cpus")));
            vmConfigureEntity.setMemory(Integer.parseInt(VmInfoJson.getString("memory")));
            vmConfigureEntity.setCpuperformance(Integer.parseInt(VmInfoJson.getString("cpuexecutioncap")));
            vmConfigureEntity.setOstype(VmInfoJson.getString("ostype"));
            vmConfigureEntity.setName(VmInfoJson.getString("name"));

            return vmConfigureEntity;
        }
        return null;
    }

    /**
     * 添加磁盘克隆
     * @param cloneDivFilePath
     * @param ResultFilePath
     */
    @Override
    public void addClone(String uuid, String cloneDivFilePath, String ResultFilePath) {
        redisTemplate.opsForValue().set("vm/" + uuid + "/clonediv", "true");
        new CloneStoragectl().exec(uuid, cloneDivFilePath, ResultFilePath, redisTemplate);
    }

}
