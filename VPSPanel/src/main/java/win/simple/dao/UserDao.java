package win.simple.dao;

import org.apache.ibatis.annotations.Param;
import win.simple.entity.UserEntity;

import java.util.List;

public interface UserDao {

    List<UserEntity> userList();

    UserEntity selectUser(@Param("username") String username);

    Integer isExistence(@Param("username") String name, @Param("password") String password);

    Integer isExistenceTwo(@Param("username") String name);

    void register(@Param("username") String name, @Param("password") String password);

    void setDeposit(@Param("username") String username, @Param("deposit") float deposit);

    void userEdit(UserEntity userEntity);

}
