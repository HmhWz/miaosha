package com.ljs.miaosha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ljs.miaosha.domain.MiaoshaGoods;
import com.ljs.miaosha.dao.GoodsDao;
import com.ljs.miaosha.vo.GoodsVo;

@Service
public class GoodsService {
	public static final String COOKIE1_NAME_TOKEN = "token";

	@Autowired
	GoodsDao goodsDao;

	public List<GoodsVo> getGoodsVoList() {
		return goodsDao.getGoodsVoList();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	/**
	 * 考虑有可能下单失败的情况,下单失败那么就不去
	 */
	public boolean reduceStock1(GoodsVo goodsvo) {
		MiaoshaGoods goods = new MiaoshaGoods();
		goods.setGoodsId(goodsvo.getId());
		int ret = goodsDao.reduceStock1(goods);
		return ret > 0;
	}
}
