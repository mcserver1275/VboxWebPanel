package win.simple.dao;

import win.simple.entity.StateEntity;
import win.simple.entity.WebInfoEntity;

public interface WebDao {

    /**
     * 获取完整的设置信息
     * @return
     */
    WebInfoEntity webInfo();

}
