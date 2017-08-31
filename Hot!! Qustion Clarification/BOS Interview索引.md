# BOS Interview索引
1. 项目简介
	1. 项目是什么？
	2. 开发周期、人员配置？
	3. 开发环境
		1. Ideal编译器（待查）
		2. Maven项目管理
		3. git版本控制
		4. Oracle数据库
			1. Oracle与MySQL的对比（待查）
		5. JDK8
	4. 项目架构（宏观、略说）
		1. management 公司员工执行后台管理
		2. fore 客户浏览的前台页面
		3. crm 客户信息管理
	5. 自己负责哪些板块
2. 项目流程+核心技术
	0. 打底技术
		1. SpringMVC+Spring+Spring Data（Hibernate）
	1. 注册
		1. 前端页面 *AnugularJS*
		2. “获取验证码”功能 *AngularJS 双向数据绑定*
		3. 短信发送验证码 
			1. 第三方短信平台-阿里大于
			2. ActiveMQ解决高并发
				1. bore发消息给ActiveMQ
				2. ActiveMQ调用第三方短信平台发送短信
		4. 注册功能后台实现
			1. 客户信息存入crm系统-->WebServiceCXF
			2. 发送激活邮件-->JavaMail发送邮件+Redis定时保存激活码
	2. 登录及注销
		1. AngularJS
		2. WebServiceCXF
		3. 权限控制
			1. ApacheShiro 判断用户登录与否、不同用户看到不同页面、注销功能
			2. ehCache 缓存框架，减少与数据库的交互(shiro默认支持)登录时验证，第一次查询数据库，之后就走缓存了。
	3. 活动展示
		1. 【management】后台管理活动
			1. 在线html编辑器-KindEditor--让用户在网站上获得所见即所得的编辑效果
		2. 【fore】列表分页展示
			1. Bootstrap+AngularJS
			2. WebServieCXF。跨系统带来两个小问题
				1. Spring data自带的Page对象，不是一个纯Pojo类，没有setter方法，无法完成系统间的数据传递。因此，需要自定义一个PageData对象
				2. fore系统显示宣传图片时，由于当时存储的时候是相对路径存储，而foremanagement不再一台服务器上，因此要以http开头，加上端口号，才能正常显示
			3. Quartz任务调度框架
				1. 设置每隔一分钟执行一次update语句
				2. update语句：将活动结束时间小于当前时间的记录中的活动状态更改。
		3. 【fore】详情展示
			1. 静态页面化技术--Freemarker
				1. 模板+Java数据对象
				2. 减少与数据库的交互，提高效率
3. 其他小技术点 depends on which program i choose
	1. citypicker 省市三级联动
	2. Apache POI 解析excel
	3. ocupload 一键上传
	4. elastic search