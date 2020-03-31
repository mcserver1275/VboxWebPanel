package win.simple.dao;

import org.apache.ibatis.annotations.Param;
import win.simple.entity.OsEntity;

import java.util.List;

public interface VmDao {

    String dataPath();

    String publicip();

    String serviceregion();

    /**
     * 最小端口
     * @return
     */
    Integer portrangemin();

    /**
     * 最大端口
     * @return
     */
    Integer portrangemax();

    /**
     * 添加新的已被占用端口
     */
    void addOccupyPort(@Param("port") int port, @Param("userid") int userid, @Param("portname") String portname);

    /**
     * 查询占用端口的用户id
     * @param port
     * @return
     */
    Integer selectOccupyPort(@Param("port") int port);

    /**
     * 删除被占用的端口
     * @param portname
     */
    void deleteOccupyPort(@Param("portname") String portname);

    List<OsEntity> osList();

    OsEntity selectos(@Param("id") int id);

}
