#Spring

##Spring介绍
<br>
轻量级的开源框架<br>
可应用于javaee，.net，android
<br>
###Spring体系结构
1. core container<br>
	beans,core,context,spEL

2. Data access/integration
	jdbcTemplate,SpringData

3. Web

4. AOP

###Spring优点
1. 方便解耦，简化开发

2. AOP 编程的支持

3. 声明式事务的支持

4. 方便程序的测试

5. 方便集成各种优秀框架

6. 降低 JavaEE API 的使用难度

##Spring环境搭建
beans,core,context,spEL四个jar包<br>
配置文件

##IOC
inversion of controller控制反转
简单说：原来由我们自己实例化的对象交给spring容器来实例化
原理：xml配置文件（统一管理对象）+工厂--->dom解析+反射
	
	//直接实现类new对象
	UserService us = new UserService();
	//耦合度高，不灵活。
	//解决措施：面向接口编程
	
	//接口--多态
	IUserService us = new UserService();
	//耦合度还是较高
	//解决措施：利用工厂模式
	
	//工厂模式
	public class factory {
		public void getUserService() {
			return new UserService();
		}
	}
	//升级版：利用工厂模式+配置文件

	//工厂模式+配置文件
	public class factory {
		public void getUserService() {
			return Class.forName(配置信息).newInstance();
		}
	}

**IOC的本质就是:**

**通过xml配置文件(统一管理对象)+factory--->dom解析 + 反射**

Spring提供一个BeanFactory工厂，我们一般使用其子接口ApplicationContext。

Spring使用步骤：

1. 在applicationContext.xml中配置bean
2. 创建ApplicationContext对象
ApplicationContext是BeanFactory的子接口。
使用时是使用ApplicationContext的实现类ClassPathXmlApplicationContext。
可以通过getBean(配置文件中id名称)来获取指定的对象(bean)

>注意：程序运行时报错
>
>java.lang.NoClassDefFoundError: org/apache/commons/logging/LogFactory
>原因:当前环境需要一个commons-loggin的jar包

代码示例：

	//在applicationContext.xml中配置bean
	<beans>
	<bean id="userService" class="com.forest1.beans.UserServiceImp"></bean>
	</beans>

	//使用spring框架创建对象调用方法的代码实现
	@Test
	public void test1() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IUserService bean = (IUserService)context.getBean("userService");
		bean.provide();
	}


##DI
dependency injection 依赖注入

在spring框架负责创建bean的时候，动态将依赖对象注入到Bean组件。

**面试题：IOC与DI的区别？**

IOC控制反转，是指对象实例化权利由Spring容器来管理。（Spring反向控制应用程序的资源）

DI依赖注入，在spring创建对象的过程中，对象所依赖的属性通过配置注入对象中。（Spring提供应用程序运行时所需的资源）

IOC是DI的前提。站在Spring的角度就是IOC，站在应用程序的角度就是DI。

代码实现：

	//applicationContext中的	配置
	<beans>
	<bean id="userService" class="com.forest1.beans.UserServiceImp">
	<property name="info" value="forest"></property>
	</bean>
	</beans>
	
	@Test
	public void test1() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		IUserService bean = (IUserService)context.getBean("userService");
		UserServiceImp uImp = (UserServiceImp)bean;
		System.out.println(uImp.getInfo());
	}


##Spring管理Bean
###Bean的获取与实例化
####ApplicationContext与BeanFactory关系

ApplicationContext它是扩展BeanFactory接口。

BeanFactory它采取延迟加载的方案，只有真正在getBean时才会实例化Bean

	@Test
	public void test1() {
		BeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
	//getBean()方法执行时，才调用bean的无参构造
		Bean1 bean1 = (Bean1) factory.getBean("bean1");
		bean1.show();
	}

在开发中我们一般使用的是ApplicationContext,真正使用的是其实现类:

1. FileSystemXmlAppliCationContext 根据文件路径获取
2. ClassPathXmlApplicationContext  根据类路径获取

	// 使用ClassPathXmlApplicationContext来获取Bean1
	@Test
	public void test2() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		Bean1 bean1 = (Bean1) applicationContext.getBean("bean1");
		bean1.show();
	}

	// FileSystemXmlApplicationContext
	@Test
	public void test3() {
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		Bean1 bean1 = (Bean1) applicationContext.getBean("bean1");
		bean1.show();
	}

ApplicationContext它会在配置文件加载时，就会初始化Bean（*可实现预处理，提前预知配置文件是否存在问题*）,并且ApplicationContext它提供不同的应用层的Context实现。例如在web开发中可以使用WebApplicationContext.

####Bean实例化的三种方式
1. 无参数构造

	bean中必须提供无参数构造

		<bean name="bean2" class="com.forest1.beans.Bean1"></bean>

