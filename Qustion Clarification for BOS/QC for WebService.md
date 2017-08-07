#WebService技术
解决多系统之间的通信问题，实现分布式。

CXF是目前最主流的WebService框架，由Apache提供。主要分为两种服务提供方式：
1. JAX-WS：以动词为中心。传输xml格式，基于SOAP协议
2. JAX-RS：以名词为中心。传输xml或json格式，基于http协议

##JAX-WS独立服务使用
1. 搭建环境
	1. 建立java项目
	2. 在项目中导入jar包支持
		1. jaxws服务jar
		2. 内置jetty web服务器jar
		3. slf4j-log4j12查看完整日志jar
		
				<dependencies>
				  	<dependency>
				  		<groupId>org.apache.cxf</groupId>
				  		<artifactId>cxf-rt-frontend-jaxws</artifactId>
				  		<version>3.0.1</version>
				  	</dependency>
				  	<dependency>
				  		<groupId>org.apache.cxf</groupId>
				  		<artifactId>cxf-rt-transports-http-jetty</artifactId>
				  		<version>3.0.1</version>
				  	</dependency>
				  	<dependency>
				  		<groupId>org.slf4j</groupId>
				  		<artifactId>slf4j-log4j12</artifactId>
				  		<version>1.7.12</version>
				  	</dependency>
			  	</dependencies>
2. 编写服务端程序
	1. 编写实体类
	2. 编写服务接口及实现类
	
			@WebService
			//使用在类上面，标记类是WebService服务提供对象
			public interface IUserService {
				@WebMethod
				//使用在方法上面，标记方法是WebService服务提供方法
				public String sayHello(String name);
				
				@WebMethod
				public List<Car> findCarByUser(User user);
			}

			@WebService(endpointInterface = "com.forest.service.IUserService", serviceName = "userService")
			//endPointInterface设置接口服务完整类名，servicename设置服务名称
			public class UserServiceImpl implements IUserService {
			
				@Override
				public String sayHello(String name) {
					return name + "...你好";
				}
			
				@Override
				public List<Car> findCarByUser(User user) {
					if ("forest".equals(user.getName())) {
						List<Car> cars = new ArrayList<>();
						Car car = new Car("Rolls-Rice", 999999999, "红色");
						cars.add(car);
						return cars;
					}
					return null;
				}
			
			}

	3. 发布服务
	
			public static void main(String[] args) {
				//使用CXF将服务发布到网络上
				//1.创建服务实现类
				IUserService us = new UserServiceImpl();
				//2.指定服务地址
				String address = "http://localhost:9999/userService";
				//3.发布服务
				Endpoint.publish(address, us);
				
				System.out.println("服务启动....");
			}
3. 编写客户端程序	

	public static void main(String[] args) {
		//指定使用哪个服务，以及服务地址
		JaxWsProxyFactoryBean bean = new JaxWsProxyFactoryBean();
		bean.setServiceClass(IUserService.class);
		bean.setAddress("http://localhost:9999/userService");
		
		//创建远程服务代理对象
		IUserService proxy = (IUserService)bean.create();
		//调用服务
		System.out.println(proxy.sayHello("zach"));
	}

