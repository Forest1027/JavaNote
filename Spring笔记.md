#Spring

##Spring介绍
轻量级的开源框架

可应用于javaee，.net，android

###Spring体系结构
1. core container
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
>
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

代码示例1（自定义一个类实现BeanPostProcessor接口，重写before和after两个方法）：

	import java.lang.reflect.InvocationHandler;
	import java.lang.reflect.Method;
	import java.lang.reflect.Proxy;
	
	import org.springframework.beans.BeansException;
	import org.springframework.beans.factory.config.BeanPostProcessor;
	
	public class MyProcessor implements BeanPostProcessor{

		@Override
		public Object postProcessAfterInitialization(final Object arg0, String arg1) throws BeansException {
			Object proxy = Proxy.newProxyInstance(arg0.getClass().getClassLoader(), arg0.getClass().getInterfaces(),new InvocationHandler() {
				
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					//在类中实现动态代理加强drive方法
					//如果调用的是drive方法，那么就输出语句
					if ("drive".equals(method.getName())) {
						System.out.println("系好安全带");
						Object result = method.invoke(arg0, args);
						return result;
					}
					return null;
				}
			});
			return proxy;
		}

		@Override
		public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
			System.out.println("before");
			//一定注意这里要return回参数，不能return null。如果返回空值，那么对象无法创建成功
			return arg0;
		}

	}

代码示例4（applicationContext.xml的配置）:

	//Car类中定义初始化和销毁的方法，并且一定要在属性上声明出来
	<bean id="car1" class="com.forest1.beans.Car" init-method="myInit" destroy-method="myDestroy"></bean>
	//自定义的实现BeanPostProcessor的类也需要在配置文件中声明出来
	<bean class="com.forest1.factory.MyProcessor"></bean>

代码示例3（测试动态代理）：

	@Test
	public void test3() {
		//动态代理
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		//该步涉及强转，此时getBean获得的已经是代理对象了。
		//如果代理对象没有和被代理对象实现同一个接口，那么此处强转会出现类转换异常
		//因此，被代理对象必须实现一个接口。这样，动态生成的代理也会继承这个接口
		Machine car = (Machine)context.getBean("car1");
		car.drive();
	}


AOP思想：不修改原来代码的基础上，对其进行增强。

###Bean的属性注入
Spring中bean的属性注入有两种：

+ 构造器注入
+ setter方法注入

####构造器注入
代码示例：

	<bean id="car1" class="com.forest1.beans.Car">
		<constructor-arg index="0" value="劳斯莱斯"></constructor-arg>
		<constructor-arg index="1" value="100000000"></constructor-arg>
	</bean>

####setter方法注入
代码示例：

	<!-- setter注入 -->
	<bean id="person" class="com.forest1.beans.Person">
		<property name="name" value="Forest"/>
		<!-- 注意这里的ref属性，用于引用另外一个对象 -->
		<property name="car" ref="car1"/>
	</bean>

####集合属性的注入
*以下代码示例均只取了property标签处，实际运用时，记得外面还有一层是bean标签，制定id和类路径*

List属性注入

代码示例：

	<property name="list">
			<list>
				<value>张三</value>
				<value>10</value>
				<ref bean="car"/>
			</list>
	</property>

Set属性注入

代码示例：

	<property name="set">
			<set>
				<value>10</value>
				<value>李四</value>
				<ref bean="person"/>
			</set>
	</property>

Map属性注入

代码示例：

	<property name="map">
			<map>
				<entry key="username" value="李四"></entry>
				<entry key-ref="person" value-ref="car"></entry> 
			</map>
	</property>

Property属性注入

代码示例：

	<property name="props">
			<props>
				<prop key="company">ITCAST</prop>
				<prop key="price">10000</prop>
			</props>
	</property>

####c与p名称空间
Spring2.0以后提供了xml的命名空间

+ P名称空间
+ C名称空间

首先它们不是真正的名称空间，是虚拟的。它是嵌入到spring内核中的。

+ 使用p名称空间可以解决我们setter注入时<property>简化 
+ 使用c名称空间可以解决我们构造器注入时<constructor-arg>简化

p属性注入：

1. 引入：xmlns:p="http://www.springframework.org/schema/p"
2. 属性注入<bean id="person1" class="cn.itheima.bean.Person" p:name="张三" p:car-ref="car1"/>

c构造注入：

1. 引入：xmlns:c="http://www.springframework.org/schema/c"
2. 构造器 <bean id="person1" class="cn.itheima.bean.Person" c:name="张三" c:car-ref="car1"/>	

####SpEl
spring expression language  是在spring 3.0之后提供的

