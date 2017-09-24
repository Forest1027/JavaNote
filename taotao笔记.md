# Day1
tomcat的并发量撑死400多个。

如何解决高并发和高可用

高并发：同一时间访问服务器的量
高可用：随时都可以用，什么情况都能用。使用集群解决

【待确定】
当当等大网站，并发量有一万就差不多了
十一那天，淘宝的并发量是一亿

1. 什么是分布式？
	1. 
2. 什么是集群？
	1. 一群服务器提供相同的服务

## 高并发的解决
>Tomcat集群是什么样的集群？
>设置session共享。其中一个tomcat的session改了，就要发送session信息，让别的也进行更改。因此tomcat集群超过5个，容易形成内网风暴（只能传session，处理不了逻辑了）
>使用nginx，并发量最高支持两万，一般算一万。并发量超过500，就是用nginx

软件无能为力，就考虑硬件：
f5负载均衡服务器。并发量4-5万

硬件也不够了，就考虑网络：
DNS服务器。从区域上就分开，不同地区访问不同服务器

处理请求的速度，也会影响并发量。让cpu少处理tomcat，多处理业务逻辑。-->分布式（将完整的系统拆成多个子系统，一起运行）

分布与集群的区别：
分布-运行的代码不同
集群-运行的代码相同

子系统间的通信 dubbo，hessian，CXF Webservice

高可用也是搭建集群来解决

## 工程构建
pojo,dao,interface单独达成jar包的目的：方便别人使用，直接依赖过去就好

什么是Maven project？什么是Maven module？

依赖管理和依赖的区别？
依赖管理：定义要使用jar包的版本
依赖：具体要使用的jar包
先进行管理，再进行导包

# Day2
1. 练习linux命令
2. dubbo
	1. 为什么使用dubbo
		1. 基于rpc协议（远程调用协议），使用socket通信。**可以统计出系统之间的调用关系和调用次数。**【可以观察出哪里需要搭建集群】/*dubbox是当当在dubbo上进行二次开发后*/
	2. 什么是dubbo
		1. alibaba旗下开源的项目
		2. 理解dubbo的四个角色
		3. 是同步通讯技术
	3. 如何使用dubbo

soa：面向服务的架构
服务：有一定业务逻辑，可被别人调用的接口

Webservice基于soap协议，传送的xml，因此效率不高。技术古老。通常是天气预报会用。
替代：使用RestFul风格的服务，http+json。一般对外提供接口

socket和package
ejb开发 重量级开发，古老，没人用了

ThreadLocal 保证线程安全。每一个线程都有一个treadlocal

## Day3
软件即服务

常见的异常解决办法
1. 启动前要确保所有项目都安装过
2. manager和web先启动谁无所谓，不会报错
3. 所有用来传输的数据都要事先serilizable接口
	1. dubbo是以二进制传输的，因此效率很高
4. 资源拷贝问题
	1. src文件夹下默认只编译java文件，不会编译xml文件

要启动监控中心，必须先启动zookeeper

log4j和监控中心一定要加上，方便自己调错

js中定义一个变量a，如果为null，undefined，0，false，""的时候，if(a){},a相当于false，除此之外，都相当于true

$("<div>")创建一个div

### FastDFS
分布式文件系统。线性扩容

FastDFS由两部分组成，其中tracker负责计算，storage负责存储

## Day4
**sku** 最小库存单位，能根据各种属性唯一确定一个商品
商品：
1. 关键属性--找商品：名称、品牌
2. 销售属性--确定某一个商品：颜色、价格、大小

SEO 搜索引擎优化。搜索引擎喜欢静态资源，所以提供伪静态化方案

瀑布流——鼠标滚动，出发事件，引发ajax发送异步请求

## Day5
一般来说最后开发首页。

**redis**

NoSQL数据库，内存数据库。将所有数据放在内存中。Key-Value格式
优点：读写速度快。
缺点：内存大小限制；占内存。

缓存同步:一般来说开发中要保证数据唯一性。即，只有一份数据起作用。但是缓存机制，是不符合这个原则的。因此我们要保证缓存中的数据与数据库中的数据要一样，于是就有了缓存同步。

加缓存的目的，仅仅是为了提高用户体验。**所有优化操作，不能影响业务逻辑的运行。**

什么是分布式事务？dubbo、webservice、hissian的区别?
是否用过图片服务器？

项目经理：良好的沟通，完善的逻辑思维

只要对查询速度有要求，就可以用缓存。

Redis不适合保存内容大的数据，是单线程的。

Redis的五种基本数据类型。都是以key-value形式做基础，存的所有数据都是字符串
1. String
	1. **get、set**。命令不区分大小写。**incr加一、decr减一**命令
	2. key、value区分大小写
2. Hash
	1. 相当于一个key对一个map
	2. **hset、hget**，向hash中设值取值；hincrby
3. List 有顺序可重复
	1. lpush：从左添加；rpush：从右添加
	2. lrange 查看；lpop 从左弹出；rpop 从右弹出
4. Set 元素无顺序，不重复
	1. sadd、smembers srem
5. SortedSet 有顺序不重复
	1. 做排行榜

是否有事务问题？考虑业务逻辑是否需要具有原子性（一个整体，全部成功或全部失败）

redis的事务做的不是特别好，只提供了watch。只是在有改动的时候，进行提示。

redis的持久化
rdb 快照
aof bgrewriteaof命令-删除aof中冗余命令行

## Day6
为什么要在spring中配置solrserver？--在程序运行期间保证只有一个solrserver。因为不断获取连接很消耗资源

为什么搭建zookeeper的集群？--保证zookeeper的高可用，solr集群基于zookeeper。所有solr服务器都使用zookeeper管理的**配置文件**。zookeeper充当solr集群的入口，由zookeeper计算访问哪一个服务器【**负载均衡**】。因此，我们搭建了zookeeper集群来保证zookeeper的高可用。

1. 搜索功能完成
	1. service
	2. controller
	3. 图片展示
2. solrcloud
	1. solr提供的的分布式搜索方案。基于zookeeper
	2. 搭建solr集群
3. 全局异常处理

一个完整的索引库分成多个分片。所有分片集合起来，才是一个完整的索引库。
zookeeper的作用：1.统一管理配置文件；2.负载均衡，是solr集群的入口

## Day7
为什么要使用ActiveMQ？
	
	为了使程序中与主逻辑无关的逻辑不影响主逻辑的运行。为了解耦，为了业务拆分明确。

ActiveMQ与dubbo的区别？
	
	dubbo是同步请求服务，ActiveMQ是一部服务。dubbo可以集群，ActiveMQ一般来说是没有集群的。

	需要实时调用服务，需要得到结果的时候用dubbo；用dubbo时，每件事都是必须完成的。开线程的时候，不需要结果的时候用ActiveMQ。

ActiveMQ发送消息却丢失了，怎么办？
信息丢失肯定会有，但是情况不多。

RabbitMQ有重发机制，连发5次没成功，就把这个错误放到错误池里。

ActiveMQ小巧、方便，RabbitMQ重量级。

