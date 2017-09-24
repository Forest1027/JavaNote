# Tips
一、我们都知道在基于Spring的Application中，需要在web.xml中增加下面类似的配置信息：

　　<listener>
　　<listener-class>
　　org.springframework.web.context.ContextLoaderListener
　　</listener-class>
　　</listener>

　　<!--   Spring MVC Servlet -->

　　<servlet>
　　<servlet-name>servletName</servlet-name>
　　<servlet-class>
　　org.springframework.web.servlet.DispatcherServlet
　　</servlet-class>
　　<load-on-startup>1</load-on-startup>
　　</servlet>

　　<servlet-mapping>
　　<servlet-name>servletName</servlet-name>
　　<url-pattern>/</url-pattern>
　　</servlet-mapping>
　　此处需要特别强调的是 <url-pattern>/</url-pattern>使用的是/，而不是/*,如果使用/*,那么请求时可以通过DispatcherServlet转发到相应的Action或者Controller中的，但是返回的内容，如返回的jsp还会再次被拦截，这样导致404错误，即访问不到jsp。所以如果以后发现总是有404错误的时候，别忘了check一下 <url-pattern>/</url-pattern>的配置是否是/*.

　　二、其实Spring 的Servlet拦截器匹配规则（即 <url-pattern>...</url-pattern> ）都可以自己定义，例：当映射为@RequestMapping("/user/add")时

　　1、拦截*.do、*.htm， 例如：/user/add.do

　　这是最传统的方式，最简单也最实用。不会导致静态文件（jpg,js,css）被拦截。

　　2、拦截/，例如：/user/add

　　可以实现现在很流行的REST风格。很多互联网类型的应用很喜欢这种风格的URL。

　　弊端：会导致静态文件（jpg,js,css）被拦截后不能正常显示。想实现REST风格，事情就是麻烦一些。后面有解决办法还算简单。

　　3、拦截/*，这是一个错误的方式，请求可以走到Action中，但转到jsp时再次被拦截，不能访问到jsp。

　　三、如何访问到静态的文件，如jpg,js,css?

　　如果你的DispatcherServlet拦截"*.do"这样的有后缀的URL，就不存在访问不到静态资源的问题。

　　如果你的DispatcherServlet拦截"/"，为了实现REST风格，拦截了所有的请求，那么同时对*.js,*.jpg等静态文件的访问也就被拦截了。

　　我们要解决这个问题。

　　目的：可以正常访问静态文件，不可以找不到静态文件报404。

　　方案一：激活Tomcat的defaultServlet来处理静态文件

　　<servlet-mapping>
　　<servlet-name>default</servlet-name>
　　<url-pattern>*.jpg</url-pattern>
　　</servlet-mapping>
　　<servlet-mapping>
　　<servlet-name>default</servlet-name>
　　<url-pattern>*.js</url-pattern>
　　</servlet-mapping>
　　<servlet-mapping>
　　<servlet-name>default</servlet-name>
　　<url-pattern>*.css</url-pattern>
　　</servlet-mapping>
　　特点：1.  要配置多个，每种文件配置一个。

　　2.  要写在DispatcherServlet的前面， 让 defaultServlet先拦截请求，这样请求就不会进入Spring了。

　　3. 高性能。

　　备注：

　　Tomcat, Jetty, JBoss, and GlassFish 自带的默认Servlet的名字 -- "default"
　　Google App Engine 自带的 默认Servlet的名字 -- "_ah_default"
　　Resin 自带的 默认Servlet的名字 -- "resin-file"
　　WebLogic 自带的 默认Servlet的名字  -- "FileServlet"
　　WebSphere  自带的 默认Servlet的名字 -- "SimpleFileServlet"

　　方案二： 在spring3.0.4以后版本提供了mvc:resources ，  使用方法：

　　<!-- 对静态资源文件的访问 -->
　　<mvc:resources mapping="/images/**" location="/images/" />
　　images/**映射到 ResourceHttpRequestHandler进行处理，location指定静态资源的位置.可以是web application根目录下、jar包里面，这样可以把静态资源压缩到jar包中。cache-period 可以使得静态资源进行web cache

　　
　　如果出现下面的错误，可能是没有配置<mvc:annotation-driven />的原因。
　　报错WARNING: No mapping found for HTTP request with URI [/mvc/user/findUser/lisi/770] in DispatcherServlet with name 'springMVC'

　　

　　使用<mvc:resources/>元素,把mapping的URI注册到SimpleUrlHandlerMapping的urlMap中,
　　key为mapping的URI pattern值,而value为ResourceHttpRequestHandler,
　　这样就巧妙的把对静态资源的访问由HandlerMapping转到ResourceHttpRequestHandler处理并返回,所以就支持classpath目录,jar包内静态资源的访问.
　　另外需要注意的一点是,不要对SimpleUrlHandlerMapping设置defaultHandler.因为对static uri的defaultHandler就是ResourceHttpRequestHandler,
　　否则无法处理static resources request.

　　方案三 ，使用<mvc:default-servlet-handler/>

　　<mvc:default-servlet-handler/>
　　会把"/**" url,注册到SimpleUrlHandlerMapping的urlMap中,把对静态资源的访问由HandlerMapping转到 org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler 处理并返回.
　　DefaultServletHttpRequestHandler使用就是各个Servlet容器自己的默认Servlet.

　　补充说明：多个HandlerMapping的执行顺序问题：

　　DefaultAnnotationHandlerMapping的order属性值是：0

　　< mvc:resources/ > 自动注册的 SimpleUrlHandlerMapping 的order属性值是： 2147483646

　　<mvc:default-servlet-handler/>自动注册 的SimpleUrlHandlerMapping 的order属性值是： 2147483647

　　spring会先执行order值比较小的。当访问一个a.jpg图片文件时，先通过 DefaultAnnotationHandlerMapping 来找处理器，一定是找不到的，因为我们没有叫a.jpg的Action。然后再按order值升序找，由于最后一个 SimpleUrlHandlerMapping 是匹配 "/**"的，所以一定会匹配上，就可以响应图片。 访问一个图片，还要走层层匹配。不知性能如何？

　　最后再说明一下，方案二、方案三 在访问静态资源时，如果有匹配的(近似)总拦截器，就会走拦截器。如果你在拦截中实现权限检查，要注意过滤这些对静态文件的请求。

　　如何你的DispatcherServlet拦截 *.do这样的URL后缀，就不存上述问题了。还是有后缀方便。