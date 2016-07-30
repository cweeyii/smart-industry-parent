package com.cweeyii.quartz.job.elasticsearch;

import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import com.cweeyii.elasticsearch.framework.index.EnterpriseESIndex;
import com.cweeyii.elasticsearch.framework.vo.ESAggrCondition;
import com.cweeyii.elasticsearch.framework.vo.ESCondition;
import com.cweeyii.elasticsearch.framework.vo.EnterpriseIndexView;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.util.EnterpriseUtil;
import com.google.common.collect.Lists;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wenyi on 16/6/13.
 * Email:caowenyi@meituan.com
 */
@Service
public class FirstElasticSearchJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstElasticSearchJob.class);
    @Resource
    private EnterpriseBasicInfoService enterpriseBasicInfoService;

    private EnterpriseESIndex enterpriseESIndex = new EnterpriseESIndex();

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        /*List<Long> ids = Lists.newArrayList(9028L, 9093L);
        List<EnterpriseBasicInfo> basicInfos = enterpriseBasicInfoService.findByIds(ids);
        for (EnterpriseBasicInfo basicInfo : basicInfos) {
            EnterpriseIndexView enterpriseIndexView = new EnterpriseIndexView();
            EnterpriseUtil.copyProperties(basicInfo, enterpriseIndexView);
            enterpriseESIndex.buildIndex(enterpriseIndexView);
        }

        ids = Lists.newArrayList(9223L, 9435L, 9507L, 9508L, 9541L, 9627L, 9758L, 9817L, 9877L, 9903L, 10094L, 10151L);
        basicInfos = enterpriseBasicInfoService.findByIds(ids);
        List<EnterpriseIndexView> indexViews = new ArrayList<>();
        for (EnterpriseBasicInfo basicInfo : basicInfos) {
            EnterpriseIndexView enterpriseIndexView = new EnterpriseIndexView();
            EnterpriseUtil.copyProperties(basicInfo, enterpriseIndexView);
            indexViews.add(enterpriseIndexView);
        }*/
        /*List<EnterpriseBasicInfo> basicInfos = enterpriseBasicInfoService.findByPage(0L,5000);
        List<EnterpriseIndexView> indexViews = new ArrayList<>();
        for (EnterpriseBasicInfo basicInfo : basicInfos) {
            EnterpriseIndexView enterpriseIndexView = new EnterpriseIndexView();
            EnterpriseUtil.copyProperties(basicInfo, enterpriseIndexView);
            indexViews.add(enterpriseIndexView);
        }
        enterpriseESIndex.batchBuildIndex(indexViews);*/

        /*ESCondition condition=new ESCondition();
        List<EnterpriseIndexView> indexViews=enterpriseESIndex.getListByCondition(condition);*/

        ESAggrCondition condition = new ESAggrCondition();
        /*
         * 1.只有地址会被分词,因此Match方法只能适用于地址字段
         * condition.setMatchPair("enterpriseName", "自助"); 不会有输出结果,因为企业名称不会进行分词
         */
        //condition.setMatchPair("enterpriseName", "自助", ESCondition.BooleanOperate.MUST);
        /*
         * 2. 输出结果包含:1)红星东路南皇营路西森泰御城（与森泰壹世界酒店公寓同楼）2)红星路与城山路交叉口向西50米路南
         */

        //condition.setMatchPair("address", "红星", ESCondition.BooleanOperate.MUST);
        /*
         * 3. queryString会分词并且匹配所有字段
         */
        //condition.setQueryString("因为爱回旋自助");
        /*
         * 4. 过滤器模式,会从结果集中过滤出想要的结果,可以实现mysql查询的功能
         * select * from enterprise_basic_info where enterprise_name='因为爱回旋自助'
         */
        //condition.setTermPair("enterpriseName","因为爱回旋自助");

        /*condition.setRangeFilter("latitude",32.951067,39.951067);
        condition.setRangeFilter("longitude",114.282008,121.477061);
        condition.setSortField("id");
        condition.setAggregation("cityName");
        condition.setSize(10000);
        List<EnterpriseIndexView> indexViews = enterpriseESIndex.getListByCondition(condition);
        LOGGER.info(indexViews.toString());*/

        condition.setRangeQuery("latitude", 32.951067, 39.951067);
        condition.setRangeQuery("longitude",114.282008,121.477061);
        condition.setAggregation("cityName");
        condition.setSize(10000);
        Map<Object,String> map=enterpriseESIndex.getMapByAggrCondition(condition);
        int total=0;
        for(Map.Entry<Object,String> entry:map.entrySet()){
            total+=Integer.parseInt(entry.getValue());
        }
        LOGGER.info(""+total);
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "elasticsearch索引重建";
    }
}
