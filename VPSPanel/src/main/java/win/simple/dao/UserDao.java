package win.simple.dao;

import org.apache.ibatis.annotations.Param;
import win.simple.entity.RechargeCodeEntity;
import win.simple.entity.UserEntity;

import java.util.List;

public interface UserDao {

    /**
     * 获取有所用户的信息
     * @return
     */
    List<UserEntity> userList();

    /**
     * 通过用户名查询指定用户的信息
     * @param username
     * @return
     */
    UserEntity selectUser(@Param("username") String username);

    /**
     * 通过用户名和密码查询指定用户的ID
     * @param name
     * @param password
     * @return
     */
    Integer isExistence(@Param("username") String name, @Param("password") String password);

    /**
     * 通过用户名查询指定用户的ID
     * @param name
     * @return
     */
    Integer isExistenceTwo(@Param("username") String name);

    /**
     * 注册一个账号
     * @param name
     * @param password
     */
    void register(@Param("username") String name, @Param("password") String password);

    /**
     * 设置指定用户的余额
     * @param username
     * @param deposit
     */
    void setDeposit(@Param("username") String username, @Param("deposit") float deposit);

    /**
     * 编辑指定用户的信息
     * @param userEntity
     */
    void userEdit(UserEntity userEntity);

    /**
     * 添加充值码
     * @param code
     * @param deposit
     * @return
     */
    Integer addRechargeCode(@Param("code") String code, @Param("deposit") float deposit);

    /**
     * 查询指定充值码信息
     * @param code
     * @return
     */
    RechargeCodeEntity rechargeCodeInfo(@Param("code") String code);

    /**
     * 删除充值码
     * @param id
     * @return
     */
    Integer deleteRechargeCode(@Param("id") int id);

    /**
     * 查询所有充值码信息
     * @return
     */
    List<RechargeCodeEntity> rechargeCodeInfos();

}
