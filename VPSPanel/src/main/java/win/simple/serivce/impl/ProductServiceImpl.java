package win.simple.serivce.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import win.simple.*;
import win.simple.dao.ProductDao;
import win.simple.dao.UserDao;
import win.simple.dao.VmDao;
import win.simple.entity.*;
import win.simple.serivce.ProductService;
import win.simple.serivce.VmService;
import win.simple.util.Base64;
import win.simple.util.Tools;
import win.simple.util.execption.BusinessException;
import win.simple.util.thread.CloneStoragectl;
import win.simple.vmenum.StoragectlType;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

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
    @Autowired
    private VmInfo vmInfo;
    @Autowired
    private VmBuild vmBuild;
    @Autowired
    private VmModify vmModify;
    @Autowired
    private VmStoragectl vmStoragectl;
    private HostInfoEntity hostInfoEntity;

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = null;
        VmInfo vminfo = null;
        try {
            objectMapper = new ObjectMapper();
            vminfo = new VmInfo();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.hostInfoEntity = objectMapper.readValue(vminfo.hostInfo(), HostInfoEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StateEntity buyvm(String token, int paymod, int exampleId, int buydate) {
        UserEntity loginUserEntity = userService.isUserLogin(token);
        if(null != loginUserEntity) {
            if(paymod > 0 && paymod <= 2 && buydate > 0 && buydate <= 12) {
                String username = loginUserEntity.getUsername();
                StateEntity stateEntity = new StateEntity();
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
                    // 构建一个虚拟机，并且生成10位随机字符串作为名字
                    String newVmName = Tools.getRandomString(10);
                    JSONObject jsonObject = new JSONObject(vmBuild.createVm(newVmName));
                    String newVmUuid = jsonObject.getString("UUID");
                    if(null != newVmUuid) {
                        // 修改虚拟机配置
                        vmModify.setMemory(newVmUuid, exampleEntity.getMemory());
                        vmModify.setCpus(newVmUuid, exampleEntity.getCpus());
                        vmModify.setCpuExecutioncap(newVmUuid, exampleEntity.getCpuexecutioncap());
                        // 创建虚拟机网卡（NAT上网，Intent内网）
                        vmModify.bridging(newVmUuid, 1, "nat");
                        vmModify.bridging(newVmUuid, 2, "intnet");

                        // 设置默认系统
                        OsEntity osEntity = vmDao.selectos(exampleEntity.getOsid());
                        String osType = osEntity.getOstype();
                        vmModify.setOsType(newVmUuid, osType);

                        // 创建随机外网远程端口
                        Integer randomMin = vmDao.portrangemin();
                        Integer randomMax = vmDao.portrangemax();
                        String randomName = Tools.getRandomString(4);
                        int randomResult = randomMin;
                        do {
                            randomResult = randomMin + (int)(Math.random() * ((randomMax - randomMin) + 1));
                        } while(null != vmDao.selectOccupyPort(randomResult));

                        if(osType.contains("WINDOWS")) {
                            vmModify.addPortMapper(newVmUuid, randomName + "_远程", 1, 3389, randomResult);
                        } else if(osType.contains("CENTOS")) {
                            vmModify.addPortMapper(newVmUuid, randomName + "_远程", 1, 22, randomResult);
                        }
                        vmDao.addOccupyPort(randomResult, userEntity.getId(), randomName + "_远程");

                        // 创建存储控制器
                        vmStoragectl.createStoragectl(newVmUuid, "sata", StoragectlType.SATA);
                        // 克隆系统盘 (如果被克隆的虚拟机运行将无法克隆)
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
                                userEntity.getId(),
                                payModeStr,
                                nowTime.getTimeInMillis() / 1000,
                                System.currentTimeMillis() / 1000,
                                exampleEntity.getName(),
                                exampleEntity.getDefalutnatport(),
                                1, exampleEntity.getOsid(),
                                exampleEntity.getId());

                        // 修改用户存款
                        userDao.setDeposit(username, userDeposit - calculatedPrice);
                    }
                    stateEntity.setMsg("ok");
                    return stateEntity;
                } else {
                    throw new BusinessException("购买失败，你的余额不足", 403);
                }
            } else {
                throw new BusinessException("参数有误", 400);
            }
        }
        throw new BusinessException("无法获取资源列表", 403);
    }

    @Override
    public StateEntity exampleList() {
        List<ExampleEntity> exampleEntities = productDao.exampleList();
        if(null != exampleEntities) {
            if(exampleEntities.size() > 0) {
                StateEntity stateEntity = new StateEntity();
                stateEntity.setState(200);
                stateEntity.setData(exampleEntities);
                stateEntity.setMsg("ok");
                return stateEntity;
            } else {
                throw new BusinessException("没有可以用的实例", 204);
            }
        }
        throw new BusinessException("无法获取实例列表", 400);
    }

    @Override
    public StateEntity myResources(String token) {
        UserEntity userEntity = userService.isUserLogin(token);
        if(null != userEntity) {
            List<VmConfigureEntity> vmConfigureEntityList = productDao.configureList(userEntity.getId());
            for(VmConfigureEntity vmConfigureEntity : vmConfigureEntityList) {

                String vmUUID = vmConfigureEntity.getVmuuid();
                if(null != vmUUID && !"".equals(vmUUID)) {
                    vmConfigureEntity.setName(vmConfigureEntity.getVmname());

                    Integer useState = productDao.getUseState(vmUUID);
                    if(null != useState) {
                        if(useState == 2) {
                            vmConfigureEntity.setVmstate("expire");
                        } else {
                            CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
                            if(null != cloneStoragectl) {
                                vmConfigureEntity.setVmstate("create");
                            }
                        }
                    }
                }
            }
            StateEntity stateEntity = new StateEntity();
            stateEntity.setState(200);
            stateEntity.setMsg("ok");
            stateEntity.setData(vmConfigureEntityList);
            return stateEntity;
        }
        throw new BusinessException("无法获取资源列表", 403);
    }

    @Override
    public StateEntity vmInfo(String token, String vmUUID) {
        if(vmService.isVmSubordinate(token, vmUUID)) {
            JSONObject VmInfoJSON = new JSONObject(vmInfo.showVmInfo(vmUUID));
            if(VmInfoJSON.length() == 0) {
                throw new BusinessException("无法获取资源，刷新重试！", 404);
            }

            VmInfoEntity vmInfoEntity = new VmInfoEntity();
            vmInfoEntity.setCpu(Integer.parseInt(VmInfoJSON.getString("cpus")));
            vmInfoEntity.setMemory(Integer.parseInt(VmInfoJSON.getString("memory")));
            vmInfoEntity.setCpuperformance(Integer.parseInt(VmInfoJSON.getString("cpuexecutioncap")));
            vmInfoEntity.setUUID(VmInfoJSON.getString("UUID"));
            vmInfoEntity.setVMStateChangeTime(VmInfoJSON.getString("VMStateChangeTime"));
            vmInfoEntity.setName(VmInfoJSON.getString("name"));
            vmInfoEntity.setOstype(VmInfoJSON.getString("ostype"));

            // 检测是否到期
            Integer useState = productDao.getUseState(vmUUID);
            if(null != useState) {
                if(useState == 2) {
                    vmInfoEntity.setVMState("expire");
                } else {
                    CloneStoragectl cloneStoragectl = CloneStoragectl.cloneStoragectlList.get(vmUUID);
                    if(null != cloneStoragectl) {
                        vmInfoEntity.setVMState("create");
                        // 设置创建进度
                        vmInfoEntity.setCurrcreateprogress(cloneStoragectl.currProgress);
                    } else {
                        vmInfoEntity.setVMState(VmInfoJSON.getString("VMState"));
                    }
                }
            }

            // 查询产品虚拟机（configure表的内容）
            VmConfigureEntity vmConfigureEntity = productDao._configure(vmUUID);
            vmInfoEntity.setPayment(vmConfigureEntity.getPayment());
            vmInfoEntity.setTime(vmConfigureEntity.getTime());
            vmInfoEntity.setCreatetime(vmConfigureEntity.getCreatetime());
            vmInfoEntity.setExamplename(vmConfigureEntity.getExamplename());
            vmInfoEntity.setNatport(vmConfigureEntity.getNatport());

            // 查询系统镜像默认的用户名和密码
            OsEntity osEntity = vmDao.selectDefaultUserNameAndPassword(vmConfigureEntity.getOsid());
            vmInfoEntity.setDefalutUserName(osEntity.getDefaultusername());
            vmInfoEntity.setDefalutPassword(osEntity.getDefalutpassword());

            // 查询虚拟机一个月的价格
            Integer exampleId = productDao.getConfigureexampleId(vmUUID);
            if(null != exampleId) {
                ExampleEntity exampleEntity = productDao.example(exampleId);
                if(null != exampleEntity) {
                    vmInfoEntity.setPrice(exampleEntity.getPrice());
                }
            }

            // 查询可以申请的端口范围
            Integer portRangeMin = vmDao.portrangemin();
            Integer portRangeMax = vmDao.portrangemax();
            if(null != portRangeMin && null != portRangeMax) {
                vmInfoEntity.setPortRangeMin(portRangeMin);
                vmInfoEntity.setPortRangeMax(portRangeMax);
            }

            // 查询内网IP（虚拟机必须安装增强）
            String intranetIp =  productDao.getIntranetIp(vmUUID);
            if(null != intranetIp) {
                vmInfoEntity.setIntranetip(intranetIp);
            }

            // 判断虚拟机是否在线
            if(vmService.isVmRunning(vmUUID)) {
                // 虚拟机预览图片
                vmInfoEntity.setBase64Snapshot(Base64.commpressPicForSize(vmDao.dataPath() + "temp\\" + vmUUID + ".png"));

                // 查询资源利用率
                JSONObject cpuUtilizationJson = new JSONObject(vmInfo.metricsQuery(vmUUID, "CPU/Load/Kernel,Guest/RAM/Usage/Free,Guest/RAM/Usage/Total"));
                if (cpuUtilizationJson.length() >= 1) {
                    // 查询CPU利用率
                    List<Object> cpuObjectList = cpuUtilizationJson.getJSONArray("CPU/Load/Kernel").toList();
                    // 查询内存利用率
                    List<Object> memoryObjectList = cpuUtilizationJson.getJSONArray("Guest/RAM/Usage/Free").toList();
                    List<Object> memoryTotalObjectList = cpuUtilizationJson.getJSONArray("Guest/RAM/Usage/Total").toList();

                    if (!"".equals(cpuObjectList.get(0)) && !"".equals(memoryObjectList.get(0)) && !"".equals(memoryTotalObjectList.get(0))) {
                        // 计算CPU百分比
                        int hostTotal = this.hostInfoEntity.getCoreCount();
                        int currTotal = Integer.parseInt(VmInfoJSON.getString("cpus"));
                        int max = 100 / hostTotal * currTotal;
                        List<Integer> newCpuStatistics = new ArrayList<>();
                        for (Object cpuUtilizationNumberObject : cpuObjectList) {
                            float cpuUtilizationNumber = Float.parseFloat((String) cpuUtilizationNumberObject);
                            int calcResult = (int) (cpuUtilizationNumber / max * 100);
                            newCpuStatistics.add(calcResult);
                        }
                        vmInfoEntity.setCpuUtilization(newCpuStatistics);

                        // 计算内存百分比
                        long memoryTotal = Long.parseLong(((String) memoryTotalObjectList.get(0)).replace("kB", ""));
                        List<Integer> newMemoryStatistics = new ArrayList<>();
                        for(Object memoryObject : memoryObjectList) {
                            long currMemory = Long.parseLong(((String) memoryObject).replace("kB", ""));
                            long usingMemory = memoryTotal - currMemory;
                            int calcResult = (int) ((float) usingMemory / memoryTotal * 100);
                            newMemoryStatistics.add(calcResult);
                        }
                        vmInfoEntity.setMemoryUtilization(newMemoryStatistics);
                    } else {
                        vmInfo.metricsSetup(vmUUID, 10, "*");
                    }
                }
            }

            StateEntity stateEntity = new StateEntity();
            stateEntity.setState(200);
            stateEntity.setMsg("ok");
            stateEntity.setData(vmInfoEntity);
            return stateEntity;
        }
        throw new BusinessException("无法获取资源，刷新重试！", 403);
    }

    @Override
    public StateEntity renew(String token, String vmUUID, int paymod, int buydate) {
        if(vmService.isVmSubordinate(token, vmUUID)) {
            if(paymod > 0 && paymod <= 2 && buydate > 0 && buydate <= 12) {
                String username = (String) redisTemplate.opsForValue().get(token);
                UserEntity userEntity = userDao.selectUser(username);
                VmConfigureEntity vmConfigureEntity = productDao._configure(vmUUID);

                // 获取用户存款
                float userDeposit = userEntity.getDeposit();
                // 实例价格
                float price = 0;
                // 计算后的价格
                float calculatedPrice = 0;

                // 查询虚拟机一个月的价格
                Integer exampleId = productDao.getConfigureexampleId(vmUUID);
                if(null != exampleId) {
                    ExampleEntity exampleEntity = productDao.example(exampleId);
                    if(null != exampleEntity) {
                        price = exampleEntity.getPrice();
                    }
                }

                if(paymod == 1) {
                    calculatedPrice = price * buydate;
                } else if(paymod == 2) {
                    calculatedPrice = (price * 12) * buydate;
                }

                if(userDeposit >= calculatedPrice) {
                    int useState = vmConfigureEntity.getUsestate();

                    Calendar nowTime = Calendar.getInstance();
                    if(useState == 1) {
                        nowTime.setTimeInMillis(vmConfigureEntity.getTime() * 1000);
                        if(paymod == 1) {
                            nowTime.add(Calendar.MONTH, buydate);
                        } else if(paymod == 2) {
                            nowTime.add(Calendar.YEAR, buydate);
                        }
                    } else if(useState == 2) {
                        if(paymod == 1) {
                            nowTime.add(Calendar.MONTH, buydate);
                        } else if(paymod == 2) {
                            nowTime.add(Calendar.YEAR, buydate);
                        }
                        productDao.setUseState(vmUUID, 1);
                    }

                    productDao.setConfigureExpireTime(vmUUID, nowTime.getTimeInMillis() / 1000);
                    userDao.setDeposit(username, userDeposit - calculatedPrice);

                    StateEntity stateEntity = new StateEntity();
                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                    return stateEntity;
                } else {
                    throw new BusinessException("续费失败，你的余额不足", 403);
                }
            } else {
                throw new BusinessException("参数有误", 501);
            }
        } else {
            throw new BusinessException("你没有权限执行此操作", 403);
        }
    }

    @Override
    public StateEntity resetExample(String token, ExampleEntity exampleEntity) {
        UserEntity loginUserEntity = userService.isUserLogin(token);
        if(null != loginUserEntity) {
            UserEntity userEntity = userDao.selectUser(loginUserEntity.getUsername());
            if(null != userEntity) {
                if(userEntity.getIdentity().equals("admin")) {
                    productDao.editExample(exampleEntity);
                    StateEntity stateEntity = new StateEntity();
                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                    return stateEntity;
                }
            }
        }
        throw new BusinessException("你没有权限执行此操作", 403);
    }


}
