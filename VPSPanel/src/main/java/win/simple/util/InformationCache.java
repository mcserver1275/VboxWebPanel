package win.simple.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.simple.VmInfo;
import win.simple.dao.ProductDao;
import win.simple.entity.CacheInfoEntity;
import win.simple.entity.VmConfigureEntity;
import win.simple.serivce.VmService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 信息写入数据库缓存，解决获取信息后端耗时长的问题
 * @author Ning
 */
@Component
public class InformationCache extends Thread {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private VmService vmService;
    @Autowired
    private VmInfo vmInfo;
    private ScheduledExecutorService service;

    @PostConstruct
    public void init(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        this.service = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        this.service.scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void inspectExpireDestroy() {
        this.service.shutdown();
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                List<VmConfigureEntity> vmConfigureEntityList = productDao.allConfigureList();
                if(null != vmConfigureEntityList) {
                    for(VmConfigureEntity vmConfigureEntity : vmConfigureEntityList) {
                        String vmUuid = vmConfigureEntity.getVmuuid();
                        // 获取虚拟机信息（高耗时）
                        JSONObject vmInfoJsonObject = null;
                        String info = vmInfo.showVmInfo(vmUuid);
                        if(null != info) {
                            vmInfoJsonObject = new JSONObject(info);
                        }

                        // 获取内网IP地址
                        String intranetIp = vmService.getIntranetIp(vmUuid);

                        CacheInfoEntity cacheInfoEntity = new CacheInfoEntity();
                        cacheInfoEntity.setVmuuid(vmUuid);
                        cacheInfoEntity.setIntranetip(intranetIp);
                        if(null != vmInfoJsonObject) {
                            if(vmInfoJsonObject.length() > 0) {
                                cacheInfoEntity.setVmname(vmInfoJsonObject.getString("name"));
                                cacheInfoEntity.setVmstate(vmInfoJsonObject.getString("VMState"));
                                cacheInfoEntity.setCpu(vmInfoJsonObject.getInt("cpus"));
                                cacheInfoEntity.setMemory(vmInfoJsonObject.getLong("memory"));
                            }
                        }
                        boolean isExec = null != cacheInfoEntity.getIntranetip() || null != cacheInfoEntity.getVmname();
                        if(isExec) {
                            try {
                                productDao.setCacheInfo(cacheInfoEntity);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