它类似于ognl或el表达式。它可以提供在程序运行时构造复杂表达式来完成对象属性存储及方法调用等。

Spel表达式的格式  #{表达式}

代码示例：

	<bean id="person1" class="cn.itheima.namsapce.Person">
		<property name="name" value="#{person.name}" />
		<!-- <property name="dog" ref="dog1"/> -->
		<property name="dog" value="#{dog1}"></property>
		<property name="age" value="#{person.getAge()+10}"></property>
	</bean>

###Spring注解开发
在Spring中使用注解，则必须在applicationContext.xml文件中添加一个标签<context:annotation-config/>。作用是让Spring中常用的注解生效。

>**注意：**
>
>使用@Autowired注解时，不需要开启扫描也不需要在所在类上配置@Component之流，但是需要在applicationContext.xml中配置<context:annotation-config/>标签，让autowired生效。
>
>除非一种情况，即,使用junittest的时候
>
>另外，使用spring整合junit4的注解 @Runwith @ContextConfiguration时，只能使用@Autowired，不能使用set属性注入的方式（这种方式即使将所在类配置进了applicationContext.xml也不行。*因为，所在类首先加载，然后根据其上的ContextConfiguration加载applicationContext.xml。如此，applicationContext.xml会再创建一个该类，但是这个类不是一开始运行的那个类了*）


####完成bean注册的注解
注解方式IOC步骤

1. 导入aop jar包
2. 引入名称空间context

	xmlns:context="http://www.springframework.org/schema/context"
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd

3. 开启注解扫描
4. 在需要被IOC的类上配置@Component

>注意事项：
>
>+ 在编写xml文件的时候，第一行一定不能写除了<?xml version="1.0" encoding="UTF-8"?>以外的任何东西
>
>包括注释、空格、回车、换行符等等
>
>+ 在spring2.5后为@Component添加了三个衍生的注解:
	1. @Repository 用于DAO层
	2. @Service 用于service层
	3. @Controller  用于表现层
	
>对于我们的bean所处在的位置可以选择上述三个注解来应用，如果你的bean不明确位置，就可以使用@Component.


####属性依赖注入
简单的属性注入

复杂的属性注入

代码示例：
	
	@Component("p1")
	public class Person {
		@Value("Forest")//简单属性注入
		private String name;
		@Autowired//复杂属性注入
		private Car car;
		@Override
		public String toString() {
			return "Person [name=" + name + ", car=" + car + "]";
		}
	}

####属性依赖注入指定名称
@Autowired和@Resource区别：

+ 如果按照类型注入：

	@Autowired和@Resource效果一样;

+ 如果按照名称注入：

	@Resource(name="car1")

	@Autowired +
	@Qualifier("car1")


####其他注解
@Scope 描述bean的作用域

@PreConstruct

	@PreConstruct
	public void mInit() {
		System.out.println("mInit()");
	}

相当于init-method="myInit"

@PreDestroy

	@PreDestroy
	public void engine() {		
		System.out.println("开车了  "+ name);
	}

相当于是destroy-method="myDestroy"

>注意：对于销毁的方法它只对bean的scope=singleton有效。如果是多例的，容器是无法管理的，所以无法销毁。

###Spring整合JUnit4
1. 导入spring-test.jar包
2. 测试类上添加两个注解
	@RunWith(SpringJUnit4ClassRunner.class)//spring将junit整合到一起
	@ContextConfiguration(locations={"classpath:beans.xml"})//指定spring配置文件的位置
3. 直接注入对象

		@Autowired
		private Person person;
				
		@Test
		public void test2() {
			System.out.println(person);
		} 

##Spring在web中的应用
1. 在web项目中要使用spring需要导入一个jar包
2. 在web.xml文件中配置Listener

		 <listener>
		  <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		 </listener>

	这个ContextLoaderListener它实现了ServletContextListener
	在这个listener中，当服务器启动时，将ApplicationContext对象，其实是它的一个实现类
	WebApplicationContext,对象存入到了ServletContext中。


3. 我们还需要在web.xml文件中配置applicationContext.xml文件的位置

		<context-param>
		  <param-name>contextConfigLocation</param-name>
		  <param-value>classpath:appllicationContext.xml</param-value>
		</context-param>

	默认情况下会在WEB-INF目录 下查找applicationContext.xmls
	如果applicationContext.xml文件不是在默认位置，我们可以在web.xml文件中配置

	Classpath:applicationContext.xml 它代表的是在当前工程的类路径下(可以理解成是在src)下来查找applicationContext.xml文件。
	contextConfigLocation它是在listener中声明的一个常量，描述的就是spring配置文件的位置。

