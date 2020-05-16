package win.simple;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class VmGuestControl {

    private VBoxRuntime vBoxRuntime;

    /**
     * 远程执行指定虚拟机里面的程序
     * @param name 虚拟机名字或者虚拟机UUID
     * @param username 虚拟机系统账号
     * @param password 虚拟机系统密码
     * @param execApplication 可执行程序
     * @param param 执行参数
     * @return
     */
    public String exec(String name, String username, String password, String execApplication, String param) {
        vBoxRuntime = new VBoxRuntime("VBoxManage guestcontrol " + name + " run --exe \"" + execApplication + "\" --username \"" + username + "\" --password \"" + password + "\" -- " + param);
        return start();
    }

    /**
     * 来宾读取虚拟机属性
     * @param name 虚拟机名字或者虚拟机UUID
     * @return
     */
    public String guestProperty(String name) {
        vBoxRuntime = new VBoxRuntime("VBoxManage guestproperty enumerate " + name);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String data = start();
        String[] splitInfos = data.split("\n");
        for(String info : splitInfos) {
            String[] propertys = info.split(", ");

            JSONObject kvJson = new JSONObject();
            for(String property : propertys) {
                String[] kv = property.split(": ");
                if(kv.length >= 2) {
                    kvJson.put(kv[0], kv[1]);
                }
            }
            jsonArray.put(kvJson);
        }

        jsonObject.put("guestproperty", jsonArray);
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
