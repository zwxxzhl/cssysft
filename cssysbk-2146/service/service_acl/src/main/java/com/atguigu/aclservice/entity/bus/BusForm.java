package com.atguigu.aclservice.entity.bus;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 业务表单
 * </p>
 *
 * @author zwx
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_form")
@ApiModel(value="BusForm实体", description="业务表单")
public class BusForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 父id
     */
    private String pid;

    /**
     * 派发人
     */
    private String userId;

    /**
     * 派发标题
     */
    private String title;

    /**
     * 派发内容
     */
    private String content;

    /**
     * 截止日期
     */
    private LocalDateTime deadline;

    /**
     * 状态：1未派发 2汇报中 3汇报完成 4已作废
     */
    private String formStatus;

    /**
     * 状态：1正常 2超时
     */
    private String timeStatus;

    /**
     * 逻辑删除 1（true）已删除， 0（false）未删除
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 创建人
     */
    private String gmtCreateUser;

    /**
     * 更新人
     */
    private String gmtUpdateUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
