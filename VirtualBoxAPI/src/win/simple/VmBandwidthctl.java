package win.simple;

import org.json.JSONObject;
import win.simple.vmenum.BandwidthctlType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class VmBandwidthctl {

    private VBoxRuntime vBoxRuntime;

    public VmBandwidthctl() {
    }

    /**
     * 给虚拟机添加带宽组并且设置默认的宽带速率（k）为单位
     * @param name 虚拟机名字或者虚拟机UUID
     * @param rate 宽带速率
     * @return
     */
    public String addBandwidthctl(String name, String groupName, long rate, BandwidthctlType bandwidthctlType) {
        String limitType = "";
        switch(bandwidthctlType) {
            case DISK:
                limitType = "disk";
                break;
            case NETWORK:
                limitType = "network";
                break;
            default:
                limitType = "network";
        }
        vBoxRuntime = new VBoxRuntime("VBoxManage bandwidthctl " + name + " add " + groupName + " --type " + limitType + " --limit " + rate + "k");
        return start();
    }

    /**
     * 修改已经添加虚拟机的宽带组的宽带速率（k）为单位
     * @param name 虚拟机名字或者虚拟机UUID
     * @param groupName 宽带组的名称
     * @param rate 宽带速率
     * @return
     */
    public String setBandwidthctl(String name, String groupName, long rate) {
        vBoxRuntime = new VBoxRuntime("VBoxManage bandwidthctl " + name + " set " + groupName + " --limit " + rate + "k");
        return start();
    }

    /**
     * 移除已经添加到虚拟机上的宽带组
     * @param name 虚拟机名字或者虚拟机UUID
     * @param groupName 宽带组的名称
     * @return
     */
    public String removeBandwidthctl(String name, String groupName) {
        vBoxRuntime = new VBoxRuntime("VBoxManage bandwidthctl " + name + " remove " + groupName);
        return start();
    }

    /**
     * 获取指定虚拟机已创建的宽带组
     * @param name 虚拟机名字或者虚拟机UUID
     * @return
     */
    public String listBandwidthctl(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage bandwidthctl " + name + " list");
        JSONObject jsonObject = new JSONObject();
        String data = start();
        String[] splitinfo = data.split(", ");
        for(int i = 0; i < splitinfo.length; i++) {
            String[] KV = splitinfo[i].split(": ");
            if(KV.length >= 2) {
                jsonObject.put(KV[0].replace("\n", ""), KV[1].replace("\n", "").replace("'", ""));
            } else {
                jsonObject.put("Type", "null");
            }
       }
        return jsonObject.toString();
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
