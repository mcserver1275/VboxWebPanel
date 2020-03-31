package win.simple.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import win.simple.entity.StateEntity;
import win.simple.entity.UserEntity;
import win.simple.serivce.UserService;

@RestController
@RequestMapping(value = "user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation(value = "登录账号", httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "登录的用户名", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "password", value = "登录的密码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json")
    public StateEntity login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return service.login(username, password);
    }


    @ApiOperation(value = "注册账号", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "注册的用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "注册的密码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "register", method = RequestMethod.POST, produces = "application/json")
    public StateEntity register(@RequestParam("username") String username, @RequestParam("password") String password) {
        return service.register(username, password);
    }


    @ApiOperation(value = "权限验证", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "authorization", method = RequestMethod.POST, produces = "application/json")
    public StateEntity authorization(@RequestParam("token") String token) {
        return service.authorization(token);
    }


    @ApiOperation(value = "用户列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "userlist", method = RequestMethod.POST, produces = "application/json")
    public StateEntity userList(@RequestParam("token") String token) {
        return service.userList(token);
    }


    @ApiOperation(value = "编辑账号", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "管理员令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "被修改的用户ID", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "需要修改的用户", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "需要修改的密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identity", value = "需要修改的余额", dataType = "float", paramType = "query")
    })
    @RequestMapping(value = "edit", method = RequestMethod.POST, produces = "application/json")
    public StateEntity userEdit(@RequestParam("token") String token, UserEntity entity) {
        return service.userEdit(token, entity);
    }

}
