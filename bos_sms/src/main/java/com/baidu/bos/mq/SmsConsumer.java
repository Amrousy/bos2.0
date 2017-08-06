package com.baidu.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baidu.bos.utils.SmsUtils;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage = (MapMessage) message;
		
		// 调用SMS服务发送短信
		try {
			SendSmsResponse response = SmsUtils.sendSms(mapMessage.getString("randomCode"),
					mapMessage.getString("telephone"));
			if (response.getCode() != null && response.getCode().equals("OK")) {
				// 发送成功
				System.out.println("发送成功，手机号：" + mapMessage.getString("telephone") + ",验证码： "
						+ mapMessage.getString("randomCode"));
			} else {
				// 发送失败
				throw new RuntimeException("短信发送失败,错误信息：" + response.getMessage());
			}
		} catch (JMSException e) {
			System.out.println("JMSException报错！！！！！！！！！！！！！！！！！！！");
			e.printStackTrace();
		} catch (ClientException e) {
			System.out.println("ClientException报错！！！！！！！！！！！！！！！！！");
			e.printStackTrace();
		}
	}

}
