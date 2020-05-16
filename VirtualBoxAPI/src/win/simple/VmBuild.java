package win.simple;

import org.json.JSONObject;
import win.simple.vmenum.StoragectlType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VmBuild {

    private VBoxRuntime vBoxRuntime;

    public VmBuild() {
    }

    /**
     * 创建虚拟机（同时注册）
     * @param name 虚拟机名字或者虚拟机UUID
     * @return json
     */
    public String createVm(String name) {
        String[] datas = start("VBoxManage createvm --name " + name + "  --register").split("\n");
        JSONObject jsonObject = new JSONObject();
        if(datas.length >= 3) {
            String[] KV = datas[1].split(": ");
            if(KV.length >= 2) {
                jsonObject.put(KV[0], KV[1]);
            }
        }
        return jsonObject.toString();
    }

    /**
     * 删除指定虚拟机
     * @param name 虚拟机名字或者虚拟机UUID
     * @return
     */
    public String deleteVm(String name) {
        return start("VBoxManage unregistervm --delete " + name);
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
