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
