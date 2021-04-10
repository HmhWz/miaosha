package com.hmh.secondskill.common;

import com.hmh.secondskill.common.StockWithRedis.RedisKeysConstant;
import com.hmh.secondskill.common.utils.RedisPoolUtil;
import com.hmh.secondskill.po.Stock;
import com.hmh.secondskill.service.api.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @auther hmh
 * @date 3/7 13:41
 * 缓存预热，在 Spring Boot 启动后立即执行此方法
 */
@Component
public class RedisPreheatRunner implements ApplicationRunner {

    @Autowired
    private StockService stockService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 从数据库中查询热卖商品
        Stock stock = stockService.getStockById(2);

        // 删除旧缓存
        RedisPoolUtil.del(RedisKeysConstant.STOCK_COUNT + stock.getCount());
        RedisPoolUtil.del(RedisKeysConstant.STOCK_SALE + stock.getSale());
        RedisPoolUtil.del(RedisKeysConstant.STOCK_VERSION + stock.getVersion());
        //缓存预热
        int sid = stock.getId();
        RedisPoolUtil.set(RedisKeysConstant.STOCK_COUNT + sid, String.valueOf(stock.getCount()));
        RedisPoolUtil.set(RedisKeysConstant.STOCK_SALE + sid, String.valueOf(stock.getSale()));
        RedisPoolUtil.set(RedisKeysConstant.STOCK_VERSION + sid, String.valueOf(stock.getVersion()));
    }
}
