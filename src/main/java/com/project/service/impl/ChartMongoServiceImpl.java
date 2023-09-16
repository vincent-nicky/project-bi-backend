package com.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.common.ErrorCode;
import com.project.exception.ThrowUtils;
import com.project.model.entity.Chart;
import com.project.model.mongo.UserChart;
import com.project.service.ChartMongoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ChartMongoServiceImpl implements ChartMongoService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void save(long userId, UserChart userChart) {
        // 如果集合存在，则新增一个数据，不存在就创建集合
        try {
            userChart.setCreateTime(new Date());
            mongoTemplate.save(userChart,getCollectionName(userId));
        } catch (Exception e) {
            ThrowUtils.throwIf(false, ErrorCode.SYSTEM_ERROR, "用户上传的图表保存失败");
        }
    }

    @Override
    public boolean update(Long userId, UserChart updateChart) {
        // 构建查询条件，这里假设你要根据文档的某个字段进行更新
        Query query = new Query(Criteria.where("_id").is(updateChart.getId()));
        // 构建更新操作
        Update update = new Update();
        update.set("status", updateChart.getStatus());
        update.set("genChart", updateChart.getGenChart());
        update.set("genResult", updateChart.getGenResult());
        update.set("updateTime",new Date());
        try {
            mongoTemplate.updateFirst(query, update, getCollectionName(userId));
            return true;
        } catch (Exception e) {
            ThrowUtils.throwIf(false, ErrorCode.SYSTEM_ERROR, "图表更新失败");
            return false;
        }
    }

    @Override
    public UserChart getById(String userId, String chartId) {
        // 构建查询条件，这里假设你要根据文档的某个字段进行更新
        Query query = new Query(Criteria.where("_id").is(chartId));
        try {
            return mongoTemplate.findOne(query, UserChart.class, getCollectionName(Long.parseLong(userId)));
        } catch (NumberFormatException e) {
            ThrowUtils.throwIf(false, ErrorCode.SYSTEM_ERROR, "查询失败");
            return null;
        }
    }

    @Override
    public Page<UserChart> page(long current, long size, long userId) {
        // 创建排序条件（按创建时间降序排序）
        Sort sort = Sort.by(Sort.Order.desc("createTime"));
        // 创建查询对象并设置查询条件和排序条件
        Query query = new Query().with(sort);
        query.skip((current - 1) * size); // 计算要跳过的文档数量
        query.limit( (int) size); // 设置每页返回的文档数量

        List<UserChart> userChartList = mongoTemplate.find(query, UserChart.class, getCollectionName(userId));

        /*
          'java.util.List<UserChart>' 转换为 'com.baomidou.mybatisplus.extension.plugins.pagination.Page<UserChart>'
          也就是 List<UserChart> 转 Page<UserChart>
          注意：  page.setTotal不是List<UserChart>的size，而应该是这个集合的文档数
         */
        Page<UserChart> page = new Page<>(current, size);
        page.setRecords(userChartList);
        long total = mongoTemplate.count(new Query(), UserChart.class, getCollectionName(userId));
        page.setTotal(total);

        return page;
    }

    @NotNull
    private String getCollectionName(long userId) {
        String collectionName = "chart_" + userId;
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
        return collectionName;
    }
}
