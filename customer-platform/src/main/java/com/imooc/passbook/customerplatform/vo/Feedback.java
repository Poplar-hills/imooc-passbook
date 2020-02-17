package com.imooc.passbook.customerplatform.vo;

import com.imooc.passbook.customerplatform.constants.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 用户评论对象定义
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    private Long userId;  // 用户 id

    private FeedbackType type;  // 评论类型

    private String templateId;  // PassTemplate RowKey, 如果是 app 类型的评论, 则没有

    private String content;  // 评论内容
}
