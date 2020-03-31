package win.simple.serivce;

import win.simple.entity.StateEntity;
import win.simple.entity.UserEntity;

public interface UserService {

    StateEntity login(String name, String password);

    StateEntity register(String name, String password);

    /**
     * 通过Token获取用户名
     * @param token
     * @return
     */
    String tokenUserName(String token);

    StateEntity authorization(String token);

    StateEntity userList(String token);

    StateEntity userEdit(String token, UserEntity entity);
}
