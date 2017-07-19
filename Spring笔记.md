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

2.	在后置通知上添加的参数

	第二个参数val它可以获取目标方法的返回值

	注意：需要在配置文件中配置

3.	环绕通知上的参数

	它是我们开发中应用最多的，可以完成日志操作，权限操作，性能监控，事务管理

4.	抛出异常通知上的参数

	第二个参数Throwable它是用于接收抛出的异常

	注意:需要在配置文件中声明

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




	