##AOP介绍
###概述
Aspect Oriented Programming 面向切面编程

对业务逻辑各个部分进行隔离，降低耦合，提高程序的可重用性，提高开发效率。

AOP：不修改原方法代码基础上动态的添加一些功能

AspectJ：基于Java语言的AOP框架，Eclipse基金组织的开源项目。就是一种插件。包含表达式和编译器。

**AOP与OOP区别**
OOP（面向对象编程）针对业务处理过程的实体及其属性和行为进行抽象封装，以获得更加清晰高效的逻辑单元划分。

而AOP则是针对业务处理过程中的切面进行提取，它所面对的是处理过程中的某个步骤或阶段，以获得逻辑过程中各部分之间低耦合性的隔离效果。这两种设计思想在目标上有着本质的差异。

换而言之，OOD/OOP面向名词领域，AOP面向动词领域。


###相关术语
+ 目标对象target
	
	被增强的对象

+ 连接点join point

	被增强对象中的所有方法（因为都可以被代理）

+ 切入点pointcut

	实际被增强的方法

+ 通知advice

	动态代理中，增强功能的代码

+ 引介intrduction

	作用在类上

+ 切面

	切入点和通知的结合，指定把什么（通知）插入到哪里（切入点）去--->关系

+ 织入weaving

	将切面应用到目标对象从而创建出AOP代理对象的过程

	织入可以在编译期，类装载期，运行期进行。Spring采用动态织入，而aspectj采用静态织入。

+ 代理proxy

	通知插入到切点对目标对象进行增强通过代理来完成

##AOP实现原理
###AOP底层实现
静态AOP（AspectJ）：编译时增强

动态AOP（Spring）：运行时增强

+ JDK动态代理

	基于接口的代理
	
	代理与被代理是兄弟关系（实现同一接口）

+ CGLib动态代理

	字节码代理

	可以为没有实现接口的类做代理,也可以为接口类做代理。

	代理与被代理是子父关系（继承）

**问题：spring采用的是哪一种动态机制:**

+ 如果目标对象，有接口，优先使用jdk动态代理

+ 如果目标对象，无接口，使用cglib动态代理。

##AOP开发
###Spring的传统aop编程（手动）（了解流程）
在传统的spring aop开发中它支持增强(advice)有五种:

1.	前置通知  目标方法执行前增强  org.springframework.aop.MethodBeforeAdvice
2.	后置通知  目标方法执行后增强 org.springframework.aop.AfterReturningAdvice
3.	环绕通知  目标方法执行前后进行增强  org.aopalliance.intercept.MethodInterceptor
4.	异常抛出通知 目标方法抛出异常后的增强 org.springframework.aop.ThrowsAdvice
5.	引介通知 在目标类中添加一些新的方法或属性

基本jar包

1. bean
2. core
3. context
4. expression
5. aop
6. 需要aop联盟的依赖jar包（aop-alliance包）

实现步骤：

1. 编写目标（target）
2. 增强（advice）

	1. 如果从前面插入通知需要实现MethodBeforeAdvice接口（before方法）
	2. 如果从后面插入通知需要实现AfterReturningAdvice接口（afterReturning方法）
	3. 如果从环绕插入通知需要实现MethodInterceptor接口（invoke方法）
	
3. 在applicationContext.xml的文件中进行配置
	1. 目标
	2. 通知
	3. 切点
	4. 切面（通知和切点的结合）
	5. 代理
4. 测试

Spring的配置文件可以拆分。利用**<import resource=""/>**这个标签在applicationContext.xml中引入卸载其他文件中的配置。

###基于AspectJ切点表达式的传统aop开发（半自动）
**传统SpringAOP开发总结**

1. 编写目标对象（target）
2. 编写通知（advice）

	传统aop开发中，通知要实现接口

3. 在配置文件中配置切面

	<aop:config>
		<aop:advisor advice-ref="orderServiceAdvice" pointcut="execution(* cn.itheima.aop.OrderServiceImpl.*(..))"/>
	</aop:config>	

	<aop:config>来声明对aop进行配置
	<aop:pointcut>用于声明切点
	<aop:adviser>定义传统aop切面。传统的aop切面只能包含一个切点和一个增强
	<aop:aspect>定义aspectJ框架的切面，可以包含多个切点和多个通知

缺点：

1. 通知类需要实现相应的接口
  
2. 通知的所有的方法只能同时去插入增强一个方法

>注意：
>
1. 需要在xml配置文件中导入aop声明
2. 因为我们使用aspectj的切面声明方式 需要在导入aspectj的jar包

