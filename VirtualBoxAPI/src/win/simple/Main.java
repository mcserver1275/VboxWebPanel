package win.simple;

import org.json.JSONArray;
import org.json.JSONObject;
import win.simple.vboxInterface.IStorageattach;
import win.simple.vmenum.BandwidthctlType;
import win.simple.vmenum.StoragectlType;

public class Main {

    public static void main(String[] args) {
//        VmInfo vmInfo = new VmInfo();
//
//        JSONObject jsonObject = new JSONObject(vmInfo.runningVms());
//        JSONArray jsonArray = jsonObject.getJSONArray("runningvms");
//        System.out.println("正在运行的服务器有");
//        for(int i = 0; i < jsonArray.length(); i++) {
//            JSONObject info = jsonArray.getJSONObject(i);
//            JSONObject vminfo = new JSONObject(vmInfo.showVmInfo(info.getString("uuid")));
//            System.out.println("服务器名称：" + vminfo.getString("name"));
//            System.out.println("服务器内存：" + vminfo.getString("memory") + "MB");
//            System.out.println("服务器类型：" + vminfo.getString("ostype"));
//            System.out.println("映射端口：" + vminfo.getString("Forwarding(0)"));
//            System.out.println("\r\n");
//        }





//        VmStoragectl vmStoragectl = new VmStoragectl();
//        vmStoragectl.cloneStorageattach(new IStorageattach() {
//            @Override
//            public void callBackLine(String data) {
//                System.out.println(data);
//            }
//        }, "D:\\VirtualBox VMs\\windows server2008\\windows server2008.vdi", "D:\\VirtualBox VMs\\test\\test.vdi");


    }

}
