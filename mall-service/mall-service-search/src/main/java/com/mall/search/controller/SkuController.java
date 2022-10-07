package com.mall.search.controller;


import com.mall.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {

    SkuService skuService;

    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map searchMap) throws IOException {
        return  skuService.search(searchMap);
    }
    /**
     * 导入数据
     *
     * @return
     */
    @GetMapping("/import")
    public Result importData() throws IOException {
        skuService.importSku();
        return new Result(true, StatusCode.OK, "导入数据到索引库中成功！");
    }
}