####关于切点表达式的写法
spring中aop开发对aspectj的语法不是完全支持。

在开发中使用的比较多的是execution语法

1. execution(public **())所有的public方法
2. execution(*cn.forest.aop.*(..))所有的aop报下的所有类的方法（不包含子包）
3. execution(*cn.forest.aop..*(..))所有的aop包及其子包下的所有类的方法
4. execution(* cn.forest.aop.IOrderService.*(..)) IOrderService接口中定义的所有方法
5. execution(* cn.forest.aop.IOrderService+.*(..)) 匹配实现特定接口所有类的方法
6. execution(* save*(..)) 区配所有的以save开头的方法


###Spring整合AspectJ框架（全自动）
Spring2.0以后支持jdk1.5注解，而整合aspectJ可以使用aspectJ语法。可以简化开发。

Aspect框架定义的通知类型有6种：

1.	前置通知Before 相当于BeforeAdvice
2.	后置通知AfterReturning 相当于AfterReturningAdvice
3.	环绕通知 Around 相当于MethodInterceptor
4.	抛出通知AfterThrowing 相当于ThrowAdvice
5.	引介通知DeclareParents 相当于IntroductionInterceptor
6.	最终通知After 不管是否异常，该通知都会执行

####基于xml开发
+ 前置通知

	在aspectj中的增强可以不实现任何方法
	
		<aop:before method="check"
		pointcut="execution(* com.forest3.service.StudentService.*(..))"></aop:before>

+ 后置通知
+ 环绕通知

	可以将前置、后置、最终通知整合到一起

		<aop:around method="work"
		pointcut="execution(* com.forest3.service.StudentService.work(..))"></aop:around>

+ 抛出通知

		<aop:aspect ref="teacherAdvice">
		<!-- 抛出 -->
		<aop:after-throwing method="exam" pointcut="execution(* com.forest3.service.StudentService.study(..))"></aop:after-throwing>
		</aop:aspect>

+ 最终通知

#####关于通知上的参数
1.	在前置通知上可以添加JoinPoint参数
通过它可以获取目标相关的信息

	使用前置通知可以获取目标对象方法的信息，完成日志记录，权限控制

		 public void before(JoinPoint jp) {
			System.out.println("拦截的目标类:" + jp.getSignature().getDeclaringTypeName());
			System.out.println("拦截的方法名称:" + jp.getSignature().getName());
			System.out.println("前置通知before");
	 	}

2.	在后置通知上添加的参数

	第二个参数val它可以获取目标方法的返回值

	注意：需要在配置文件中配置

		//需要在配置文件中配置
		 <aop:after-returning method="afterReturning" pointcut-ref="pointCutAll" returning="val"/>

		  public void afterReturning(Object val) {
			System.out.println("目标方法返回值:" + val);
			System.out.println("后置通知");
		  }

3.	环绕通知上的参数

	它是我们开发中应用最多的，可以完成日志操作，权限操作，性能监控，事务管理。可以执行目标行为。

4.	抛出异常通知上的参数

	第二个参数Throwable它是用于接收抛出的异常

	注意:需要在配置文件中声明

		<aop:after-throwing method="afterThrowing" pointcut-ref="pointCutAll" throwing="tx"/>

	   	public void afterThrowing(Throwable tx) {
			System.out.println("发现了异常。。。。"+tx);
	   	}	

5.	最终通知上的参数

	可以使用最终通知完成资源释放

#####关于代理方式的选择
代理实现有两种：

1. jdk的proxy
2. cglib

spring框架默认情况下，会对有接口的类使用proxy代理了。没有接口的类使用cglib

Proxy-target-class的值默认是false,它代表有接口使用proxy代理
问题：如果现在对目标要使用cglib代理(不考虑是否有接口)？
只需要将proxy-target-class设置为true.

####基于注解的方式
1. 编写目标

	在spring的配置文件中配置扫描注解

2. 编写增强

	使用@Aspect来声明切面

	使用@Before来声明前置通知

	>注意:必须在spring的配置文件中开启aspectJ注解自动代理功能。

3. 测试

#####使用@Pointcut注解定义切点
定义两个方法，在其上使用@Pointcut注解，定义切点表达式 

##Spring JDBC模板介绍
类似于DBUtils

jar包：jdbc、事务、数据库驱动

**快速入门代码**

	//c3p0获取数据源方式
	ComboPooledDataSource dataSource2 = new ComboPooledDataSource();
	
	//spring框架内置连接池获取数据源方式
	DriverManagerDataSource dataSource = new DriverManagerDataSource();
	dataSource.setDriverClassName("");
	dataSource.setUrl("");
	dataSource.setUsername("");
	dataSource.setPassword("");
	
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	jdbcTemplate.update("");

