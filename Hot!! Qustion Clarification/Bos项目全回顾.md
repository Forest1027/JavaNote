# Bos项目全回顾
>思路：
> 
> * 项目分模块概述
> * 该模块主要实现的功能
> * 实现的业务逻辑
> * 使用的技术点
> * 技术点详解

## 宣传活动模块

### 主要功能
* 后台(bos_management)管理宣传活动
	* 增删改查
	* 分页展示
* 前台(bos_fore)展示宣传活动
	* 分页展示所有活动
	* 展示活动详情
* 宣传活动定时过期功能实现

### 业务逻辑
#### 后台(bos_management)管理宣传活动
**主要做的增加功能**

* 编辑器中的图片上传
	* 将上传的图片保存
		* 获取真实路径
		* 获取上传的图片的后缀名
		* 使用uuid生成随机文件名
		* 将文件名与后缀名拼接
		* 将上传的文件写入真实路径下的uuid随机名文件中
	* 通知浏览器文件上传成功
		* 返回成功与否的状态信息
		* 返回contextPath(因为编辑器中要显示该上传的图片)
* 图片空间管理
	
> 文件上传注意点：
> * form的enctype="multipart/form-data"
> * action中定义
> ```
	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
> ```

* 表单提交
	* 保存表单中的title图片，方法同上，只是不用返回上传成功的信息
	* 将contextPath路径以及其他表单的数据存入model
	* 调用业务层，dao层，对数据库进行增加操作

**技术点**

* KindEditor
	* 在线html编辑器
	* 使用方法：将官方文件部分拷入webapp文件夹下
##### 分页展示
* 使用datagrid，向后台传page和rows两个参数
* 后台收到这两个参数，创建pageable对象
* 调用业务层、dao层完成查询
* 将总条数、该页具体信息返回浏览器

#### 前台(bos_fore)展示宣传活动
* 分页展示所有活动
	* 查询所有活动
		* 接收page和rows
		* 使用WebService从bos_management中获取宣传活动的数据
		* 在bos_management的service层定义接口，接收此两个参数，并调用dao层方法，返回数据
	* 分页显示活动

>跨系统查询活动的难点
> * 以往查询数据使用Spring中自带的Page对象封装数据。 由于此Page对象不是一个完全的POJO类，它没有setter方法。在传递给bos_fore，将json数据转化成Page对象时无setter方法可用，则无法成功转换
> * 因此，需自定义一个PageBean。PageBean与Promotion都需要使用@XmlRootElement(name="...")注解
> * 另外，bos_management需要配置web.xml和applicationContext.xml
> * 在WebService中需要返回带有泛型的集合，如PageBean中有一个属性为 private List<T> pageData，此时需要在类上加上注解:@XmlSeeAlso({Promotion.class})

* 展示活动详情
	* 
#### 宣传活动定时过期功能实现