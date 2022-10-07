package com.mall.goods.feign;

import com.mall.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 找到goods服务下的对应的接口，
 * 如findAll()对应的就是 goods服务下的SkuController中的findAll接口
 */
@FeignClient(value = "goods", path="/sku", decode404 = true)
public interface SkuFeign {

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    Result<List<Sku>> findAll();

    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable String status);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable(name="id") Long id);

    /***
     * 库存递减
     * @param username
     * @return
     */
    @PostMapping(value = "/decr/count")
    Result decrCount(@RequestParam(value = "username") String username);


    /**
     * 商品信息更新
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value = "/{id}")
    Result update(@RequestBody Sku sku, @PathVariable String id);
}