以上数据源的获取方式以及JdbcTemplate都是使用new创建对象。那么既然是在spring框架中，我们当然可以使用ioc，将创建对象的权利交给spring框架。

因此，可以使用以下配置。

	<bean id="driverManagerDateSource"
				class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
		<property name="url" value="jdbc:mysql:///springtest" />
		<property name="username" value="root" />
		<property name="password" value="root"></property>
	</bean>
	
	
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="driverManagerDateSource"/>
	</bean>

###引入外部属性文件
Spring支持将经常修改属性，在properties文件中声明，在xml配置文件中引入外部的properties文件的信息。
	
	//db.properties中
	jdbc.driverClass=com.mysql.jdbc.Driver
	jdbc.jdbcUrl=jdbc:mysql:///springtest
	jdbc.user=root
	jdbc.password=123
	
	//applicationContext.xml
	 <!-- jdbc -->
	<context:property-placeholder location="classpath:db.properties"/>
	<import resource="./aop2.xml"/>

	//aop2.xml。以下两个配置一定要有，因为要将两者都纳入spring的管理中，才能让spring来new对象、注入对象。
	//另外，注意c3p0中的配置${}用这个符号引用配置文件中设置的值
	  <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
     	<property name="driverClass" value="${jdbc.driverClass}"></property>
     	<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
      	<property name="user" value="${jdbc.user}"></property>
      	<property name="password" value="${jdbc.password}"></property>
     </bean>
     <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
     	<property name="dataSource" ref="dataSource"></property>
     </bean>

从context:property-placeholder加载的属性文件取属性值

@Value("${car_name}")

###JDBCTemplate CRUD
####增删改

	jdbcTemplate.update("update t_user set name=? where id=?", "tom", 1);
	jdbcTemplate.update("insert into t_user values(null,?,?,?)", "赵六", 30, "男");
	jdbcTemplate.update("delete from t_user where id=?", 4);

####查询
#####返回简单数据
	String name = jdbcTemplate.queryForObject("select name from t_user where id=?", String.class, 2);
	Integer count = jdbcTemplate.queryForObject("select count(*) from t_user", Integer.class);

#####返回复杂对象

	User user = jdbcTemplate.queryForObject("select * from t_user where id=?",new RowMapper<User>(){
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt(1);
			String name = rs.getString(2);
			User user = new User();
			return user;
		}
	}, 1);


	List<User> list = jdbcTemplate.query("select * from t_user",new RowMapper<User>(){
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			int id = rs.getInt(1);
			String name = rs.getString(2);
			User user = new User();
			
			return user;
		}
	});

>注意：如果只返回一个domain对象，可以使用queryForObject方法，如果返回的是List<?>对象，可以使用query方法，但是都需要使用RowMapper来对ResultSet进行处理。


一个rowmapper对象就对应着数据库中的一行记录。

一般在开发中使用BeanPropertyRowMapper,此对象能将查询到的数据一次性封装到实体类中(*实体类必须提供一个无参数的public构造方法,类中的bean属性名称与表中的列要对应*)。这个类是在spring2.5后提供

一般在实体类与数据库的表中的字段不能一一对应时，则使用rowmapper进行一个属性一个属性的封装。

或者在查询的时候使用表的别名，将数据库的字段与实体类的属性对应起来。

##Spring事务管理
使用dao需要往其中注入JdbcTemplate

###转账案例

**方法一：dao通过@Autowired注入JdbcTemplate**

步骤：

1. jdbcTemplate在Spring容器中	
		  
	    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="driverManagerDateSource"/>
	    </bean>

2. Dao对象自己在在Spring容器中

    	<bean id="userDao" class="cn.itheima.dao.UserDao"/>

3. Dao类直接通过@Autowired注入JdbcTemplate 
  
	    public class UserDao {	
		  @Autowired
		  private JdbcTemplate jdbcTemplate;
	    }	

*使用@AutoWired的前提条件：*

1. 自己在spring的管理中
2. 依赖注入的对象也在spring的管理中

>注意：**此种办法不用set方法设置属性**
>
>@Autowired注解的意思就是使用方要求spring给自己注入一个对象，那么这个对象必须要在spring的管理中。
>
>并且，spring要能够给使用方注入对象，也就意味着spring也能管理使用方。
>
>因此，使用@Autowired注解，使用方和被注入方都需要处于spring的管理之中。（*处于管理的配置，可以是xml，也可以是注解*）

**方法二：dao通过property属性注入JdbcTemplate**

