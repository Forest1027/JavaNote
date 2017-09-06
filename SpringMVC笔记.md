# SpringMVC
1. spring
AOP-用在事务管理器
Core-用来创建对象

2. 架构、流程
	1. 处理器映射器、处理器适配器、视图解析器称为springmvc的三大组件。
	2. 处理器映射器
		1. 根据客户的请求(发来的路径等)找到处理器
	3. 处理器适配器
		1. DispatcherServlet只负责接收ModelAndView。但是cotroller层可以返回各种各样的对象，所以需要适配器来将其统一转化成ModelAndView
	4. 视图解析器
		1. View Resolver负责将处理结果生成View视图，View Resolver首先根据逻辑视图名解析成物理视图名即具体的页面地址，再生成View视图对象，最后对View进行渲染将处理结果通过页面展示给用户。
3. SpringMVC如何配置的？
4. ssm整合
5. ssm参数传递

6. 重定向与请求转发
	1. 都可以用
	2. 重定向：注意参数不要掉
	3. 请求转发：注意路径（页面上的相对路径，可能会出错）
	4. 退出一定要使用重定向（对安全性要求比较高）

>注意：WEB-Inf文件夹下的资源，不能在公共页面上面访问，这是基于安全性考虑。如果可以访问，那么就可以访问到web.xml-->applicationContext-dao-->db.properties-->数据库及密码