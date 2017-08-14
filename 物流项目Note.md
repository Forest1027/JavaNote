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

**补充：**

@Actions注解的作用

	 @Actions( { @Action("/one/url"), @Action("/another/url") })  
    public String execute() {  
        message = "经过HelloWorld3的处理";  
        return SUCCESS;  
    }  

本例中在String execute()方法上使用了@Actions和@Action注解。其中@Actions注解包含@Action("/one/url")和@Action("/another/url")注解。这样的注解除了可以通过http://应用服务器ip:端口/应用/hello-world3访问com.example.actions.HelloWorld3,并最终显示http://应用服务器ip:端口/应用/hello-world3.jsp之外，还有两种访问方式：

即：

通过http://应用服务器ip:端口/应用/one/url,访问com.example.actions.HelloWorld3,并最终显示http://应用服务器ip:端口/应用/one/url.jsp。

通过http://应用服务器ip:端口/应用/another/url,访问com.example.actions.HelloWorld3,并最终显示http://应用服务器ip:端口/应用/another/url.jsp。
这样就达到了一个ACTION中的一个方法，响应多个不同的URL的效果。

 使用@Action注解解决一个ACTION中的多个方法，其中每个方法响应不同的URL.

这是实际项目开发过程中最常用的。

#2017/8/2
##1、SpringData原理和api使用
	<!-- 整合Springdata -->
	<jpa:repositories base-package="com.forest.bos.dao"></jpa:repositories>

*注意事项：*

在整合springdata的时候，指定包一定要指定继承了jparepository的接口所在的那个包，包里只能有这种接口。所以不能写com.forest.bos：）

**SpringData的意义**

jpa（oracle制定）只整合了关系型数据库，因此SpringData（Spring制定）出现了。

SpringData整合了所有。

>在与第三方整合这方面，Spring做了持久化这一块的工作，我个人的感觉是Spring希望把持久化这块内容也拿下。
>
>于是就有了Spring-data-**这一系列包。包括，Spring-data-jpa,Spring-data-template,Spring-data-mongodb,Spring-data-redis，还有个民间产品，mybatis-spring，和前面类似，这是和mybatis整合的第三方包，这些都是干的持久化工具干的事儿。

SpringData出现的目的就是为了简化、统一持久层各种实现技术的api。

两个jar包：spring-data-commons一套标准api和
spring-data-jpa基于整合jpa的实现

为什么springdata只写接口就可以完成操作？

代理就是创造某个接口的实现类。调用代理对象的时候实现的方法其实是SimpleJpaRepository的方法。

##2、SpringData 查询的使用
1. 根据方法命名规则自动生成
	方法中啥也不写
		
		public interface StandardRepository extends JpaRepository<Standard,Integer>{
			public List<Standard> findByName(String name);
		}

2. 不按命名规则，配置@Query
	指定语句

		public interface StandardRepository extends JpaRepository<Standard,Integer>{
			@Query(value="from Standard where name=?",nativeQuery=false)
			public List<Standard> queryName(String name);
		}

3. 不安命名规则写查询方法，配置@Query，但不写语句。
	
		//实体类中定义
		@NamedQueries({
			@NamedQuery(name="Standard.queryName2",query="from Standard where name=?")
		})
		public class Standard {
			......
		}
		//在dao层定义
		public interface StandardRepository extends JpaRepository<Standard,Integer>{
			
			@Query
			public List<Standard> queryName2(String name);
		}
	
##3、SpringData 修改和删除的使用
使用@Query，搭配@Modifying标记修改。

	@Query(value="update Customer set fixedAreaId=?2 where id=?1")
	@Modifying
	public void updateCIdWithFAId(String id, String fixedAreaId);

注意：单体测试DAO，要设置事务@Transactional，并且要设置不回滚@Rollback(false)

##4、数据表格datagrid的简单使用
##5、分页原理分析
给服务器发-当前页码，每页多少条
服务器回复-总记录数，当前页的记录

##6、收派标准分页查询
1. action中
	1. 属性驱动接收两个参数（page rows）
	2. 调用业务层查询总记录数（total）
	3. 调用业务层查询当前页的数据（pageable）
	4. 2和3实际上不需要分开做。使用spring data封装的page就可以了，会将两个数据都包含进去
2. Service中
	1. 调用StandardRepository的findAll方法，传入pageable
3. dao中
	1. 继承jparepository

