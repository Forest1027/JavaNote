# ssm整合思路

5.2	jar包

包括：spring（包括springmvc）、mybatis、mybatis-spring整合包、数据库驱动、第三方连接池。
参考：“mybatis与springmvc整合全部jar包”目录 

5.3	工程搭建

5.3.1	整合思路

Dao层：

1、SqlMapConfig.xml，空文件即可。需要文件头。

2、applicationContext-dao.xml。

a)	数据库连接池

b)	SqlSessionFactory对象，需要spring和mybatis整合包下的。

c)	配置mapper文件扫描器。

Service层：

1、applicationContext-service.xml包扫描器，扫描@service注解的类。

2、applicationContext-trans.xml配置事务。

表现层：

Springmvc.xml

1、包扫描器，扫描@Controller注解的类。

2、配置注解驱动。

3、视图解析器

Web.xml

配置前端控制器。

# 自定义参数绑定解决日期Date对象的生成
需求：由于日期数据有很多种格式，所以springmvc没办法把字符串转换成日期类型。所以需要自定义参数绑定。前端控制器接收到请求后，找到注解形式的处理器适配器，对RequestMapping标记的方法进行适配，并对方法中的形参进行参数绑定。在springmvc这可以在处理器适配器上自定义Converter进行参数绑定。如果使用<mvc:annotation-driven/>可以在此标签上进行扩展。*此处在同文件夹下的工程中亦有编写。*
```
public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return simpleDateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}

```