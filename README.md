# AI数据分析平台 后端

## 一、项目概述

在传统的数据分析平台中，如果我们想要分析近一年网站的用户增长趋势，通常需要手动导入数据、选择要分析的字段和图表，并由专业的数据分析师完成分析，最后得出结论。

然而，本次设计的项目与传统平台有所不同。在这个项目中，用户只需输入想要分析的目标，并上传原始数据，系统将利用 AI 自动生成可视化图表和学习的分析结论。这样，即使是对数据分析一窍不通的人也能轻松使用该系统。

## 二、技术选型

后端

- Spring Boot
- MySQL 数据库
- MyBatis-Plus 及 MyBatis X 自动生成
- Redis + Redisson 限流
- RabbitMQ 消息队列
- [鱼聪明 AI SDK（AI 能力）](https://www.yucongming.com/)
- JDK 线程池及异步化
- Easy Excel 表格数据处理
- Swagger + Knife4j 接口文档生成
- Hutool、Apache Common Utils 等工具库

## 三、项目架构图

![img](https://cdn.jsdelivr.net/gh/vincent-nicky/image_store/blog/1687756290740-41ba43c4-24b3-400f-a77a-db9321e0a200.png)

## 四、运行展示

上传数据并分析：

![image-20230920152122148](https://cdn.jsdelivr.net/gh/vincent-nicky/image_store/blog/image-20230920152122148.png)

查看当前用户已分析过的图表：

![image-20230920152207398](https://cdn.jsdelivr.net/gh/vincent-nicky/image_store/blog/image-20230920152207398.png)
