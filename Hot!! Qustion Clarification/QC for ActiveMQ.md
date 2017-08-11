现实的企业中，对于消息通信的应用一直都非常的火热，而且在J2EE的企业应用中扮演着特殊的角色，所以对于它研究是非常有必要的。

上篇博文深入浅出JMS(一)–JMS基本概念，我们介绍了消息通信的规范JMS，我们这篇博文介绍一款开源的JMS具体实现——ActiveMQ。ActiveMQ是一个易于使用的消息中间件。

消息中间件

我们简单的介绍一下消息中间件，对它有一个基本认识就好，消息中间件（MOM：Message Orient middleware）。

消息中间件有很多的用途和优点： 
1. 将数据从一个应用程序传送到另一个应用程序，或者从软件的一个模块传送到另外一个模块； 
2. 负责建立网络通信的通道，进行数据的可靠传送。 
3. 保证数据不重发，不丢失 
4. 能够实现跨平台操作，能够为不同操作系统上的软件集成技工数据传送服务

MQ

首先简单的介绍一下MQ，MQ英文名MessageQueue，中文名也就是大家用的消息队列，干嘛用的呢，说白了就是一个消息的接受和转发的容器，可用于消息推送。

下面进入我们今天的主题，为大家介绍ActiveMQ：

ActiveMQ

简要概述ActiveMQ

Apache ActiveMQ ™ is the most popular and powerful open source messaging and Integration Patterns server.
Apache ActiveMQ is fast, supports many Cross Language Clients and Protocols, comes with easy to use Enterprise Integration Patterns and many advanced features while fully supporting JMS 1.1 and J2EE 1.4. 
ActiveMQ是由Apache出品的，一款最流行的，能力强劲的开源消息总线。ActiveMQ是一个完全支持JMS1.1和J2EE 1.4规范的 JMS Provider实现，它非常快速，支持多种语言的客户端和协议，而且可以非常容易的嵌入到企业的应用环境中，并有许多高级功能。

下面我们下载一个版本，玩一玩。

下载ActiveMQ

官方网站：http://activemq.apache.org/ 
现在ActiveMQ最新的版本是5.11.1，下载挺简单的，就不再截图了。

运行ActiveMQ服务

下载，解压缩 
大家现在好之后，将apache-activemq-5.11.1-bin.zip解压缩，我们可以看到它的整体目录结构： 
这里写图片描述 
从它的目录来说，还是很简单的： 
bin存放的是脚本文件
conf存放的是基本配置文件
data存放的是日志文件
docs存放的是说明文档
examples存放的是简单的实例
lib存放的是activemq所需jar包
webapps用于存放项目的目录
1. 启动ActiveMQ 
我们了解activemq的基本目录，下面我们运行一下activemq服务，双击bin目录下的activemq.bat脚本文件或运行自己电脑版本下的activemq.bat，就可以看下图的效果。 
这里写图片描述
从上图我们可以看到activemq的存放地址，以及浏览器要访问的地址. 

2. 测试
ActiveMQ默认使用的TCP连接端口是61616, 通过查看该端口的信息可以测试ActiveMQ是否成功启动 netstat -an|find “61616”
C:\Documents and Settings\Administrator>netstat -an|find "61616" 
TCP     0.0.0.0:61616     0.0.0.0:0       LISTENING
3. 监控 
ActiveMQ默认启动时，启动了内置的jetty服务器，提供一个用于监控ActiveMQ的admin应用。 
admin：http://127.0.0.1:8161/admin/
用户名和密码都是admin
这里写图片描述
1. 至此，服务端启动完毕

停止服务器，只需要按着Ctrl+Shift+C，之后输入y即可。

我们简单说说ActiveMQ特性，网上很多，只是为了保证博文的完整。

ActiveMQ特性列表

- 多种语言和协议编写客户端。语言: Java, C, C++, C#, Ruby, Perl, Python, PHP。应用协议: OpenWire,Stomp REST,WS Notification,XMPP,AMQP
- 完全支持JMS1.1和J2EE 1.4规范 (持久化,XA消息,事务)
- 对Spring的支持,ActiveMQ可以很容易内嵌到使用Spring的系统里面去,而且也支持Spring2.0的特性
- 通过了常见J2EE服务器(如 Geronimo,JBoss 4, GlassFish,WebLogic)的测试,其中通过JCA 1.5 resource adaptors的配置,可以让ActiveMQ可以自动的部署到任何兼容J2EE 1.4 商业服务器上
- 支持多种传送协议:in-VM,TCP,SSL,NIO,UDP,JGroups,JXTA
- 支持通过JDBC和journal提供高速的消息持久化
- 从设计上保证了高性能的集群,客户端-服务器,点对点
- 支持Ajax
- 支持与Axis的整合
- 可以很容易得调用内嵌JMS provider,进行测试
- 什么情况下使用ActiveMQ?