2. 静态工厂方法
	
	需要创建一个工厂类，在工厂类中提供一个static返回bean对象的方法就可以。

	**好处：**在new对象之前，干一些其他的事情，比如读取配置文件获取一些参数。

	代码示例：
		
		//注意：这里的类路径配置的是工厂的路径，而不是bean1的
		<bean name="bean1" class="com.forest1.factory.Bean1Factory" factory-method="createBean1">
		</bean>
	
3. 实例工厂方法

	需要创建一个工厂类，在工厂类中提供一个非static的创建bean对象的方法，在配置文件中需要将工厂配置，还需要配置bean
	
	**实例工厂与静态工厂方法在配置文件上的区别在于，要先实例化工厂，再使用其中的方法**

	代码实例：
		
		<!-- 实例工厂方法 -->
		<bean name="bean2Factory" class="com.forest1.factory.Bean2Factory"></bean>
		<bean name="bean3" factory-bean="bean2Factory" factory-method="createBean1"></bean>

####Bean的作用域
scope属性，用于描述bean的作用域。

可取值有：

+ singleton:单例 代表在spring ioc容器中只有一个Bean实例 (默认的scope)
+ prototype多例 每一次从spring容器中获取时，都会返回一个新的实例
+ request 用在web开发中，将bean对象request.setAttribute()存储到request域中
+ session 用在web开发中，将bean对象session.setAttribute()存储到session域中

代码示例：

	<bean id="car1" class="cn.itheima.bean.Car" scope="prototype">
	<property name="name" value="宝马"/>
	</bean>
			
scope="prototype"配置，getBean的时候才实例化bean对象

scope="singleton"配置，配置文件加载的时候就


####Bean的生命周期
1. 实例化BeanLifeCycle对象

2. 属性注入

3. 如果当前Bean实现了BeanNameAware接口，则重写setBeanName方法。得到bean的id、name

4. 如果Bean实现BeanFactoryAwar或ApplicationContextAwar设置工厂setBeanFactory或上下文对象setApplicationContext

5. 如果存在类实现BeanPostProcessor(后处理Bean),执行postProcessBeforeInitialization

6. 如果Bean实现InitializingBean执行afterPropertiesSet

7. 调用自定义的init-method方法（须在配置文件中指定具体方法是谁）

8. 如果存在类实现BeanPostProcessor(处理Bean),执行postProcessAfterInitialization

9. 执行业务处理

10. 如果Bean实现DisposableBean执行destroy

11. 调用自定义的destroy-method

>3与4--->了解Spring的容器
>
>5与8--->针对指定的bean进行功能增强，一般会使用动态代理
>
>6与10--->通过实现指定的接口来完成init与destroy操作
>
>开发中一般不使用6与10，而是使用7与11
>7与11的初始化与销毁操作它无耦合，推荐使用的。但是必须在配置文件中指定初始化与销毁的方法

总结:

对于bean的生命周期，我们需要关注的主要有两个方法:

1.	增强bean的功能可以使用后处理Bean, BeanPostProcessor
2.	如果需要初始化或销毁操作我们可以使用init-method  destroy-method

其他：
代理对象和真实对象要实现同一个接口的原因（不然强转时不认得）。

AOP思想：不修改原来代码的基础上，对其进行增强。

###Bean的属性注入
Spring中bean的属性注入有两种：

+ 构造器注入
+ setter方法注入

####构造器注入


####setter方法注入


####集合属性的注入
List属性注入

Set属性注入

Map属性注入

Property属性注入

####c与p名称空间
Spring2.0以后提供了xml的命名空间

+ P名称空间
+ C名称空间

首先它们不是真正的名称空间，是虚拟的。它是嵌入到spring内核中的。

+ 使用p名称空间可以解决我们setter注入时<property>简化 
+ 使用c名称空间可以解决我们构造器注入时<constructor-arg>简化

####SpEl
spring expression language  是在spring 3.0之后提供的

它类似于ognl或el表达式。它可以提供在程序运行时构造复杂表达式来完成对象属性存储及方法调用等。

Spel表达式的格式  #{表达式}

###Spring注解开发
在Spring中使用注解，则必须在applicationContext.xml文件中添加一个标签。作用是让Spring中常用的注解生效。

####完成bean注册的注解
注解方式IOC步骤

1. 导入aop jar包
2. 引入名称空间context
3. 开启注解扫描
4. 在需要被IOC的类上配置@Component

####属性依赖注入
简单的属性注入

复杂的属性注入

####属性依赖注入指定名称


####其他注解
@Scope 描述bean的作用域

@PreConstruct

@PreDestroy

###Spring整合JUnit4
1. 导入test jar包
2. 测试类上
3. 直接注入对象

##Spring在web中的应用
