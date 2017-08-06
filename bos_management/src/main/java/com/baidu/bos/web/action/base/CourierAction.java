package com.baidu.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.base.Courier;
import com.baidu.bos.domain.base.Standard;
import com.baidu.bos.service.base.CourierService;
import com.baidu.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends BaseAction<Courier> {

    @Autowired
    private CourierService courierService;

    // 属性驱动
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    // 取派员作废
    @Action(value = "courier_delBatch", results = {
            @Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String delBatch() {
        String[] idArray = ids.split(",");
        courierService.delBatch(idArray);
        return SUCCESS;
    }

    // 取派员还原
    @Action(value = "courier_recBatch", results = {
            @Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String recBatch() {
        String[] idArray = ids.split(",");
        courierService.recBatch(idArray);
        return SUCCESS;
    }

    // 添加取派员
    @Action(value = "courier_save", results = {
            @Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String save() {
        courierService.save(model);
        return SUCCESS;
    }

    // 分页查询方法
    @Action(value = "courier_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        // 封装pageabe对象
        Pageable pageable = new PageRequest(page - 1, rows);

        // 根据分页查询条件 构造Specification 条件查询对象(类似Hibernate的QBC查询)
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            /**
             * 构造条件查询方法,如果方法返回Null,代表无条件查询 Root 参数获取条件表达式 name=?、age=?
             * CriteriaQuery 参数,构造简单查询条件返回 提供where方法 CriteriaBuilder
             * 参数,构造Predicate对象,条件对象, 构造复杂查询效果
             */
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 当前查询Root根对象Courier
                List<Predicate> list = new ArrayList<Predicate>();

                // 单表查询(查询当前对象 对应 数据表)
                if (StringUtils.isNotBlank(model.getCourierNum())) {
                    // 进行快递员 工号查询
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCompany())) {
                    // 进行公司查询 模糊查询
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(model.getType())) {
                    // 进行快递员类型查询，等值查询 type=?
                    Predicate p3 = cb.equal(root.get("type").as(String.class), model.getType());
                    list.add(p3);
                }

                // 多表查询 (查询当前对象 关联 对象 对应数据表)
                // 使用Courier(Root),关联Standard
                Join<Courier, Standard> standardRoot = root.join("standard", JoinType.INNER);
                if (model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())) {
                    // 进行收派标准 模糊查询
                    Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
                            "%" + model.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        Page<Courier> pageData = courierService.findPageData(specification, pageable);
        // 压入值栈
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    // 查询所有未关联定区的快递员
    @Action(value = "courier_findnoassociation", results = {@Result(name = "success", type = "json")})
    public String findnoassociation() {
        List<Courier> couriers = courierService.findNoAssociation();
        // 查询到结果压入值栈
        ActionContext.getContext().getValueStack().push(couriers);
        return SUCCESS;
    }
}
