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

Excel解析、生成：POI技术

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

#2017/7/30
##13、页面分析
iframe与ajax的区别？

为什么使用easyui？

##14、eayui页面布局
easyui-layout

可以在直接在标签上加class属性，或者使用js
$("#id").layout()

easyUI所有组件都能用加样式或者js两种方法进行初始化。

##15、折叠面板
easyui-accordion,一定要设置title属性，不然不能出效果。

如果要让面板将父类区域自动填满，加fit:true

##16、选项卡面板
easyui-tabs

注意如何通过点击添加函数

	$("#bLink").click(function() {
		$('#myTab').tabs('add', {
			title: '新面板',
			closable: true
		});
	})

##17、ztree
在实现点击子节点，打开对应选项卡功能的时候，核心代码如下：

	callback: {
		onClick: function(event, treeId, treeNode, clickFlag) {
			var content = "<div style='width:100%;height:100%;overflow: hidden;'><iframe style='width: 100%;height: 100%;border: 0;' scrolling='auto' src='" + treeNode.page + "'></iframe></div>";
				//判断是否是有page属性
			if(treeNode.page != undefined && treeNode.page != "") {
				//判断名为name的选项卡面板是否存在
				if($("#myTab").tabs('exists', treeNode.name)) {
					//存在--->选中
					$("#myTab").tabs('select', treeNode);
				} else {
					//不存在--->添加
					$('#myTab').tabs('add', {
						title: treeNode.name,
						content:content,
						closable: true
					});
				}
			}
		}
	}

其中：

	var content = "<div style='width:100%;height:100%;overflow: hidden;'><iframe style='width: 100%;height: 100%;border: 0;' scrolling='auto' src='" + treeNode.page + "'></iframe></div>";

这里各种设置100%是要让content占满选项卡的区域

框架中给出的点击函数：

	//这个绑定的点击函数已经封装了识别“点击了谁”的方法，使用时只需要使用方法上声明的参数treeNode去调用被点击节点的属性就好。

	function zTreeOnClick(event, treeId, treeNode) {
	    alert(treeNode.tId + ", " + treeNode.name);
	};
	var setting = {
		callback: {
			onClick: zTreeOnClick
		}
	};

##1、选项卡自定义右键菜单
easyui-menu

菜单的初始化时隐藏的，要看到菜单，就要使用'show'

有两件事要做

1. 点击鼠标右键-->弹出菜单onContextMenu
2. 菜单出现的位置-->e.pageX,e.pageY

>注意：
>
>1.当要给菜单里面的每一项绑定点击事件的时候，只能使用$("#id").menu({onClick:.....}) 的方法，不能使用class标明了类为easyui-menu后，直接在菜单中注明id绑定点击事件($("#id").click(function(){})
>
>2.在遍历时，只能用this，不能用tabs[i]（尚不明原因）。如下图对比 

	//遍历一
	$(tabs).each(function() {
		var tempTitle = this.panel("options").title;
		if(tempTitle != theTitle){
			$("#myTab").tabs("close",tempTitle);
		}
	})
	//遍历二(爆bug)
	$(tabs).each(function(i) {
		var tempTitle = tabs[i].panel("options").title;
		if(tempTitle != theTitle){
			$("#myTab").tabs("close",tempTitle);
		}
	})

##2、下拉菜单menubutton
1. 在页面提供超链接，设置class="easyui-menubutton".使用data-options="iconCls:'icon-save'/*可选*/"切入图标
2. 制作下拉菜单
3. 设置超链接a标签的menu属性指向下拉菜单

##3、消息窗口
警告窗口

确认窗口

输入窗口

右下角窗口

进度条窗口

##5、基础设置模块业务分析

#2017/7/31
##9、收派标准添加窗口制作
整合jpa的代码

	<!-- 整合JPA配置 -->
	<bean id="entityManagerFactory"
		class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="packagesToScan" value="cn.itcast.bos.domain" />
		<property name="persistenceProvider">
			<bean class="org.hibernate.jpa.HibernatePersistenceProvider" />
		</property>
		<property name="jpaVendorAdapter">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
				<property name="generateDdl" value="true" /><!-- 是否自动建表 -->
				<property name="database" value="ORACLE" />
				<property name="databasePlatform" value="org.hibernate.dialect.Oracle10gDialect" />
				<property name="showSql" value="true" />
			</bean>
		</property>
		<property name="jpaDialect">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaDialect" />
		</property>

	<!--下面是可以不配置的-->
		<property name="jpaPropertyMap">
			<map>
				<entry key="hibernate.query.substitutions" value="true 1, false 0" />
				<entry key="hibernate.default_batch_fetch_size" value="16" />
				<entry key="hibernate.max_fetch_depth" value="2" />
				<entry key="hibernate.generate_statistics" value="true" />
				<entry key="hibernate.bytecode.use_reflection_optimizer"
					value="true" />
				<entry key="hibernate.cache.use_second_level_cache" value="false" />
				<entry key="hibernate.cache.use_query_cache" value="false" />
			</map>
		</property>
	</bean>

整合jpa相比于整合hibernate，多出了persistenceProvider和jpaVendorAdapter。整合hibernate时,只需要配置hibernate的properties就好了。

>注意：
>出现complition failure时，将父工程runas-mavenInstall（**这个有什么用？**）。然后在子项目上runas-complie一下，重新编译

在此处，需要在点击‘增加’的时候，弹出表单。所以要做的是在点击该按钮的时候触发点击事件

##10、收派标准添加表单客户端校验
##11、服务端编写
struts2框架使用，要注意包名含有action、struts、web等，类名以action结尾。这样才能被扫描到

>注意：
>
>转发和重定向的区别：
>
>1、转发地址栏不会变，重定向地址栏会变化
>
>2、转发一次请求一次响应，重定向二次请求二次响应
>
>3、转发只能在当前容器跳转，重定向可以跳到任意网站

