package win.simple.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import win.simple.VmControl;
import win.simple.dao.ProductDao;
import win.simple.dao.VmDao;
import win.simple.entity.VmConfigureEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 预览截图
 */
@Component
public class ScreenshotPng extends Thread{

    @Autowired
    private ProductDao productDao;
    @Autowired
    private VmDao vmDao;

    private ScheduledExecutorService service = null;
    private VmControl vmControl = null;

    public ScreenshotPng() {
        this.vmControl = new VmControl();
    }

    @PostConstruct
    public void init(){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        this.service = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        this.service.scheduleAtFixedRate(this, 2, 10, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void inspectExpireDestroy() {
        this.service.shutdown();
    }

    @Override
    public void run() {
        List<VmConfigureEntity> vmConfigureEntityList = productDao.allConfigureList();
        if(null != vmConfigureEntityList) {
            for(VmConfigureEntity vmConfigureEntity : vmConfigureEntityList) {
                vmControl.screenshotPng(vmConfigureEntity.getVmuuid(), vmDao.dataPath() + "temp");
            }
        }
    }

}
