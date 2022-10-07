package com.mall.search.service;

import java.io.IOException;
import java.util.Map;

public interface SkuService {

    /***
     * 搜索
     * @param searchMap
     * @return
     */
    Map search(Map<String, String> searchMap) throws IOException;

    /***
     * 导入SKU数据
     */
    void importSku() throws IOException;
}
