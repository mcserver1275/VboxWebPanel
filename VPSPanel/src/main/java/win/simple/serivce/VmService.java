package win.simple.serivce;

import win.simple.entity.StateEntity;
import win.simple.entity.VmConfigureEntity;

public interface VmService {

    StateEntity power(String token, String vmUUID, int type);

    StateEntity portMapperList(String token, String vmUUID);

    StateEntity addPortMapper(String token, String vmUUID, String name, int agreement, int intranetPort, int externalPort);

    StateEntity deletePortMapper(String token, String vmUUID, String name);

    StateEntity osList(String token, String vmUUID);

    StateEntity changeOs(String token, String vmUUID, int osId);

    /**
     * 添加磁盘克隆
     * @param cloneDivFilePath
     * @param ResultFilePath
     */
    void addClone(String uuid, String cloneDivFilePath, String ResultFilePath);

    /**
     * 判断该虚拟机是否属于该用户
     * @param token
     * @param vmUUID
     * @return
     */
    boolean isVmSubordinate(String token, String vmUUID);

    /**
     * 检查指定虚拟机是否运行
     * @param vmUUID
     * @return
     */
    boolean isVmRunning(String vmUUID);

    /**
     * 获取虚拟机的内网地址 (会导致耗时较高)
     * @param vmUuid
     * @return
     */
    String getIntranetIp(String vmUuid);

    /**
     * 修改虚拟机密码，该功能必须安装增强且需要在系统创建一个新账户（拥有管理员权限）
     * @param token
     * @param vmUUID
     * @param password
     * @return
     */
    StateEntity resetPass(String token, String vmUUID, String password);

}
