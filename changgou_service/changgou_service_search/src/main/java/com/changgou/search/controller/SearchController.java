package com.changgou.search.controller;

import com.changgou.search.service.ESManagerService;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/sku_search")
public class SearchController {
//    @Autowired
//    private ESManagerService esManagerService;
    @Autowired
    private SearchService searchService;
    //对搜索入参带有特殊符号进行处理
    public void handlerSearchMap(Map<String,String> paramMap){
        if (null != paramMap){
            Set<Map.Entry<String, String>> entries = paramMap.entrySet();
            for (Map.Entry<String,String> entry:
                 entries) {
                if (entry.getKey().startsWith("spec_")){
                    paramMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
                }
            }
        }
    }
    /**
     * 全文检索
     */
    @GetMapping
    @ResponseBody
    public Map search(@RequestParam Map<String,String> paramMap) throws Exception{
        //特殊符号处理
        System.out.println(paramMap);
        this.handlerSearchMap(paramMap);
        Map resultMap = searchService.search(paramMap);
        return resultMap;
    }
}
