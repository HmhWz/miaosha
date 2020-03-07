package com.ljs.miaosha.rabbitmq;

import com.ljs.miaosha.domain.MiaoshaOrder;
import com.ljs.miaosha.domain.MiaoshaUser;
import com.ljs.miaosha.redis.RedisService;
import com.ljs.miaosha.service.GoodsService;
import com.ljs.miaosha.service.MiaoshaService;
import com.ljs.miaosha.service.MiaoshaUserService;
import com.ljs.miaosha.service.OrderService;
import com.ljs.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
	@Autowired
	GoodsService goodsService;
	@Autowired
	RedisService redisService;
	@Autowired
	MiaoshaUserService miaoshaUserService;
	@Autowired
	MiaoshaService miaoshaService;
	@Autowired
	OrderService orderService;

	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

	@RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
	public void receiveMiaosha(String message) {
		MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
		MiaoshaUser user = mm.getUser();
		long goodsId = mm.getGoodsId();
		GoodsVo goodsvo = goodsService.getGoodsVoByGoodsId(goodsId);
		int stockcount = goodsvo.getStockCount();
		if (stockcount <= 0) {
			return;
		}
		// 判断是否重复秒杀
		MiaoshaOrder order = orderService.getMiaoshaOrderByUidAndGid(user.getId(), goodsId);
		if (order != null) {
			return;
		}
		miaoshaService.miaosha1(user, goodsvo);
	}

}
