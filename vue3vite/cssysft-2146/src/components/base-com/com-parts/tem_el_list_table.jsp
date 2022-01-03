<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>

<style>
    #tem-el-list-table-div input::-webkit-outer-spin-button,
    #tem-el-list-table-div input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    #tem-el-list-table-div input[type="number"] {
        -moz-appearance: textfield;
    }

    #tem-el-list-table-div .el-form-item--mini.el-form-item {
        margin-bottom: 0;
    }

    #tem-el-list-table-div .el-form-item {
        margin-bottom: 0;
    }

    #tem-el-list-table-div .span-xin {
        color: #F56C6C;
        font-size: 12px;
        margin-right: 4px;
    }
    
</style>

<template id="tem-el-list-table" >
    <div id="tem-el-list-table-div">

        <el-form class="form-css" :model="form" :rules="rules" ref="ruleForm" size="small">
            <div>
                <el-table 
                    ref="refTable"
                    :key="tableKey"
                    :data="form.formList" 
                    style="width: 100%"
                    :height="tableHeight"
                    :max-height="tableMaxHeight"
                    stripe 
                    :size="tableSize"
                    :border="tableBorder"
                    :header-row-class-name="headerRowClassName"
                    :header-cell-class-name="headerCellClassName"
                    :row-class-name="rowClassName"
                    :cell-class-name="cellClassName"
                    :highlight-current-row="highlightCurrentRow"
                    :row-key="rowKey"
                    @select="handleSelect"
                    @select-all="handleSelectAll"
                    @selection-change="handleSelectionChange"
                    @row-click="rowClick"
                    @row-dblclick="rowDblclick"
                >
                    <slot name="selectionslot" v-bind="this"></slot>

                    <el-table-column type="index" label="序号" width="55"></el-table-column>
                    <el-table-column 
                        v-for="(col, index) in tablehead"
                        :key="index" 
                        header-align="center" 
                        show-overflow-tooltip
                        :align="col.align || 'center'"
                        :width="col.width || ''"
                        :prop="col.prop" 
                        :label="col.label"
                        :column-key="index.toString()"
                        v-if="col.formatter"
                        :formatter="formatterFun"
                    >
                        <template slot="header" slot-scope="scope">
                            <template v-if="!col.headerslot">
                                <span v-if="col.validate" class="span-xin" @click="headerClick($event,'spanXin')">*</span>
                                <span v-if="col.iconLeft" :class="col.iconLeftClass" style="padding-top: 5px;" @click="headerClick($event,'iconLeft')"></span>
                                <span @click="headerClick($event, scope.column.label)">{{ scope.column.label }}</span>
                                <span v-if="col.iconRight" :class="col.iconRightClass" style="padding-top: 5px;" @click="headerClick($event,'iconRight')"></span>
                            </template>
                            <template v-else>
                                <slot :name="col.prop.toLowerCase()+'header'" v-bind="this"></slot>
                            </template>
                        </template>
                    </el-table-column>
                    <el-table-column 
                        header-align="center" 
                        show-overflow-tooltip
                        :align="col.align || 'center'"
                        :width="col.width || ''"
                        :prop="col.prop" 
                        :label="col.label"
                        :column-key="index.toString()"
                        v-else
                    >
                        <template slot="header" slot-scope="scope">
                            <template v-if="!col.headerslot">
                                <span v-if="col.validate" class="span-xin" @click="headerClick($event,'spanXin')">*</span>
                                <span v-if="col.iconLeft" :class="col.iconLeftClass" style="padding-top: 5px;" @click="headerClick($event,'iconLeft')"></span>
                                <span @click="headerClick($event, scope.column.label)">{{ scope.column.label }}</span>
                                <span v-if="col.iconRight" :class="col.iconRightClass" style="padding-top: 5px;" @click="headerClick($event,'iconRight')"></span>
                            </template>
                            <template v-else>
                                <slot :name="col.prop.toLowerCase()+'header'" v-bind="this"></slot>
                            </template>
                        </template>

                        <template slot-scope="scope">
                            <template v-if="!col.rowslot">
                                <div v-if="col.domType === 'checkbox'">
                                    <el-checkbox v-if="col.domKey === 'isDel'" v-model="scope.row[col.prop]" :true-label="0" :false-label="1" disabled></el-checkbox>
                                    <el-checkbox v-else v-model="scope.row[col.prop]" :true-label="1" :false-label="0" disabled></el-checkbox>
                                </div>
                                <span v-else>{{ scope.row[col.prop] }}</span>
                            </template>
                            <template v-else>
                                <slot :name="col.prop.toLowerCase()+'row'" v-bind="this"></slot>
                            </template>
                        </template>
                    </el-table-column>

                    <slot name="opertip" v-bind="this">
                        
                    </slot>

                    <slot name="opertip1" v-bind="this">

                    </slot>

                </el-table>
            </div>
        </el-form>

    </div>
</template>
    
<script type="text/javascript">

    Vue.component('tem-el-list-table', {
        template: '#tem-el-list-table',
        props:{
            tableList:{
                type: Array,
                required: false,
                default: ()=>([])
            },
            tablehead:{
                type: Array,
                required: false,
                default: ()=>([])
            },
            tableSize: {
                type: String,
                required: false,
                default: ''
            },
            rowKey:{
                type: String,
                required: true
            },
            handleSelect:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            handleSelectAll:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            handleSelectionChange:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            rowClick:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            rowDblclick:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            propFormatter:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            tableBorder:{
                type: Boolean,
                required: false,
                default: false
            },
            headerRowClassName:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            headerCellClassName:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            rowClassName:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            cellClassName:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            highlightCurrentRow:{
                type: Boolean,
                required: false,
                default: false
            },
            headerClick:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            tableHeight:{
                type: Number,
                required: false,
                default: null
            },
            tableMaxHeight:{
                type: Number,
                required: false,
                default: null
            }
        },
        computed:{
            formatterFun: function () {
                if (this.propFormatter && !ComUtil.isEmptyFunction(this.propFormatter)) {
                    return this.propFormatter;
                } else {
                    return this.formatter;
                }
            }
        },
        data() {
            return {
                form: {
                    formList: []
                },
                tableKey: 0,
                rules: null,

                mixProp: {},
                dictObj: {}
            }
        },
        created() {
            this.rules = this.getRules();
            this.$emit('created',this);
        },
        mounted() {
            this.$emit('mounted');
        },
        watch: {
            tableList: {
                handler: function(newVal, oldVal) {
                    this.form.formList = newVal;
                },
                deep: true
            }
        },
        methods: {
            getRules(){
                let rules = {};
                for (const it of this.tablehead) {
                    if (it.validate) {
                        let obj = {};
                        this.$set(obj, 'required', true);
                        this.$set(obj, 'message', it.validMsg);
                        this.$set(obj, 'trigger', it.validTrigger);
                        rules[it.prop] = [obj];
                    }
                }
                return rules;
            },
            formatter(row, column, cellValue, index) {
                let obj = this.tablehead.find(f => f.formatter && f.prop === column.property);
                if (obj) {
                    if (this[obj.formatter]) {
                        return this[obj.formatter](row, column, cellValue, index);
                    } else {
                        console.error("没有格式化函数: " + obj.formatter);
                        return row[column.property];
                    }
                }
            }
        }
    });

</script>