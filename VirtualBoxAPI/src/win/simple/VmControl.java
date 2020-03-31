package win.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VmControl {

    private VBoxRuntime vBoxRuntime;

    /**
     * 虚拟机截屏
     * @param name 虚拟机名字或者虚拟机UUID
     * @param path 图片保存路径（存储的文件夹）
     * @return
     */
    public String screenshotPng(String name, String path) {
        vBoxRuntime = new VBoxRuntime("VboxManage controlvm " + name + " screenshotpng \"" + path + "\\" + name + ".png\"");
        return start();
    }

    /**
     * 添加端口映射 (虚拟机正在运行的情况下操作)
     * @param name 虚拟机名字或者虚拟机UUID
     * @param portName 端口应用名
     * @param agreement 映射协议 1(TCP)，2(UDP)
     * @param intranetPort 内网端口
     * @param externalPort 外部端口
     * @return
     */
    public String addPortMapper(String name, String portName, int agreement, int intranetPort, int externalPort) {
        String portAgreement;
        switch(agreement) {
            case 1:
                portAgreement = "tcp";
                break;
            case 2:
                portAgreement = "udp";
                break;
            default:
                portAgreement = "tcp";
        }
        vBoxRuntime = new VBoxRuntime("VBoxManage controlvm " + name + " natpf1 \"" + portName + "," + portAgreement + ",," + externalPort + ",," + intranetPort + "\"");
        return start();
    }

    /**
     * 删除端口映射 (虚拟机正在运行的情况下操作)
     * @param name 虚拟机名字或者虚拟机UUID
     * @param portName 端口应用名
     * @return
     */
    public String deletePortMapper(String name, String portName) {
        vBoxRuntime = new VBoxRuntime("VBoxManage controlvm " + name + " natpf1 delete \"" + portName + "\"");
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
