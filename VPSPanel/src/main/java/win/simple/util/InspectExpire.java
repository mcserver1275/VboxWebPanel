package win.simple.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.simple.VmBuild;
import win.simple.VmPower;
import win.simple.dao.ProductDao;
import win.simple.entity.VmConfigureEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

/**
 * 产品到期检查类，多线程检查产品是否到期
 * @author Ning
 */
@Component
public class InspectExpire extends Thread {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private VmBuild vmBuild;
    @Autowired
    private VmPower vmPower;
    private ScheduledExecutorService service = null;

    public InspectExpire() {

    }

    @PostConstruct
    public void init(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        this.service = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        this.service.scheduleAtFixedRate(this, 2, 60 * 60, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void inspectExpireDestroy() {
        this.service.shutdown();
    }

    @Override
    public void run() {
        if(null != productDao.allConfigureList()) {
            long currTime = System.currentTimeMillis();
            List<VmConfigureEntity> vmConfigureEntityList = productDao.allConfigureList();
            for(VmConfigureEntity vmConfigureEntity : vmConfigureEntityList) {
                String vmUuid = vmConfigureEntity.getVmuuid();

                // 获取当前实例的到期时间
                long productTime = (vmConfigureEntity.getTime() * 1000);

                // 到期暂停检测
                int useState = vmConfigureEntity.getUsestate();
                final int normalUse = 1;
                if(productTime < currTime && useState == normalUse) {
                    vmPower.powerOff(vmUuid);
                    productDao.setUseState(vmUuid, 2);
                    System.out.println("实例:" + vmUuid + " 超过有效使用期自动暂停使用");
                }

                // 超过7天删除所有数据
                Calendar nowTime = Calendar.getInstance();
                nowTime.setTimeInMillis(productTime);
                nowTime.add(Calendar.DAY_OF_MONTH, 7);
                if(currTime >= nowTime.getTimeInMillis()) {
                    vmBuild.deleteVm(vmUuid);
                    productDao.deleteConfigure(vmConfigureEntity.getId());
                    System.out.println("实例:" + vmUuid + " 到期已超过七天自动删除所有数据");
                }
            }
        }

    }



}