1. jdbcTemplate在Spring容器中
			  
		<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		   <property name="dataSource" ref="driverManagerDateSource"/>
		</bean>

2.	Dao对象自己在在Spring容器中

    	<bean id="userDao" class="cn.itheima.dao.UserDao"/>

3.  Dao提供set属性

    public class UserDao {
		private JdbcTemplate jt;
		public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.jt = jdbcTemplate;
		}
	}	
	
4.  在xml bean配置property注入JdbcTemplate
     
		<bean id="userDao" class="cn.itheima.dao.UserDao">
			<property name="jdbcTemplate" ref="jdbcTemplate"></property>
		</bean>

方法二（set注入方法）的前提条件：

1. 被注入对象在容器中
2. 调用对象在容器中
3. 被注入对象要有set方法

**方法三：使用JdbcDaoSupport**

步骤：

1. Dao类继承JdbcDaoSupport
2. 给Dao对象注入dataSource这里的datasource是父类的属性）

		<bean id="userDao" class="cn.itheima.dao.UserDao">
		 <property name="dataSource" ref="c3p0DataSource"></property>
		</bean>

3. 在Dao方法里面直接getJdbcTemplate(无需另外配置)

在service层调用dao的方法的流程：

AccountServiceImpl--DI-->AccountDAOImpl---DI---->JdbcTemplate-----DI---->dataSource

	//service层
	public class AccountServiceImp implements IAccountService{
		@Autowired
		private IAccountDao ad;
	
		@Override
		public void transferMoney(String inname, String outname, double money) {
			//从outname转出钱
			ad.accountIn(inname, money);
			//转钱给inname
			ad.accountOut(outname, money);
		}
	}

	//dao层。继承JdbcDaoSupport，该对象里面掉了dataSourse。
	public class AccountDaoImp extends JdbcDaoSupport implements IAccountDao{
	
		@Override
		public void accountOut(String outname, double money) {
			this.getJdbcTemplate().update("update account set money=money-? where name=?",money,outname);
		}
	
		@Override
		public void accountIn(String inname, double money) {
			this.getJdbcTemplate().update("update account set money=money+? where name=?",money,inname);
		}
	}

	//测试类。注意注释掉的部分。在使用spring和junit4结合的注解时，只能采用autowired注入属性的方法，不能采取set注入属性的方法。
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations="classpath:applicationContext.xml")
	public class AccountServiceImpTest {
		@Autowired
		private IAccountService service;
	
		/*public IAccountService getService() {
			return service;
		}
	
		public void setService(IAccountService service) {
			this.service = service;
		}*/
	
		@Test
		public void testTransferMoney() {
	//		IAccountService service = new AccountServiceImp();
			service.transferMoney("fox", "tom", 500);
		}
	
	}

	//配置文件。注意：以下注释中的代码是不可以加的，加了会报错。因为各个类中的注入方式选择的是autowired注入。这种注入方式不需要提供set方法，配置中也不能加property标签。
	<!-- service/dao层配置 -->
	<bean id="accountService" class="com.forest5.service.AccountServiceImp">
		<!-- <property name="ad" ref="accountDao"></property> -->
	</bean>
	<bean id="accountDao" class="com.forest5.dao.AccountDaoImp">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
	<!-- 测试 -->
	<bean id="test" class="com.forest5.tests.AccountServiceImpTest">
		<!-- <property name="service" ref="accountService"></property> -->
	</bean>

###Spring事务管理机制
Spring事务管理的四个优点:

1.	提供一致的对于不同的事务管理的API
2.	支持声明式事务管理(重点)
3.	编程事务管理(在开发中应用比较少)
4.	优秀的整合与Spring的数据访问
我们重点讲解spring的事务管理的相关的API，还有声明式事务管理

Spring事务管理主要提供了三个接口来完成

1. org.springframework.transaction.PlatformTransactionManager
这是一个事务管理器，可以来选择相关的平台(jdbc  hibernate  jpa…)
2.	TransactionDefinition
它定义事务的一些相关信息 例如 隔离 传播 超时 只读
3.	TransactionStatus
它主要描述事务具体的运行状态

**PlatformTransactionManager**

平台事务管理器

在不同的持久化层解决技术它的事务代码不一样。

+ JDBC开发

	Connection con=……;

	con.setAutoCommit(false);//开启事务

	con.rollback();

	con.commit();

+ Hibernate开发

	Session session=….;

	Transaction t=session.beginTransaction();

	t.commit();

	t.rollback();

>DataSourceTransactionManager 主要针对于JdbcTemplate开发  MyBatis开发
>
>HibernateTransactionManasger主要针对于Hibernate开发
>
>JpaTransactionManager 主要针对于JPA开发。

