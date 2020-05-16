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
import win.simple.util.execption.BusinessException;
import win.simple.util.thread.CloneStoragectl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VmServiceImpl implements VmService {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private VmDao vmDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private VmInfo vmInfo;
    @Autowired
    private VmControl vmControl;
    @Autowired
    private VmModify vmModify;
    @Autowired
    private VmPower vmPower;
    @Autowired
    private VmStoragectl vmStoragectl;
    @Autowired
    private VmGuestControl vmGuestControl;

    @Override
    public StateEntity power(String token, String vmUUID, int type) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(400);

        if(this.isVmSubordinate(token, vmUUID)) {
            // 检测是不是在克隆磁盘
            CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
            if(null == cloneStoragectl) {
                // 检测是否暂停使用
                Integer useState = productDao.getUseState(vmUUID);
                if(null != useState) {
                    if(useState == 1) {
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
                    } else {
                        stateEntity.setMsg("你的机器已被暂停使用");
                        return stateEntity;
                    }
                }
            }
        }
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity portMapperList(String token, String vmUUID) {
        StateEntity stateEntity = new StateEntity();
        if(this.isVmSubordinate(token, vmUUID)) {
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
        if(this.isVmSubordinate(token, vmUUID)) {
            Integer useState = productDao.getUseState(vmUUID);
            if(null != useState) {
                if(useState == 1) {
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
                                        vmControl.addPortMapper(vmUUID, randomName + "_" + name, agreement, intranetPort, externalPort);
                                    } else {
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
                    stateEntity.setMsg("你的机器已被暂停使用");
                }
            }
        } else {
            stateEntity.setMsg("你没有权限执行此操作");
        }
        return stateEntity;
    }

    @Override
    public StateEntity deletePortMapper(String token, String vmUUID, String name) {
        StateEntity stateEntity = new StateEntity();
        if(this.isVmSubordinate(token, vmUUID)) {
            Integer natPort = productDao.getNatPort(vmUUID);
            if(null != natPort) {
                if(isVmRunning(vmUUID)) {
                    vmControl.deletePortMapper(vmUUID, name);
                } else {
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
        stateEntity.setState(400);

        Integer useState = productDao.getUseState(vmUUID);
        if(null != useState) {
            if(useState == 1) {
                if(isVmSubordinate(token, vmUUID)) {
                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                    stateEntity.setData(vmDao.osList());
                    return stateEntity;
                }
            } else {
                throw new BusinessException("你的机器已被暂停使用", 403);
            }
        }
        throw new BusinessException("你没有权限执行此操作", 403);
    }

    @Override
    public StateEntity changeOs(String token, String vmUUID, int osId) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(400);
        if(this.isVmSubordinate(token, vmUUID)) {
            // 使用状态判断
            Integer useState = productDao.getUseState(vmUUID);
            if(null != useState) {
                if(useState == 1) {
                    if(!isVmRunning(vmUUID)) {
                        // 关闭虚拟机电源（虽然已经关了，为了保险）
                        vmPower.powerOff(vmUUID);

                        OsEntity osEntity = vmDao.selectos(osId);
                        CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
                        if(null == cloneStoragectl) {
                            if(null != osEntity) {
                                JSONObject VmInfoJSON = new JSONObject(vmInfo.showVmInfo(vmUUID));
                                if(VmInfoJSON.length() == 0) {
                                    stateEntity.setMsg("机器不存在");
                                    return stateEntity;
                                }
                                String vmName = VmInfoJSON.getString("name");

                                vmStoragectl.cancelStorageattach(vmUUID, "sata", 0);
                                vmStoragectl.deleteStorageattach(vmDao.dataPath() + "VirtualBox VMs\\" + vmName + "\\" + vmName + ".vdi");

                                vmModify.setOsType(vmUUID, osEntity.getOstype());

                                productDao.setConfigureOsId(vmUUID, osId);

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
                    stateEntity.setMsg("你的机器已被暂停使用");
                }
            }
        } else {
            stateEntity.setMsg("你没有权限执行此操作");
        }
        return stateEntity;
    }

    @Override
    public StateEntity resetPass(String token, String vmUUID, String password) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(400);
        if(this.isVmSubordinate(token, vmUUID)) {
            // 使用状态判断
            Integer useState = productDao.getUseState(vmUUID);
            if(null != useState) {
                if(useState == 1) {
                    if(isVmRunning(vmUUID)) {
                        JSONObject infoJSON = new JSONObject(vmInfo.showVmInfo(vmUUID));
                        if(infoJSON.length() == 0) {
                            stateEntity.setMsg("机器不存在");
                            return stateEntity;
                        }

                        String osType = infoJSON.getString("ostype").toUpperCase();
                        VmGuestControl vmGuestControl = new VmGuestControl();


                        VmConfigureEntity vmConfigureEntity = productDao._configure(vmUUID);
                        OsEntity osEntity = vmDao.selectGuestUserNameAndPassword(vmConfigureEntity.getOsid());


                        String execStr = "";
                        String guestUserName = osEntity.getGuestusername();
                        String guestPassWord = osEntity.getGuestpassword();
                        if(osType.contains("WINDOWS")) {
                            execStr = vmGuestControl.exec(vmUUID, guestUserName, guestPassWord, "cmd.exe", "cmd /c net user Administrator " + password);
                        } else if(osType.contains("CENTOS")) {
                            execStr = vmGuestControl.exec(vmUUID,  guestUserName, guestPassWord, "/bin/sh", "sh /bin/resetpass.sh " + password);
                        } else {
                            execStr = vmGuestControl.exec(vmUUID,  guestUserName, guestPassWord, "/bin/sh", "sh /bin/resetpass.sh " + password);
                        }

                        if(!"".equals(execStr)) {
                            stateEntity.setState(200);
                            stateEntity.setMsg("ok");
                        } else {
                            stateEntity.setMsg("修改失败");
                        }


                        return stateEntity;

                    } else {
                        stateEntity.setMsg("请开启服务器后在修改密码");
                    }
                } else {
                    stateEntity.setMsg("你的机器已被暂停使用");
                }
            }
        } else {
            stateEntity.setMsg("你没有权限执行此操作");
        }
        return stateEntity;
    }


    @Override
    public boolean isVmSubordinate(String token, String vmUUID) {
        UserEntity userEntity = userService.isUserLogin(token);
        if(null != userEntity) {
            Integer requestUserId = productDao.vmOwner(vmUUID);
            // 检查请求的用户ID和虚拟机所属者的ID是否一致
            if(null != requestUserId && requestUserId.equals(userEntity.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
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
    public String getIntranetIp(String vmUuid) {
        // 查询内网IP（虚拟机必须安装增强）
        JSONArray jsonGuestPropertys = new JSONObject(vmGuestControl.guestProperty(vmUuid)).getJSONArray("guestproperty");
        String intranetIP = "";
        for(int i = 0; i < jsonGuestPropertys.length(); i++) {
            JSONObject jsonGuestProperty = jsonGuestPropertys.getJSONObject(i);
            if(jsonGuestProperty.length() >= 2) {
                if("/VirtualBox/GuestInfo/Net/0/V4/IP".equals(jsonGuestProperty.getString("Name"))) {
                    intranetIP = jsonGuestProperty.getString("value");
                }
            }
        }
        if(!"".equals(intranetIP)) {
            return intranetIP;
        }
        return null;
    }

    @Override
    public void addClone(String uuid, String cloneDivFilePath, String ResultFilePath) {
        redisTemplate.opsForValue().set("vm/" + uuid + "/clonediv", "true");
        new CloneStoragectl().exec(uuid, cloneDivFilePath, ResultFilePath, redisTemplate, vmPower);
    }

}