>注意：
>
>1.在action中将数据转换成json数据要使用到struts2-json-plugin包。另外要使用“json”这个result-type，action类必须继承json-default包的。
>
>2.PageRequest是Pageable的实现类。在构造PageRequest对象时，要传入page-1，因为PageRequest的page页码是从0开始的。

	private int page;
		private int rows;
	
		public void setPage(int page) {
			this.page = page;
		}
	
		public void setRows(int rows) {
			this.rows = rows;
		}
	
	@Action(value = "standard_pageQuery", results = { @Result(name = "success", type = "json") })
		public String query() {
			System.out.println("query");
			// 将接收的两个参数传递给业务层，调用业务层方法获取数据
			// PageRequest是Pageable的实现类--->下面是多态
			Pageable pageable = new PageRequest(page - 1, rows);
			Page<Standard> pageData = ss.findPageData(pageable);
			// 将数据转换成json数据，存入值栈
			Map<String, Object> result = new HashMap<>();
			result.put("total", pageData.getTotalElements());
			result.put("rows", pageData.getContent());
			ActionContext.getContext().getValueStack().push(result);
			return SUCCESS;
		}

	//前端页面
	// 收派标准信息表格
	$('#grid').datagrid({
		iconCls: 'icon-forward',
		fit: true,
		border: false,
		rownumbers: true,
		striped: true,
		pageList: [10, 20, 50],
		pagination: true,
		toolbar: toolbar,
		url: "../../standard_pageQuery",
		idField: 'id',
		columns: columns
	});
	//工具栏
	var toolbar = [{
		id: 'button-add',
		text: '增加',
		iconCls: 'icon-add',
		handler: function() {
			$("#standardWindow").window('open');
		}
	}, {
		id: 'button-edit',
		text: '修改',
		iconCls: 'icon-edit',
		handler: function() {
			alert('修改');
		}
	}, {
		id: 'button-delete',
		text: '作废',
		iconCls: 'icon-cancel',
		handler: function() {
			alert('作废');
		}
	}, {
		id: 'button-restore',
		text: '还原',
		iconCls: 'icon-save',
		handler: function() {
			alert('还原');
		}
	}];
	
	// 定义列
	var columns = [
		[{
			field: 'id',
			checkbox: true
		}, {
			field: 'name',
			title: '标准名称',
			width: 120,
			align: 'center'
		}, {
			field: 'minWeight',
			title: '最小重量',
			width: 120,
			align: 'center'
		}, {
			field: 'maxWeight',
			title: '最大重量',
			width: 120,
			align: 'center'
		}, {
			field: 'minLength',
			title: '最小长度',
			width: 120,
			align: 'center'
		}, {
			field: 'maxLength',
			title: '最大长度',
			width: 120,
			align: 'center'
		}, {
			field: 'operator',
			title: '操作人',
			width: 120,
			align: 'center'
		}, {
			field: 'operatingTime',
			title: '操作时间',
			width: 120,
			align: 'center'
		}, {
			field: 'operatingCompany',
			title: '操作单位',
			width: 120,
			align: 'center'
		}]
	];

##7、修改功能
需求：

1. 点击修改按钮，获取datagrid选中的一行数据。
	1. 绑定函数，函数中做判断，是否为一行。
2. 提供隐藏域，装载id
3. 使用load方法完成表单数据的回显

只需要将前台页面的逻辑写好就行。修改数据保存，与新增数据走的是同一个action，都是save。而JpaRepository里面的save方法里做了判断，如果不存在此id，就新增，存在此id，就修改。

