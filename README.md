# 尼特社区-NiterForum
供初学者，学习、交流使用，喜欢的话，恳请给个star(*❦ω❦)。

 [介绍视频（欢迎留言收藏）](https://www.bilibili.com/video/av93706388 "视频介绍")
 
 [NiterApp（欢迎下载体验）](https://m3w.cn/niter "欢迎下载")。完美适配了NiterForum，支持app端扫码登录！
## 在线体验
[https://niter.cn/forum](https://niter.cn/forum "https://niter.cn/")

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
13. 验证码：vaptcha
14. 扫码登录
15. 身份验证:JWT

## 主要功能

#### 帖子相关
1. 发帖
2. 编辑
3. 点赞
4. 收藏
5. 回复[（支持楼中楼回复）](https://niter.cn/p/80 "支持楼中楼回复")
6. 视频帖[支持插入iframe代码和video视频链接，高度完美自适应](https://niter.cn/p/172 "视频帖")
7. [阅读权限](https://niter.cn/p/102 "阅读权限")
8. 帖子分类
9. 话题标签
10. 图片处理[（图片审核，图片水印，图片压缩，头像智能剪切）](https://niter.cn/p/162 "图片水印")
11. 置顶帖
12. 精华帖
13. 内容审核[（支持图片与文本智能审核）](https://niter.cn/p/157 "内容审核")
14. 分享[（支持web端与移动app端）](https://niter.cn/p/169 "分享")。
15. 管理面板[（支持加精、置顶、删除、提升、快改等操作）](https://niter.cn/p/148 "分享")。

#### 用户相关
1. 登录（八大登录方式-[支持app端扫码登录](https://niter.cn/p/171 "扫码登录")）
2. 注册（支持使用手机、邮箱、QQ、微博、百度、Github注册账号）
3. [账号体系(绑定账户)](https://niter.cn/p/83 "账号体系")（手机号、邮箱号、QQ、微博、百度、Github六合一）
4. 上传头像[(支持人脸自动定位)](https://niter.cn/p/107 "支持人脸自动定位")
5. [积分策略](https://niter.cn/p/78 "积分策略")
6. [用户组晋升](https://niter.cn/p/83 "用户组晋升")
7. 会员特权
8. 消息通知
9. 个人主页
10. 更新资料
11. 设置、修改密码

#### 更多功能
1. 搜索
2. 排序
3. 聊天室
4. 瀑布流模式
5. 看看板块[定期更新新闻资讯](https://niter.cn/news "看看")
6. 针对搜索引擎进行优化（SEO）
7. 验证码-防灌水、攻击
8. 智能标签[根据正文内容自动生成标签](https://niter.cn/p/133 "看看")
9. 身份验证JWT

## 快速运行
1. 安装必备工具  
JDK，Maven
2. 克隆代码到本地  
3. 将[resources](/src/main/resources/ "resources")目录下的[niter.sql](/src/main/resources/niter.sql "niter.sql")导入新创建的数据库。
4. 根据[提示与说明](https://niter.cn/p/135/ "提示与说明")，编辑[resources](/src/main/resources/ "resources")目录下的[application.properties](/src/main/resources/application.properties "application.properties")文件。 [（完整视频教程）](https://www.bilibili.com/video/av94451055/ "详细配置教程")
5. 编辑[resources](/src/main/resources/ "resources")目录下的[generatorConfig.xml](/src/main/resources/generatorConfig.xml "generatorConfig.xml")文件，配置数据库相关信息（只需修改数据库链接、用户名、密码）。
6. 运行打包命令
   ```sh 
   mvn clean package
   ```
 
7. 部署到服务器并运行项目  
   ```sh
    nohup java -jar NiterForum-2.5.jar >temp.txt &   
    ```
8. 访问项目
   ```
   https://yourdomain
   ```

[(NiterForum安装配置常见问题汇总)](https://niter.cn/p/255/ "点击这里查看更多提示")

## 项目演示

更多演示，请移步：[https://niter.cn/forum](https://niter.cn/forum) 

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

官方交流社区：[https://niter.cn/forum](https://niter.cn/forum "欢迎交流")

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

### 打赏我们
扫码二维码或者点击下方图片向我们打赏。您的每一份捐赠，对我们都是莫大的鼓励与支持,一块也是爱(* ￣3)(ε￣ *)

[![使用手机支付宝、微信、QQ扫码，向我打赏](images/qrcode_small.png "使用手机支付宝、微信、QQ扫码，向我打赏")](https://api-1251590924.cos.ap-nanjing.myqcloud.com/qrcode/index.html "使用手机支付宝、微信、QQ扫码，向我打赏")

## 其它
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
