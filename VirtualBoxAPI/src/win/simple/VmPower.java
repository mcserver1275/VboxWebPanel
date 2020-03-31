package win.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VmPower {

    private VBoxRuntime vBoxRuntime;

    /**
     * 开启虚拟机
     * @param name
     * @return
     */
    public String startVM(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage startvm " + name + " --type headless");
        return start();
    }

    /**
     * 休眠虚拟机
     * @param name
     */
    public String sleepVM(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage controlvm " + name + " savestate");
        return start();
    }


    /**
     * 断电关闭虚拟机
     * @param name
     * @return
     */
    public String powerOff(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage controlvm " + name + " poweroff");
        return start();
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
