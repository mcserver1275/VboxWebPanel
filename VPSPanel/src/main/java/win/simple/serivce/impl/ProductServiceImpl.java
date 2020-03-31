package win.simple.serivce.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import win.simple.*;
import win.simple.dao.ProductDao;
import win.simple.dao.UserDao;
import win.simple.dao.VmDao;
import win.simple.dao.WebDao;
import win.simple.entity.*;
import win.simple.serivce.ProductService;
import win.simple.serivce.VmService;
import win.simple.util.Base64;
import win.simple.util.Tools;
import win.simple.util.thread.CloneStoragectl;
import win.simple.vmenum.StoragectlType;

import java.util.Calendar;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private WebDao webDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private VmDao vmDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VmService vmService;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public StateEntity buyvm(String token, int paymod, int exampleId, int buydate) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(400);
        String username = (String) redisTemplate.opsForValue().get(token);
        if(null != username) {
            UserEntity userEntity = userDao.selectUser(username);
            ExampleEntity exampleEntity = productDao.example(exampleId);
            // 获取用户存款
            float userDeposit = userEntity.getDeposit();
            // 获取实例余额
            float price = exampleEntity.getPrice();
            // 计算后的价格
            float calculatedPrice = 0;
            // 月付费
            if(paymod == 1) {
                calculatedPrice = price * buydate;
                //年付费
            } else if(paymod == 2) {
                calculatedPrice = (price * 12) * buydate;
            }
            if(userDeposit >= calculatedPrice) {
                stateEntity.setState(200);
                VmBuild vmBuild = new VmBuild();
                // 构建一个虚拟机，并且生成10位随机字符串作为名字
                String newVmName = Tools.getRandomString(10);
                JSONObject jsonObject = new JSONObject(vmBuild.createVm(newVmName));
                String newVmUuid = jsonObject.getString("UUID");
                if(null != newVmUuid) {
                    // 修改虚拟机配置
                    VmModify vmModify = new VmModify();
                    vmModify.setMemory(newVmUuid, exampleEntity.getMemory());
                    vmModify.setCpus(newVmUuid, exampleEntity.getCpus());
                    vmModify.setCpuExecutioncap(newVmUuid, exampleEntity.getCpuexecutioncap());
                    vmModify.bridging(newVmUuid);

                    OsEntity osEntity = vmDao.selectos(exampleEntity.getOsid());
                    vmModify.setOsType(newVmUuid, osEntity.getOstype());

                    //创建存储控制器
                    VmStoragectl vmStoragectl = new VmStoragectl();
                    vmStoragectl.createStoragectl(newVmUuid, "sata", StoragectlType.SATA);
                    //克隆系统盘 (如果被克隆的虚拟机运行将无法克隆)
                    vmService.addClone(newVmUuid, osEntity.getOsvdi(), vmDao.dataPath() + "VirtualBox VMs\\" + newVmName + "\\" + newVmName + ".vdi");

                    String payModeStr = paymod == 1 ? "月付费" : "年付费";
                    Calendar nowTime = Calendar.getInstance();
                    if(paymod == 1) {
                        nowTime.add(Calendar.MONTH, buydate);
                    } else if(paymod == 2) {
                        nowTime.add(Calendar.YEAR, buydate);
                    }
                    // 创建用户的虚拟机配置表
                    productDao.createConfigure(newVmUuid,
                            userDao.isExistenceTwo(username), payModeStr,
                            nowTime.getTimeInMillis() / 1000,
                            System.currentTimeMillis() / 1000,
                            exampleEntity.getName(),
                            exampleEntity.getDefalutnatport());

                    // 修改用户存款
                    userDao.setDeposit(username, userDeposit - calculatedPrice);
                }
                stateEntity.setMsg("ok");
            } else {
                stateEntity.setMsg("购买失败，你的余额不足");
            }
            return stateEntity;
        }
        stateEntity.setMsg("抱歉你没有登录");
        return stateEntity;
    }

    @Override
    public StateEntity exampleList() {
        List<ExampleEntity> exampleEntities = productDao.exampleList();
        StateEntity stateEntity = new StateEntity();
        if(null != exampleEntities) {
            if(exampleEntities.size() > 0) {
                stateEntity.setState(200);
                stateEntity.setData(exampleEntities);
                stateEntity.setMsg("ok");
            } else {
                stateEntity.setState(204);
                stateEntity.setMsg("没有可以用的实例");
            }
            return stateEntity;
        }
        stateEntity.setState(400);
        stateEntity.setMsg("无法获取实例列表");
        return stateEntity;
    }

    @Override
    public StateEntity myResources(String token) {
        String username = userService.tokenUserName(token);
        StateEntity stateEntity = new StateEntity();
        if(null != username) {
            Integer userid = userDao.isExistenceTwo(username);
            if(null != userid) {
                List<VmConfigureEntity> vmConfigureEntityList = productDao.configureList(userid);
                VmInfo vmInfo = new VmInfo();
                for(int i = 0; i < vmConfigureEntityList.size(); i++) {
                    VmConfigureEntity vmConfigureEntity = vmConfigureEntityList.get(i);
                    String vmUUID = vmConfigureEntity.getVmuuid();
                    if(null != vmUUID && !vmUUID.equals("")) {
                        JSONObject jsonObject = new JSONObject(vmInfo.showVmInfo(vmConfigureEntity.getVmuuid()));
                        if(jsonObject.length() > 0) {
                            vmConfigureEntity.setCpu(Integer.parseInt(jsonObject.getString("cpus")));
                            vmConfigureEntity.setMemory(Integer.parseInt(jsonObject.getString("memory")));
                            vmConfigureEntity.setState(jsonObject.getString("VMState"));
                            vmConfigureEntity.setCpuperformance(Integer.parseInt(jsonObject.getString("cpuexecutioncap")));

                            CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
                            if(null != cloneStoragectl) {
                                vmConfigureEntity.setState("create");
                            } else {
                                vmConfigureEntity.setState(jsonObject.getString("VMState"));
                            }
                        }
                    }

                }
                stateEntity.setState(200);
                stateEntity.setMsg("ok");
                stateEntity.setData(vmConfigureEntityList);
                return stateEntity;
            }
        }
        stateEntity.setState(400);
        stateEntity.setMsg("无法获取资源列表");
        return stateEntity;
    }

    @Override
    public StateEntity vmInfo(String token, String vmUUID) {
        Integer requestUserId = productDao.vmOwner(vmUUID);
        StateEntity stateEntity = new StateEntity();
        if(null != requestUserId) {
            String username = (String) redisTemplate.opsForValue().get(token);
            Integer inspectUserId = userDao.isExistenceTwo(username);
            if(null != inspectUserId) {
                // 检查请求的用户ID和虚拟机所属者的ID是否一致
                if(requestUserId.equals(inspectUserId)) {
                    VmInfo vmInfo = new VmInfo();
                    JSONObject VmInfoJSON = new JSONObject(vmInfo.showVmInfo(vmUUID));
                    VmInfoEntity vmInfoEntity = new VmInfoEntity();
                    vmInfoEntity.setCpu(Integer.parseInt(VmInfoJSON.getString("cpus")));
                    vmInfoEntity.setMemory(Integer.parseInt(VmInfoJSON.getString("memory")));
                    vmInfoEntity.setCpuperformance(Integer.parseInt(VmInfoJSON.getString("cpuexecutioncap")));
                    vmInfoEntity.setUUID(VmInfoJSON.getString("UUID"));
                    vmInfoEntity.setVMStateChangeTime(VmInfoJSON.getString("VMStateChangeTime"));
                    vmInfoEntity.setName(VmInfoJSON.getString("name"));
                    vmInfoEntity.setOstype(VmInfoJSON.getString("ostype"));

                    CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
                    if(null != cloneStoragectl) {
                        vmInfoEntity.setVMState("create");
                        // 设置创建进度
                        vmInfoEntity.setCurrcreateprogress(cloneStoragectl.currProgress);
                    } else {
                        vmInfoEntity.setVMState(VmInfoJSON.getString("VMState"));
                    }

                    // 查询产品虚拟机（configure表的内容）
                    VmConfigureEntity vmConfigureEntity = productDao._configure(vmUUID);
                    vmInfoEntity.setPayment(vmConfigureEntity.getPayment());
                    vmInfoEntity.setTime(vmConfigureEntity.getTime());
                    vmInfoEntity.setCreatetime(vmConfigureEntity.getCreatetime());
                    vmInfoEntity.setExamplename(vmConfigureEntity.getExamplename());


                    // 查询虚拟机运行状态
                    String vmstate = VmInfoJSON.getString("VMState");
                    if(null != vmstate && !vmstate.equals("poweroff")) {
                        VmControl vmControl = new VmControl();
                        vmControl.screenshotPng(vmUUID, vmDao.dataPath() + "temp");
                        vmInfoEntity.setBase64Snapshot(Base64.imageToBase64ByLocal(vmDao.dataPath() + "temp\\" + vmUUID + ".png"));
                    }

                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                    stateEntity.setData(vmInfoEntity);
                    return stateEntity;
                }
            }
        }
        stateEntity.setState(400);
        stateEntity.setMsg("无法获取资源信息");
        return stateEntity;
    }
}
