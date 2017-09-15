# QC for 框架
## Spring
1. 体系结构（5个部分）
	1. Core Container
		1. Beans
		2. Core-IOC、DI
		3. Context-ApplicationContext
		4. SpEL
	2. Data Access
		1. JDBC-支持jdbc
		2. ORM-支持orm框架
		3. OXM-对象xml映射
		4. JMS-java消息服务
		5. Transactions-事务控制
	3. Web
		1. web-基本的web功能，如文件上传
		2. webmvc-提供SpringMVC
		3. websocket
		4. webportlet
	4. Test
	5. AOP、Aspects、Instrumentation、Messaging
2. 核心概念
	1. IOC
		1. 由Spring来控制对象的生命周期、对象间的关系
	2. DI
	3. AOP
3. Spring-Bean
3. Spring中的设计模式
4. 优点


## MyBatis
1. 是什么？
	1. 持久层框架，对JDBC进行封装
	2. 非完全的orm框架
		1. orm：操作对象的时候，数据库的数据会改变；查询数据时，自动封装到对象中。
		2. 非完全：【做到后部分】使用MyBatis查询，可以得到对象；操作对象，不能改变数据库。
	3. 自己写SQL
		1. 灵活

> 为什么不用注解开发？
> 
> 修改备注后要重新编译，重新打包；不用注解开发，修改配置文件，重新启动服务器就好了。

MyBatis优缺点
	1. 缺点：sql语句不通用
	2. 优点：灵活

2. MyBatis的配置
	1. 入口：SqlMapConfig.xml
	2. 数据库四要素
	3. 别名
	4. 插件
	5. mapper
3. mapper代理开发【重点】
	1. 规范
		1. Mapper.xml文件中的namespace与mapper接口的类路径相同。
		2. Mapper接口方法名和Mapper.xml中定义的每个statement的id相同 
		3. Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql 的parameterType的类型相同
		4. Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同
	2. 好处
		1. 只用写接口，不用写实现类
4. Hibernate和MyBatis的区别

## SpringMVC【重要】
1. 流程
	1. 用户发送请求-->前端控制器
	2. 交给处理器映射器，进行解析，得到很多对象-->前端控制器
	3. 交给处理器适配器-->通过传来的信息，找到handler，执行逻辑，返回modelandview-->前端控制器
	4. 把view对象交给视图解析器解析，返回文件路径-->前端控制器
	5. 前端控制器用model数据将返回的view对象渲染-->jsp-->返回用户
2. MVC
	1. Model-处理数据
		1. 处理器适配器
		2. handler处理器
		3. 处理器适配器
		4. 视图解析器
	2. 
	3. 
3. 报404的地方
	1. 前端控制器（拦截请求、渲染视图时）
	2. 处理器适配器（请求路径与handler匹配）

ResponseBody：得到的值直接返回给浏览器

## lucene和solr
1. 什么是全文检索
2. 使用流程
	1. 原始文档
		1. 要找的最终目的
	2. 包装成文档对象
		1. 将其重要特点等抽象出来
solr和elasticsearch的区别