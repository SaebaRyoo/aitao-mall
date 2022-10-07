package com.mall.goods.dao;

import com.mall.goods.pojo.Album;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("albumMapper")
public interface AlbumMapper extends Mapper<Album> {
}