1. 多个项目之间集成 
(1) 跨平台 
(2) 多语言 
(3) 多项目
1. 降低系统间模块的耦合度，解耦 
(1) 软件扩展性
1. 系统前后端隔离 
(1) 前后端隔离，屏蔽高安全区
其实ActiveMQ的应用还有很多，大家可以上网查查，不再一一举例。

总结

ActiveMQ并不难，具有很多的优势。

下篇博文，我们做一个简单实例，真正的体会一把ActiveMQ的魅力。

--- 
 深入浅出JMS(四)--Spring和ActiveMQ整合的完整实例
标签： activemqjms消息中间件spring
2015-09-27 00:13 16511人阅读 评论(36) 收藏 举报
 分类： 【ActiveMQ】（3）  
版权声明：本文为博主原创文章，未经博主允许不得转载。
目录(?)[+]
第一篇博文深入浅出JMS(一)–JMS基本概念，我们介绍了JMS的两种消息模型：点对点和发布订阅模型，以及消息被消费的两个方式：同步和异步，JMS编程模型的对象，最后说了JMS的优点。

第二篇博文深入浅出JMS(二)–ActiveMQ简单介绍以及安装，我们介绍了消息中间件ActiveMQ，安装，启动，以及优缺点。

第三篇博文深入浅出JMS(三)–ActiveMQ简单的HelloWorld实例，我们实现了一种点对点的同步消息模型，并没有给大家呈现发布订阅模型。

前言

这篇博文,我们基于Spring+JMS+ActiveMQ+Tomcat，做一个Spring4.1.0和ActiveMQ5.11.1整合实例，实现了Point-To-Point的异步队列消息和PUB/SUB（发布/订阅）模型，简单实例，不包含任何业务。
环境准备

工具

JDK1.6或1.7

Spring4.1.0

ActiveMQ5.11.1

Tomcat7.x

目录结构

这里写图片描述

所需jar包

这里写图片描述

项目的配置

配置ConnectionFactory

connectionFactory是Spring用于创建到JMS服务器链接的，Spring提供了多种connectionFactory，我们介绍两个SingleConnectionFactory和CachingConnectionFactory。

SingleConnectionFactory：对于建立JMS服务器链接的请求会一直返回同一个链接，并且会忽略Connection的close方法调用。

CachingConnectionFactory：继承了SingleConnectionFactory，所以它拥有SingleConnectionFactory的所有功能，同时它还新增了缓存功能，它可以缓存Session、MessageProducer和MessageConsumer。我们使用CachingConnectionFactory来作为示例。

<bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
    </bean>

Spring提供的ConnectionFactory只是Spring用于管理ConnectionFactory的，真正产生到JMS服务器链接的ConnectionFactory还得是由JMS服务厂商提供，并且需要把它注入到Spring提供的ConnectionFactory中。我们这里使用的是ActiveMQ实现的JMS，所以在我们这里真正的可以产生Connection的就应该是由ActiveMQ提供的ConnectionFactory。所以定义一个ConnectionFactory的完整代码应该如下所示：

    <!-- ActiveMQ 连接工厂 -->
    <!-- 真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供-->
    <!-- 如果连接网络：tcp://ip:61616；未连接网络：tcp://localhost:61616 以及用户名，密码-->
    <amq:connectionFactory id="amqConnectionFactory"
        brokerURL="tcp://192.168.3.3:61616" userName="admin" password="admin"  />

    <!-- Spring Caching连接工厂 -->
    <!-- Spring用于管理真正的ConnectionFactory的ConnectionFactory -->  
    <bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
        <!-- 目标ConnectionFactory对应真实的可以产生JMS Connection的ConnectionFactory -->  
        <property name="targetConnectionFactory" ref="amqConnectionFactory"></property>
        <!-- 同上，同理 -->
        <!-- <constructor-arg ref="amqConnectionFactory" /> -->
        <!-- Session缓存数量 -->
        <property name="sessionCacheSize" value="100" />
    </bean>

