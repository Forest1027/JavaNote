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
		1. Spring的AOP为动态AOP，实现的技术为： JDK提供的动态代理技术 和 CGLIB(动态字节码增强技术)
		2. JDK代理（基于接口的代理）:代理对象和目标对象是兄弟关系，都有一个共同的父接口
		3. CGLIB代理（字节码代理）：代理对象和目标对象是子父关系
		4. spring采用的是哪一种动态机制:
				如果目标对象，有接口，优先使用jdk动态代理
				如果目标对象，无接口，使用cglib动态代理
		5. AOP相关术语介绍
			1. 目标对象target:   被增强的对象
			2. 连接点join point: 所有的方法
			3. 切入点pointcut:   实际插入增强的方法
			4. 通知advice:       增强的方法
			5. 切面aspect(关系): 通知和切点的结合，指定把什么（通知）插入到哪里（切点）去 --->关系
			6. 织入weaving:      通知插入到切点的过程
			7. 代理 Proxy:       通知插入到切点对目标对象进行增强通过代理来完成
			8. 引介 introduction:作用类上
3. Spring-Bean
3. Spring中的设计模式
4. Spring中常用的注解
4. 优点

Spring和SpringMVC是父子容器的关系。子容器更为强大。理论上，配置SpringMVC的配置文件就好了，但是为了好维护、方便扩展，我们还是会用到Spring的配置文件，而且拆成了dao、service等。

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
	1. MyBatis不是完全的orm框架，Hibernate是完全的orm框架。
	2. MyBatis的sql语句不通用，Hibernate的数据库无关性好。

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
	2. View
	3. Controller
3. 报404的地方
	1. 前端控制器（拦截请求、渲染视图时）
	2. 处理器适配器（请求路径与handler匹配）
4. SpringMVC和Struts2的区别
	1. springmvc的入口是一个servlet即前端控制器，而struts2入口是一个filter过虑器。
	2. springmvc是基于方法开发(一个url对应一个方法)，请求参数传递到方法的形参，可以设计为单例或多例(建议单例)，struts2是基于类开发，传递参数是通过类的属性，只能设计为多例。
	3. Struts采用值栈存储请求和响应的数据，通过OGNL存取数据， springmvc通过参数解析器是将request请求内容解析，并给方法形参赋值，将数据和视图封装成ModelAndView对象，最后又将ModelAndView中的模型数据通过reques域传输到页面。Jsp视图解析器默认使用jstl。

ResponseBody：得到的值直接返回给浏览器

## lucene和solr
1. 什么是全文检索
2. 使用流程
	1. 原始文档
		1. 要找的最终目的
	2. 包装成文档对象
		1. 将其重要特点等抽象出来
solr和elasticsearch的区别