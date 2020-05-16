package win.simple.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import win.simple.entity.StateEntity;
import win.simple.serivce.VmService;

@RestController
@RequestMapping(value = "vm")
@Api(tags = "虚拟机模块")
public class VmController {

    @Autowired
    VmService vmService;

    @ApiOperation(value = "修改虚拟机电源", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "操作类型, 1(开机), 2(关机)", dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "power", method = RequestMethod.POST, produces = "application/json")
    public StateEntity power(@RequestParam("token") String token, @RequestParam("vmuuid") String vmUUID, @RequestParam("type") int type) {
        return vmService.power(token, vmUUID, type);
    }


    @ApiOperation(value = "获取所有添加的端口映射列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "portmapperlist", method = RequestMethod.POST, produces = "application/json")
    public StateEntity portMapper(@RequestParam("token") String token, @RequestParam("vmuuid") String vmUUID) {
        return vmService.portMapperList(token, vmUUID);
    }


    @ApiOperation(value = "添加端口映射", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "端口映射应用名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "agreement", value = "端口映射协议类型, 1(TCP), 2(UDP)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "intranetPort", value = "端口映射内网端口", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "externalPort", value = "端口映射外网端口", dataType = "Integer", paramType = "query"),
    })
    @RequestMapping(value = "addportmapper", method = RequestMethod.POST, produces = "application/json")
    public StateEntity addPortMapper(@RequestParam("token") String token,
                                     @RequestParam("vmuuid") String vmUUID,
                                     @RequestParam("name") String name,
                                     @RequestParam("agreement") int agreement,
                                     @RequestParam("intranetPort") int intranetPort,
                                     @RequestParam("externalPort") int externalPort) {
        return vmService.addPortMapper(token, vmUUID, name, agreement, intranetPort, externalPort);
    }


    @ApiOperation(value = "删除端口映射", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "端口映射应用名", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "deleteportmapper", method = RequestMethod.POST, produces = "application/json")
    public StateEntity deletePortMapper(@RequestParam("token") String token,
                                        @RequestParam("vmuuid") String vmUUID,
                                        @RequestParam("name") String name) {
        return vmService.deletePortMapper(token, vmUUID, name);
    }


    @ApiOperation(value = "查询操作系统列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "oslist", method = RequestMethod.POST, produces = "application/json")
    public StateEntity osList(@RequestParam("token") String token, @RequestParam("vmuuid") String vmUUID) {
        return vmService.osList(token, vmUUID);
    }


    @ApiOperation(value = "改变操作系统", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "osId", value = "需要使用的操作系统ID", dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "changeos", method = RequestMethod.POST, produces = "application/json")
    public StateEntity changeOs(@RequestParam("token") String token, @RequestParam("vmuuid") String vmUUID, @RequestParam("osid") int osId) {
        return vmService.changeOs(token, vmUUID, osId);
    }


    @ApiOperation(value = "修改系统密码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "新的密码", dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "resetPass", method = RequestMethod.POST, produces = "application/json")
    public StateEntity resetPass(@RequestParam("token") String token, @RequestParam("vmuuid") String vmUUID, @RequestParam("password") String password) {
        return vmService.resetPass(token, vmUUID, password);
    }

}
