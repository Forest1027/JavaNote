# Bos项目全回顾
>思路：
> 
> * 项目分模块概述
> * 该模块主要实现的功能
> * 实现的业务逻辑
> * 使用的技术点
> * 技术点详解

模板
## 模块名
### 主要功能
* 主要功能

### 业务逻辑
#### 用此标题，将主要功能逐条对应扩充 *if necessary*
* 主要功能
> 注意点。。。
**技术点**

## 1、项目搭建
### 主要功能
* 确认开发工具
* 综述整个项目用到的技术
* 后台界面解决方案--EasyUI；树形菜单--ZeeTree

## 2、基础模块-取派管理
### 主要功能
* 基础数据表搭建
* 收派标准的CRUD
* 快递员的CRUD

### 业务逻辑
* 收派标准的CRUD
	* 增加：
		* validatebox的使用
	* 删除：
	* 修改：
		* 表单回显
			* $("#id").form('load',数据来源)
			* 提供隐藏域装载id
	* 查询：
		* 分页查询
			* 【Action】有条件查询时，使用Specification接口.并将其连同pageable传到service
			* 【Dao】Dao层继承JpaSpecificationExecutor<T>
* 快递员的CRUD
	* 增、改、查同收派标准
	* 删除
		* 实际为改，将detag改为1
		* 【页面】使用datagrid的formatter，进行逻辑判断，选择显示的内容
		* datagrid的getSelections方法

## 3、区域设置
### 主要功能
* 批量上传
* 分页查询

### 业务逻辑
* 批量上传
	* 【前端】一键上传
		* jQuery ocupload-->隐藏文件上传的传统页面、给“选择文件”的内容绑定onchange时间
		* 拷贝文件到工程中，页面引用
	* 【前端】判断后缀
		* 取消自动提交表单
		* 使用正则表达式，判断扩展名是否为excel会有的
	* 【后台】使用Apache POI解析excel
		* 导包
		* excel book--sheet--row--cell
	* 【后台】Pinyin4j实现拼音简码的自动生成
		* 导入jar包
		* 使用写好的pinyinUtils
* 分页查询
* 代码优化
	* 抽取BaseAction
		* model
		* page/row
**技术点**

## 6、宣传活动模块

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
	* 前端页面
		* 让查看详情的超链接值携带该活动的id
		* index页面的路由的条件链接上在参数值前面加上“:”则可以接收这个id
		* 将接收到的参数携带到访问action的链接上
	* 后台逻辑
		*  接收id，并创建包含此id的特定文件路径
		*  判断这个文件路径是否存在
			*  存在-->将此文件的内容用输出流打印到页面
			*  不存在-->
				*  从bos_management中查询出此活动
				*  利用Freemarker静态页面生成技术，结合模板和查询到的数据，生成此文件
				*  将此文件的内容用输出流打印到页面

**技术点**

* AngularJs路由$routeParam的使用
* FreeMarker
	* 是什么？
		* 一种静态页面化技术
			* 服务器将根据id查询到的数据动态生成一个html页面
			* 下次再查询相同数据的时候，直接将此页面返回
			* 此举有利于减少与数据库的交互，提高查询效率
		* 原理
			* 模板+Java数据对象-->输出任意格式文本，html/jsp...
			* 后缀名通常为ftl
	* 怎么用？
		* jar包-导入struts2的时候自动导了
		* 插件-freemarker_eclipseplugin复制到elipse的dropins文件夹下
		* 编辑模板-放在WEB-INF/classes下
		* 变量声明格式-${变量}
		* 编写代码-获取数据、模板，并将两者整合到一起输出文件
		
#### 宣传活动定时过期功能实现
* 当宣传活动的期限过了之后，自动将状态改为“过期”

> 主要做的是，利用Quartz框架，每分钟(时间自定义)调用一次update方法，将当前时间大于“结束日期”并且状态仍为“进行中”的记录的状态改为“已过期”

* 导入依赖
	* quarts
	* quarts-jobs
* 编写Job和JobFactory
	* Quartz与Spring整合时，Job中由Spring管理的bean无法注入
	* 因此专门编写一个JobFactory(需要在schedule中配置)，将Job也置于Spring的管理中
* 编写Service/Dao进行update操作
* 配置applicationContext.xml

**技术点**

* Quartz
	* Job：工作任务，你要做什么，Job一般自定义实现，由接口JobDetail来创建
	* Trigger执行工作任务，什么时间执行，多久执行一次;常用的有：SimpleTrigger、CronTrigger
	* Scheduler定时器对象，开启定时任务

## 7、登录下单、工单管理
### 主要功能
* 用户登录
* 用户下单功能
	* 用户下单后表单提交
	* 用户系统自动分配快递员
	* 生成工单

### 业务逻辑
#### 用户登录
* 前端页面设置form、action、method及name
* bos_fore新增login方法
	* 用webService向crm_management请求数据，携带电话号及密码
	* crm_management新增服务接口，功能为根据telephone和password查询客户，并将此数据返回bos_fore
	* 如果customer对象为空，则登录失败-->返回login页面；如果customer对象不为空，则登录成功-->将customer对象存进session

#### 用户下单功能
* 用户下单后表单提交
	* 前端
		* citypicker实现省市三级联动
		* 填写地址自动补全
	* 后台

**技术点**

1. citypicker
	1. 拷贝压缩包中的部分内容到工程目录下
	2. 在页面引入city-picker,city-picker-data等文件
	3. 在需要实现省市三级联动的标签加data-toggle="city-picker"或$("#area").citypicker()js调用


* 用户系统自动分配快递员
* 生成工单

> 注意点。。。

