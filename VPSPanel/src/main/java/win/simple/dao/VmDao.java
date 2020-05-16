package win.simple.dao;

import org.apache.ibatis.annotations.Param;
import win.simple.entity.OsEntity;

import java.util.List;

public interface VmDao {

    /**
     * 默认数据存储位置
     * @return
     */
    String dataPath();

    /**
     * 查询公网IP
     * @return
     */
    String publicip();

    /**
     * 查询服务地区
     * @return
     */
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

    /**
     * 查询系统列表
     * @return
     */
    List<OsEntity> osList();

    /**
     * 查询指定ID的系统信息
     * @param id
     * @return
     */
    OsEntity selectos(@Param("id") int id);

    /**
     * 查询指定ID的系统默认用户名和密码
     * @param id
     * @return
     */
    OsEntity selectDefaultUserNameAndPassword(@Param("id") int id);

    /**
     * 查询指定ID的系统来宾账号和密码
     * @param id
     * @return
     */
    OsEntity selectGuestUserNameAndPassword(@Param("id") int id);

}
