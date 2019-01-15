package com.kindstar.hst.financial.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.kindstar.hst.cw.pojo.SaveCloudVo;
import com.kindstar.hst.financial.config.RedisCommon;
import com.kindstar.hst.lis.common.pojo.KindStartResult;
import com.kindstar.hst.lis.common.pojo.SubmitDataVo;
import com.kindstar.hst.lis.common.pojo.WebLayUiPageInfo;
import com.kindstar.hst.lis.pojo.PatItemResult_JZBBView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class LisController {

    /**
     * lis微服务
     */
    @Value("${global.HTTP_Lis_URL}")
    private String HTTP_Lis_URL;
    /**
     * 财务微服务
     */
    @Value("${global.HTTP_CW_URL}")
    private String HTTP_CW_URL;

    /**
     * 微服务调用
     */
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisCommon redisPageCommon;

    @Value("${global.pageSize}")
    private int pageSize;

    /**
     * 查询lis系统中的数据
     * @param pageNum
     * @return
     */
    private PageInfo<PatItemResult_JZBBView> getLisData(SubmitDataVo submitDataVo,int pageNum) {
        //包装传输数据
        submitDataVo.setPage(pageNum).setLimit(pageSize);
        PageInfo pageInfo = restTemplate.postForObject(HTTP_Lis_URL + "/lis/getPatResult", submitDataVo, PageInfo.class);
        return pageInfo;
    }

    /**
     * 同步数据到销售数据库添加
     *
     * @param resultList
     * @return
     */
    private KindStartResult insertTempLisInfo(List<PatItemResult_JZBBView> resultList, SubmitDataVo submitDataVo) {
        SaveCloudVo saveCloudVo = new SaveCloudVo();
        saveCloudVo.setIsSubmit(submitDataVo.getIsSubmit()).setList(resultList);
        KindStartResult kindStartResult = restTemplate.postForObject(HTTP_CW_URL + "/saleInfo/insertAll", saveCloudVo, KindStartResult.class);
        return kindStartResult;
    }
    /**
     * 同步远程lis库数据mysql数据
     * @param submitDataVo
     * @return
     */
    @GetMapping("/lis/synchronizationLisAll")
    public KindStartResult synchronizationWebLisAll(SubmitDataVo submitDataVo) {
        //默认第一页
        int pageNum = 1;
        try {
        PageInfo<PatItemResult_JZBBView> pageInfo = this.getLisData(submitDataVo,pageNum);
        int pages = pageInfo.getPages();
        //如果超过一页才会开始同步
        if (pages > 0) {
            //写入到mysql数据库中
            KindStartResult kindStartResult = this.insertTempLisInfo(pageInfo.getList(),submitDataVo);
            if (kindStartResult.getStatus() == 500) {
                return KindStartResult.build(500,"第1页同步失败!!!");
            }
            //循环读取
            for(pageNum = 2;pageNum <= pages;pageNum++) {
                pageInfo = this.getLisData(submitDataVo,pageNum);
                //写入到mysql数据库中
                kindStartResult = this.insertTempLisInfo(pageInfo.getList(),submitDataVo);
                if (kindStartResult.getStatus() == 500) {
                    return KindStartResult.build(500,"第"+pageNum+"页同步失败!!!");
                }
            }

        }
        //同步地区到Redis中
        KindStartResult arealistKindStartResult = restTemplate.getForObject(HTTP_Lis_URL + "/lis/arealist", KindStartResult.class);
        //同步医院到Redis中
        KindStartResult hospitalKindStartResult = restTemplate.getForObject(HTTP_Lis_URL + "/lis/hospital", KindStartResult.class);
        } catch (Exception e) {
            return KindStartResult.build(500,"拉取数据失败,错误异常为："+e.getMessage());
        }
        return KindStartResult.build(200,"拉取数据成功!!!");
    }











    /**
     * 以下所有方法都是测试方法
     * （测试）
     * 同步到redis数据(此方法不适合条件查询所以没有用)
     * @return
     */
    @GetMapping("/lis/redisSearch")
    public KindStartResult redisSearch(SubmitDataVo submitDataVo) {
        System.out.println(submitDataVo);
        //默认第一页
        int pageNum = 1;
        PageInfo<PatItemResult_JZBBView> pageInfo = this.getLisData(submitDataVo,pageNum);

        int pages = pageInfo.getPages();
        //如果超过一页才会开始同步
        if (pages > 0) {
            //写入到redis中
            try {
                //写入分页信息到redis
                this.writePageInfoToRedis(pageInfo);
                //写入数据到redis
                this.writeToRedis(pageInfo.getList());
                //循环读取
                for(pageNum = 2;pageNum <= pages;pageNum++) {
                    pageInfo = this.getLisData(submitDataVo,pageNum);
                    //写入到redis中
                    this.writeToRedis(pageInfo.getList());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        return KindStartResult.build(200,"同步成功!!!");
    }
    /**
     * 将当前分页的内容写入redis（测试）
     * @param pageInfo
     */
    private void writePageInfoToRedis(PageInfo<PatItemResult_JZBBView> pageInfo) throws JsonProcessingException {
        WebLayUiPageInfo webLayUiPageInfo = new WebLayUiPageInfo();
        webLayUiPageInfo.setCode(0).setCount(pageInfo.getTotal()).setMsg("保存分页信息");
        String layuiPageInfo = objectMapper.writeValueAsString(webLayUiPageInfo);
        redisPageCommon.getValueOperations().set("pat-pageinfo",layuiPageInfo);
    }
    /**
     * 将数据写入redis（测试）
     * @param patItemResults
     */
    private void writeToRedis(List<PatItemResult_JZBBView> patItemResults) throws JsonProcessingException, ParseException {
        Date noteTime = null;
        long score;

        for (int i = 0; i < patItemResults.size(); i++) {
            PatItemResult_JZBBView patBean = objectMapper.convertValue(patItemResults.get(i),
                    PatItemResult_JZBBView.class);
            String bean = objectMapper.writeValueAsString(patItemResults.get(i));
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            noteTime = sdf.parse(patBean.getNoteTime());
            score =  noteTime.getTime()/ 1000;
            redisPageCommon.add("patkey",patBean.getJzbbxh()+":" + patBean.getCombinationItemName(),
                    score,"patinfo",bean);
        }
    }
    /**
     * 获取redis数据（测试）
     * @param submitDataVo
     * @return
     * @throws IOException
     */
    @GetMapping("/lis/testSearch")
    public WebLayUiPageInfo search(SubmitDataVo submitDataVo) throws IOException {
        Integer pageNum = 1;
        Integer pageSize = 20;
        if (ObjectUtils.isEmpty(submitDataVo)) {
            pageNum = 1;
            pageSize = 20;
        }else {

            //设置分页的参数
            pageNum = submitDataVo.getPage();
            pageSize = submitDataVo.getLimit();
        }


        int start = (pageNum - 1) * pageSize;
        int end = start + pageSize;
        //读取分页第一页的key
        List<Object> listData = redisPageCommon.range("patkey", "patinfo", start, end, PatItemResult_JZBBView.class);
        //读取分页信息
        String setKeys = redisPageCommon.getValueOperations().get("pat-pageinfo");
        WebLayUiPageInfo webLayUiPageInfo = objectMapper.readValue(setKeys,WebLayUiPageInfo.class);

        return webLayUiPageInfo.setData(listData);
    }
}
