# QC for taotao
## Dubbo
1. 什么是Dubbo？有什么用？
	1. alibaba旗下一个开源的项目
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

## Zookeeper
1. 什么是Zookeeper？有什么用？