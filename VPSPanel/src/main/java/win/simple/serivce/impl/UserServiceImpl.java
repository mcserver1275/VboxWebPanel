package win.simple.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import win.simple.dao.ProductDao;
import win.simple.dao.UserDao;
import win.simple.dao.VmDao;
import win.simple.entity.*;
import win.simple.serivce.UserService;
import win.simple.util.Tools;
import win.simple.util.execption.BusinessException;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private VmDao vmDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public StateEntity login(String name, String password) {
        Integer userid = userDao.isExistence(name, password);
        StateEntity stateEntity = new StateEntity();
        if(null != userid) {
            TokenEntity tokenEntity = new TokenEntity();
            String token = UUID.randomUUID().toString().replace("-","");
            tokenEntity.setToken(token);

            Iterator<String> keys = redisTemplate.keys("*").iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                String username = (String) redisTemplate.opsForValue().get(key);
                if(username.equals(name)) {
                    redisTemplate.delete(key);
                }
            }
            redisTemplate.opsForValue().set(token, name, 3, TimeUnit.DAYS);

            stateEntity.setState(200);
            stateEntity.setMsg("ok");
            stateEntity.setData(tokenEntity);
            return stateEntity;
        }
        stateEntity.setState(404);
        stateEntity.setMsg("登录失败，请检查用户名密码是否有误");
        return stateEntity;
    }

    @Override
    public StateEntity register(String name, String password) {
        Integer userid;
        userid = userDao.isExistenceTwo(name);
        StateEntity stateEntity = new StateEntity();
        if(null != userid) {
            stateEntity.setState(400);
            stateEntity.setMsg("该用户名已经存在了");
            return stateEntity;
        }
        userDao.register(name, password);
        userid = userDao.isExistenceTwo(name);
        if(null != userid) {
            UserEntity userEntity = userDao.selectUser(name);
            if(null != userEntity) {
                stateEntity.setState(200);
                stateEntity.setMsg("账号注册成功");
                stateEntity.setData(userEntity);
                return stateEntity;
            }
        }
        stateEntity.setState(400);
        stateEntity.setMsg("账号注册失败，请重试");
        return stateEntity;
    }

    @Override
    public StateEntity authorization(String token) {
        StateEntity stateEntity = new StateEntity();
        String username = tokenUserName(token);
        if(null != username) {
            UserEntity userEntity = userDao.selectUser(username);
            if(null != userEntity) {
                AuthorizationEntity authorizationEntity = new AuthorizationEntity();
                authorizationEntity.setId(userEntity.getId());
                authorizationEntity.setName(userEntity.getUsername());
                authorizationEntity.setIdentity(userEntity.getIdentity());

                Integer userId = userDao.isExistenceTwo(username);
                if(null != userId && userId > 0) {
                    List<VmConfigureEntity> vmConfigureEntityList = productDao.configureList(userId);
                    if(null != vmConfigureEntityList && vmConfigureEntityList.size() > 0) {
                        authorizationEntity.setPublicip(vmDao.publicip());
                        authorizationEntity.setServiceregion(vmDao.serviceregion());
                    }
                }

                stateEntity.setState(200);
                stateEntity.setMsg("ok");
                stateEntity.setData(authorizationEntity);
                return stateEntity;
            }
        }
        stateEntity.setState(404);
        stateEntity.setMsg("验证失败");
        return stateEntity;
    }

    @Override
    public StateEntity userList(String token) {
        StateEntity stateEntity = new StateEntity();
        UserEntity loginUserEntity = this.isUserLogin(token);
        if(null != loginUserEntity) {
            if(this.isAdmin(loginUserEntity.getUsername())) {
                List<UserEntity> userEntityList = userDao.userList();
                if(null != userEntityList) {
                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                    stateEntity.setData(userEntityList);
                    return stateEntity;
                }
            }
        }
        stateEntity.setState(404);
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity userEdit(String token, UserEntity entity) {
        StateEntity stateEntity = new StateEntity();
        String username = this.tokenUserName(token);
        if(null != username) {
            UserEntity userEntity = userDao.selectUser(username);
            if(null != userEntity) {
                if(userEntity.getIdentity().equals("admin")) {
                    userDao.userEdit(entity);
                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                    return stateEntity;
                }
            }
        }
        stateEntity.setState(404);
        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity userInfo(String token) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(404);
        String username = this.tokenUserName(token);
        if(null != username) {
            Integer userId = userDao.isExistenceTwo(username);
            if(null != userId) {
                UserEntity userEntity = userDao.selectUser(username);
                List<VmConfigureEntity> vmConfigureEntityList = productDao.configureList(userId);

                UserInfoEntity userInfoEntity = new UserInfoEntity();
                userInfoEntity.setDeposit(userEntity.getDeposit());
                userInfoEntity.setIdentity(userEntity.getIdentity());
                userInfoEntity.setExamplenumber(vmConfigureEntityList.size());

                stateEntity.setState(200);
                stateEntity.setData(userInfoEntity);
                stateEntity.setMsg("ok");
                return stateEntity;
            }
        }

        stateEntity.setMsg("你没有权限执行此操作");
        return stateEntity;
    }

    @Override
    public StateEntity useRechargeCode(String token, String code) {
        UserEntity loginUserEntity = this.isUserLogin(token);
        if(null != loginUserEntity) {
            RechargeCodeEntity rechargeCodeEntity = userDao.rechargeCodeInfo(code);
            if(null != rechargeCodeEntity && code.equals(rechargeCodeEntity.getCode())) {
                // 使用充值码
                String userName = loginUserEntity.getUsername();
                UserEntity userEntity = userDao.selectUser(userName);
                userDao.setDeposit(userName, userEntity.getDeposit() + rechargeCodeEntity.getDeposit());

                // 删除充值码
                userDao.deleteRechargeCode(rechargeCodeEntity.getId());

                StateEntity stateEntity = new StateEntity();
                stateEntity.setState(200);
                stateEntity.setMsg("ok");
                stateEntity.setData("充值成功");
                return stateEntity;
            } else {
                throw new BusinessException("你输入的充值码有误", 403);
            }
        }
        throw new BusinessException("你没有权限执行此操作", 403);
    }

    @Override
    public StateEntity RechargeCodeInfos(String token) {
        UserEntity loginUserEntity = this.isUserLogin(token);
        if(null != loginUserEntity) {
            if(this.isAdmin(loginUserEntity.getUsername())) {
                StateEntity stateEntity = new StateEntity();
                stateEntity.setState(200);
                stateEntity.setMsg("ok");
                stateEntity.setData(userDao.rechargeCodeInfos());
                return stateEntity;
            }
        }
        throw new BusinessException("你没有权限执行此操作", 403);
    }

    @Override
    public StateEntity addRechargeCodeCustom(String token, String code, float deposit) {
        return null;
    }

    @Override
    public StateEntity addRechargeCode(String token, float deposit, int number) {
        UserEntity loginUserEntity = this.isUserLogin(token);
        if(null != loginUserEntity) {
            if(this.isAdmin(loginUserEntity.getUsername())) {
                for(int i = 0; i < number; i++) {
                    // 创建长度12位的激活码
                    String code = Tools.getRandomString(20);
                    RechargeCodeEntity rechargeCodeEntity = userDao.rechargeCodeInfo(code);
                    if(null == rechargeCodeEntity) {
                        userDao.addRechargeCode(code, deposit);
                    }
                }
                StateEntity stateEntity = new StateEntity();
                stateEntity.setState(200);
                stateEntity.setMsg("ok");
                return stateEntity;
            }
        }
        throw new BusinessException("你没有权限执行此操作", 403);
    }

    @Override
    public StateEntity removeRechargeCode(String token, int id) {
        UserEntity loginUserEntity = this.isUserLogin(token);
        if(null != loginUserEntity) {
            if(this.isAdmin(loginUserEntity.getUsername())) {
                StateEntity stateEntity = new StateEntity();
                if(userDao.deleteRechargeCode(id) != 0) {
                    stateEntity.setState(200);
                    stateEntity.setMsg("ok");
                } else {
                    stateEntity.setState(404);
                    stateEntity.setMsg("无法移除该资源");
                }
                return stateEntity;
            }
        }
        throw new BusinessException("你没有权限执行此操作", 403);
    }


    @Override
    public String tokenUserName(String token) {
        String username = (String) redisTemplate.opsForValue().get(token);
        if(null != username) {
            return username;
        }
        return null;
    }

    @Override
    public UserEntity isUserLogin(String token) {
        String username = this.tokenUserName(token);
        if(null != username) {
            Integer userId = userDao.isExistenceTwo(username);
            if(null != userId) {
                UserEntity userEntity = new UserEntity();
                userEntity.setId(userId);
                userEntity.setUsername(username);
                return userEntity;
            }
        }
        return null;
    }

    @Override
    public boolean isAdmin(String userName) {
        UserEntity userEntity = userDao.selectUser(userName);
        if(null != userEntity) {
            if("admin".equals(userEntity.getIdentity())) {
                return true;
            }
        }
        return false;
    }


}