>注意：
>
>只要修改了父工程，就要install一下父工程。因为子模块run的时候是去仓库找。

	//工具栏
	var toolbar = [{
		id: 'button-add',
		text: '增加',
		iconCls: 'icon-add',
		handler: function() {
			$("#standardWindow").window('open');
		}
	}, {
		id: 'button-edit',
		text: '修改',
		iconCls: 'icon-edit',
		handler: function() {
			//获取所有选中的记录
			var rows = $("#grid").datagrid('getSelections');
			//判断记录是否只有一条，如果不是-->弹出警告窗
			if (rows.length!=1) {
				$.messager.alert('警告','只能选择一条记录哦','warning');  
			} else{
				alert(JSON.stringify(rows));
				var row = rows[0];
				alert(JSON.stringify(row));
				$("#standardForm").form('load',row);
				//显示窗口
				$("#standardWindow").window('open');
			}
		}
	}

>注意：在使用load方法将数据回填到表单中时，一定要加上“var row = rows[0];”这个语句将数组转化成对象。详见一下json数据对比：

	//rows的json
	[
	    {
	        "id": 1,
	        "maxLength": 60,
	        "maxWeight": 60,
	        "minLength": 50,
	        "minWeight": 50,
	        "name": "50-60公斤",
	        "operatingCompany": "一公司",
	        "operator": "a"
	    }
	]

	//row的json
	{
	    "id": 1,
	    "maxLength": 60,
	    "maxWeight": 60,
	    "minLength": 50,
	    "minWeight": 50,
	    "name": "50-60公斤",
	    "operatingCompany": "一公司",
	    "operator": "a"
	}

##8、快递员管理概述
1. 快递员管理，依赖收派标准选择
2. 快递员分页查询

##9、快递员添加功能
1. jquery easyui window
2. jquery easyui form 客户端表单校验
3. spring data jpa提供save方法

**收派标准的列表显示 easyui-combobox**

	<input type="text" name="standard.id" 
	class="easyui-combobox" 
	data-options="required:true,valueField:'id',textField:'name',
	url:'../../standard_findAll.action'"/>

只需要在url里面添加action的路径，valueField用来记录选择的是哪一个standard，textField用来作为standard各个属性的代表显示在页面上。然后在后台写好对应的方法就好。

##10、添加功能的实现
**html**

1. 检验表单输入的元素，name字段是否与实体类 属性字段一致。
2. 对快递员添加表单。设置action和method
3. 使用easyui-form的校验功能
4. 添加点击事件
	1. 如果满足校验规则--->提交
	2. 不满足--->表单中提示

##11、无条件的分页查询
1. datagrid在页面加载后，会自动向url地址发送一次请求。传递参数page当前页码和rows每页显示的记录条数到服务器。
2. spring data提供pageable对象（pagerequest）接收两个参数
3. 调用spring data中page findall（pageable）方法查询 总记录数和当前页数据
4. 将page中的数据封装到一个自定义的map集合。将total和rows转换为json返回客户端。

在查询代码编写完成后，遇到如下bug:

*Caused by: org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.forest.bos.domain.base.Courier.fixedAreas, could not initialize proxy - no Session*

这是因为courier中有一个集合属性

	private Set<FixedArea> fixedAreas = new HashSet<FixedArea>();

这个属性，hibernate不会加载*(why?因为这个属性不存在于courier的表中，它放弃了这个属性的外键维护权！*)而在序列化的时候（*当返回sucess，就会将result转换成json数据。当转换成json数据的时候，就需要查询fixedArea这个属性*），又将其序列化进去了，而此时session已经关闭，因此报错。什么时候调用get方法？

解决方法：get方法上加下面的注解

	@JSON(serialize=false)
	public Set<FixedArea> getFixedAreas() {
		return fixedAreas;
	}

##12、有条件的分页查询
1. 制作一个查询按钮--->点击即显示查询表单
2. 在查询窗口条件，绑定到数据表格上。让数据表格发送请求时，自动携带条件。**使用datagrid的load方法***（在原有基础上，添加新的数据）*。


Json.Stringify()将对象转化成字符串

##13、后台接收
action类

	// 编写条件
		Specification<Courier> specification = new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				/*
				 * 参数说明 Root 用于获取属性字段， CriteriaQuery可以用于简单条件查询，
				 * CriteriaBuilder用于构造复杂条件查询
				 */
				/*
				 * courierNum:1 standard.name company type
				 */
				// 创建集合，接收条件
				List<Predicate> list = new ArrayList<>();
				// 判断表单中的那四个条件存不存在
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					// 员工号精确查询
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(courier.getCompany())) {
					// 公司 模糊查询
					Predicate p2 = cb.like(root.get("company").as(String.class), courier.getCompany());
					list.add(p2);
				}
				if (StringUtils.isNoneBlank(courier.getType())) {
					// 快递员类型精确查询
					Predicate p3 = cb.like(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				// 多表查询需要先关联到对象
				Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					// 名字 模糊查询
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), courier.getStandard().getName());
					list.add(p4);
				}
				Predicate predicate = cb.and(list.toArray(new Predicate[0]));
				return predicate;
			}

		};

其中，Predicate predicate = cb.and(list.toArray(new Predicate[0]));这个语句的意思，是使上述四个条件同时满足。and方法中传入一个数组（list.toArray()集合转数组）是因为：

	@Override
	public Predicate and(Predicate... restrictions) {
		return new CompoundPredicate( this, Predicate.BooleanOperator.AND, restrictions );
	}

这个方法的参数是可变参数，本质是数组。因此可以穿进去一个数组。而toArray方法中传一个new Predicate[0]相当于指定泛型（创建数组的几种方式，用此种方式创建数组的时候，必须要传一个长度）。

继承JpaSpecificationExecutor接口

#2017/8/3
##1、快递员的批量作废 formatter的使用
标记删除，而非真的删除（why？）

Courier表中，提供delTag的字段，如果字段设值为1，则表示打上了删除的标记。

而在页面上要显示已作废还是可使用。此时要使用datagrid的formatter。

##2、快递员批量作废功能实现
1. 点击页面作废按钮时，获取所有勾选快递员id
	1. 没有选中数据--->提示必须选中一条以上的数据
2. 实现作废的后台代码

		//删除的点击事件部分代码
		function doDelete() {
			//获取勾选了的id
			var rows = $("#grid").datagrid('getSelections');
			//进行判断，如果没有勾选任何记录--->提示
			if(rows.length == 0) {
				//没选中数据
				$.messager.alert('警告', '请至少选中一条记录', 'warning');
			} else {
				//选中数据
				var array = new Array();
				for(var i = 0; i < rows.length; i++) {
					array.push(rows[i].id);
				}
				//将数组转换成字符串
				var ids = array.join(",");
				//将字符串发送服务器
				window.location.href = "../../courier_delBatch?ids=" + ids;
			}
		}

