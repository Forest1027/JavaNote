package com.forest.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Service;

@Service("jobFactory")
public class JobFactory extends AdaptableJobFactory {
//quartz和spring整合Bean无法注入问题解决
//另外在applicationContext.xml中需配置
//<bean class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
//	<property name="jobFactory" ref="jobFactory"></property>
//	<property name="triggers">
//	<list>
//		<ref bean="simpleTrigger"></ref>
//	/list>
//	</property>
//</bean>

	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		// 调用父类的方法
		Object jobInstance = super.createJobInstance(bundle);
		// 进行注入
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}

}
