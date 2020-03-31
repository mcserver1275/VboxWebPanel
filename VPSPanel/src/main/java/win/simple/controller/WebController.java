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
import win.simple.serivce.WebService;

@RestController
@RequestMapping(value = "web")
@Api(tags = "网站模块")
public class WebController {

    @Autowired
    WebService webService;

    @ApiOperation(value = "网站信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "webinfo", method = RequestMethod.POST, produces = "application/json")
    public StateEntity webInfo(@RequestParam("token") String token) {
        return webService.webInfo(token);
    }

}