>这部分代码，可以注意的是js数组的使用，包括将数组转化成字符串的方法。另外，此处将参数传到后台的方式也值得学习。
>
>同时，当使用这种方法将id们转为一个字符串，后台接收时也需要专门进行一次切割。

##3、区域批量导入原理分析
重点在于导入，其他增删改查的功能与收派标准差不多。

涉及：文件上传，excel的解析

1. 如何上传批量数据表格
	1. 必须同步提交form表单
	2. form表单编码方式 multipart/formdata
	3. 提交方式必须是post
	4. 上传文件对应input type="file" 元素要提供name属性
2. 一键上传原理
	1. 点击按钮，触发表单中浏览文件的动作

##4、jQuery ocupload实现一键上传
1. 导入文件 jquery.ocupload-1.1.2.js
2. 页面引入
3. 完成上传代码

	//文件上传
	$("#button-import").upload({
		action:'../../area_batchImport.action',
		//不能写在这里
		//autoSubmit: false,
		onSelect: function() {
			//此语句要写在这里，而不能写在上方
			this.autoSubmit=false;
			//选中后，判断文件后缀名是否符合要求
			var filename = this.filename();
			var regex = /^.*\.(xls|xlsx)$/;
			if(regex.test(filename)) {
				//符合--->提交
				this.submit();
			}else {
				//不符合--->警告框
				$.messager.alert('警告','只能上传后缀名为xls或xlsx的','warning');
			}
		},
		onComplete: function(response) {
			//参数response接收后台返回的数据
			alert("文件上传成功");
		}
	});

##5、上传的验证
限制后缀名

##6、完成文件上传
1. 使用struts2文件上传机制，接收上传文件。struts2-default.xml配置
2. 实现excel的文件解析
	1. 借助Apache的POI---专门解析微软的系列软件。
	2. HSSF解析
	
	@Action(value = "area_batchImport")
	public String batchImport() throws Exception {
		// 创建集合，用于接收area对象
		List<Area> areas = new ArrayList<>();
		// 创建hssfworkbook对象
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		// 获取sheet
		HSSFSheet sheet = workbook.getSheetAt(0);
		// 遍历sheet获取rows
		for (Row row : sheet) {
			// 跳过首行
			if (row.getRowNum() == 0) {
				continue;
			}
			// 跳过空行
			if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			// 从rows中取出cell，存入area对象中
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			// area对象存入集合
			areas.add(area);
		}
		// 将集合传给业务层--->存数据
		as.batchImport(areas);
		return SUCCESS;
	}

	//业务层调用repository，直接使用save方法就好。封装了当传入集合的时候，遍历集合分别存入数据库的方法
	@Service
	@Transactional
	public class AreaServiceImp implements IAreaService{
		@Autowired
		private AreaRepository repository;
	
		@Override
		public void batchImport(List<Area> areas) {
			repository.save(areas);
		}
	
	}

##7、Pinyin4j生成区域简码和城市编码
Pinyin4j-java类库 将中文转换成英文

	// 基于pinyin4j生成简码和城市编码
	String province = area.getProvince();
	String city = area.getCity();
	String district = area.getDistrict();

	// 将最后一个字切掉(“省”，“市”)
	province = province.substring(0, province.length() - 1);
	city = city.substring(0, city.length() - 1);
	district = district.substring(0, district.length() - 1);

	//简码--省市区字符串的拼音首字母
	String[] headArray = PinYin4jUtils.getHeadByString(province+city+district);
	StringBuilder sb = new StringBuilder();
	for (String head : headArray) {
		sb.append(head);
	}
	String shortcode = sb.toString();

	//城市编码
	String citycode = PinYin4jUtils.hanziToPinyin(city, "");

	//将简码和城市编码存入area中
	area.setShortcode(shortcode);
	area.setCitycode(citycode);

##9、区域列表查询分页
1. 设置查询按钮，编写弹出窗口的点击事件
2. 对查询窗口的查询按钮添加点击事件

##10、代码重构优化
优化每次编写都要重复编写的代码，实现代码简化编写。

优化actioin代码，抽取BaseAction。


	//将类定义为抽象类，则不能实例化此对象
	public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

		//模型驱动。此处使用protected的原因---让子类可见此属性
		//此处不能直接new T()。因为泛型在编译的过程中会被擦除。
		protected T model;
		@Override
		public T getModel() {
			return model;
		}
		
		//model实例化---定义构造方法，则子类在实例化的过程中必然调用此构造方法。即可实例化model
		public BaseAction() {
			//获取BaseAction<Area>
			Type genericSuperclass = this.getClass().getGenericSuperclass();
			
			//获取第一个泛型参数。此处为Area类型
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
			
			try {
				//实例化获取的类型
				model=modelClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("模型构造失败");
			} 
		} 
	}

##11、分页查询代码的重构优化

	// 分页
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void pushDataToValueStack(Page<T> page) {
		// 将数据封装到map集合，存进值栈
		Map<String, Object> result = new HashMap<>();
		result.put("total", page.getTotalElements());
		result.put("rows", page.getContent());
		
		ActionContext.getContext().getValueStack().push(result);
	}

