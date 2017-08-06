package com.baidu.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.take_delivery.Promotion;
import com.baidu.bos.service.take_delivery.PromotionService;
import com.baidu.bos.web.action.common.BaseAction;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html") })
	public String save() throws IOException {
		// 宣传表上传、在数据表保存宣传图路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String urlPath = ServletActionContext.getRequest().getContextPath() + "/upload/";

		// 生成随机图片名
		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;

		// 保存图片（绝对路径）
		FileUtils.copyFile(titleImgFile, new File(savePath + "/" + randomFileName));

		// 将保存路径，相对工程web访问路径，保存Model中
		model.setTitleImg(ServletActionContext.getRequest().getContextPath() + "/upload/" + randomFileName);

		// 调用业务层完成数据保存
		promotionService.save(model);
		return SUCCESS;
	}

	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 构造分页查询参数
		PageRequest pageable = new PageRequest(page - 1, rows);
		// 调用业务层完成查询
		Page<Promotion> pageData = promotionService.findPageData(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

}
