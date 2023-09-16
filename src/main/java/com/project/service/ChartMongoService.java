package com.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.model.entity.Chart;
import com.project.model.mongo.UserChart;

public interface ChartMongoService{
    void save(long userId, UserChart userChart);

    boolean update(Long id, UserChart updateChart);

    UserChart getById(String userId, String chartId);

    Page<UserChart> page(long current, long size, long userId);
}