#2017/8/5
##12、内容概述
结束基础数据模块的内容

1. 定区管理功能实现（重要）
	1. 关联了基础数据模块的所有内容
	2. 为后面的自动分单功能提供数据支撑
2. 添加定区功能实现
3. 定区管理客户功能实现（WebService多系统通信技术 RestService实现方式）
4. 定区关联快递员实现
5. 分区添加的业务逻辑

##13、定区添加

##14、定区列表的分页条件查询

##15、定区关联客户业务分析
重要功能

业务需求：定区就是物流覆盖配送区域，快递员将为定区中所有客户进行快递业务服务。

关联快递员--->排班功能<br>
关联客户--->客户下单时，通过所在定区，分配快递员

在整个物流平台系统中，客户信息不是保存在bos系统中，而是在crm系统中。

因此：

1. 需要一个crm系统
2. 学习WebService，完成bos和crm的通信

##16、CRM系统初始搭建
如果都放在bos系统，则这个系统的访问压力会很大。

所以采用分布式的架构。

crm_domain crm后台管理系统

crm_management 存放客户系统实体类，方便共享实体类，bos项目也可以引入实体。

>注意：被依赖的项目（crm_domain）要install

##1、WebServiceCXF简介
WebService完成多个系统间的通信

CXF是目前最主流的WebService框架

##2、JAX-WS独立服务的使用
1. 建立maven java项目
2. 在项目中导入对应CXF-ws jar包支持和内置的jetty服务器，log4j日志实现
3. 编写服务端程序
	1. 写服务接口
	2. 写接口的实现类
		1. @WebService
	3. 发布服务
		1. 创建服务实现对象
		2. 发布服务地址
		3. 发布服务
4. 编写客户端程序
	1. 调用发布webservice
	2. 创建调用远程服务代理对象
	3. 调用代理对象任何一个方法，都将通过网络调用web服务

##3、WS原理分析与日志消息显示
以soap协议的格式发请求和响应

##4、WS与Spring的整合
大多数时候CXF都是与spring联合使用的

1. 建立一个maven的web项目
2. 导入坐标
	1. CXF开发：jaxws
	2. Spring：spring-context，spring-web
	3. test：spring-test，junit
3. web.xml配置：
	1. Spring：contextLoader
4. 导入实体类、service
5. applicationContext.xml
	1. 引入名称空间 jaxws
	2. 配置服务
	3. 配置服务启动端口

##5、WS与Spring整合的客户端代码

ws独立使用与was和spring整合的区别在于，发布服务和客户端的编写的不同。独立服务是自己写代码，整合是配置文件。

##6、Restful简介
一种风格或理念

在访问服务器资源的时候，采用的不同的http协议的请求方式，服务器端可以得知进行crud的哪个操作。

http请求有8种，常见有两种get post。
get、post、put、delete、head、trance、connect、options

POST--保存
PUT--修改
GET--查询
DELETE--删除

##7、JAX-RS服务独立发布
1. 建立maven的java项目
2. 引入坐标
	1. jaxrs
	2. jetty
	3. log4j12
	4. json转换相关jar包
3. 导入实体类
	1. @XmlRootElemet-->将类转化成xml的时候，这个类到底叫什么名字？
4. 编写业务类
	1. 写接口

可以在浏览器上面访问时在尾部加?_type=xml或者?_type=json。来指定返回值的数据形式

#2017/8/6
##8、JAX-RS客户端的使用
有两种做法

1. 使用http client了工具(Apache)，需要自己对http协议内容进行定制和解析
2. WebClient工具类的使用
	1. 引入rs-client的坐标
	2. 编写客户端代码
		1. create 建立与调用服务
		2. 资源路径连接
		2. type 发送给服务器数据格式 @Consumes
		3. accept 接收服务器传输数据格式 @Produces

##9、JAX-RS传输json数据的方法使用
引入rs-extension-providers，它提供了转换json的接口。而此提供者又依赖于jettison，一个工作包。

##10、JAX-RS与Spring整合
1. 创建web项目
	1. 导入坐标
	2. 导入**web-inf**和配置**web.xml**
	3. 导domain和service
	4. applicationContext.xml 引入名称空间

