package win.simple.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import win.simple.dao.ProductDao;
import win.simple.dao.UserDao;
import win.simple.dao.WebDao;
import win.simple.entity.StateEntity;
import win.simple.serivce.WebService;

@Service
public class WebServiceImpl implements WebService {

    @Autowired
    private WebDao webDao;

    @Override
    public StateEntity webInfo(String token) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(200);
        stateEntity.setMsg("ok");
        stateEntity.setData(webDao.webInfo());
        return stateEntity;
    }

}
