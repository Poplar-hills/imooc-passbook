package com.imooc.passbook.customerplatform.vo;

import com.imooc.passbook.customerplatform.constants.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户评论对象定义
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {

    @NotNull(message = "用户 id 为空")
    private Long userId;

    @NotNull(message = "评论类型为空")
    private FeedbackType type;

    private String templateId;  // PassTemplate RowKey, 如果是 app 类型的评论, 则没有

    @NotBlank(message = "评论内容为空")
    private String content;
}