配置生产者

配置好ConnectionFactory之后我们就需要配置生产者。生产者负责产生消息并发送到JMS服务器。但是我们要怎么进行消息发送呢？通常是利用Spring为我们提供的JmsTemplate类来实现的，所以配置生产者其实最核心的就是配置消息发送的JmsTemplate。对于消息发送者而言，它在发送消息的时候要知道自己该往哪里发，为此，我们在定义JmsTemplate的时候需要注入一个Spring提供的ConnectionFactory对象。

在利用JmsTemplate进行消息发送的时候，我们需要知道发送哪种消息类型：一个是点对点的ActiveMQQueue，另一个就是支持订阅/发布模式的ActiveMQTopic。如下所示：

    <!-- Spring JmsTemplate 的消息生产者 start-->

    <!-- 定义JmsTemplate的Queue类型 -->
    <bean id="jmsQueueTemplate" class="org.springframework.jms.core.JmsTemplate">
        <!-- 这个connectionFactory对应的是我们定义的Spring提供的那个ConnectionFactory对象 -->  
        <constructor-arg ref="connectionFactory" />
        <!-- 非pub/sub模型（发布/订阅），即队列模式 -->
        <property name="pubSubDomain" value="false" />
    </bean>

    <!-- 定义JmsTemplate的Topic类型 -->
    <bean id="jmsTopicTemplate" class="org.springframework.jms.core.JmsTemplate">
         <!-- 这个connectionFactory对应的是我们定义的Spring提供的那个ConnectionFactory对象 -->  
        <constructor-arg ref="connectionFactory" />
        <!-- pub/sub模型（发布/订阅） -->
        <property name="pubSubDomain" value="true" />
    </bean>

    <!--Spring JmsTemplate 的消息生产者 end-->

生产者如何指定目的地和发送消息？大家看源码即可，就不再这提供了。

配置消费者

生产者往指定目的地Destination发送消息后，接下来就是消费者对指定目的地的消息进行消费了。那么消费者是如何知道有生产者发送消息到指定目的地Destination了呢？每个消费者对应每个目的地都需要有对应的MessageListenerContainer。对于消息监听容器而言，除了要知道监听哪个目的地之外，还需要知道到哪里去监听，也就是说它还需要知道去监听哪个JMS服务器，通过配置MessageListenerContainer的时候往里面注入一个ConnectionFactory来实现的。所以我们在配置一个MessageListenerContainer的时候有三个属性必须指定：一个是表示从哪里监听的ConnectionFactory；一个是表示监听什么的Destination；一个是接收到消息以后进行消息处理的MessageListener。

<!-- 消息消费者 start-->

    <!-- 定义Queue监听器 -->
    <jms:listener-container destination-type="queue" container-type="default" connection-factory="connectionFactory" acknowledge="auto">
        <jms:listener destination="test.queue" ref="queueReceiver1"/>
        <jms:listener destination="test.queue" ref="queueReceiver2"/>
    </jms:listener-container>

    <!-- 定义Topic监听器 -->
    <jms:listener-container destination-type="topic" container-type="default" connection-factory="connectionFactory" acknowledge="auto">
        <jms:listener destination="test.topic" ref="topicReceiver1"/>
        <jms:listener destination="test.topic" ref="topicReceiver2"/>
    </jms:listener-container>

    <!-- 消息消费者 end -->

ActiveMQ.xml

