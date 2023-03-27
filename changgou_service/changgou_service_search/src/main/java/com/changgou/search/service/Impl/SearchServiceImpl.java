package com.changgou.search.service.Impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map<String,Object> search(Map<String, String> paramMap) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();
        //有条件才查询Es
        if (null != paramMap){
            //组合条件对象
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            //0:关键词
            if (!StringUtils.isEmpty(paramMap.get("keywords"))){
                boolQuery.must(QueryBuilders.matchQuery("name",
                        paramMap.get("keywords")).operator(Operator.AND));
            }
            //1.按照   品牌  进行过滤查询
            if (!StringUtils.isEmpty(paramMap.get("brand"))){
                boolQuery.filter(QueryBuilders.termQuery("brandName",
                        paramMap.get("brand")));
            }
            //2.按照   规格 进行过滤查询
            for (String key:
                 paramMap.keySet()) {
                if (key.startsWith("spec_")){
                    String value = paramMap.get(key).replace("%2B", "+");
                    //spec_黑色
                    boolQuery.filter(QueryBuilders.termQuery(("specMap."+key.substring(5)+".keyword"),value));
                }
            }
            //3.按照   价格    进行区间过滤查询
            if (!StringUtils.isEmpty(paramMap.get("price"))){
                String[] prices = paramMap.get("price").split("-");
                if (prices.length == 2){
                    boolQuery.filter(QueryBuilders.rangeQuery("price").lte(prices[1]));
                }
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(prices[0]));
            }

            //4.原生搜索实现类
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withQuery(boolQuery);
            //5.高亮
            HighlightBuilder.Field field = new HighlightBuilder
                    .Field("name")
                    .preTags("<span style='color:red'>")
                    .postTags("</span>");
            nativeSearchQueryBuilder.withHighlightFields(field);
            //6.品牌聚合(分组)查询
            String skuBrand = "skuBrand";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));
            //7.规格聚合(分组)查询
            String skuSpec = "skuSpec";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keyword"));
            //8.排序
            //设置排序
            //要排序的域
            String sortField = paramMap.get( "sortField" );
            //要排序的规则
            String sortRule = paramMap.get( "sortRule" );
            if (StrUtil.isNotEmpty( sortField ) && StrUtil.isNotEmpty( sortRule )) {
                if ("ASC".equals( sortRule )) {
                    //升序
                    nativeSearchQueryBuilder.withSort( SortBuilders.fieldSort( sortField ).order( SortOrder.ASC ) );
                } else {
                    //降序
                    nativeSearchQueryBuilder.withSort( SortBuilders.fieldSort( sortField ).order( SortOrder.DESC ) );
                }
            }
            //9.分页
            String pageNum = paramMap.get("pageNum");
            String pageSize = paramMap.get("pageSize");
            if (null == pageNum){
                pageNum = "1";
            }
            if (null == pageSize){
                pageSize = "30";
            }

            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum)-1,Integer.parseInt(pageSize)));
            //10.执行查询,返回结果对象
            AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    List<T> list = new ArrayList<>();
                    SearchHits hits = searchResponse.getHits();
                    if (null != hits){
                        for (SearchHit hit:
                             hits) {
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            if (null != highlightFields && highlightFields.size() > 0){
                                skuInfo.setName(highlightFields.get("name").getFragments()[0].toString());
                            }
                            list.add((T) skuInfo);
                        }
                    }
                    return new AggregatedPageImpl<T>(list,pageable,hits.getTotalHits(),searchResponse.getAggregations());
                }

                @Override
                public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                    return null;
                }
            });
            //11.总条数
            resultMap.put("total",aggregatedPage.getTotalElements());
            //12.总页数
            resultMap.put("totalPages",aggregatedPage.getTotalPages());
            //13.查询结果集合
            resultMap.put("rows",aggregatedPage.getContent());

            //14.获取品牌聚合结果
            StringTerms brandTerms = (StringTerms) aggregatedPage.getAggregation(skuBrand);
            List<String> brandList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("brandList",brandList);
            //15.获取规格聚合结果
            StringTerms specTerms = (StringTerms) aggregatedPage.getAggregation(skuSpec);
            List<String> specList = specTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("specList",specList(specList));
            //16.返回当前页
            resultMap.put("pageNum",pageNum);
            return resultMap;

        }
        return null;
    }

    /**
     * 原有数据
     * [
     * "{'颜色':'黑色','尺码':'平光防蓝光-无度数电脑手机护目镜'}",
     * "{'颜色':'红色','尺码':'150度'"}"
     * ]
     * 需要数据格式
     * {
     *     颜色:[黑色,红色],
     *     尺码:[100度,150度]
     * }
     *
     * @param specList  规格数据
     * @return          转换后的map
     */

    public Map<String,Set<String>> formatSpec(List<String> specList){
        HashMap<String, Set<String>> resultMap = new HashMap<>();
        if (specList  != null && specList.size() > 0){
            //specJsonString ={'颜色':'蓝色','版本':'6GB+128GB'}
            for (String specJsonString:
                 specList) {
                /**  将获取到的json转换为map
                 *  map={
                 *      key = 颜色 value=蓝色
                 *      key=  版本 value=6GB+128GB
                 *  }
                 */

                Map<String,String> specMap = JSON.parseObject(specJsonString, Map.class);
                for (String specKey:
                     specMap.keySet()) {
                    //拿到第一个key{颜色，版本}
                    Set<String> specSet = resultMap.get(specKey);
                    //判断是都在map中存在此key的set
                    if (specSet == null){
                        specSet = new HashSet<String>();
                    }
                    //将规格的值放入set中  {蓝色}{6GB+128GB}
                    specSet.add(specMap.get(specKey));
                    //将set放入map中{key=颜色，value=set{蓝色}}{key=版本,value=set{6GB+128GB}}
                    resultMap.put(specKey,specSet);
                }
            }

        }
        return resultMap;
    }

    //处理规格集合
    public Map<String, Set<String>> specList(List<String> specList){
        HashMap<String, Set<String>> specMap = new HashMap<>();
        if (null != specList && specList.size() > 0){
            for (String spec:
                 specList) {
                Map<String,String> map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String,String> entry:
                     entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Set<String> specValues = specMap.get(key);
                    if (null == specValues){
                         specValues = new HashSet<>();
                    }
                    specValues.add(value);
                    specMap.put(key,specValues);

                }

            }
        }
        return specMap;
    }
}
