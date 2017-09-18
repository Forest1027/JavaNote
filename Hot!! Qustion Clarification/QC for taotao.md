# QC for taotao
## Dubbo
1. 什么是Dubbo？有什么用？
	1. alibaba旗下一个开源的项目，是一个rpc协议的远程通讯技术
	2. 网站规模越来越大，垂直应用架构已经无法应对，因此逐渐往分布式发展。使用分布式，则带来一个问题，即系统间如何互相调用。因此我们需要系统间能完成通讯
	3. 但是这样还不够，调用者【消费者】和被调用者【生产者】都很多，到底是谁调用了谁，调用谁的次数多呢？为了知道这个，我们就需要一个治理中心记录这一切。
	4. 有了治理中心，我们可以知道哪个服务被调用的频率高，哪个被调用的频率低，从而合理分配集群的个数，提高集群的利用率。
2. 为什么使用Dubbo
	1. Dubbo就是一个在分布式开发中，实现子系统之间通讯，并且提供治理中心以提高集群利用率的框架
	2. Dubbo的架构
		1. **Provider**: 暴露服务的服务提供方。
		2. **Consumer**: 调用远程服务的服务消费方。
		3. **Registry**: 服务注册与发现的注册中心。即治理中心
		4. Monitor: 统计服务的调用次调和调用时间的监控中心。
		5. Container: 服务运行容器。
3. 如何使用Dubbo
```
<!--发布服务：-->
<!-- 和本地服务一样实现远程服务 -->
<bean id="xxxService" class="com.xxx.XxxServiceImpl" />
<!-- 增加暴露远程服务配置 -->
<dubbo:service interface="com.xxx.XxxService" ref="xxxService" />

<!--调用服务：-->
<!-- 增加引用远程服务配置 -->
<dubbo:reference id="xxxService" interface="com.xxx.XxxService" />
<!-- 和本地服务一样使用远程服务 -->
<bean id="xxxAction" class="com.xxx.XxxAction">
	<property name="xxxService" ref="xxxService" />
</bean>

```
1. webservice跨平台，dubbo不跨平台
2. 分布式开发，子系统之间用dubbo通信，是对内，我们甚至知道使用方是谁；与第三方之间的通信用webservice，对外使用，我们不关心外是谁

## Zookeeper
1. 什么是Zookeeper？有什么用？

## FastDFS
分布式文件服务器

分为Tracker和Storage。

线性扩容功能，我们需要往FastDFS上面挂载Storage，实时报告storage的状态，然后tracker可以计算，并且根据情况进行自动扩容。

## KindEditor
使用步骤：
1. 在jsp中引入KindEditor的css和js代码。
```
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
```
2. 在表单中添加一个textarea控件。是一个富文本编辑器的载体。类似数据源
```
<td>商品描述:</td>
<td>
    <textarea style="width:800px;height:300px;visibility:hidden;" name="desc"></textarea>
</td>
```
3. js中初始化富文本编辑器。使用官方提供的方法初始化
```
//创建富文本编辑器
itemAddEditor = TAOTAO.createEditor("#itemAddForm [name=desc]");

createEditor : function(select){
	return KindEditor.create(select, TT.kingEditorParams);
},
```
4. 取富文本编辑器的内容。表单提交之前，把富文本编辑器的内容同步到textarea控件中
```
itemAddEditor.sync();
```