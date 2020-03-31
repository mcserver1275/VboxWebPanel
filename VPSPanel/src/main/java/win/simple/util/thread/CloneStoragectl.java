package win.simple.util.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import win.simple.VmStoragectl;
import win.simple.vboxInterface.IStorageattach;

import java.util.HashMap;
import java.util.Map;

@Component
public class CloneStoragectl extends Thread {

    private RedisTemplate redisTemplate;

    public static Map<String, CloneStoragectl> cloneStoragectlList = new HashMap<>();

    public int currProgress;
    private String vmUuid;
    private String cloneDivFilePath;
    private String resultFilePath;

    public void exec(String vmUuid, String cloneDivFilePath, String resultFilePath, RedisTemplate redisTemplate) {
        this.vmUuid = vmUuid;
        this.cloneDivFilePath = cloneDivFilePath;
        this.resultFilePath = resultFilePath;
        this.redisTemplate = redisTemplate;
        cloneStoragectlList.put(vmUuid, this);
        start();
    }

    @Override
    public void run() {
        VmStoragectl vmStoragectl = new VmStoragectl();
        vmStoragectl.cloneStorageattach(new IStorageattach() {
            @Override
            public void callBackLine(String s) {
                currProgress = Integer.parseInt(s);
                if(100 == currProgress) {
                    cloneStoragectlList.remove(vmUuid);
                    redisTemplate.delete("vm/" + vmUuid + "/clonediv");
                    VmStoragectl vmStoragectl = new VmStoragectl();
                    vmStoragectl.storageattach(vmUuid, "sata", 0, resultFilePath);
                }
            }
        }, this.cloneDivFilePath, this.resultFilePath);
    }

}
