package com.baidu.bos.web.action.transit;

import com.baidu.bos.dao.transit.TransitInfoRepository;
import com.baidu.bos.domain.transit.TransitInfo;
import com.baidu.bos.service.transit.TransitInfoService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amrous on 2017/08/07.
 */
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {

    @Autowired
    private TransitInfoService transitInfoService;

    private String wayBillIds;

    public void setWayBillIds(String wayBillIds) {
        this.wayBillIds = wayBillIds;
    }

    @Action(value = "transit_create", results = {@Result(name = "success", type = "json")})
    public String creat() {
        Map<String, Object> result = new HashMap<>();
        try {
            transitInfoService.creatTransits(wayBillIds);
            // 成功
            result.put("success", true);
            result.put("msg", "开始中转配送成功");
        } catch (Exception e) {
            e.printStackTrace();
            // 失败
            result.put("success", true);
            result.put("msg", "开启中转配送失败");
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    @Action(value = "transit_pageQuery",results = {@Result(name="success",type = "json")})
    public String pageQuery() {
        // 分页查询
        Pageable pageable = new PageRequest(page-1,rows);
        // 调用业务层查询
        Page<TransitInfo> pageData = transitInfoService.findPageQuery(pageable);
        // 压入值栈
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }
}
