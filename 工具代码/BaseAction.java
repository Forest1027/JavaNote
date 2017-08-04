package com.forest.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/*
此类是同文件夹中的“获取父类参数化类型.txt”文件的应用
主要作用是抽取action类中的重复代码
*/

//将类定义为抽象类，则不能实例化此对象
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	// 模型驱动。此处使用protected的原因---让子类可见此属性
	// 此处不能直接new T()。因为泛型在编译的过程中会被擦除。
	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	// model实例化---定义构造方法，则子类在实例化的过程中必然调用此构造方法。即可实例化model
	public BaseAction() {
		// 获取BaseAction<Area>
		Type genericSuperclass = this.getClass().getGenericSuperclass();

		// 获取第一个泛型参数。此处为Area类型
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		try {
			// 实例化获取的类型
			model = modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("模型构造失败");
		}
	}
}