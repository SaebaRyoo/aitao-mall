package com.mall.order.controller;

import com.mall.order.pojo.OrderItem;
import com.mall.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    private CartService cartService;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    //@Autowired
    //private TokenDecode tokenDecode;

    /**
     * 添加购物车
     *
     * @param id  要购买的商品的SKU的ID
     * @param num 要购买的数量
     * @return
     */
    @RequestMapping("/add")
    public Result add(Long id, Integer num, String username) {

        System.out.println("哇塞::用户名:"+username);

        cartService.add(id, num, username);
        return new Result(true, StatusCode.OK, "添加成功");

    }

    @RequestMapping("/list")
    public Result<List<OrderItem>> list(String username) {

        System.out.println("哇塞::用户名:"+username);
        List<OrderItem> orderItemList = cartService.list(username);
        return new Result<List<OrderItem>>(true, StatusCode.OK, "列表查询成功", orderItemList);


    }


}
