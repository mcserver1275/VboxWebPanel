package win.simple.util.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.simple.VmInfo;
import win.simple.VmPower;
import win.simple.VmStoragectl;
import win.simple.vboxInterface.IStorageattach;

import java.util.HashMap;
import java.util.Map;

/**
 * 克隆进度回调
 */
@Component
public class CloneStoragectl extends Thread {

    private VmPower vmPower;
    private RedisTemplate redisTemplate;
    public static Map<String, CloneStoragectl> cloneStoragectlList = new HashMap<>();
    public int currProgress;
    private String vmUuid;
    private String cloneDivFilePath;
    private String resultFilePath;


    public void exec(String vmUuid, String cloneDivFilePath, String resultFilePath, RedisTemplate redisTemplate, VmPower vmPower) {
        this.vmUuid = vmUuid;
        this.cloneDivFilePath = cloneDivFilePath;
        this.resultFilePath = resultFilePath;
        this.redisTemplate = redisTemplate;
        this.vmPower = vmPower;
        cloneStoragectlList.put(vmUuid, this);
        start();
        System.out.println("实例:" + vmUuid + " 开始克隆磁盘");
    }

    @Override
    public void run() {
        VmStoragectl vmStoragectl = new VmStoragectl();
        try {
            vmStoragectl.cloneStorageattach(new IStorageattach() {
                @Override
                public void callBackLine(String s) {
                    currProgress = Integer.parseInt(s);
                }

                @Override
                public void over() {
                    cloneStoragectlList.remove(vmUuid);
                    redisTemplate.delete("vm/" + vmUuid + "/clonediv");
                    VmStoragectl vmStoragectl = new VmStoragectl();
                    // 给虚拟机添加新克隆的磁盘
                    vmStoragectl.storageattach(vmUuid, "sata", 0, resultFilePath);
                    vmPower.startVM(vmUuid);
                    System.out.println("实例:" + vmUuid + " 克隆磁盘完成");
                }

            }, this.cloneDivFilePath, this.resultFilePath);
        } catch(StringIndexOutOfBoundsException e) {
            cloneStoragectlList.remove(vmUuid);
            redisTemplate.delete("vm/" + vmUuid + "/clonediv");
            System.out.println("虚拟机UUID：" + vmUuid + " 更换系统失败");
        }
    }

}
