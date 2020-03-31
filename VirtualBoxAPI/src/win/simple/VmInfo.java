package win.simple;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class VmInfo {

    private VBoxRuntime vBoxRuntime;

    public VmInfo() {
    }

    /**
     * 查询虚拟机版本
     * @return
     */
    public String version() {
        vBoxRuntime = new VBoxRuntime("VBoxManage -v");
        JSONObject jsonObject = new JSONObject();
        String data = start();
        jsonObject.put("version", data.replace("\n", ""));
        return jsonObject.toString();
    }

    /**
     * 列出所有的虚拟机
     * @return
     */
    public String vms() {
        vBoxRuntime = new VBoxRuntime("VBoxManage list vms");
        JSONObject jsonObject = new JSONObject();
        JSONArray InfoArray = new JSONArray();

        String data = start();
        String[] splitInfo = data.split("\n");
        for(int i = 0; i < splitInfo.length; i++) {
            JSONObject vmInfoJson = new JSONObject();

            String[] KV = splitInfo[i].split(" ");
            if(KV.length >= 2) {
                vmInfoJson.put("name", KV[0].replace("\"", ""));
                vmInfoJson.put("uuid", KV[1]);
                InfoArray.put(vmInfoJson);
            }
        }
        jsonObject.put("vms", InfoArray);
        return jsonObject.toString();
    }

    /**
     * 列出所有正在运行的虚拟机
     * @return
     */
    public String runningVms() {
        vBoxRuntime = new VBoxRuntime("VBoxManage list runningvms");
        JSONObject jsonObject = new JSONObject();
        JSONArray InfoArray = new JSONArray();

        String data = start();
        String[] splitInfo = data.split("\n");
        for(int i = 0; i < splitInfo.length; i++) {
            JSONObject vmInfoJson = new JSONObject();
            String[] KV = splitInfo[i].split(" ");
            if(KV.length >= 2) {
                vmInfoJson.put("name", KV[0].replace("\"", ""));
                vmInfoJson.put("uuid", KV[1].replace("{", "").replace("}", ""));
                InfoArray.put(vmInfoJson);
            }
        }
        jsonObject.put("runningvms", InfoArray);
        return jsonObject.toString();
    }

    /**
     * 列出VirtualBox当前正在使用的虚拟磁盘的信息
     * @return
     */
    public String hdds() {
        vBoxRuntime = new VBoxRuntime("VBoxManage list hdds");
        JSONObject jsonObject = new JSONObject();
        JSONArray InfoArray = new JSONArray();

        String data = start();
        String[] splitHdds = data.split("\n\n");
        for(int hddsIndex = 0; hddsIndex < splitHdds.length; hddsIndex++) {
            JSONObject hddJson = new JSONObject();

            String splitHdd = splitHdds[hddsIndex].replace(" ", "");
            String[] splithddinfos = splitHdd.split("\n");
            for(int i = 0; i < splithddinfos.length; i++) {
                String splithddinfo = splithddinfos[i];
                String key = splithddinfo.substring(0, splithddinfo.indexOf(":"));
                String value = splithddinfo.substring(splithddinfo.indexOf(":") + 1, splithddinfo.length());
                hddJson.put(key, value);
            }
            InfoArray.put(hddJson);
        }
        jsonObject.put("hhds", InfoArray);
        return jsonObject.toString();
    }

    /**
     * 列出虚拟机配置文件中加载的虚拟磁盘镜像的信息
     * @return
     */
    public String dvds() {
        vBoxRuntime = new VBoxRuntime("VBoxManage list dvds");
        JSONObject jsonObject = new JSONObject();
        JSONArray InfoArray = new JSONArray();

        String data = start();
        String[] splitDvds = data.split("\n\n");
        for(int dvdsIndex = 0; dvdsIndex < splitDvds.length; dvdsIndex++) {
            JSONObject dvdJson = new JSONObject();

            String splitDvd = splitDvds[dvdsIndex].replace(" ", "");
            String[] splitdvdinfos = splitDvd.split("\n");
            for(int i = 0; i < splitdvdinfos.length; i++) {
                String splitdvdinfo = splitdvdinfos[i];
                String key = splitdvdinfo.substring(0, splitdvdinfo.indexOf(":"));
                String value = splitdvdinfo.substring(splitdvdinfo.indexOf(":") + 1, splitdvdinfo.length());
                dvdJson.put(key, value);
            }
            InfoArray.put(dvdJson);
        }
        jsonObject.put("dvds", InfoArray);
        return jsonObject.toString();
    }

    /**
     * 详细显示指定虚拟机的配置信息
     * @param name 虚拟机名字或者虚拟机UUID
     * @return
     */
    public String showVmInfo(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage showvminfo " + name + " --machinereadable");
        String str = start();
        String[] splitInfo = str.split("\n");
        JSONObject jsonObject = new JSONObject();
        for(int i = 0; i < splitInfo.length; i++) {
            String[] KV = splitInfo[i].split("=");
            if(KV.length >= 2) {
                jsonObject.put(KV[0].replace("\"", ""), KV[1].replace("\"", ""));
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