##11、定区关联客户CRM系统服务接口编写
1. crm_management项目中引入cxf的jar包
2. 在客户表添加定区编号（多的一方添加外键）
3. 编写webservice服务接口
	1. 查询所有未关联的客户
	2. 基于定区编号，查询已经关联的客户
	3. 基于客户id字符串和定区id，将客户关联到定区上
		1. 因为客户id会有多个，所以先将其拼成字符串
	
	//接口
	@Produces("*/*")
	public interface ICustomerService {
	// 查询所有未关联客户
	@Path("/findnoassosncustomer")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssosnCustomer();

	// 查询所有已关联客户
	@Path("/findassosncustomer/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findAssosnCustomer(@PathParam("fixedareaid") String fixedAreaId);

	// 将客户(多)关联到定区(一)
	@Path("/assosncustomertofixedarea")
	@PUT
	public void assosnCustomerToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);
	}
	//实现类
	@Service
	@Transactional
	public class CustomerServiceImpl implements ICustomerService{
		@Autowired
		private CustomerRepository repository;
	
		@Override
		public List<Customer> findNoAssosnCustomer() {
			//fixedAreaId is null
			return repository.findByFixedAreaIdIsNull();
		}
	
		@Override
		public List<Customer> findAssosnCustomer(String fixedAreaId) {
			// find by fixedAreaId
			return repository.findByFixedAreaId(fixedAreaId);
		}
	
		@Override
		public void assosnCustomerToFixedArea(String customerIdStr, String fixedAreaId) {
			//本质是将客户的fixedareaid的值进行更新
			//获取客户id
			String[] ids = customerIdStr.split(",");
			for (String idStr : ids) {
				Integer id = Integer.parseInt(idStr);
				repository.updateCIdWithFAId(id,fixedAreaId);
			}
		}
	
	}

	//applicationContext.xml
	<jaxrs:server id="customerService" address="/customerService">
		<!-- 服务实现类 -->
		<jaxrs:serviceBeans>
			<bean class="com.forest.crm.service.impl.CustomerServiceImpl" />
		</jaxrs:serviceBeans>
		<!-- <jaxrs:inInterceptors>
			<bean class="org.apache.cxf.interceptor.LoggingInInterceptor"></bean>
		</jaxrs:inInterceptors>
		<jaxrs:outInterceptors>
			<bean class="org.apache.cxf.interceptor.LoggingOutInterceptor"></bean>
		</jaxrs:outInterceptors> -->
	</jaxrs:server>

	//web.xml的配置及实体类略


##12、提供服务接口的实现

##13、CRM服务发布
1. 先配置web.xml
2. 配置applicationContext.xml
	1. 引入名称空间
	2. 引入applicationContext_webService.xml

##14、定区关联客户窗口弹出发起ajax请求
1. 选择（有且只有）一个定区--->弹出关联客户的窗口
	1. 注意隐藏域赋值
2. 弹出关联客户窗口，有遮罩窗口
	1. div中设置modal属性为true
3. 弹出窗口后发ajax请求
	1. 查询未关联定区客户列表
	2. 查询有关联定区客户列表

			//判断选中的记录是否为一条
			var rows = $("#grid").datagrid("getSelections");
			if(rows.length == 1) {
				$('#customerWindow').window('open');
				//并且将选中的记录的地区id填入表单隐藏域中
				$("#customerFixedAreaId").val(rows[0].id);
				//发送ajax请求
				$.post("../../fixedArea_findNoAssosnCustomers.action", function(data) {
					$("#noassociationSelect").empty();
					$(data).each(function() {
						var option = $("<option value='"+this.id+"'>"+this.username+"("+this.address+")"+"</option>")
						$("#noassociationSelect").append(option);
					})
					
				}, "json");
				$.post("../../fixedArea_findAssosnCustomers.action",{"id":rows[0].id},function(data) {
					$("#associationSelect").empty();
					$(data).each(function() {
						var option = $("<option value='"+this.id+"'>"+this.username+"("+this.address+")"+"</option>")
						$("#associationSelect").append(option);
					})
				},"json");
			} else {
				$.messager.alert("警告", "请选择并只能选择一条", "warning");
			}

##15、服务器端webservice远程加载
在bos的action中创建为关联定区列表

使用webClient调用webService

##1、定区关联客户功能
1. 实现页面中已关联和未关联客户左右移动的效果
	
		//关联客户，向左向右移
		$("#toRight").click(function() {
			$("#associationSelect").append($("#noassociationSelect option:selected"));
		})
		$("#toLeft").click(function() {
			$("#noassociationSelect").append($("#associationSelect option:selected"));
		})

2. 点击关联客户的按钮提交form表单
	1. 给需要被提交的一边设置name，另一侧不设置name(customerIds)。

			<td>
				<input type="hidden" name="id" id="customerFixedAreaId" />
				<select id="noassociationSelect" multiple="multiple" size="10" style="width: 200px;"></select>
			</td>
			<td>
				<input type="button" value="》》" id="toRight">
				<br/>
				<input type="button" value="《《" id="toLeft">
			</td>
			<td>
				<select id="associationSelect" name="customerIds" multiple="multiple" size="10" style="width: 200px;"></select>
			</td>

	2. 为关联客户的按钮，添加click事件
		1. 选中已关联定区客户select中选项
		
				//点击关联客户的按钮，提交表单
				$("#associationBtn").click(function() {
					//将已关联定区的所有选项选中
					$("#associationSelect option").attr("selected","selected");
					//提交
					$("#customerForm").submit();
				})
				
		
解除--->在service层关联用户时，首先把该地区的所有关联解除，再将传过来的客户id的地区关联上。如此一来就实现了解除关联的操作。

