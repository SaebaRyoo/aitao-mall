package com.mall.seckill.dao;
import com.mall.seckill.pojo.SeckillGoods;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("seckillGoods")
public interface SeckillGoodsMapper extends Mapper<SeckillGoods> {
}
