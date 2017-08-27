# QC for Apache Shiro
[Shiro学习详解 ](http://blog.csdn.net/he90227/article/details/38680499)

[第一章 Shiro简介——《跟我学Shiro》](http://jinnianshilongnian.iteye.com/blog/2018936)

## Apache Shiro
1. 是什么？优缺点？
	1. Apache旗下的开源项目，是一个强大且易用的java安全框架，执行身份验证、授权、加密、会话管理功能。
	2. shiro使用简单、轻量、不依赖spring
	3. Spring Security也是一个安全框架，功能强大，但是相比于shiro，spring security的使用更为复杂，并且依赖于spring
2. 有什么用？
	1. **Authentication**
		1. 认证--用户登录、身份识别
	2. **Authorization**
		1. 授权--用户有哪些权限、角色
	3. **Cryptography**
		1. 数据加密
		2. 如注册时，用户输入的密码会通过算法加密，转换成无意义的字符串
	4. **Session Management**
		1. 会话管理
		2. 作用一：用户再停留在某页面长时间无动作，再访问其他链接时，会重定向到登录页面。而不需要程序员在Servlet中不停的判断Session中是否包含User对象。
		3. 作用二：针对不同的模块采取不同的会话处理。以淘宝为例，用户注册淘宝以后可以选择记住用户名和密码。之后再次访问就无需登陆。但是如果你要访问支付宝或购物车等链接依然需要用户确认身份。
	5. Web Integration
		1. web系统集成
	6. Integrations
		1. 与Spring、缓存框架的集成
3. 原理？
	1. 运行流程
		1. Subject
			1. 主体，代表“用户”。与当前应用交互的任何东西都是Subject
			2. 所有Subject(门面)都绑定到SecurityManager，与Subject的所有交互都会委托给SecurityManager(实际执行者)
		3. SecurityManager
			1. 安全管理器。管理所有Subject，是shiro的核心。它负责与其他组件交互
		2. Realm
			1. 域。SecurityManager需要验证用户身份、权限等，需要获取数据。Realm就是其获取数据的途径。
			2. 我们通常通过自定义的realm与数据库等交互，从而获得真实的数据
4. 应用场景？