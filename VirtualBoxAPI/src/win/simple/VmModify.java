package win.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VmModify {

    private VBoxRuntime vBoxRuntime;

    /**
     * 修改内存，需要关闭虚拟机才能修改内存
     * @param name 虚拟机名字或者虚拟机UUID
     * @param memory 需要设置的内存
     * @return
     */
    public String setMemory(String name, long memory) {
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --memory " + memory);
        return start();
    }

    /**
     * 修改处理器数量，需要关闭虚拟机才能修改
     * @param name 虚拟机名字或者虚拟机UUID
     * @param number 需要设置CPU的数量
     * @return
     */
    public String setCpus(String name, int number) {
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --cpus " + number);
        return start();
    }

    /**
     * 修改CPU的基准性能，需要关闭虚拟机才能修改<br />
     * 数字越低CPU性能越低，1 ~ 100, 默认100%性能
     * @param name 虚拟机名字或者虚拟机UUID
     * @param executioncap 需要设置基准性能的数字
     */
    public String setCpuExecutioncap(String name, int executioncap) {
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --cpuexecutioncap " + executioncap);
        return start();
    }

    /**
     * 设置系统类型
     * @param name 虚拟机名字或者虚拟机UUID
     * @param osType 系统类型
     * @return
     */
    public String setOsType(String name, String osType) {
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --ostype \"" + osType + "\"");
        return start();
    }

    /**
     * 设置网络为NAT
     * @param name 虚拟机名字或者虚拟机UUID
     */
    public String bridging(String name, int port, String netWorkType) {
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --nic" + port + " " + netWorkType + " --nictype" + port + " 82540EM --cableconnected" + port + " on --bridgeadapter" + port + " enp5s0f0");
        return start();
    }


    /**
     * 添加端口映射 (虚拟机已经停止的情况下操作)
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
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --natpf1 \"" + portName + "," + portAgreement + ",," + externalPort + ",," + intranetPort + "\"");
        return start();
    }

    /**
     * 删除端口映射 (虚拟机已经停止的情况下操作)
     * @param name 虚拟机名字或者虚拟机UUID
     * @param portName 端口应用名
     * @return
     */
    public String deletePortMapper(String name, String portName) {
        vBoxRuntime = new VBoxRuntime("VBoxManage modifyvm " + name + " --natpf1 delete \"" + portName + "\"");
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
