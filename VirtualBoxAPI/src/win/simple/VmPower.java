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
        return start("VBoxManage startvm " + name + " --type headless");
    }

    /**
     * 休眠虚拟机
     * @param name
     */
    public String sleepVM(String name) {
        return start("VBoxManage controlvm " + name + " savestate");
    }


    /**
     * 断电关闭虚拟机
     * @param name
     * @return
     */
    public String powerOff(String name) {
        return start("VBoxManage controlvm " + name + " poweroff");
    }



    private String start(String command) {
        VBoxRuntime vBoxRuntime = new VBoxRuntime(command);
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
