# QC for Web
## Servlet数据访问范围总结
1. 请求范围（ServletRequest）
	1. 何时创建和销毁的
		1. 创建：当用户向服务器发送一次请求，服务器创建一个request对象。
		2. 销毁：当服务器对这次请求作出了响应，服务器就会销毁这个request对象。
	2. 如何存取数据
		1. 存数据：void setAttribute(String name,Object value);
		2. 取数据：Object getAttribute(String name);
	3. 作用范围：一次请求。（转发就是一次请求）。
2. 会话范围（HttpSession）
	1. 何时创建和销毁的
		1. 创建：服务器端第一次调用getSession()方法的时候。
		2. 销毁：三种情况。
			1. Session过期，默认的过期时间30分钟(web.xml中配置)。
			2. 非正常关闭服务器。（正常关闭服务器—session会被序列化）。
			3. 手动调用session.invalidate();
	2. 如何存取数据
		1. 存数据：void setAttribute(String name,Object value);
		2. 取数据：Object getAttribute(String name);
	3. 作用范围：一次会话（多次请求）
3. 应用范围（ServletContext）
	1. 何时创建和销毁的
		1. 创建：服务器启动的时候创建，为每个web项目创建一个单独ServletContext对象。 
		2. 销毁：服务器关闭的时候，或者项目从服务器中移除的时候。
	2. 如何存取数据
		1. 存数据：void setAttribute(String name,Object value);
		2. 取数据：Object getAttribute(String name);
	3. 作用范围：整个应用

> 注意：
> 
> 什么是会话：从用户打开浏览器访问web资源开始、到用户关闭浏览器，这一整个过程就称为一次会话。
> 
> 我们需要作用范围在一次会话内的域对象来保存购物车商品信息。此时request域和servletcontext域均无法满足需求。

## Cookie和Session的实现原理及区别
1. 会话技术分为Cookie和Session。
	1. Cookie：Cookie属于客户端技术，web程序通过cookie响应头告诉浏览器保存用户的一些信息，当用户再用浏览器来访问这个web程序时就会带着之前cookie保存的数据去。
		1. 会话级别的Cookie
			1. 没有设置Cookie的有效时间，那么它默认存活的时间就是一次会话（直到浏览器关闭），这个级别的Cookie是保存在浏览器的内存中的，所以浏览器关闭了，数据也就销毁了。
		2. 持久化的Cookie
			1. 设置了Cookie的有效时间，这种级别的Cookie会被保存到硬盘中，当我们下次打开浏览器时，浏览器会自己去加载这个文件读取Cookie信息发送给服务器。
	2. Session：Session属于服务器技术，用户通过浏览器去访问web程序，web程序会根据用户需求在服务器开辟一块唯一的空间（创建一个独享的Session对象）来存储信息，并把这块存储区域的唯一id通过响应头告诉浏览器(Cookie)保存起来。当用户下次使用浏览器去访问web程序时会带着这个id去服务器找这块区域的数据。
2. 区别
	1. Cookie是把数据保存在浏览器(相对不安全)，Session是把数据保存在服务器(相对安全)。
	2. Cookie是有个数和大小限制的；而Session没有。

> 注意：
> 
> 浏览器存储的Cookie是有限制的，Cookie存的值也有大小限制。
> 
> Session其实是基于Cookie实现的，因为服务器会将一个唯一的SessionId写到Cookie里面，响应给浏览器，浏览器下次访问服务器会带着这个Cookie里面的SessionId来找到对应的区域数据。

## jQuery选择器
基本选择器：#id  .class  element  #id,.class,element组合

