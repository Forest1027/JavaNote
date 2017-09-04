# Spring解决乱码问题的三种方法
解决post请求代码：在web.xml中加入：
```
<filter>
	<filter-name>CharacterEncodingFilter</filter-name>
	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
	<init-param>
		<param-name>encoding</param-name>
		<param-value>utf-8</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>CharacterEncodingFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>

```

解决get请求乱码：

方法一：修改tomcat配置文件添加编码与工程编码一致，如下(server-->server.xml中配置URIEncoding)
```
<Connector URIEncoding="utf-8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>
```

方法二：对参数进行重新编码
```
String userName new = String(request.getParamter("userName").getBytes("ISO8859-1"),"utf-8")

```