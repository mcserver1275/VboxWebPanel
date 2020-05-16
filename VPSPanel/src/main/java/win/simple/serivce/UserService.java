package win.simple.serivce;

import win.simple.entity.StateEntity;
import win.simple.entity.UserEntity;

public interface UserService {

    StateEntity login(String name, String password);

    StateEntity register(String name, String password);

    StateEntity authorization(String token);

    StateEntity userList(String token);

    StateEntity userEdit(String token, UserEntity entity);

    StateEntity userInfo(String token);

    StateEntity useRechargeCode(String token, String code);

    StateEntity RechargeCodeInfos(String token);

    StateEntity removeRechargeCode(String token, int id);

    /**
     * 自定义充值码
     * @param token
     * @param code
     * @param deposit
     * @return
     */
    StateEntity addRechargeCodeCustom(String token, String code, float deposit);

    /**
     * 随机生成充值码
     * @param token
     * @param deposit
     * @param number 创建的数量
     * @return
     */
    StateEntity addRechargeCode(String token, float deposit, int number);

    /**
     * 通过Token获取用户名
     * @param token
     * @return
     */
    String tokenUserName(String token);

    UserEntity isUserLogin(String userName);

    /**
     * 判断指定用户名是否为管理员
     * @param userName
     * @return
     */
    boolean isAdmin(String userName);

}