##JAX-WS与Spring整合使用
1. 搭建环境
	1. 建立web项目
	2. 在项目中导入jar包支持 **(有差异)**
		1. jaxws服务jar
		2. Spring开发
			1. spring-context
			2. spring-web
			3. spring-test
			4. junit
		3. tomcat插件
			1. 配置端口：configuration标签及port标签

					<build>
						<finalName>bos</finalName>
						<pluginManagement>
							<plugins>
								<plugin>
									<groupId>org.apache.maven.plugins</groupId>
									<artifactId>maven-compiler-plugin</artifactId>
									<version>3.2</version>
									<configuration>
										<source>1.8</source>
										<target>1.8</target>
										<encoding>UTF-8</encoding>
										<showWarnings>true</showWarnings>
									</configuration>
								</plugin>
							</plugins>
						</pluginManagement>
						<plugins>
							<plugin>
								<groupId>org.codehaus.mojo</groupId>
								<artifactId>tomcat-maven-plugin</artifactId>
								<version>1.1</version>
								<configuration>
									<port>9998</port>
								</configuration>
							</plugin>
						</plugins>
					</build>
					<dependencies>
						<dependency>
							<groupId>org.apache.cxf</groupId>
							<artifactId>cxf-rt-frontend-jaxws</artifactId>
							<version>3.0.1</version>
						</dependency>
						<dependency>
							<groupId>org.springframework</groupId>
							<artifactId>spring-context</artifactId>
							<version>4.1.7.RELEASE</version>
						</dependency>
						<dependency>
							<groupId>org.springframework</groupId>
							<artifactId>spring-web</artifactId>
							<version>4.1.7.RELEASE</version>
						</dependency>
						<dependency>
							<groupId>org.springframework</groupId>
							<artifactId>spring-test</artifactId>
							<version>4.1.7.RELEASE</version>
						</dependency>
						<dependency>
							<groupId>junit</groupId>
							<artifactId>junit</artifactId>
							<version>4.12</version>
						</dependency>
					</dependencies>

	3. web.xml文件 **(有差异)**
		1. spring核心配置文件
		2. spring监听器
		
			<!-- spring配置文件位置 -->
			<context-param>
				<param-name>contextConfigLocation</param-name>
				<param-value>classpath:applicationContext.xml</param-value>
			</context-param>
			<!-- spring核心监听器 -->
			<listener>
				<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
			</listener>
		3. CXF基于web访问

			<servlet>
				<servlet-name>CXFService</servlet-name>
				<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
				<load-on-startup>1</load-on-startup>
			</servlet>
			<servlet-mapping>
				<servlet-name>CXFService</servlet-name>
				<url-pattern>/services/*</url-pattern>
			</servlet-mapping>

2. 编写服务端程序
	1. 编写实体类
	2. 编写服务接口及实现类
	3. 发布服务 **(有差异)**
		1. 配置applicationContext.xml
			1. 引入名称空间
			
			```xmlns:jaxws="http://cxf.apache.org/jaxws"
			http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd```

			2. 配置jaxws:server

				<?xml version="1.0" encoding="UTF-8"?>
				<beans xmlns="http://www.springframework.org/schema/beans"
					xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jaxws="http://cxf.apache.org/jaxws"
					xsi:schemaLocation="
					http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
					http://cxf.apache.org/jaxws http://cxf.apache.org/schemas/jaxws.xsd">
				
					<!-- address 客户端访问服务路径 serviceClass 配置接口 serviceBean 配置实现类 -->
					<jaxws:server id="userService" address="/userService"
						serviceClass="com.forest.service.IUserService">
						<jaxws:serviceBean>
							<bean class="com.forest.service.UserServiceImpl" />
						</jaxws:serviceBean>
					</jaxws:server>
				</beans>

3. 编写客户端程序	
	1. 配置applicationContext.xml **(有差异)**

		<!-- 
			serviceClass 服务接口
			address 服务访问地址 
		 -->
		<jaxws:client id="userServiceClient" 
			serviceClass="com.forest.service.IUserService" 
			address="http://localhost:9998/cxf_ws_spring/services/userService" >
			<!-- 来源消息拦截器 -->
			<jaxws:inInterceptors>
				<bean class="org.apache.cxf.interceptor.LoggingInInterceptor"/>
			</jaxws:inInterceptors>
			<!-- 输出消息拦截器 -->
			<jaxws:outInterceptors>
				<bean class="org.apache.cxf.interceptor.LoggingOutInterceptor" />
			</jaxws:outInterceptors>
		</jaxws:client>

	2. 编写测试代码 **(有差异)**

		@RunWith(SpringJUnit4ClassRunner.class)
		@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
		public class JAXWS_Spring_Test {
			@Autowired
			private IUserService proxy;
		
			@Test
			public void testCXF() {
				System.out.println(proxy.sayHello("forest"));
			}
		}

**总结：**

JAX-WS独立发布和与Spring整合的区别主要在于：

1. 搭建环境阶段
	1. jar包。后者需要导入Spring相关的若干jar包，并需要使用tomcat插件
	2. web.xml。后者需要配置spring的配置文件路径、监听器，以及CXF servlet
2. 服务端程序编写阶段
	1. 写代码vs写配置。前者通过写代码发布服务，后者在applicationContext.xml中配置
3. 客户端程序编写阶段
	1. 写代码vs写配置。

##JAX-RS独服务使用
1. 搭建环境
	1. 建立java项目
	2. 在项目中导入jar包支持
		1. jaxrs服务jar (注意这里是rs，不是ws)
		2. 内置jetty web服务器jar
		3. slf4j-log4j12查看完整日志jar
		4. rs客户端
		5. CXF扩展提供者cxf-rt-rs-extension，提供转换json接口
		6. CXF扩展提供者jettison 转换json默认需要一个工具包
2. 编写服务端程序
	1. 编写实体类
		1. 类上面使用@XmlRootElement(name="...")--->指定序列化对象的名字
			
				@XmlRootElement(name="User")
				public class User {
					...
				}

	2. 编写服务接口及实现类
		1. 接口中的注释有三种
			1. @Path("/...")服务访问资源路径
			2. @Produces({"application/xml","application/json"})生成(方法返回值)；@Consumes({"application/xml","application/json"})消费(方法参数)
			3. @Get查询 @Put修改 @Post增加 @Delete删除
			4. 另外：如果实现某条数据单独查询，使用以下方法：
				
				@GET
				@Path("/user/{id}")
				@Consumes("application/xml")
				public User findUserById(@PathParam("id") Integer id);

		2. 实现类无需注释
	3. 发布服务
		
			public static void main(String[] args) {
				// 创建业务接口 实现类对象
				IUserService userService = new UserServiceImpl();
				// 服务器FactoryBean 创建服务
				JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
				jaxrsServerFactoryBean.setResourceClasses(User.class, Car.class); // 将哪些实体数据转换xml、json发送
				jaxrsServerFactoryBean.setServiceBean(userService);
				jaxrsServerFactoryBean.setAddress("http://localhost:9997");
		
				// 发布服务
				jaxrsServerFactoryBean.create();
			}

3. 编写客户端程序(两种做法)
	1. 使用http client工具，需要自己对http协议内容进行定制和解析
	2. WebClient工具类(CXF自带)
		1. 导入依赖

				<dependency>
			  		<groupId>org.apache.cxf</groupId>
			  		<artifactId>cxf-rt-rs-client</artifactId>
			  		<version>3.0.1</version>
			  	</dependency>
		
		2. 客户端代码

				public static void main(String[] args) {
					// create 建立与调用 服务资源路径 连接
					// type 发送给服务器数据格式 --- @Consumes
					// accept 接收服务器传输数据格式 ---- @Produces
					// 采用HTTP协议哪种方式访问服务器
			
					Collection<? extends User> collection = WebClient.create("http://localhost:9997/userService/user")
							.accept(MediaType.APPLICATION_XML).getCollection(User.class);
			
					// 添加用户
					User user = new User();
					WebClient.create("http://localhost:9997/userService/user").type(MediaType.APPLICATION_JSON).post(user);
			
					// 查询某个用户，获取json
					User resultUser = WebClient.create("http://localhost:9997/userService/user/1")
							.accept(MediaType.APPLICATION_JSON).get(User.class);
					System.out.println(resultUser);
				}

WS和RS的区别主要体现在实体类和服务类编写的风格不同，随之而来，发布服务和客户端的书写也有差异。

##JAX-RS与Spring整合使用
1. 搭建环境
	1. 建立web项目
	2. 在项目中导入jar包支持
		1. jaxrs服务jar (注意这里是rs，不是ws)
		2. slf4j-log4j12查看完整日志jar
		3. rs客户端
		4. CXF扩展提供者cxf-rt-rs-extension，提供转换json接口
		5. CXF扩展提供者jettison 转换json默认需要一个工具包
		6. Spring相关的4个jar包(同ws)
		7. tomcat插件
2. 编写服务端程序
	1. 导入实体类和服务类
	2. 发布服务
		1. 配置applicationContext.xml(注意，服务类接口上面的@Path要注释掉，因为与applicationContext.xml配置重复)
			
			<?xml version="1.0" encoding="UTF-8"?>
			<beans xmlns="http://www.springframework.org/schema/beans"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jaxrs="http://cxf.apache.org/jaxrs"
				xsi:schemaLocation="
				http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
				http://cxf.apache.org/jaxrs http://cxf.apache.org/schemas/jaxrs.xsd">
			
				<jaxrs:server id="userService" 
					address="/userService" >
					<!-- 服务实现类 -->
					<jaxrs:serviceBeans>
						<bean class="com.forest.service.UserServiceImpl" />
					</jaxrs:serviceBeans>
					<!-- 来源消息拦截器 -->
					<jaxrs:inInterceptors>
						<bean class="org.apache.cxf.interceptor.LoggingInInterceptor"/>
					</jaxrs:inInterceptors>
					<!-- 输出消息拦截器 -->
					<jaxrs:outInterceptors>
						<bean class="org.apache.cxf.interceptor.LoggingOutInterceptor" />
					</jaxrs:outInterceptors>
				</jaxrs:server>
			</beans>
		2. 最终资源访问路径：**服务器根目录地址+web.xml配置+applicationContext.xml address配置+类@Path+方法@Path**
		

3. 编写客户端程序
	1. 使用http client工具，需要自己对http协议内容进行定制和解析
	2. WebClient工具类(CXF自带)
		1. 导入依赖
		2. 客户端代码
			1. 注意，此时访问路径为http://localhost:9996/cxf_rs_spring/services/userService/user

**总结：**

JAX-RS独立发布和与spring整合的区别主要体现于：

1. 搭建环境阶段
	1. jar包。后者需要导入Spring相关的若干jar包，并需要使用tomcat插件
	2. web.xml。后者需要配置spring的配置文件路径、监听器，以及CXF servlet
2. 服务端程序编写阶段
	1. 写代码vs写配置。前者通过写代码发布服务，后者在applicationContext.xml中配置
3. 客户端程序编写阶段
	1. 写代码vs写配置。

			