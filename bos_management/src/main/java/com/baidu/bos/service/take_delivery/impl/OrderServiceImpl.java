package com.baidu.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.bos.dao.base.AreaRepository;
import com.baidu.bos.dao.base.FixedAreaRepository;
import com.baidu.bos.dao.take_delivery.OrderRepository;
import com.baidu.bos.dao.take_delivery.WorkBillRepository;
import com.baidu.bos.domain.base.Area;
import com.baidu.bos.domain.base.Courier;
import com.baidu.bos.domain.base.FixedArea;
import com.baidu.bos.domain.base.SubArea;
import com.baidu.bos.domain.constant.Constants;
import com.baidu.bos.domain.take_delivery.Order;
import com.baidu.bos.domain.take_delivery.WorkBill;
import com.baidu.bos.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private WorkBillRepository workBillRepository;

	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	@Override
	public void saveOrder(Order order) {
		order.setOrderNum(UUID.randomUUID().toString()); // 设置订单号
		order.setOrderTime(new Date()); // 设置下单时间
		order.setStatus("1"); // 待取件

		// 寄件人省市区
		Area sendArea = order.getSendArea();
		Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(), sendArea.getCity(),
				sendArea.getDistrict());
		// 收件人省市区
		Area recArea = order.getRecArea();
		Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(), recArea.getCity(),
				recArea.getDistrict());
		order.setSendArea(persistArea);
		order.setRecArea(persistRecArea);

		// 自动分单 ，基于客户地址已匹配定区，获取定区，匹配快递员
		String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL
				+ "/services/customerService/customer/findFixedAreaIdByAddress?address=" + order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		if (fixedAreaId != null) {
			// 在bos_management中根据定区Id查找定区
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			// 得到快递员
			Courier courier = fixedArea.getCouriers().iterator().next();
			if (courier != null) {
				// 自动分单成功
				System.out.println("基于crm地址完全匹配自动分单成功...");
				saveOrder(order, courier);

				// 生成工单 发送短信
				generateWorkBill(order);
				return;
			}
		}

		// 自动分单，通过省市区，查询分区关键字，匹配地址，基于分区实现自动分单
		Area area = order.getSendArea();
		Area presistArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(),
				area.getDistrict());
		for (SubArea subArea : presistArea.getSubareas()) {
			// 当前客户下单地址是否包含分区 关键字
			if (order.getSendAddress().contains(subArea.getKeyWords())) {
				// 找到分区、找到定区、快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();
					if (courier != null) {
						// 自动分单成功
						System.out.println("基于分区关键字匹配自动分单成功...");
						saveOrder(order, courier);

						// 生成工单 发送短信
						generateWorkBill(order);
						return;
					}
				}
			}

		}
		for (SubArea subArea : presistArea.getSubareas()) {
			// 当前客户下单地址是否包含分区 辅助关键字
			if (order.getSendAddress().contains(subArea.getAssistKeyWords())) {
				// 找到分区、定区、快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				if (iterator.hasNext()) {
					Courier courier = iterator.next();
					if (courier != null) {
						// 自动分单成功
						System.out.println("基于分区辅助关键字匹配自动分单成功...");
						saveOrder(order, courier);

						// 生成工单 发送短信
						generateWorkBill(order);
						return;
					}
				}
			}
		}

		// 进入人工分单
		order.setStatus("2");
		orderRepository.save(order);
	}

	// 生成工单发送短信
	private void generateWorkBill(final Order order) {
		// 生成工单
		WorkBill workBill = new WorkBill();
		workBill.setType("新");
		workBill.setPickstate("新单");
		workBill.setBuildtime(new Date());
		workBill.setRemark(order.getRemark());
		final String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber);
		workBill.setOrder(order);
		workBill.setCourier(order.getCourier());
		workBillRepository.save(workBill);

		// 调用mq发送短信
		jmsTemplate.send("bos_sms", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				System.out.println("快递员电话：" + order.getCourier().getTelephone());
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				mapMessage.setString("randomCode", smsNumber);
				return mapMessage;
			}
		});

		// 修改工单状态
		workBill.setPickstate("已通知");
	}

	// 自动分单保存订单
	private void saveOrder(Order order, Courier courier) {
		// 将快递员关联订单
		order.setCourier(courier);
		// 设置自动分单
		order.setStatus("1");
		// 保存订单
		orderRepository.save(order);
	}
	
	@Override
	public Order findByOrderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}

}
