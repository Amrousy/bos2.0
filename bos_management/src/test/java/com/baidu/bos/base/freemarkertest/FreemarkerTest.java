package com.baidu.bos.base.freemarkertest;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class FreemarkerTest {

	@Test
	public void testOutput() throws Exception {
		// 配置对象，模板对象
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/templates"));

		// 获取模板对象
		Template template = configuration.getTemplate("hello.ftl");

		// 动态数据对象
		Map<String, Object> paramterMap = new HashMap<>();
		paramterMap.put("title", "你好世界");
		paramterMap.put("msg", "你好啊、世界");

		// 合并输出
		template.process(paramterMap, new PrintWriter(System.out));
	}
}
