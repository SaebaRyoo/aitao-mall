package com.mall.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.termvectors.Filter;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.mall.search.pojo.SkuInfo;
import com.mall.search.utils.EsClient;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticTest {

    //@Test
    public void insertData() throws IOException {
        //// And create the API client
        ElasticsearchClient client = EsClient.elasticClient();

        ArrayList<BulkOperation> bulkOperations = new ArrayList<>();
        BulkOperation.Builder builder = new BulkOperation.Builder();
        User user = new User("张12", 88, "安徽", "张12住在安徽");
        bulkOperations.add(builder.create(d -> d.index("user").id("10").document(user)).build());

        BulkResponse response = client.bulk(e -> e.index("user").operations(bulkOperations));
        System.out.println(response);

        EsClient.close();
    }

    /**
     * Indexing single documents
     */
    //@Test
    public void singleDocument() throws IOException {
        // 创建单条文档数据
        User user = new User("张13", 8, "常州", "张13住在常州");

        // create the API client
        ElasticsearchClient client = EsClient.elasticClient();

        IndexResponse response = client.index(i -> i.index("user").id("11").document(user));

        System.out.println(response);

        EsClient.close();
    }

    /**
     * 多条记录操作
     */
    //@Test
    public void multipleDocument() throws IOException {
        // create the API client
        ElasticsearchClient client = EsClient.elasticClient();
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("张14", 10, "常州", "张14住在常州"));
        users.add(new User("张15", 11, "常州", "张15住在常州"));
        users.add(new User("张16", 11, "常州", "张16住在常州"));
        users.add(new User("张17", 23, "常州", "张17住在常州"));

        // create bulkRequest
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (User user: users) {
            br.operations(op -> op.create(idx -> idx.index("user").id(user.getName()).document(user)));
        }

        //执行操作
        BulkResponse result = client.bulk(br.build());

        EsClient.close();
    }


    /**
     * 柱状图聚合查询
     * @throws IOException
     */
    //@Test
    public void aggregationTest() throws IOException {
        ElasticsearchClient esClient = EsClient.elasticClient();
        Query query = MatchQuery.of(m -> m
                .field("name")
                .query("华为")
        )._toQuery();


        SearchResponse<SkuInfo> response = esClient.search(b -> b
                        .index("skuinfo")
                        .query(query)
                        .aggregations("price-histogram", a -> a
                                .histogram(h -> h
                                        .field("price")
                                        .interval(50.0)
                                )
                        ),
                SkuInfo.class
        );
        List<HistogramBucket> buckets = response.aggregations()
                .get("price-histogram")
                .histogram()
                .buckets().array();

        for (HistogramBucket bucket: buckets) {
            System.out.println("There are " + bucket.docCount() +
                    " bikes under " + bucket.key());
        }
        EsClient.close();
    }

    /**
     * 排序
     */
    //@Test
    public void sort() throws IOException {
        Map<String, String> searchMap = new HashMap<>();
        //searchMap.put("sortField", "price");
        searchMap.put("sortRule", "ASC");
        ElasticsearchClient esClient = EsClient.elasticClient();

        SearchRequest.Builder builder = new SearchRequest.Builder();
        String sortField = searchMap.get("sortField");
        String sortRule = searchMap.get("sortRule");
        if (StringUtils.hasLength(sortField) && StringUtils.hasLength(sortRule)) {
            builder.sort(s -> s.field(f -> f
                    .field(sortField)
                    .order(sortRule.equals("DESC") ? SortOrder.Desc : SortOrder.Asc)
            ));
        }

        builder.from(0).size(100);

        SearchResponse<SkuInfo> response = esClient.search(builder.index("skuinfo").build(), SkuInfo.class);


        List<Hit<SkuInfo>> hits = response.hits().hits();
        ArrayList<SkuInfo> skuInfos = new ArrayList<>();
        for (Hit<SkuInfo> hit : hits) {
            SkuInfo source = hit.source();
            System.out.println(source.getPrice());
            //skuInfos.add(source);
        }

        //System.out.println(skuInfos);
    }

    /**
     * 分组聚合查询
     */
    //@Test
    public void aggregationTerms() throws IOException {
        Map<String, String> searchMap = new HashMap<>();
        searchMap.put("spec_存储", "16G");
        //searchMap.put("spec_机身内存", "16G");
        //searchMap.put("spec_网络", "联通4G");
        ElasticsearchClient esClient = EsClient.elasticClient();
        Query queryByName = MatchQuery.of(m -> m
                .field("name")
                .query("华为")
        )._toQuery();
        List<Query> filterList = new ArrayList<>();

        //规格过滤查询 - 如果前端传了，就不要再查了
        if (searchMap != null) {
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")) {
                    Query query = TermsQuery.of( t -> t
                            .field("specMap." + key.substring(5) + ".keyword")
                            .queryName(searchMap.get(key))
                    )._toQuery();
                    filterList.add(
                        query
                    );
                }
            }
        }

        SearchResponse<SkuInfo> response = esClient.search(s -> s
                .index("skuinfo")
                .size(100)
                .query(q -> q
                        .bool( b-> b
                                .must(queryByName)
                                .filter(filterList)
                        )
                )
                // 分组聚合
                .aggregations("categoryAggr", a -> a
                        .terms( t-> t
                                .field("categoryName.keyword")
                        )
                )
                .aggregations("brandAggr", a -> a
                        .terms( t-> t
                                .field("brandName.keyword")
                        )
                )
                .aggregations("specMapAgr", a -> a
                        .terms( t-> t
                                .field("spec.keyword")
                                .size(999)
                        )
                )
                //.from(0)
                //.size(10)
                , SkuInfo.class);

        System.out.println("总数:" + response.hits().total().value());


        Aggregate aggregate = response.aggregations().get("categoryAggr");
        StringTermsAggregate sterms = aggregate.sterms();
        Buckets<StringTermsBucket> buckets = sterms.buckets();

        for (StringTermsBucket b : buckets.array()) {
            System.out.println(b.key() + " : " + b.docCount());
        }

        System.out.println("-------------------------");

        Aggregate brandAggr = response.aggregations().get("brandAggr");
        StringTermsAggregate sterms1 = brandAggr.sterms();
        Buckets<StringTermsBucket> buckets1 = sterms1.buckets();

        for (StringTermsBucket b : buckets1.array()) {
            System.out.println(b.key() + " : " + b.docCount());
        }
        System.out.println("-------------------------");


        Aggregate specMap = response.aggregations().get("specMapAgr");
        StringTermsAggregate sterms2 = specMap.sterms();
        Buckets<StringTermsBucket> buckets2 = sterms2.buckets();

        for (StringTermsBucket b : buckets2.array()) {
            System.out.println(b.key() + " : " + b.docCount());
        }
        EsClient.close();
    }

}