##2、bug修复
1. 数据重复加载问题
	1. 在每次append之前，数据都需要清空一下。使用empty()
2. 网络传输很慢，在加载完成前显示进度条

##3、定区关联快递员(排班)
##4、快递员列表显示
##5、收派时间列表显示
##6、关联快递员功能实现
在提交关联表单的时候，要为表单的隐藏域设置选中的id

#2017/8/9
##7、分区管理业务
快递员覆盖的区域叫做定区。分区是对定区的一个细分，分区属于自然行政区域。

分区有更为具体的信息。不给分区指定定区，则分区是毫无意义的。

##8、前端系统
1. 前端系统搭建 BootStrap + AngularJS
2. 用户注册功能实现
3. ActiveMQ 消息队列的使用
4. Redis实现邮件激活码保存，完成邮件绑定功能
5. Spring Data Redis

##9、前台项目导入功能分析

##10、AngularJs的简介
1. 什么是AngularJs
	1. JS框架
	2. 最为核心：MVVM、模块化的编程思想、自动化双向数据绑定、依赖注入、内部指令、语义化标签。
	3. 融入了很多服务器端开发的思想

##11、快速编程入门
1. 在页面引入angular.js
2. 双向数据绑定

##12、基于模块化实现MVC案例
MVC：是一种设计思想。Model-View-Controller。主要解决视图代码和业务代码分离。

**面试**：对MVC的理解

##14、Angular路由分析(页面架构)
在页面内部通过【#/页面名称】，映射到访问正文内容页面，基于ajax方式将变化的内容加载后，显示到指定区域。

##15、Angular路由使用
1. 引入angular-route.min.js
2. 编写页面布局，将AngularJS加载变化部分
3. 编写Angular的路由配置

##17、用户注册功能
1. 点击获取验证码--->60秒倒计时
	1. 页面导入angular，在div应用模块和控制器
	2. 获取验证码绑定点击事件

##1、发短信功能的实现
吉信通——第三方短信平台

1. 业务咨询，套磁送条数试用
2. 查看接口服务
	1. http
	2. WebService
3. 下载对应实例
	1. java实例

##2、服务器发送短信的功能
0. 检查web.xml的Struts2过滤器是否配置
1. pom.xml引入crm_domain的支持

##4、注册功能的实现
使用webService去连接crm系统

#2017/8/10
##5、邮箱绑定功能
1. 用户注册时，输入邮件
2. javamail技术，向用户邮箱发送一封激活邮件，含激活码
3. 用户在24小时内可以点击激活---Redis

1. 发送邮件
	1. 导入MaiUtils工具类和依赖
	2. 配置发送邮件相关参数
	3. 编辑发送邮件正文

##6、邮件激活码的保存---Redis
1. redis的下载安装

redis应用场景--游戏排行榜

##7、redis的图形界面工具---jedis
java程序操纵redis，使用jedis

##8、SpringDataRedis的使用
1. 引入pom.xml的坐标
2. applicationContext.xml中配置redis template

##9、客户注册时，发送绑定邮件，将激活码保存到redis

##10、客户注册后邮箱绑定的激活功能
1. customerAction提供activeMail方法
2. 先判断激活码是否有效
3. 如果有效，将用户的邮箱绑定状态进行修改
4. 要进行判断，用户是否绑定过邮箱

##11、ActiveMQ消息队列
防止拥堵，解决大批量并发访问的问题

作用：
1. 解决服务之间的耦合
2. 使用消息队列，增加系统并发处理量

##12、ActiveMQ的安装与使用
ActiveMQ使用的是标准生产者 和消费者模型

有两种数据结构 Queue、Topic

1. Queue队列，生产者生产了一个消息，只能由一个消费者进行消费
2. Topic话题，生产者生产了一个消息，可以由多个消费者进行使用

##13、使用java程序操作ActiveMQ 生产者
1. 导入依赖 activemq-all
2. 编写MQ消息生产者
	1. 创建连接工厂
	2. 获取连接
	3. 建立会话
	4. 创建队列/话题对象
	5. 创建生产者/消费者
	6. 发送消息
	7. 提交操作

##14、使用java程序操作ActiveMQ 消费者
1. 创建连接工厂
2. 获取连接
3. 开启连接
3. 建立会话
	1. 第一个参数：是否使用事务，如果设置true--->操作队列后，必须使用session.commit()
4. 创建队列/话题对象
5. 创建生产者/消费者
6. 发送消息
7. 提交操作

##1、ActiveMQ整合Spring实现生产者
1. 导入依赖
	1. springcontext，springtest，junit
	2. activemq-all
	3. spring-jms spring整合activemq
2. 编写生产者

##2、ActiveMQ整合Spring实现消费者
topic有有效期，过了则无法获取到

#2017/8/11
##3、重构客户注册基于MQ实现短信验证码生产者
Qustions：
为什么使用queue和topic都可以？？？

