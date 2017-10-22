## ssh综合练习（本项目可见ssh-practice的仓库）
### jar包导入
### 包的建立
### 注解环境搭建
struts2的bug，在action上面@Controller没用，因为在spring去创建action的时候，beanname是由struts传过去的，不管有没有controller（有没有将action纳入spring的管理），spring都能根据beanname这个类名去运用反射创建action

注意：

spring整合struts和hibernate，在applicationContext.xml中的三点配置

1. spring注解开发中，要在applicationContext里面配置<context:component-scan base-package="com.forest"/>

2. spring整合hibernate的注解，是要在applicationContext.xml中声明sessionFactory的地方，将原本的

		<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
			<property name="dataSource" ref="dataSource" />
			<property name="hibernateProperties">
				<value>
					hibernate.show_sql=true
					hibernate.format_sql=true
					hibernate.dialect=org.hibernate.dialect.MySQLDialect
					hibernate.hbm2ddl.auto=update
				</value>
			</property>
			<property name="mappingDirectoryLocations">
				<!-- mappingDirectoryLocations这个属性可以加载类路径下所有文件 -->
				<list>
					<value>classpath:com/forest1/domain</value>
				</list>
			</property>
		</bean>

	变成（即只有最后一个property有变化，*此外，在这个配置之前，数据源也还是照常配置。*）：

		<!-- hibernate核心文件的配置,声明sessionFactory -->
		<bean id="sessionFactory"
			class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
			<property name="dataSource" ref="dataSource" />
			<property name="hibernateProperties">
				<value>
					hibernate.show_sql=true
					hibernate.format_sql=true
					hibernate.dialect=org.hibernate.dialect.MySQLDialect
					hibernate.hbm2ddl.auto=update
				</value>
			</property>
			<!-- 映射相关的注解 -->
			<property name="packagesToScan">
				<list>
					<value>com.forest.domain</value>
				</list>
			</property>
		</bean>

3. 事务管理器的配置。从如下：
	
		<!-- 事务管理器 -->
		<bean id="transactionManager"
			class="org.springframework.orm.hibernate5.HibernateTransactionManager">
			<property name="sessionFactory" ref="sessionFactory" />
		</bean>
		<!-- 事务通知 -->
		<tx:advice id="txAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="add" />
				<tx:method name="del" />
				<tx:method name="update" />
				<tx:method name="find*" read-only="true" />
			</tx:attributes>
		</tx:advice>
		<!-- 切面 -->
		<aop:config>
			<aop:advisor advice-ref="txAdvice"
				pointcut="execution(* com.forest1.service.*..*(..))" id="myPointcut" />
		</aop:config>

	到：

		<!-- 事务管理器 -->
	<bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
		<property name="sesstionFactory" ref="sessionFactory"/>
	</bean>
	<!-- 开启事务注解驱动 -->
	<tx:annotation-driven transaction-manager="transactionManager"/>
 
在applicationContext.xml中的用到的标签需要在文件头上配置，如tx,aop等。


开发中，图片不会放在tomcat中，会放在磁盘上某处，并配置一个虚拟路径。不然每次重启tomat图片就不见了。

### 添加用户

前端页面表单提交的地方。注意cusName，cusPhone等是name，而不是id。如果是id，无法在表单提交的时候传给后台。如果使用的name，那么会存为属性值，可以被后台获取到。

	<form role="form" class="bg" method="post" encType="multipart/form-data"
		action="${pageContext.request.contextPath}/customer/addCustomer">
		<div class="form-group">
			<label for="CUSTOMER NAME">CUSTOMER NAME</label> <input type="text"
				class="form-control" name="cusName" placeholder="CUSTOMER NAME">
		</div>
		<div class="form-group">
			<label for="PHONE NUMBER">PHONE NUMBER</label> <input type="text"
				class="form-control" name="cusPhone" placeholder="PHONE NUMBER">
		</div>
		<div class="form-group">
			<label for="CUSTOMER IMAGE">CUSTOMER IMAGE</label> <input type="file"
				name="cusImg">
		</div>
		<div class="checkbox">
			<label> <input type="checkbox"> Check me out
			</label>
		</div>
		<button type="submit" class="btn btn-default">Submit</button>
	</form>

