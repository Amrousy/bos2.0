package com.baidu.bos.web.action.base;

import com.baidu.bos.base.utils.PinYin4jUtils;
import com.baidu.bos.domain.base.Area;
import com.baidu.bos.service.base.AreaService;
import com.baidu.bos.web.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {

    // 注入属性
    @Autowired
    private AreaService areaService;

    @Action(value = "area_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        // 创建分页查询对象
        PageRequest pageable = new PageRequest(page - 1, rows);
        // 创建条件查询对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 创建保存条件集合对象
                List<Predicate> list = new ArrayList<>();
                // 添加条件
                if (StringUtils.isNotBlank(model.getProvince())) {
                    // 根据省份  模糊查询
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCity())) {
                    // 根据城市  模糊查询
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(model.getDistrict())) {
                    // 根据区域  模糊查询
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层查询
        Page<Area> pageData = areaService.findPagePageData(specification, pageable);
        // 压入值栈顶部
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    // 接收上传文件
    private File file;

    private String fileFileName;

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Action(value = "area_batchImport")
    public String batchImport() throws IOException {
        List<Area> areas = new ArrayList<>();
        InputStream is = new FileInputStream(file);
        Workbook workbook = null;
        // 判断文件结尾
        // 1.加载Excel对象
        if (fileFileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(is);
        } else if (fileFileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(is);
        }
        Sheet sheet = workbook.getSheetAt(0);
        // 3.读取sheet中的每一行
        for (Row row : sheet) {
            // 第一行跳过
            if (row.getRowNum() == 0) {
                continue;
            }
            // 跳过空行
            if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            Area area = new Area();
            area.setId(row.getCell(0).getStringCellValue());
            area.setProvince(row.getCell(1).getStringCellValue());
            area.setCity(row.getCell(2).getStringCellValue());
            area.setDistrict(row.getCell(3).getStringCellValue());
            area.setPostcode(row.getCell(4).getStringCellValue());
            String province = area.getProvince();
            String city = area.getCity();
            String district = area.getDistrict();
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            district = district.substring(0, district.length() - 1);
            String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
            StringBuffer buffer = new StringBuffer();
            for (String headStr : headArray) {
                buffer.append(headStr);
            }
            String shortcode = buffer.toString();
            area.setShortcode(shortcode);
            // 城市编码
            area.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));
            areas.add(area);
        }
        // 调用业务层
        areaService.saveBatch(areas);

        return NONE;
    }

}