层级选择器：父子关系（$(“div #d”)   $(“div>#d”)）；兄弟选择器（$(“div+p”)  $(“div~p”)）

基本过滤选择器：:first    :last      :not(selector)      :even     :odd 

内容过滤选择器：:contains(text)   :empty    :has(selector) 

属性过滤选择器：[attribute]    [attribute=value]    [attribute*=value]     [attrSel1][attrSel2][attrSelN] 

子元素过滤选择器:	: nth-child  	:first-child 	:last-child 

表单元素过滤选择器：:input     :text     :password     :radio     :checkbox     :submit     :button 

表单元素属性过滤选择器：:checked   :selected 

## ajax
[原理图](http://www.jb51.net/article/90528.htm)
[ajax原理及优缺点](http://www.cnblogs.com/SanMaoSpace/archive/2013/06/15/3137180.html)
1. 是什么？
	1. 通过在后台与服务器进行少量数据交换，ajax可以使网页实现异步更新。这意味着可以在不重新加载整个网页的情况下，对网页的某部分进行更新。
	2. 异步：
		1. 当前页面发送一个请求给服务器，当前页面不需要等待服务器响应才能操作网页。发送完请求之后，当前页面可以继续浏览，操作。
2. 工作原理？
	1. 在用户和服务器之间加一个中间层（ajax引擎）
	1. ajax的核心对象是XMLHttpRequest对象
		1. 其readyState属性有0，1，2，3，4五个取值。用于记录服务器的处理状态
		2. 当readyState值发生改变，触发其onreadystatechange绑定的回调方法
		3. 其responseText和responseXML属性用于在前台展示数据

## jsp
1. 是什么？
	1. java server page，java服务器页面，是一个简化的Servlet，是在html中插入java代码形成的文件。
2. [运行原理？](http://blog.csdn.net/oncealong/article/details/51393266)
	1. 浏览器发送请求
	2. 找到index.jsp
	3. index.jsp翻译成index_jsp.java
	4. 再编译成index_jsp.class
	5. 执行class文件，创建Servlet实例
	6. 调用jspInit方法，初始化
	7. 通过jspService接收请求
	8. 作出响应，销毁
3. jsp九大内置对象
	1. 所谓的内置对象就是可以直接使用的对象。（**以下的前六个要记住**）
	2. request：客户端向服务器发送的请求对象。------------------------HttpServletRequest--------REQUEST_SCOPE
	3. response：服务器向客户端作的响应对象。-------------------------HttpServletResponse-------PAGE_SCOPE
	4. session：客户端和服务器之间的会话对象。------------------------HttpSession---------------SESSION_SCOPE
	5. application：整个应用----------------------------------------ServletContext------------APPLICATION_SCOPE
	6. pageContext：当前jsp页面的上下文对象--------------------------PageContext---------------PAGE_SCOPE
	7. page：当前jsp页面被翻译成的Servlet对象------------------------Object--------------------PAGE_SCOPE
	8. out：页面输出内容的对象---------------------------------------JspWriter-----------------PAGE_SCOPE
	9. config：当前jsp的ServletConfig对象---------------------------ServletConfig--------------PAGE_SCOPE
	10. exception：当前页面的异常对象，只有当前页面的page指令中指明了isErrorPage=”true”才有此对象。-------- Throwable---PAGE_SCOPE
4. jsp的四个作用范围
	1. pageContext提供四个常量
		1. PAGE_SCOPE：页面范围
			1. 指的是在当前的页面内有效，出了这个页面，用pageContext保存的数据就无效了。
		2. REQUEST_SCOPE：请求范围
			1. 从客户端向服务器发送一次请求，直到服务器作出了响应之后，数据就无效，相当于Request域，也可以使用Request取值。
		3. SESSION_SCOPE：会话范围
			1. 从打开浏览器到关闭浏览器数以一次会话，在这个会话内数据都有效，相当于Session域。
		4. APPLICATION_SCOPE：应用范围
			1. 在整个应用中任意的地方都可以获取，相当于ServletContext域。
	2. pageContext对象有2个作用：
		1. 作为第四大域对象进行存值、取值、移除值、查找值（该域对象独有的方法）
		2. 作为一个jsp的内置对象，同时可以获取其它8个内置对象