**TransactionDefinition**

描述的是事务的定义信息。TransactionDefinition中定义了大量的常量

+ 隔离
	+ ISOLATION_DEFUALT 它使用后端数据库的默认隔离级别(spring中选项)
	+ ISOLATION_READ_UNCOMMITTED 不能解决问题，会发生脏读 不可重复读 虚读
	+ ISOLATION_READ_COMMITTED 可以解决脏读 会产生不可重复读与虚读。
	+ ISOLATION_REPEATABLE_READ 可以解决脏读，不可重复读 解决不了虚读
	+ ISOLATION_SERIALIZABLE 串行化，可以解决所有问题，但效率低下
	
	对于不同的数据库，它的底层默认事务隔离级别不一样。

	Oracle数据库它默认的是read_committed

	Mysql数据库它默认的是repeatable_read.

+ 超时
	+ 默认值是-1 它使用的是数据库默认的超时时间。
	
+ 只读
	+ 它的值有两个true/false,一般是在select操作时选择true
	
+ 传播
	+ 两个被事务管理的方法互相调用问题，被调用方法对调用者带来事务的处理态度。
	+ required：b方法中调用a方法。如果a方法中有事务，那么就使用a的事务。没有事务带过来，就自己开启事务。
	+ never：a带事务过来，就报错
	+ mandatory：a带事务就用a的事务；不带事务就报错

>定义传播行为的常量详解
>
>以下三种是常用操作
>
>+ PROPAGATION_REQUIRED 默认值 两个操作处于同一个事务，如果之前没有事务，新建一个事务
>
>+ PROPAGATION_REQUIRES_NEW
两个操作处于不同的事务
>
>+ PROPAGATION_NESTED
它是一种嵌套事务，它是使用SavePoint来实现的。事务回滚时可以回滚到指定的savepoint,注意：它只对DataSourceTransactionManager有作用
>
>以下四种不常用
>
>+ PROPAGATION_SUPPORTS 支持当前事务，如果不存在，就不使用事务
>
>+ PROPAGATION_MANDATORY 支持当前事务，如果不存在，抛出异常
>
>+ PROPAGATION_NOT_SUPPORTED 以非事务运行，如果有事务存在，挂起当前事务
>
>+ PROPAGATION_NEVER 以非事务运行，如果有事务存在，抛出异常

	DefaultTransactionDefinition transactionDefinition = new  DefaultTransactionDefinition();

	transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);

	transactionDefinition.setTimeout(transactionDefinition.TIMEOUT_DEFAULT);

	transactionDefinition.setReadOnly(true);

	//定义传播行为
	transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

**TransactionStatus**

它定义了事务状态信息，在事务运行过程中，得到某个时间点的状态

###声明式事务管理
####事务管理方式
1.	编码方案 不建议使用，它具有侵入性。在原有的业务代码基础上去添加事务管理代码
2.	声明式事务控制，基于AOP对目标进行代理，添加around环绕通知。

	这种方案，它不具有侵入性，不需要修改原来的业务代码

	spring的事务管理采用aop的方式

####xml配置声明式事务管理方案
1. 定义事务管理器

		<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="c3p0DataSource"/>
		</bean>

2. 定义事务通知

		<tx:advice id="txAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="save*"/>
				<tx:method name="update*"/>
				<tx:method name="delete*"/>
				<tx:method name="query*" read-only="true"/>
				<tx:method name="find*" read-only="true"/>
				<tx:method name="get*" read-only="true"/>
			</tx:attributes>
		</tx:advice>
			
3. 配置切面

		<aop:config>
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* *..IAccountService.*(..))"/>
		</aop:config>

####注解配置声明式事务管理方案

使用注解看起来好像简化了代码。但是实际上，如果使用xml，可以批量去操作、管理。

图：事务管理器

##Spring整合Hibernate以及Struts2
###jar包分析
struts2 2.3.24

spring 4.2.4

hibernate 5.0.7

####struts整合需要的jar包
+ Asm 是关于字节码操作
+ Commons-fileupload 关于文件上传
+ Commons-io 关于io流操作工具
+ Commons-lang 也是一个工具，包含了关于数据与字符串操作
+ Freemaker 标签库模板文件
+ Javassist 它也是关于字节码操作，动态代理可以使用它实现(类似于cglib)
+ Log4j关于日志
+ Ognl 关于ognl表达式
+ Struts2-core  xwork-cor  struts2框架底层是使用xwork

Struts2与spring整合还需要(struts
提供的)---**struts2-spring-plugin-2.3.24.jar**(*创建action等的权利从struts转移到spring*)
和 **spring-web-4.2.4.release.jar**（*Spring在web项目中需要导*）

