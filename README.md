# 尼特社区-NiterForum
供初学者，学习、交流使用，喜欢的话，恳请给个star(*❦ω❦)
## 在线体验
[https://niter.cn/](https://niter.cn/ "https://niter.cn/")

## 技术栈
1. SpringBoot框架。
2. Thymeleaf模板引擎。
3. 数据访问层：Mybatis，mybatis generator。
4. 数据库：MySql。
5. 服务器：内置Tomcat。
6. 前端相关:Jquery,Bootstrap，Ajax，Layer等。
7. 前端模板：LayUI_fly社区模板。
8. 文件上传：腾讯云COS对象存储。
9. 短信验证：极光短信。
10. 邮箱验证：腾讯企业邮箱。
11. 富文本编辑器：WangEditor。
12. OAuth2授权登入（QQ、微博、百度、Github）

## 主要功能

#### 帖子相关
1. 发帖
2. 编辑
3. 点赞
4. 收藏
5. 回复[（支持楼中楼回复）](https://niter.cn/p/80 "支持楼中楼回复")
6. [视频帖](https://niter.cn/p/98 "视频帖")
7. [阅读权限](https://niter.cn/p/102 "阅读权限")
8. 帖子分类
9. 话题标签
10. [图片水印](https://niter.cn/p/33 "图片水印")
11. 置顶帖
12. 精华帖

#### 用户相关
1. 登录
2. 注册
3. [账号体系(绑定账户)](https://niter.cn/p/83 "账号体系")（手机号、邮箱号、QQ、微博、百度、Github六合一）
4. 上传头像[(支持人脸自动定位)](https://niter.cn/p/107 "支持人脸自动定位")
5. [积分策略](https://niter.cn/p/78 "积分策略")
6. [用户组晋升](https://niter.cn/p/83 "用户组晋升")
7. 会员特权
8. 消息通知
9. 个人主页

#### 更多功能
1. 搜索
2. 排序
3. 聊天室

## 快速运行
1. 安装必备工具  
JDK，Maven
2. 克隆代码到本地  
3. 将[resources](/src/main/resources/ "resources")目录下的[niter.sql](/src/main/resources/niter.sql "niter.sql")导入新创建的数据库。
4. 根据提示，编辑[resources](/src/main/resources/ "resources")目录下的[application.properties](/src/main/resources/application.properties "application.properties")文件。
5. 编辑[resources](/src/main/resources/ "resources")目录下的[generatorConfig.xml](/src/main/resources/generatorConfig.xml "generatorConfig.xml")文件，配置数据库相关信息。
6. 运行打包命令
   ```sh 
   mvn clean package
   ```
 
7. 部署到服务器并运行项目  
   ```sh
    nohup java -jar NiterForum-2.0.1-SNAPSHOT.jar >temp.txt &   
    ```
8. 访问项目
   ```
   https://yourdomain
   ```

## 项目演示

更多演示，请移步：[https://niter.cn/](https://niter.cn/) 

## 目录结构
   ```
       ├─cn.niter.forum         应用目录
       │  ├─controller         控制器目录
       │  ├─modal              映射数据库实体类
       │  ├─dto                数据传输层
       │  ├─intercepter        拦截器
       │  ├─enums              枚举类
       │  ├─provider           提供类
       │  ├─service            业务逻辑层
       │  ├─advice             异常处理
       │  ├─exception          自定义异常
       │  ├─dao                数据访问层
       │  ├─utils              工具类
       │__├─config             配置类
   ``` 
     

## 更多链接
### 联系我们

尼特社区官方交流群：[955295791](https://jq.qq.com/?_wv=1027&k=5uPXrY2 "欢迎加入")

官方交流社区：[https://niter.cn/](https://niter.cn/ "欢迎交流")

更新日志：[https://niter.cn/p/26](https://niter.cn/p/26/ "欢迎订阅")

### 资料
[Spring 文档](https://spring.io/guides)    
[Spring Web](https://spring.io/guides/gs/serving-web-content/)   
[es](https://elasticsearch.cn/explore)    
[Github deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)    
[Bootstrap](https://v3.bootcss.com/getting-started/)    
[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)    
[Spring](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)    
[菜鸟教程](https://www.runoob.com/mysql/mysql-insert-query.html)    
[Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values)    
[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[Spring MVC](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor)  
[Markdown 插件](http://editor.md.ipandao.com/)   
[UFfile SDK](https://github.com/ucloud/ufile-sdk-java)  
[Count(*) VS Count(1)](https://mp.weixin.qq.com/s/Rwpke4BHu7Fz7KOpE2d3Lw)  

### 工具
[Git](https://git-scm.com/download)   
[Visual Paradigm](https://www.visual-paradigm.com)    
[Flyway](https://flywaydb.org/getstarted/firststeps/maven)  
[Lombok](https://www.projectlombok.org)    
[ctotree](https://www.octotree.io/)   
[Table of content sidebar](https://chrome.google.com/webstore/detail/table-of-contents-sidebar/ohohkfheangmbedkgechjkmbepeikkej)    
[One Tab](https://chrome.google.com/webstore/detail/chphlpgkkbolifaimnlloiipkdnihall)    
[Live Reload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei/related)  
[Postman](https://chrome.google.com/webstore/detail/coohjcphdfgbiolnekdpbcijmhambjff)

### 特别感谢
[NiterForum](https://niter.cn/) 
[码问](http://www.mawen.co/) 
[LayUI](https://fly.layui.com/) 


## 其它
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```

