package com.baidu.bos.web.action.transit;


import com.baidu.bos.domain.transit.InOutStorageInfo;
import com.baidu.bos.service.transit.InOutStorageInfoService;
import com.baidu.bos.web.action.common.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by Amrous on 2017/08/08.
 */
@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo> {

    @Autowired
    private InOutStorageInfoService inOutStorageInfoService;

    private String transitInfoId;

    public void setTransitInfoId(String transitInfoId) {
        this.transitInfoId = transitInfoId;
    }

    @Action(value = "inoutstore_save", results = {@Result(name = "success", type = "redirect", location = "pages/transit/transitinfo.html")})
    public String save() {
        inOutStorageInfoService.save(transitInfoId, model);
        return SUCCESS;
    }
}