### 删除用户
	//删除客户
	function delCustomer(customerId) {
		location.href="${pageContext.request.contextPath}/customer/delCustomer?id="+customerId;
	}

删除客户的点击事件绑定的函数。这时只需要将id传给后台，删除对应id的用户。不需要获取数据回显页面，所以采取这种带参数的方法。可以和查询订单的点击事件绑定函数对比。

这里的参数只能是"id",不能写其他的。

另外，在删除订单时，不可以用上述类似方法，即：

	location.href="${pageContext.request.contextPath}/order/deleteOrder?id="+orderId;

如果要是该方法成立，则必须在customerAction中定义deleteOrder方法，并且定义orderId属性用于接收数据。不然，这里的location.href访问的还是CustomerAction，而不是OrderAction

### 查询与删除订单（包含分页）
jsp页面的代码如下：

	//分页相关变量
	var pageNum=1;//当前页码
	var currentCount=5;//每页个数
	var totalCount = 0;//总条数
	var totalPage = 0//总页数
	
	//定义cid的全局变量
	var cid;
	//查询订单
	function findOrders(customerId) {
		cid=customerId;
	
		$.post("${pageContext.request.contextPath}/order/findOrders",{"customerId":customerId,"pageNum":pageNum,"currentCount":currentCount},function(data) {
			$("#orderInfo").html("");
			var pageBean = eval(data);
			var content = pageBean.content;
			$.each(content,function(i) {
				var content1="<tr><td>"+content[i].orderNum+"</td><td>"+content[i].price+"</td><td>"+content[i].receiverInfo
				+"</td><td>"+content[i].customer.cusName+"</td><td><a href='#' onclick=\"deleteOrder('"+content[i].orderNum+"')\">DELETE</a>&nbsp&nbsp</td></tr>";
				$("#orderInfo").append(content1);
			});
			//接收数据
			pageNum = pageBean.pageNum;
			currentCount = pageBean.currentCount;
			totalCount = pageBean.totalCount;
			totalPage = pageBean.totalPage;
			//分页拼接
			//清空page
			$("#page").html("");
			$("#page").append("<ul class='pagination' id='pagUl'></ul>");
			//判断是否能向上翻页
			if (pageNum==1) {
				$("#pagUl").append("<li class='disabled'><a href='#' aria-label='Previous'> <span aria-hidden='true'>&laquo;</span></a></li>");
			}else {
				$("#pagUl").append("<li class='active'><a href='#' aria-label='Previous' onclick='prePage()'><span aria-hidden='true'>&laquo;</span></a></li>");
			}
			//判断中间数字
			for (var i = 1; i <=totalPage; i++) {
				if (i==pageNum) {
					$("#pagUl").append("<li class='active'><a href='#' onclick='select("+i+")'>"+i+"</a></li>");
				} else {
					$("#pagUl").append("<li><a href='#' onclick='select("+i+")'>"+i+"</a></li>");
				}
			}
			//判断能否能向下翻页
			if (pageNum==totalPage) {
				$("#pagUl").append("<li class='disabled'><a href='#' aria-label='Next'> <span aria-hidden='true'>&raquo;</span></a></li>");
			}else {
				$("#pagUl").append("<li class='active'><a href='#' aria-label='Next' onclick='nextPage()'> <span aria-hidden='true'>&raquo;</span></a></li>");
			}
		},"json");
	}
	
	function prePage() {
		pageNum = pageNum-1;
		findOrders(cid);
	}
	
	function nextPage() {
		pageNum = pageNum+1;
		findOrders(cid);
	}
	
	function select(i) {
		pageNum=i;
		findOrders(cid);
	}
	//删除订单
	function deleteOrder(orderId) {
		alert(orderId);
		$.post("${pageContext.request.contextPath}/order/deleteOrder",{"orderId":orderId},function(data) {
			findOrders(cid);
		});
	}
