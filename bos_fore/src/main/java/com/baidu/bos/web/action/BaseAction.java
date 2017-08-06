package com.baidu.bos.web.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	protected T model;

	@Override
	public T getModel() {
		return model;
	}

	public BaseAction() {
		// 构造子类Action对象 ，获取继承父类型的泛型
		// AreaAction extends BaseAction<Area>
		// BaseAction<Area>
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		// 获取类型第一个泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		@SuppressWarnings("unchecked")
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("模型构造失败");
		}
	}

	// 属性注册驱动
	protected int page;

	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	protected void pushPageDataToValueStack(Page<T> pageData) {
		// 将数据分装为map集合返回到浏览器
		Map<String, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		// json 数据格式返回,压入栈顶
		ActionContext.getContext().getValueStack().push(map);
	}
}
