package com.project.bizmq;

import com.project.common.ErrorCode;
import com.project.constant.CommonConstant;
import com.project.exception.BusinessException;
import com.project.manager.AiManager;
import com.project.model.entity.Chart;
import com.project.model.mongo.UserChart;
import com.project.service.ChartMongoService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

@Component
@Slf4j
public class BiMessageConsumer {
    @Resource
    private AiManager aiManager;

    @Resource
    private ChartMongoService chartMongoService;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(HashMap<String, String> messageMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        log.info("receiveMessage message = {}", messageMap.get("chartId"));
        if (StringUtils.isBlank(messageMap.get("chartId"))) {
            // 如果失败，消息拒绝
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        UserChart userChart = chartMongoService.getById(messageMap.get("userId"), messageMap.get("chartId"));
        if (userChart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        // 将字符串的userId改为long
        long userId = Long.parseLong(messageMap.get("userId"));
        // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
        UserChart updateChart = new UserChart();
        updateChart.setId(userChart.getId());
        updateChart.setStatus("running");
        boolean b = chartMongoService.update(userId, updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(userId, userChart.getId(), "更新图表执行中状态失败");
            return;
        }

        // 调用 AI
        String result = aiManager.doChat(CommonConstant.BI_MODEL_ID, buildUserInput(userChart));
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(userId, userChart.getId(), "AI 生成错误");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        UserChart updateChartResult = new UserChart();
        updateChartResult.setId(userChart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        // todo 建议定义状态为枚举值
        updateChartResult.setStatus("succeed");
        chartMongoService.update(userId, updateChartResult);

        // 消息确认
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 构建用户输入
     *
     * @param chart
     * @return
     */
    private String buildUserInput(UserChart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(Long userId, ObjectId chartId, String execMessage) {
        UserChart updateChartResult = new UserChart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = chartMongoService.update(userId, updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

}
