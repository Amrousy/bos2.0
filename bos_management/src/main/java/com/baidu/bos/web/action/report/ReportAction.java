package com.baidu.bos.web.action.report;

import com.baidu.bos.base.utils.FileUtils;
import com.baidu.bos.domain.take_delivery.WayBill;
import com.baidu.bos.service.take_delivery.WayBillService;
import com.baidu.bos.web.action.common.BaseAction;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.List;

/**
 * Created by Amrous on 2017/08/08.
 */
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class ReportAction extends BaseAction<WayBill> {

    @Autowired
    private WayBillService wayBillService;

    @Action("report_exportXls")
    public String exportXls() throws IOException {
        // 查询出满足当前条件 结果数据
        List<WayBill> wayBills = wayBillService.findWayBills(model);

        // 生成Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet("运单数据");
        // 表头
        HSSFRow headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("运单号");
        headRow.createCell(1).setCellValue("寄件人");
        headRow.createCell(2).setCellValue("寄件人电话");
        headRow.createCell(3).setCellValue("寄件人地址");
        headRow.createCell(4).setCellValue("收件人");
        headRow.createCell(5).setCellValue("收件人电话");
        headRow.createCell(6).setCellValue("收件人地址");
        // 表格数据
        for (WayBill wayBill : wayBills) {
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            dataRow.createCell(0).setCellValue(wayBill.getWayBillNum());
            dataRow.createCell(1).setCellValue(wayBill.getSendName());
            dataRow.createCell(2).setCellValue(wayBill.getSendMobile());
            dataRow.createCell(3).setCellValue(wayBill.getSendAddress());
            dataRow.createCell(4).setCellValue(wayBill.getRecName());
            dataRow.createCell(5).setCellValue(wayBill.getRecMobile());
            dataRow.createCell(6).setCellValue(wayBill.getRecAddress());
        }
        // 下载导出
        // 设置头信息
        ServletActionContext.getResponse().setContentType("application/vnd.ms-excel");
        String filename = "运单数据.xls";
        String agent = ServletActionContext.getRequest()
                .getHeader("user-agent");
        filename = FileUtils.encodeDownloadFilename(filename, agent);
        ServletActionContext.getResponse().setHeader("Content-Disposition",
                "attachment;filename=" + filename);

        ServletOutputStream outputStream = ServletActionContext.getResponse()
                .getOutputStream();
        hssfWorkbook.write(outputStream);

        // 关闭
        hssfWorkbook.close();

        return NONE;
    }

    @Action("report_exportPdf")
    public String exportPdf() throws IOException, DocumentException {
        // 查询出满足当前条件的结果数据
        List<WayBill> wayBills = wayBillService.findWayBills(model);

        // 下载导出
        // 设置头信息
        ServletActionContext.getResponse().setContentType("application/pdf");
        String filename = "运单数据.pdf";
        String agent = ServletActionContext.getRequest()
                .getHeader("user-agent");
        filename = FileUtils.encodeDownloadFilename(filename, agent);
        ServletActionContext.getResponse().setHeader("Content-Disposition",
                "attachment;filename=" + filename);
        // 生成pdf文件
        Document document = new Document();
        PdfWriter.getInstance(document, ServletActionContext.getResponse().getOutputStream());
        document.open();
        // 写PDF数据
        // 向document 生成PDF表格
        Table table = new Table(7);
        table.setWidth(80);// 宽度
        table.setBorder(1);// 边框
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);// 水平对齐方式
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);// 垂直对齐方式

        // 设置表格字体
        BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
        // 写表头
        table.addCell(buildCell("运单号", font));
        table.addCell(buildCell("寄件人", font));
        table.addCell(buildCell("寄件人电话", font));
        table.addCell(buildCell("寄件人地址", font));
        table.addCell(buildCell("收件人", font));
        table.addCell(buildCell("收件人电话", font));
        table.addCell(buildCell("收件人地址", font));
        // 写数据
        for (WayBill wayBill : wayBills) {
            table.addCell(buildCell(wayBill.getWayBillNum(), font));
            table.addCell(buildCell(wayBill.getSendName(), font));
            table.addCell(buildCell(wayBill.getSendMobile(), font));
            table.addCell(buildCell(wayBill.getSendAddress(), font));
            table.addCell(buildCell(wayBill.getRecName(), font));
            table.addCell(buildCell(wayBill.getRecMobile(), font));
            table.addCell(buildCell(wayBill.getRecAddress(), font));
        }
        // 将表格加入文档
        document.add(table);

        document.close();
        return NONE;
    }

    private Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }

}
