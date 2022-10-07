package com.mall.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSON;
import com.mall.goods.feign.SkuFeign;
import com.mall.goods.pojo.Sku;
import com.mall.search.pojo.SkuInfo;
import com.mall.search.service.SkuService;
import com.mall.search.utils.EsClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

@Service("skuService")
public class SkuServiceImpl implements SkuService {
    private final String ES_INDEX = "skuinfo";// 索引库
    private final String SEARCH_PRICE = "price";
    private final String SEARCH_CATEGORY = "categoryName"; // categoryName域字段名
    private final String ES_CATEGORY_AGR = "categoryNameAgr"; // categoryName聚合别名
    private final String SEARCH_BRAND = "brandName"; // brandName域数据字段名
    private final String ES_BRAND_AGR = "brandNameAgr"; // brandName聚合数据别名
    private final String ES_SPEC_MAP_AGR = "specMapAgr"; // specMap 聚合数据别名
    private final Integer PAGE_SIZE = 30;

    private SkuFeign skuFeign;

    @Autowired
    public void setSkuFeign(SkuFeign skuFeign) {
        this.skuFeign = skuFeign;
    }


    @Override
    public Map search(Map<String, String> searchMap) throws IOException {
        ElasticsearchClient esClient = EsClient.elasticClient();

        String keywords = searchMap.get("keywords");
        if (!StringUtils.hasLength(keywords)) {
            keywords = "华为"; // 目前默认给华为关键字搜索
        }
        final String finalKeywords = keywords;

        //构建查询语句
        SearchRequest.Builder builder = new SearchRequest.Builder();

        //指定查询索引库
        builder.index(ES_INDEX);

        //1.构建过滤语句
        BoolQuery boolQuery = BoolQuery.of(b -> {
            //过滤条件1.1.关键字查询
            if (StringUtils.hasLength(finalKeywords)) {
                b.must(must -> must
                        .match(m -> m
                                .field("name")
                                .query(finalKeywords)
                        )
                );
            }

            //过滤条件1.2.分类过滤
            if (StringUtils.hasLength(searchMap.get(SEARCH_CATEGORY))) {
                b.filter(f -> f
                        .term(t -> t
                                //keyword: 不分词
                                .field(SEARCH_CATEGORY + ".keyword")
                                .value(searchMap.get(SEARCH_CATEGORY))
                        )
                );
            }

            //过滤条件1.3.品牌过滤
            if (StringUtils.hasLength(searchMap.get(SEARCH_BRAND))) {
                b.filter(f -> f
                        .term(t -> t
                                .field(SEARCH_BRAND+".keyword")
                                .value(searchMap.get(SEARCH_BRAND))
                        )
                );
            }

            //过滤条件1.4.规格过滤
            if (searchMap != null) {
                for (String key : searchMap.keySet()) {
                    if (key.startsWith("spec_")) {
                        b.filter(f -> f
                                .term(t -> t
                                        // elasticsearch将导入的Map字符串生成对应的map属性
                                        .field("specMap." + key.substring(5) + ".keyword")
                                        .value(searchMap.get(key))
                                )
                        );
                    }
                }
            }

            //过滤条件1.5.价格过滤
            String price = searchMap.get(SEARCH_PRICE);
            if (StringUtils.hasLength(price)) {
                String[] split = price.split("-");
                if (!split[1].equalsIgnoreCase("*")) {
                    b.must(must -> must
                            .range(r -> r
                                    .field(SEARCH_PRICE)
                                    .gte(JsonData.of(split[0]))
                                    .lte(JsonData.of(split[1]))
                            )
                    );
                } else {
                    b.must(must -> must
                            .range(r -> r
                                    .field(SEARCH_PRICE)
                                    .gte(JsonData.of(split[0]))
                            )
                    );
                }
            }

            return b;
        });

        builder.query(boolQuery._toQuery());

        //2.排序条件构建
        String sortField = searchMap.get("sortField");
        String sortRule = searchMap.get("sortRule");
        if (StringUtils.hasLength(sortField) && StringUtils.hasLength(sortRule)) {
            builder.sort(s -> s.field(f -> f
                    .field(sortField)
                    .order(sortRule.equals("DESC") ? SortOrder.Desc : SortOrder.Asc)
            ));
        }

        //3.聚合数据
        // 分类聚合
        Aggregation categoryNameArg = Aggregation.of(a -> a.terms(t -> t.field(SEARCH_CATEGORY+".keyword").size(50)));
        // 品牌聚合
        Aggregation brandNameArg = Aggregation.of(a -> a.terms(t -> t.field(SEARCH_BRAND+".keyword").size(50)));
        // 商品规格聚合
        Aggregation specMapArg = Aggregation.of(a -> a.terms(t -> t.field("spec.keyword").size(10000)));

        builder.aggregations(ES_CATEGORY_AGR, categoryNameArg);
        builder.aggregations(ES_BRAND_AGR, brandNameArg);
        builder.aggregations(ES_SPEC_MAP_AGR, specMapArg);

        //4.分页， 用户如果不传分页参数，默认第一页
        Integer pageNum = pageConvert(searchMap);
        builder.from((pageNum - 1) * PAGE_SIZE);
        builder.size(PAGE_SIZE);

        SearchResponse<SkuInfo> response = esClient.search(builder.build(), SkuInfo.class);
        //返回Map结构数据
        Map resultMap = new HashMap<>();

        // 判断是否需要回显数据
        // 如果没有传分类名，就回显聚合的分类数据
        if (!StringUtils.hasLength(searchMap.get(SEARCH_CATEGORY))) {
            List<String> categoryList = getAggregationData(response, ES_CATEGORY_AGR);
            resultMap.put("categoryList", categoryList);
        }
        // 如果没有传品牌名，就回显聚合的品牌数据
        if (!StringUtils.hasLength(searchMap.get(SEARCH_BRAND))) {
            // 获取分组数据
            List<String> brandList = getAggregationData(response, ES_BRAND_AGR);
            resultMap.put("brandList", brandList);
        }

        Map<String, Set<String>> specMap = getStringSetMap(response, ES_SPEC_MAP_AGR, searchMap);
        resultMap.put("specMap", specMap);

        List<Hit<SkuInfo>> hits = response.hits().hits();
        ArrayList<SkuInfo> skuInfos = new ArrayList<>();
        for (Hit<SkuInfo> hit : hits) {
            SkuInfo source = hit.source();
            skuInfos.add(source);
        }

        long total = response.hits().total().value();
        resultMap.put("total", total);
        resultMap.put("rows", skuInfos);
        //分页数据
        //先转换为double类型，不然两个整数相除，余数会被截断
        double totalPages = (double) total / (double) PAGE_SIZE;
        resultMap.put("totalPages", (int) Math.ceil(totalPages));
        resultMap.put("pageSize", PAGE_SIZE);
        resultMap.put("pageNumber", pageNum); // 当前页

        EsClient.close();
        return resultMap;
    }

