package com.mall.seckill.dao;
import com.mall.seckill.pojo.SeckillOrder;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("seckillOrder")
public interface SeckillOrderMapper extends Mapper<SeckillOrder> {
}
