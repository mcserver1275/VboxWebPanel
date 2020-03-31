# VboxWebPanel

>这是一个简单的Vbox管理面板
>这是一个使用SSM框架开发的Web项目

**搭建方法**
>**准备工具 (这些东西百度都能搜到)**
>1. Redis
>2. Mysql
>3. Tomcat (用来跑后端的)
>4. PHPStudy (用来跑前端，不一定要用这个)
>5. Oracle VM VirtualBox


>**开始搭建**
>1. 打开VirtualBox的根目录，复制路径到环境里 (打开CMD输入VboxManage有反应就行了)
>2. 创建数据库，执行vps.sql生成字段
>3. 下载的VPSPanel通过ide编译成war包放到tomcat跑会生成目录关掉tomcat，修改里面的db.properties修改对应数据库
>4. 默认Redis没有启用验证，所以可以直接运行redis-server.exe (在你安装的Redis目录下)
>5. VPSPanelWeb文件放到PHPStudy里，修改VPSPanelWeb目录下的js/function.js文件
> ``
    var apiAddress = "http://localhost:8080/";
> ``
> 修改成你的tomcat后端地址

>**注意**
>war包的名字请改成ROOT.war
>如果你生成的war包名字叫VPSPanel.war，就修改成ROOT.war
>这样可以防止访问后端地址可以不用输入项目名
>
>例如:
>VPSPanel.war > http://localhost:8080/VPSPanel/
>ROOT.war > http://localhost:8080/

>**啰嗦**
>这个项目开发没多久，所以很多功能没有完善
>如果你对项目感兴趣，可以提供一些代码的话欢迎加我的QQ：1275886165，一起讨论