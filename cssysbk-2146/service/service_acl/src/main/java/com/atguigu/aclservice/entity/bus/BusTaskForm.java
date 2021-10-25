package com.atguigu.aclservice.entity.bus;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 派发任务表单
 * </p>
 *
 * @author jobob
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_task_form")
@ApiModel(value="BusTaskForm对象", description="派发任务表单表")
public class BusTaskForm implements Serializable {

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
     * 流程实例id
     */
    private String procinstId;

    /**
     * 流程定义id
     */
    private String procdefId;

    /**
     * 所属用户
     */
    private String userId;

    /**
     * 派发任务标题
     */
    private String title;

    /**
     * 派发任务内容
     */
    private String content;

    /**
     * 逻辑删除 1（true）已删除， 0（false）未删除
     */
    private Boolean isDeleted;

    /**
     * 创建人
     */
    private String gmtCreateUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 更新人
     */
    private String gmtUpdateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