1. bos_fore客户注册action作为生产者
	1. 引入applicationContext_mq.xml,修改配置
	2. 不直接使用smsutils，使用template发送
	3. 使用mapMassage，将电话号码也存进去
	4. 缺少xbean的jar包
	5. 常见错误：配置文件中bean的id定义重名（cannot convert value of...to...）

##4、重构客户注册基于MQ实现短信验证码消费者
Q:
消费者中不用回显数据之类的吗？？

1. 新建项目
	1. 配置tomcat的端口
	2. 配置web.xml
	3. 配置applicationContext.xml
2. 消费者实现MessageLitener接口
	1. mapmessage取出电话号码
	2. 发送短信
	3. 判断是否发送成功

##5、促销、取派功能概述
##6、促销活动分析与数据表建立
促销的实时更新。

前台bos_fore展示

后台bos_management管理活动信息crud

1. 实现促销活动信息自定义录入---在线html编辑器技术（用于论坛、商品详情、活动详情、新闻内容）
2. 导入取派活动的实体类

##7、kindeditor使用入门
在线html编辑器

在webapp下建立editor文件夹

1. 引入文件
2. 根据官方文档粘贴代码

##8、kindeditor的初始化参数配置
1. items--按钮定制
2. 图片上传编辑预览-uploadJson、fileUploadJson
3. allowfilemanager

##9、kindeditor自定义图片上传
默认是php实现，如果要使用java，则需要设置初始化参数

查看文件上传返回参数
写一个action

##10、kindeditor图片上传效果显示
```
@Action(value = "image_upload", results = { @Result(name = "success", type = "json") })
	public String upload() throws IOException {
		System.out.println("文件：" + imgFile);
		System.out.println("文件名：" + imgFileFileName);
		System.out.println("文件类型：" + imgFileContentType);
		// 生成保存路径/显示路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
		String saveUrl2 = ServletActionContext.getServletContext().getContextPath() + "/upload/";
		System.out.println("realpath--" + savePath);
		System.out.println("request contextPath--" + saveUrl);
		System.out.println("servletcontext contextPath--" + saveUrl2);
		// 生成随机的文件名/后缀名
		UUID uuid = UUID.randomUUID();
		String type = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + type;
		// 保存文件
		FileUtils.copyFile(imgFile, new File(savePath + "/" + randomFileName));
		// 通知浏览器文件上传成功
		Map<String, Object> result = new HashMap<>();
		result.put("error", 0);
		result.put("url", saveUrl + randomFileName);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
```

上述代码中的文件，文件名，文件类型要遵循一定的规范编写，不然获取不到数据。

imgFile，imgFile**FileName**，imgFile**ContentType**

##11、kindeditor图片管理器功能实现
点击“图片空间”-->显示所有已经已经上传的图片列表

点击“图片空间”-->向image_manage.action发送请求

##12、保存功能实现
##13、富文本提交问题
kindeditor的工作原理：创建一个iframe，将原来的textarea隐藏起来

##14、宣传活动后台分页显示
##1、BootStrap分页表格demo编写
datagrid的分页效果太丑，前台页面是要给客户看的，因此要用bootstrap写。

##2、angular实现分页

#8/13/2017 9:04:18 AM 
##3、活动分页实现
共享实体，抽取bos_domain项目

##4、活动分页后台实现
pagebean的抽取

##6、宣传活动详情页面展示
静态页面化技术-FreeMarcker

##7、Freemarker模板引擎入门
##8、宣传活动详情显示
##9、宣传活动自动过期
quartz

job-scheduling framework
企业级任务调度框架

##quartz和spring整合Bean无法注入问题解决

	//运行代码：
	public class HelloJob implements Job{
		@Autowired
		private JobService js;
	
		public void execute(JobExecutionContext context) throws JobExecutionException {
			// TODO Auto-generated method stub
			js.helloJob();
		}
	
	}

	//配置文件
	<context:component-scan base-package="com.forest.service"/>
	<!-- job -->
	<bean id="helloJob"
		class="org.springframework.scheduling.quartz.JobDetailFactoryBean">
		<property name="jobClass" value="com.forest.job.HelloJob"></property>
	</bean>
	<!-- trigger -->
	<bean id="simpleTrigger"
		class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean">
		<property name="jobDetail" ref="helloJob"></property>
		<property name="startDelay" value="3000"></property>
		<property name="repeatInterval" value="5000"></property>
	</bean>
	<!-- schedule -->
	<bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
		<property name="triggers">
			<list>
				<ref bean="simpleTrigger"></ref>
			</list>
		</property>
	</bean>

js无法注入，原因在于，@Autowired的使用前提是被注入的一方与注入的对象均需处于spring的管理之中。而HelloJob并没有处于Spring的管理之中。

详情可见applicationContext.xml文件中关于HelloJob的配置，JobDetailFactoryBean中只是设置HelloJob的字节码对象为其属性值，并没有创建其对象-->因此Spring并不具备创建HelloJob对象的能力--->注入失败？？？？？？