####hibernate整合需要的jar包
+ Antlr 语法解析包
+ Dom4j 解析xml
+ Geronimo-jta  apache geronimo它是一个开源javaEE服务器 Geronimo-jta是这个开源项目提供jar包，在hibernate中是关于jta事务相关
+ Hibernate-commoins-annotations
这个包是我们在hibernate下来使用jpa相关的注解，这样它不依赖于hibernate
+ Hibernate-core 开发hibernate必须
+ Hibernate-jpa 它是关于hibernate对jpa的支持
+ Jandex 用于索引annotation
+ Javassist 关于字节码操作(注意:strtus2中也引入这个jar包了)
+ Jboss-logging 它是关于jboss统一日志处理

**spring-orm-4.2.4.RELEASE.jar**
####基于xml方式整合所需的jar包


###基于xml方式整合
####前期配置文件
+ Strtsu2框架   src/strtus.xml
+ Hibernate框架   src/hibernate.cfg.xml
  	在domain有 Xxx.hbm.xml
+ Spring框架   src/applicationContext.xml
+ 关于日志   log4j.properties
+ 关于数据库连接   db.properties
+ web.xml	配置struts过滤器，配置spring的ContextLoaderListener

####Spring整合hibernate
核心：sessionFactory交给spring来管理创建---》通过orm这个jar包(localsessionfactory)

**方式一：零障碍整合（用的少）**

我们只需要使用spring中提供的一个LocalSessionFacotry来加载Hibernate的配置文件。

ssh-xml工程加载到服务器后，如果可以自动创建表，就代表spring整合hibernate ok.
注意:我们必须配置spring的ContextLoaderListener（*这个ContextLoaderListener它实现了ServletContextListener在这个listener中，当服务器启动时，将ApplicationContext对象，其实是它的一个实现类WebApplicationContext,对象存入到了ServletContext中。*）

	<bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
		<property name="configLocation" value="classpath:hibernate.cfg.xml"/>
	</bean>

####spring整合hibernate后的dao
spring整合hiberante后，我们的dao只需要继承HibernateDaoSupport类
在HibernateDaoSupport中只需要注入SessionFactory就可以获得到HibernateTemplate，它是对hibernate操作的一个简单封装，可以让我们方便使用原来hibernate的操作.


**方式二(spring管理hibernate配置)**

不在需要hibernate.cfg.xml文件，所有关于hibernate.cfg.xml文件中的配置都在spring的配置文件中来配置。

核心是创建LocalSessionFactoryBean来完成spring管理hibernate中的SessionFactory

	<!-- 数据源 -->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<property name="driverClass" value="${jdbc.driverClass}"></property>
		<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
		<property name="user" value="${jdbc.user}"></property>
		<property name="password" value="${jdbc.password}"></property>
	</bean>
	<!-- jdbc配置文件的位置 -->
	<context:property-placeholder location="classpath:db.properties" />
	<!-- hibernate核心文件的配置 -->
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
		<property name="mappingDirectoryLocations">
		<!-- mappingDirectoryLocations这个属性可以加载类路径下所有文件 -->
			<list>
				<value>classpath:com/forest1/domain</value>
			</list>
		</property>
	</bean>

####spring整合struts2
**前期整合工作**

创建一个addUser.jsp页面

创建UserAction类

#####pring整合struts2原理分析
1.	spring整合struts2框架必须导入一个jar包
struts2-spring-plugin.jar
	作用：改变struts2的对象工厂。以前action等是由struts自己的对象工厂创建，通过这个包，转化为spring的对象工厂来创建了（buildbean方法）。

2.	struts2框架配置文件加载顺序

	a. default.properties

	b. struts-default.xml

	c. strtus-plugin.xml

3.	在struts2框架中所有的action interceptor  result全是bean,在struts2框架中默认是使用strtus2自己bean初化操作.

4.	当我们在工程中导入struts2-spring-plugin.jar文件
就会加载这个包下的strtus-plugin.xml

5. 通过上述分析，spring整合struts2框架它的方式有两种

	1.	spring管理action（简单说就是在applicationContext.xml文件中来声明action）
	2.	action自动装配service

####spring整合struts2框架方式一
这种方案是基于spring管理action

1.	在applicationContext.xml文件中配置action
2.	action中
3.	struts.xml中
	配置伪类名方式（id），而非以前的全类名

####Spring整合struts2框架方式二(action中自动注入service)
全类名方式


###基于注解方式整合

jar包

在原来的基础上再多导入一个struts2-convention-plugin

