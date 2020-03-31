package win.simple;

import win.simple.vboxInterface.IStorageattach;
import win.simple.vmenum.StoragectlType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VmStoragectl {

    private VBoxRuntime vBoxRuntime;

    /**
     * 创建存储控制器
     * @param vmName 虚拟机名字或者虚拟机UUID
     * @param name 新建的存储控制器名称
     * @param type 存储器类型
     * @return
     */
    public String createStoragectl(String vmName, String name, StoragectlType type) {
        switch(type) {
            case IDE:
                vBoxRuntime = new VBoxRuntime("VBoxManage storagectl " + vmName + " --name " + name + " --add ide --controller PIIX4 --bootable on");
                break;
            default:
                vBoxRuntime = new VBoxRuntime("VBoxManage storagectl " + vmName + " --name " + name + " --add sata --controller IntelAhci --bootable on");
        }
        return start();
    }

    /**
     *
     * @param vmName 虚拟机名字或者虚拟机UUID
     * @param name 存储控制器名称
     * @return
     */
    public String removeStoragectl(String vmName, String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage storagectl " + vmName + " --name " + name + " --remove");
        return start();
    }

    /**
     * 关联虚拟机磁盘
     * @param vmName 虚拟机名字或者虚拟机UUID
     * @param storagectlName 存储控制器名称
     * @param port 磁盘端口号
     * @param name 需要关联的虚拟机磁盘
     * @return
     */
    public String storageattach(String vmName, String storagectlName, int port, String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage storageattach " + vmName + " --storagectl " + storagectlName + " --port " + port + " --device 0 --type hdd --medium \"" + name + "\"");
        return start();
    }

    /**
     * 取消磁盘关联
     * @param vmName 虚拟机名字或者虚拟机UUID
     * @param storagectlName 存储控制器名称
     * @param port 磁盘端口号
     * @return
     */
    public String cancelStorageattach(String vmName, String storagectlName, int port) {
        vBoxRuntime = new VBoxRuntime("VBoxManage storageattach " + vmName + " --storagectl " + storagectlName + " --port " + port + " --device 0 --type hdd --medium none");
        return start();
    }

    /**
     * 删除虚拟磁盘
     * @param name 需要删除的虚拟机磁盘
     * @return
     */
    public String deleteStorageattach(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage closemedium disk \"" + name + "\" --delete");
        return start();
    }

    /**
     * 克隆磁盘
     * @param iStorageattach 回调事件，回调进度
     * @param cloneDivFilePath 被克隆的磁盘
     * @param ResultFilePath 克隆到指定位置的磁盘名字
     */
    public void cloneStorageattach(IStorageattach iStorageattach, String cloneDivFilePath, String ResultFilePath) {
        vBoxRuntime = new VBoxRuntime("VBoxManage clonevdi \"" + cloneDivFilePath + "\" \"" + ResultFilePath + "\"");
        vBoxRuntime.exec();
        Process process = vBoxRuntime.getProcess();
        int len = 0;
        byte[] bytes = new byte[1024];
        while(true) {
            try {
                if(len == -1) {
                    break;
                }
                len = process.getErrorStream().read(bytes,0,1024);
                if(len > 0) {
                    String data = new String(bytes, 0, len);
                    iStorageattach.callBackLine(data.substring(0, data.indexOf("%")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String start() {
        vBoxRuntime.exec();
        Process process = vBoxRuntime.getProcess();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
