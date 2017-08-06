package com.baidu.bos.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.baidu.bos.domain.constant.Constants;
import com.baidu.bos.utils.MailUtils;
import com.baidu.crm.domain.Customer;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	// 客户注册短信
	@Action(value = "customer_sendSms")
	public String sendSms() throws Exception {
		// 手机号保存在Customer对象
		// 生成短信验证码
		String randomCode = RandomStringUtils.randomNumeric(4);
		// 将短信验证码保存到session
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
		System.out.println("生成的手机验证码为：" + randomCode);

		// 编辑短信内容
		// String msg = "尊敬的用户您好，本次获取的验证码为：" + randomCode + "，服务电话：40061840000";

		// 调用Mq发送一条消息
		jmsTemplate.send("bos_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("randomCode", randomCode);
				return mapMessage;
			}
		});
		return NONE;
		// 使用SMS发送短信
		/*
		 * SendSmsResponse response = SmsUtils.sendSms(randomCode,
		 * model.getTelephone()); System.out.println(response.getCode()); if
		 * (response.getCode() != null && response.getCode().equals("OK")) { //
		 * 发送成功 return NONE; } else { throw new RuntimeException("短信发送失败,错误信息："
		 * + response.getMessage()); }
		 */
	}

	// 属性驱动
	private String checkCode;

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	// 客户注册
	@Action(value = "customer_regist", results = {
			@Result(name = "success", type = "redirect", location = "signup-success.html"),
			@Result(name = "input", type = "redirect", location = "signup.html") })
	public String regist() {
		// 校验短信验证码，不通过返回注册页面
		// session获取验证码
		String checkCodeSession = (String) ServletActionContext.getRequest().getSession()
				.getAttribute(model.getTelephone());
		if (checkCodeSession == null || !checkCodeSession.equals(checkCode)) {
			System.out.println("短信验证码错误");
			return INPUT;
		}
		WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer")
				.type(MediaType.APPLICATION_JSON).post(model);
		System.out.println("客户注册成功...");

		// 发送激活邮件
		// 生成激活码
		String activeCode = RandomStringUtils.randomNumeric(32);

		// 将激活吗保存到redis 设置24小时失效
		redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24, TimeUnit.HOURS);

		// 调用MailUtils发送激活邮件
		String content = "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址进行绑定：<br/><a href='" + MailUtils.activeUrl + "?telephone="
				+ model.getTelephone() + "&activeCode=" + activeCode + "'>速运快递邮箱绑定地址</a>";
		MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());
		return SUCCESS;
	}

	// 属性驱动
	private String activeCode;

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	// 客户邮箱激活
	@Action(value = "customer_activeMail")
	public String setActive() throws IOException {
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		// 判断激活码是否有效
		String activeCodeRedis = redisTemplate.opsForValue().get(model.getTelephone());
		if (activeCodeRedis == null || !activeCodeRedis.equals(activeCode)) {
			// 激活码无效
			ServletActionContext.getResponse().getWriter().println("激活码无效，请登录系统重新绑定！");
		} else {
			// 激活码有效
			// 防止重复绑定
			Customer customer = WebClient
					.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/telephone/"
							+ model.getTelephone())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if (customer.getType() == null || customer.getType() != 1) {
				// 未绑定
				WebClient.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/updatetype/"
						+ model.getTelephone()).get();
				ServletActionContext.getResponse().getWriter().println("邮箱绑定成功!");
			} else {
				// 已经绑定
				ServletActionContext.getResponse().getWriter().println("邮箱已绑定，请勿重复绑定!");
			}
			// 删除redis激活码
			redisTemplate.delete(model.getTelephone());
		}
		return NONE;
	}

	// 客户登录操作
	@Action(value = "customer_login", results = { @Result(name = "login", location = "login.html", type = "redirect"),
			@Result(name = "success", location = "index.html#/myhome", type = "redirect") })
	public String login() {
		Customer customer = WebClient
				.create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/customer/login?telephone="
						+ model.getTelephone() + "&password=" + model.getPassword())
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (customer == null) {
			// 登录失败
			return LOGIN;
		} else {
			// 登录成功
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}
	}
}
