package com.baidu.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.xmlbeans.impl.common.SystemCache;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.constant.Constants;
import com.baidu.bos.domain.take_delivery.PageBean;
import com.baidu.bos.domain.take_delivery.Promotion;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {

	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 基于bos_management获取活动的数据
		PageBean<Promotion> pageBean = WebClient
				.create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/pageQuery?page=" + page
						+ "&rows=" + rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}

	@Action(value = "promotion_showDetail")
	public String showDetail() throws Exception {
		// 先判断id对应的html是否存在
		String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freemarker");
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		if (!htmlFile.exists()) {
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(
					new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/freemarker_templates")));
			// 获取模板对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			// 动态数据
			Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL
					+ "/services/promotionService/promotion/" + model.getId())
					.accept(MediaType.APPLICATION_JSON).get(Promotion.class);

			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("promotion", promotion);

			// 合并输出
			template.process(parameterMap, new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));
		}
		// 存在 直接返回
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}
}
