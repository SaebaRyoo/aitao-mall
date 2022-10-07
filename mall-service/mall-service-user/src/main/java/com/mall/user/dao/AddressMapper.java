package com.mall.user.dao;
import com.mall.user.pojo.Address;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("addressMapper")
public interface AddressMapper extends Mapper<Address> {
}
