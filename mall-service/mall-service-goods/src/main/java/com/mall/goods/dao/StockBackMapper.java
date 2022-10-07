package com.mall.goods.dao;
import com.mall.goods.pojo.StockBack;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("stockBackMapper")
public interface StockBackMapper extends Mapper<StockBack> {
}
