package com.mall.search.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class EsClient {
    public static RestClient restClient;
    public static ElasticsearchTransport transport;

    //创建elastic客户端连接
    public static ElasticsearchClient elasticClient() {
        // Currently, the RestClient defaults to 30 connections. https://github.com/elastic/elasticsearch/issues/65578
        // Create the low-level client
        restClient = RestClient
                .builder(new HttpHost("120.26.69.48", 9200))
                .setRequestConfigCallback(
                        //new RestClientBuilder.RequestConfigCallback() {
                        //    @Override
                        //    public RequestConfig.Builder customizeRequestConfig(
                        //            RequestConfig.Builder requestConfigBuilder) {
                        //        return requestConfigBuilder
                        //                .setConnectTimeout(5000)
                        //                .setSocketTimeout(600000);
                        //    }
                        //})
                        requestConfigBuilder -> requestConfigBuilder
                                .setConnectTimeout(5000)
                                .setSocketTimeout(600000))
                .build();

        // Create the transport with a Jackson mapper
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        return client;
    }

    /**
     * 资源释放
     * @throws IOException
     */
    public static void close() throws IOException {
        EsClient.restClient.close();
        EsClient.transport.close();
    }
}
