create table bus_task_form
(
    id              char(19)                      not null comment '主键id'
        primary key,
    pid             char(19)                      null comment '父id',
    procinst_id     varchar(64)                   null comment '流程实例id',
    procdef_id      varchar(64)                   null comment '流程定义id',
    user_id         char(19)                      not null comment '所属用户',
    title           varchar(255)                  null comment '派发任务标题',
    content         varchar(1000)                 null comment '派发任务内容',
    is_deleted      tinyint(1) unsigned default 0 not null comment '逻辑删除 1（true）已删除， 0（false）未删除',
    gmt_create      datetime                      not null comment '创建时间',
    gmt_create_user varchar(50)                   null comment '创建人',
    gmt_update_user varchar(50)                   null comment '更新人',
    gmt_modified    datetime                      not null comment '更新时间'
)
    comment '派发任务表单';