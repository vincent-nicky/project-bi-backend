package com.project.model.mongo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserChart implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    private ObjectId id;
    /**
     * 图表名称
     */
    private String chartName;
    /**
     * 分析目标
     */
    private String goal;
    /**
     * 图表数据
     */
    private String chartData;
    /**
     * 存储图表数据的表id
     */
    private String ChartDataId;
    /**
     * 图表类型
     */
    private String chartType;
    /**
     * 图表数据
     */
    private String csvData;
    /**
     * 生成的图表数据
     */
    private String genChart;
    /**
     * 生成的分析结论
     */
    private String genResult;
    /**
     * 执行信息
     */
    private String execMessage;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}
