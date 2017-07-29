#2017/7/29
##1、概述
项目分析

开发工具：STS、maven集成配置、Hbuilder

导入项目，对项目进行版本控制 Git

后台界面开发框架：jQuery框架、Ztree树形菜单

##2、系统背景及核心业务
后台管理系统 7个部分

1. 基础设置（物流业务管理 元数据）；取派标准、取派时间管理、车辆管理、快递员管理、区域管理
2. 取派；下单管理、取件管理、配送管理
3. 中转；货物运输过程中，中转点出入库操作
4. 路由；运输线路、运输交通工具
5. PDA；快递员无线通讯设备
6. 财务；快递费用处理
7. 管理报表需求；针对物流的业务数据进行分析，产生报表

##3、项目开发相关
OA(Office Automation办公自动化)、CRM(Customer Relationship Management)、ERP(Enterprise Resourse Planning)基于MIS（信息管理系统）

BOS(Business Operating System)业务操作系统

**软件开发流程**

瀑布开发模型

1. 需求调研分析
2. 设计（概要设计-从软件整体出发做整体的架构设计，详细设计-每个功能点如何设计）
3. 编码
4. 测试
5. 实施和运维

##4、开发环境和开发技术
**开发环境**

**开发技术**
Server端架构：ssh+SpringData(简化持久层)

后台管理系统页面架构：jQuery EasyUI框架

前端互联网那个系统页面架构：BootStrap+AngularJS

Ecel解析、生成：POI技术

运程调用：基于Restful风格CXF编程

第三方短信平台、邮件平台使用

Redis缓存、ActiveMQ消息队列

搜索服务器ElasticSearch安装配送使用，SpringData操作ElasticSearch服务器

定时调度框架：Quartz

权限管理框架：ApacheShiro

##5、开发工具的使用
STS 版本匹配

IntelliJIDEA 使用风格与Eclipse有区别，流行于互联网公司

HBuilder

##6、STS与Maven集成
##7、STS与Hbuilder同步开发
##8、Git与TortoiseGit
SVN必须给予远程仓库进行版本控制。

Git是分布式版本工具（除了具有远程仓库外，还具有本地仓库，可以在离线情况下进行版本控制）

##9、Git与svn的区别
Git允许本地有仓库。每个本地的仓库都有版本控制信息。即使没有网络，也可以基于本地的版本控制来控制版本。

SVN版本控制信息保存在服务器。无法联网就无法进行版本控制。

##10、Git的使用
##11、Git冲突的解决
两个人操作同一个代码，在push到中央仓库的时候，就会产生冲突。

首先commit(不会出现冲突)，在push的时候就会提示有冲突。这时再pull一下，解决冲突edit conflit
(两段代码都要还是要其中某一个)。解决好了之后，再push。

##12、在线仓库进行版本控制
##13、
iframe与ajax的区别？

为什么使用easyui？

##14、eayui页面布局
##17、
父节点和子节点的id是否有什么规定？？