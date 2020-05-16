package win.simple.dao;

import org.apache.ibatis.annotations.Param;
import win.simple.entity.CacheInfoEntity;
import win.simple.entity.ExampleEntity;
import win.simple.entity.VmConfigureEntity;

import java.util.List;

public interface ProductDao {

    List<ExampleEntity> exampleList();

    ExampleEntity example(@Param("id") int id);

    List<VmConfigureEntity> allConfigureList();

    List<VmConfigureEntity> configureList(@Param("userid") int userid);

    VmConfigureEntity configure(@Param("id") int id);

    Integer deleteConfigure(@Param("id") int id);

    Integer getNatPort(@Param("vmuuid") String vmuuid);

    void setNatPort(@Param("vmuuid") String vmuuid, @Param("natport") int natport);

    Integer getUseState(@Param("vmuuid") String vmuuid);

    void setUseState(@Param("vmuuid") String vmuuid, @Param("usestate") int usestate);

    Integer createConfigure(@Param("vmuuid") String vmuuid,
                            @Param("userid") int userid,
                            @Param("payment") String payment,
                            @Param("time") long time,
                            @Param("createtime") long createtime,
                            @Param("examplename") String examplename,
                            @Param("natport") int natport,
                            @Param("usestate") int usestate,
                            @Param("osid") int osid,
                            @Param("exampleid") int exampleid);

    /**
     * 查询创建的产品虚拟机信息
     * @param vmuuid
     * @return
     */
    VmConfigureEntity _configure(@Param("vmuuid") String vmuuid);

    /**
     * 通过虚拟机UUID查询所有者
     * @param vmuuid
     * @return
     */
    Integer vmOwner(@Param("vmuuid") String vmuuid);


    void setConfigureOsId(@Param("vmuuid") String vmuuid, @Param("osid") int osid);

    Integer getConfigureexampleId(@Param("vmuuid") String vmuuid);

    /**
     * 设置虚拟机到期时间
     * @param vmuuid
     * @param time
     */
    void setConfigureExpireTime(@Param("vmuuid") String vmuuid, @Param("time") long time);

    int editExample(ExampleEntity exampleEntity);

    void setCacheInfo(CacheInfoEntity cacheInfoEntity);

    String getIntranetIp(@Param("vmuuid") String vmuuid);

}
