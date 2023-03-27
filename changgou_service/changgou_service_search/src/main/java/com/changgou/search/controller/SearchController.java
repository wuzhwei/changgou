package com.changgou.search.controller;

import cn.hutool.core.util.ObjectUtil;
import com.changgou.common.pojo.Page;

import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/search")
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
     * 搜索接口
     * @param paramMap   搜索条件
     * @return   数据集合
     * @throws Exception
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
    @GetMapping("/list")
    public String search(@RequestParam Map<String,String> paramMap, Model model) throws  Exception{
        //特殊符号处理
        this.handlerSearchMap(paramMap);
        //执行查询返回值
        Map<String,Object> resultMap = searchService.search(paramMap);
        System.out.println(resultMap);
        model.addAttribute("paramMap",paramMap);
        model.addAttribute("result",resultMap);
        //封装分页数据并返回
        //1.总记录数
        //2.当前页数
        //3.每页显示条数
        Page<SkuInfo> page = new Page<>(Long.parseLong(String.valueOf(resultMap.get("total"))),
                Integer.parseInt(String.valueOf(resultMap.get("pageNum"))),
                Page.pageSize);
        model.addAttribute("page",page);


        //拼装url
        StringBuilder url = new StringBuilder("/search/list");
        if (ObjectUtil.isNotEmpty(paramMap)){
            //有查询条件,开始拼接
            url.append("?");
            for (String paramKey:
                 paramMap.keySet()) {
                if (!"sortRule".equals(paramKey) && !"sortField".equals(paramKey) && !"pageNum".equals(paramKey)){
                    url.append(paramKey).append("=").append(paramMap.get(paramKey)).append("&");
                }
            }
            //得到http://localhost:9006/search/list?keywords=手机&spec_颜色=黑色&
            String urlString = url.toString();
            //去除路径最后一个&
            urlString = urlString.substring(0, urlString.length() - 1);
            model.addAttribute("url",urlString);
        }else{
            model.addAttribute("url",url);
        }
        return "search";
    }

}
