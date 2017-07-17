#Spring

##Spring介绍
<br>
轻量级的开源框架<br>
可应用于javaee，.net，android
<br>
##Spring体系结构
1. core container<br>
	beans,core,context,spEL

2. Data access/integration
	jdbcTemplate,SpringData

3. Web

4. AOP

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

**IOC的本质就是通过xml配置文件+反射+factory来实现**

Spring提供一个BeanFactory工厂，我们一般使用其子接口ApplicationContext。

Spring使用步骤：

1. 在applicationContext.xml中配置bean
2. 创建ApplicationContext对象
ApplicationContext是BeanFactory的子接口。
使用时是使用ApplicationContext的实现类ClassPathXmlApplicationContext。
可以通过getBean(配置文件中id名称)来获取指定的对象(bean)

##DI
dependency injection 依赖注入

在spring框架负责创建bean的时候，动态将依赖对象注入到Bean组件。

**面试题：IOC与DI的区别？**

IOC控制反转，是指对象实例化权利由Spring容器来管理。（Spring反向控制应用程序的资源）

DI依赖注入，在spring创建对象的过程中，对象所依赖的属性通过配置注入对象中。（Spring提供应用程序运行时所需的资源）

IOC是DI的前提。站在Spring的角度就是IOC，站在应用程序的角度就是DI。

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

AppliCationContext它会在配置文件加载时，就会初始化Bean（*可实现预处理，提前预知配置文件是否存在问题*）,并且ApplicationContext它提供不同的应用层的Context实现。例如在web开发中可以使用WebApplicationContext.

####Bean实例化的三种方式
1. 无参数构造

	bean中必须提供无参数构造

2. 静态工厂方法
	
	需要创建一个工厂类，在工厂类中提供一个static返回bean对象的方法就可以。

	**好处：**在new对象之前，干一些其他的事情，比如读取配置文件获取一些参数。
	
3. 实例工厂方法

	需要创建一个工厂类，在工厂类中提供一个非static的创建bean对象的方法，在配置文件中需要将工厂配置，还需要配置bean
	
	**实例工厂与静态工厂方法在配置文件上的区别在于，要先实例化工厂，再使用其中的方法**

####Bean的作用域
scope属性，用于描述bean的作用域。

可取值有：

+ singleton:单例 代表在spring ioc容器中只有一个Bean实例 (默认的scope)
+ prototype多例 每一次从spring容器中获取时，都会返回一个新的实例
+ request 用在web开发中，将bean对象request.setAttribute()存储到request域中
+ session 用在web开发中，将bean对象session.setAttribute()存储到session域中


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