    /**
     * 获取当前页
     *
     * @param searchMap
     * @return
     */
    private Integer pageConvert(Map<String, String> searchMap) {
        Integer pageNum = 1;
        if (StringUtils.hasLength(searchMap.get("pageNum"))) {
            try {
                pageNum = Integer.valueOf(searchMap.get("pageNum"));
                //页数不能小于1
                if (pageNum < 1) {
                    pageNum = 1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                pageNum = 1;
            }
        }
        return pageNum;
    }

    /**
     * 获取分类、品牌数据
     *
     * @param response
     * @param agrName
     * @return
     */
    private List<String> getAggregationData(SearchResponse<SkuInfo> response, String agrName) {
        ArrayList<String> data = new ArrayList<>();
        Aggregate aggregate = response.aggregations().get(agrName);
        StringTermsAggregate sterms = aggregate.sterms();
        Buckets<StringTermsBucket> buckets = sterms.buckets();

        for (StringTermsBucket b : buckets.array()) {
            //System.out.println(b.key() + " : " + b.docCount());
            data.add(b.key());
        }

        return data;
    }

    /**
     * 获取规格列表数据
     *
     * @param response  elasticsearch返回的数据
     * @param agrName   聚合数据时起的别名
     * @param searchMap 客户端传入的数据
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(SearchResponse<SkuInfo> response, String agrName, Map<String, String> searchMap) {
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
        Set<String> specList = new HashSet<>();
        if (response != null) {
            Aggregate aggregate = response.aggregations().get(agrName);
            //sterms统计字符类型，其他还有lterms统计数字类型等
            StringTermsAggregate terms = aggregate.sterms();
            Buckets<StringTermsBucket> buckets = terms.buckets();
            for (StringTermsBucket b : buckets.array()) {
                specList.add(b.key());
            }
        }
        //将json 字符串转换，并组装成如下的数据结构
        /*
            specList = [
                "{"手机屏幕尺寸": "5.5寸", "网络": "移动4g", "颜色": "黑"}",
                "{"手机屏幕尺寸": "5.5寸", "网络": "移动4g", "颜色": "红"}",
                "{"手机屏幕尺寸": "5.5寸", "网络": "电信4g", "颜色": "黑"}",
                "{"手机屏幕尺寸": "5.5寸", "网络": "电信4g", "颜色": "红"}",
            ]
                ↓ ↓ ↓ ↓ ↓
            {
                "手机屏幕尺寸": ["5.5寸"],
                "网络": ["移动4G", "电信4G"],
                "颜色": ["黑", "红"]
            }
        */
        for (String specJson : specList) {
            Map<String, String> map = JSON.parseObject(specJson, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {//
                String key = entry.getKey();        //规格名字
                String value = entry.getValue();    //规格选项值

                // 只返回用户没有填写的商品属性,方便前端直接根据数据来显示需要选择的数据
                if (!StringUtils.hasLength(searchMap.get("spec_" + key))) {
                    //获取当前规格名字对应的规格数据
                    Set<String> specValues = specMap.get(key);
                    if (specValues == null) {
                        specValues = new HashSet<String>();
                    }
                    //将当前规格加入到集合中
                    specValues.add(value);
                    //将数据存入到specMap中
                    specMap.put(key, specValues);
                }
            }
        }
        return specMap;
    }


    /**
     * 导入sku数据到es
     */
    @Override
    public void importSku() throws IOException {
        ElasticsearchClient esClient = EsClient.elasticClient();
        BulkRequest.Builder br = new BulkRequest.Builder();
        //mall-service-goods微服务
        //Result<List<Sku>> skuListResult = skuFeign.findByStatus("1");
        Result<List<Sku>> skuListResult = skuFeign.findAll();
        //将数据转成search.Sku
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(skuListResult.getData()), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            // 获取spec -> Map(String) -> Map类型
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec());
            //如果需要生成动态的域，只需要将该域存入到一个Map<String, Object>对象中即可，
            //该Map<String, Object>的key会生成一个域，域的名字为该Map的key
            skuInfo.setSpecMap(specMap);

            br.operations(op ->
                    op.create(idx ->
                            idx
                                    .index("skuinfo")
                                    .id(skuInfo.getId().toString())
                                    .document(skuInfo)
                    )
            );
        }
        //导入到elasticsearch
        //skuEsMapper.saveAll(skuInfos);
        esClient.bulk(br.build());
        EsClient.close();
    }
}