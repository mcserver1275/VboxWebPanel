package win.simple.dao;

import org.apache.ibatis.annotations.Param;
import win.simple.entity.ExampleEntity;
import win.simple.entity.VmConfigureEntity;

import java.util.List;

public interface ProductDao {

    List<ExampleEntity> exampleList();

    ExampleEntity example(@Param("id") int id);

    List<VmConfigureEntity> configureList(@Param("userid") int userid);

    VmConfigureEntity configure(@Param("id") int id);

    Integer getNatPort(@Param("vmuuid") String vmuuid);

    void setNatPort(@Param("vmuuid") String vmuuid, @Param("natport") int natport);

    Integer createConfigure(@Param("vmuuid") String vmuuid,
                            @Param("userid") int userid,
                            @Param("payment") String payment,
                            @Param("time") long time,
                            @Param("createtime") long createtime,
                            @Param("examplename") String examplename,
                            @Param("natport") int natport);

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

}
