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
import win.simple.entity.ExampleEntity;
import win.simple.entity.StateEntity;
import win.simple.serivce.impl.ProductServiceImpl;

@RestController
@RequestMapping(value = "product")
@Api(tags = "产品模块")
public class ProductController {

    @Autowired
    ProductServiceImpl productService;

    @ApiOperation(value = "购买一个实例", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "paymod", value = "付费模式 1(月付费), 2(年付费)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "exampleId", value = "需要购买的实例ID", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "buydate", value = "购买时间 1~12之间", dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "buyvm", method = RequestMethod.POST, produces = "application/json")
    public StateEntity buyvm(@RequestParam("token") String token,
                             @RequestParam("paymod") int paymod,
                             @RequestParam("exampleId") int exampleId,
                             @RequestParam("buydate") int buydate) {
        return productService.buyvm(token, paymod, exampleId, buydate);
    }


    @ApiOperation(value = "实例列表", httpMethod = "GET")
    @RequestMapping(value = "examplelist", method = RequestMethod.GET, produces = "application/json")
    public StateEntity exampleList() {
        return productService.exampleList();
    }


    @ApiOperation(value = "获取用户的实例列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "myresources", method = RequestMethod.POST, produces = "application/json")
    public StateEntity myResources(@RequestParam("token") String token) {
        return productService.myResources(token);
    }


    @ApiOperation(value = "获取虚拟机信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "vminfo", method = RequestMethod.POST, produces = "application/json")
    public StateEntity vmInfo(@RequestParam("token") String token, @RequestParam("vmuuid") String vmUUID) {
        return productService.vmInfo(token, vmUUID);
    }


    @ApiOperation(value = "续费虚拟机", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "vmuuid", value = "虚拟机ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "paymod", value = "付费模式 1(月付费), 2(年付费)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "exampleId", value = "需要购买的实例ID", dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "renew", method = RequestMethod.POST, produces = "application/json")
    public StateEntity renew(@RequestParam("token") String token,
                             @RequestParam("vmuuid") String vmUUID,
                             @RequestParam("paymod") int paymod,
                             @RequestParam("buydate") int buydate) {
        return productService.renew(token, vmUUID, paymod, buydate);
    }


    @ApiOperation(value = "修改实例", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户名令牌", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "需要修改的实例ID", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "需要修改的实例名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cpuexecutioncap", value = "需要修改的CPU峰值", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "cpus", value = "需要修改的CPU数量", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "cputype", value = "需要修改的CPU型号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cpuhz", value = "需要修改的CPU频率", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "memory", value = "需要修改的内存 (MB)", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "需要修改的产品价格", dataType = "Float", paramType = "query"),
            @ApiImplicitParam(name = "osid", value = "需要修改的默认系统ID", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "defalutnatport", value = "需要修改的默认端口数量", dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "resetExample", method = RequestMethod.POST, produces = "application/json")
    public StateEntity resetExample(@RequestParam("token") String token, ExampleEntity exampleEntity) {
        return productService.resetExample(token, exampleEntity);
    }


}
