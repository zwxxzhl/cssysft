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
 * 业务表单与用户关系
 * </p>
 *
 * @author zwx
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_form_ref_user")
@ApiModel(value="BusFormRefUser实体", description="业务表单与用户关系")
public class BusFormRefUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 关联表单id
     */
    private String formId;

    /**
     * 用户
     */
    private String userId;

    /**
     * 状态：1未汇报 2阶段汇报 3汇报完成
     */
    private String userFormStatus;

    /**
     * 状态：1正常 2超时
     */
    private String userTimeStatus;

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
