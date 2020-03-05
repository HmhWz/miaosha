package com.ljs.miaosha.service;

import com.ljs.miaosha.dao.OrderDao;
import com.ljs.miaosha.domain.MiaoshaOrder;
import com.ljs.miaosha.domain.MiaoshaUser;
import com.ljs.miaosha.domain.OrderInfo;
import com.ljs.miaosha.redis.OrderKey;
import com.ljs.miaosha.redis.RedisService;
import com.ljs.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
	@Autowired
	OrderDao orderDao;
	@Autowired
	RedisService redisService;

	/**
	 * 代码2.0
	 * 生成订单的时候，将订单同时写入到缓存里面去。
	 * 判断是否秒杀到某商品，即去miaosha_order里面去查找是否有记录userId和goodsId的一条数据。
	 */
	public MiaoshaOrder getMiaoshaOrderByUidAndGid_Cache(Long userId, Long goodsId) {
		//1.先去缓存里面取得
		MiaoshaOrder morder = redisService.get(OrderKey.getMiaoshaOrderByUidAndGid, "" + userId + "_" + goodsId, MiaoshaOrder.class);
		if (morder != null) {
			return morder;
		}
		return orderDao.getMiaoshaOrderByUidAndGid(userId, goodsId);
	}

	/**
	 * 生成订单,事务,同时写入到缓存
	 */
	@Transactional
	public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsvo) {
		//1.生成order_info
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setDeliveryAddrId(0L);//long类型 private Long deliveryAddrId;   L
		orderInfo.setCreateDate(new Date());
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goodsvo.getId());
		orderInfo.setGoodsPrice(goodsvo.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		//订单状态  ---0-未支付  1-已支付  2-已发货  3-已收货
		orderInfo.setOrderStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insert(orderInfo);
		OrderInfo oi = orderDao.selectorderInfo(user.getId(), goodsvo.getId());

		//2.生成miaosha_order
		MiaoshaOrder miaoshaorder = new MiaoshaOrder();
		miaoshaorder.setGoodsId(goodsvo.getId());
		miaoshaorder.setOrderId(oi.getId());
		miaoshaorder.setUserId(user.getId());
		orderDao.insertMiaoshaOrder(miaoshaorder);

		redisService.set(OrderKey.getMiaoshaOrderByUidAndGid, "" + user.getId() + "_" + goodsvo.getId(), miaoshaorder);
		return orderInfo;
	}

	/**
	 * 代码  1.0
	 * 根据用户userId和goodsId判断是否有者条订单记录，有则返回此纪录
	 */
	public MiaoshaOrder getMiaoshaOrderByUidAndGid(Long userId, Long goodsId) {
		return orderDao.getMiaoshaOrderByUidAndGid(userId, goodsId);
	}

	public OrderInfo getOrderByOrderId(long orderId) {
		return orderDao.getOrderByOrderId(orderId);
	}


}