此时，Spring和JMS，ActiveMQ整合的ActiveMQ.xml已经完成，下面展示所有的xml
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:amq="http://activemq.apache.org/schema/core"
    xmlns:jms="http://www.springframework.org/schema/jms"
    xsi:schemaLocation="http://www.springframework.org/schema/beans   
        http://www.springframework.org/schema/beans/spring-beans-4.0.xsd   
        http://www.springframework.org/schema/context   
        http://www.springframework.org/schema/context/spring-context-4.0.xsd
        http://www.springframework.org/schema/jms
        http://www.springframework.org/schema/jms/spring-jms-4.0.xsd
        http://activemq.apache.org/schema/core
        http://activemq.apache.org/schema/core/activemq-core-5.8.0.xsd">

    <!-- ActiveMQ 连接工厂 -->
    <!-- 真正可以产生Connection的ConnectionFactory，由对应的 JMS服务厂商提供-->
    <!-- 如果连接网络：tcp://ip:61616；未连接网络：tcp://localhost:61616 以及用户名，密码-->
    <amq:connectionFactory id="amqConnectionFactory"
        brokerURL="tcp://192.168.3.3:61616" userName="admin" password="admin"  />

    <!-- Spring Caching连接工厂 -->
    <!-- Spring用于管理真正的ConnectionFactory的ConnectionFactory -->  
    <bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
        <!-- 目标ConnectionFactory对应真实的可以产生JMS Connection的ConnectionFactory -->  
        <property name="targetConnectionFactory" ref="amqConnectionFactory"></property>
        <!-- 同上，同理 -->
        <!-- <constructor-arg ref="amqConnectionFactory" /> -->
        <!-- Session缓存数量 -->
        <property name="sessionCacheSize" value="100" />
    </bean>

    <!-- Spring JmsTemplate 的消息生产者 start-->

    <!-- 定义JmsTemplate的Queue类型 -->
    <bean id="jmsQueueTemplate" class="org.springframework.jms.core.JmsTemplate">
        <!-- 这个connectionFactory对应的是我们定义的Spring提供的那个ConnectionFactory对象 -->  
        <constructor-arg ref="connectionFactory" />
        <!-- 非pub/sub模型（发布/订阅），即队列模式 -->
        <property name="pubSubDomain" value="false" />
    </bean>

    <!-- 定义JmsTemplate的Topic类型 -->
    <bean id="jmsTopicTemplate" class="org.springframework.jms.core.JmsTemplate">
         <!-- 这个connectionFactory对应的是我们定义的Spring提供的那个ConnectionFactory对象 -->  
        <constructor-arg ref="connectionFactory" />
        <!-- pub/sub模型（发布/订阅） -->
        <property name="pubSubDomain" value="true" />
    </bean>

    <!--Spring JmsTemplate 的消息生产者 end-->


    <!-- 消息消费者 start-->

    <!-- 定义Queue监听器 -->
    <jms:listener-container destination-type="queue" container-type="default" connection-factory="connectionFactory" acknowledge="auto">
        <jms:listener destination="test.queue" ref="queueReceiver1"/>
        <jms:listener destination="test.queue" ref="queueReceiver2"/>
    </jms:listener-container>

    <!-- 定义Topic监听器 -->
    <jms:listener-container destination-type="topic" container-type="default" connection-factory="connectionFactory" acknowledge="auto">
        <jms:listener destination="test.topic" ref="topicReceiver1"/>
        <jms:listener destination="test.topic" ref="topicReceiver2"/>
    </jms:listener-container>

    <!-- 消息消费者 end -->
</beans>  
``` 
鉴于博文内容较多，我们只是在粘贴web.xml的配置，就不在博文中提供Spring和SpringMVC的XML配置，其他内容，大家查看源码即可。

web.xml

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
    version="3.0">
    <display-name>ActiveMQSpringDemo</display-name>

    <!-- Log4J Start -->
    <context-param>
        <param-name>log4jConfigLocation</param-name>
        <param-value>classpath:log4j.properties</param-value>
    </context-param>
    <context-param>
        <param-name>log4jRefreshInterval</param-name>
        <param-value>6000</param-value>
    </context-param>
    <!-- Spring Log4J config -->
    <listener>
        <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
    </listener>
    <!-- Log4J End -->

    <!-- Spring 编码过滤器 start -->
    <filter>
        <filter-name>characterEncoding</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterEncoding</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    <!-- Spring 编码过滤器 End -->

    <!-- Spring Application Context Listener Start -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath*:applicationContext.xml,classpath*:ActiveMQ.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!-- Spring Application Context Listener End -->


    <!-- Spring MVC Config Start -->
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>

        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-mvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <!-- Filter all resources -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <!-- Spring MVC Config End -->

</web-app>

运行效果

这里写图片描述

这里写图片描述

从上图可以看出队列模型和PUB/SUB模型的区别，Queue只能由一个消费者接收，其他Queue中的成员无法接受到被已消费的信息，而Topic则可以，只要是订阅了Topic的消费者，全部可以获取到生产者发布的信息。

总结

Spring提供了对JMS的支持，ActiveMQ提供了很好的实现，而此时我们已经将两者完美的结合在了一起。

下篇博文我们实现Spring和ActiveMQ消息的持久化。

--- 

问题引出 
	一个系统内容 实现服务调用 
	多个系统 间 webservice 调用
    MQ 消息中间件 

StreamMessage
MapMessage
TextMessage
ObjectMessage
ByteMessage

