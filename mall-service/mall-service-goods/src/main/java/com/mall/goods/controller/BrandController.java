package com.mall.goods.controller;

import com.github.pagehelper.PageInfo;
import com.mall.goods.pojo.Brand;
import com.mall.goods.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/brand") // RequestMapping会捕获这个类中的异常
@CrossOrigin // 解决跨域
public class BrandController {

    private BrandService brandService;

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    /**
     * 查询对应分类下的品牌列表
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Brand>> findByCategoryId(@PathVariable(value="id") Integer categoryId) {
        List<Brand> list = brandService.findByCategoryId(categoryId);
        return new Result<List<Brand>>(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 查询所有品牌
     * @return Result<List<Brand>>
     */
    @GetMapping
    public Result<List<Brand>> findAll() {
        //int i = 1 / 0; // 手动制造异常
        List<Brand> brand = brandService.findAll();

        //响应结果封装
        return new Result<List<Brand>>(true, StatusCode.OK, "查询品牌信息成功", brand);
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable int id) {
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK, "查询品牌成功", brand);
    }

    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @PostMapping
    public Result addBrand(@RequestBody Brand brand) {
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "添加品牌成功");
    }

    /**
     * 根据id更新品牌信息
     * @param id
     * @param brand
     * @return
     */
    @PatchMapping("/{id}")
    public Result updateBrand(@PathVariable(value="id") int id, @RequestBody Brand brand) {
        // 添加id
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK, "更新品牌信息成功");
    }

    /**
     * 根据id删除品牌
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteBrand(@PathVariable(value="id") int id) {
        brandService.delete(id);
        return new Result(true, StatusCode.OK, "删除品牌成功");
    }

    /**
     * 多条件搜索
     * @param brand
     * @return
     */
    @PostMapping("/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result<List<Brand>>(true, StatusCode.OK, "条件查询成功", list);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(
            @PathVariable int page,
            @PathVariable int size) {
        PageInfo<Brand> curPage = brandService.findPage(page, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "分页查询成功", curPage);
    }

    /**
     * 分页条件查询
     * @param page
     * @param size
     * @param brand
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(
            @PathVariable int page,
            @PathVariable int size,
            @RequestBody Brand brand
    ) {
        PageInfo<Brand> curPage = brandService.findPage(brand, page, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "分页条件查询成功", curPage);
    }
}
