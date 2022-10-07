package com.mall.goods.dao;
import com.mall.goods.pojo.Sku;
import com.mall.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;


@Repository("skuMapper")
public interface SkuMapper extends Mapper<Sku> {

    /**
     * 递减库存
     * @param orderItem
     * @return
     */
    @Update("UPDATE tb_sku SET num=num-#{num},sale_num=sale_num+#{num} WHERE id=#{skuId} AND num>=#{num}")
    int decrCount(OrderItem orderItem);
}
