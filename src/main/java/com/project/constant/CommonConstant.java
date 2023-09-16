package com.project.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * 通用常量
*/
public interface CommonConstant {

    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = "descend";

    /*
      TODO 怎样使它可以从yml中读取 鱼聪明模型id
      chatgpt：在 Java 接口中，字段默认是 public static final 的，这意味着它们是常量，不允许被赋值，
      因此你不能在接口中赋值 BI_MODEL_ID。如果你希望在运行时为 BI_MODEL_ID 赋值，
      你应该将它定义为一个普通的类（class）的静态字段或使用常规的 getter 方法。
     */
    long BI_MODEL_ID = 1698850741122301953L;

}
