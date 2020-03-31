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

    VmConfigureEntity getVmInfo(int configureid);

    void addClone(String uuid, String cloneDivFilePath, String ResultFilePath);

}
