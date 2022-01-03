<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>

<style>
    #tem-el-list-table-expand-div input::-webkit-outer-spin-button,
    #tem-el-list-table-expand-div input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    #tem-el-list-table-expand-div input[type="number"] {
        -moz-appearance: textfield;
    }

    #tem-el-list-table-expand-div .el-form-item--mini.el-form-item {
        margin-bottom: 0;
    }

    #tem-el-list-table-expand-div .el-form-item {
        margin-bottom: 0;
    }

    #tem-el-list-table-expand-div .span-xin {
        color: #F56C6C;
        font-size: 12px;
        margin-right: 4px;
    }
    
</style>

<template id="tem-el-list-table-expand" >
    <div id="tem-el-list-table-expand-div">

        <el-form class="form-css" :model="form" :rules="rules" ref="ruleForm" size="small">
            <div>
                <el-table 
                    ref="refTable"
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
                    :expand-row-keys="expandRowKeys"
                    @expand-change="handleExpandChange">
                    <slot name="selectionslot" v-bind="this"></slot>
                    <el-table-column type="expand">
                        <template slot-scope="supscope">
                            <el-table
                                :data="supscope.row[expandRowProp]"
                                style="width: 100%"
                                size="small"
                                stripe
                                :header-row-class-name="headerRowClassNameSub"
                                :header-cell-class-name="headerCellClassNameSub"
                                :row-class-name="rowClassNameSub"
                                :cell-class-name="cellClassNameSub">
                                <el-table-column type="index" label="序号" width="55"></el-table-column>
                                <el-table-column 
                                    v-for="(col, index) in subTablehead"
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
                                            <slot :name="col.prop.toLowerCase()+'subheader'" v-bind="this"></slot>
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
                                            <slot :name="col.prop.toLowerCase()+'subheader'" v-bind="this"></slot>
                                        </template>
                                    </template>

                                    <template slot-scope="scope">
                                        <template v-if="!col.rowslot">
                                            <div v-if="col.dom === 'checkbox'">
                                                <el-checkbox v-if="col.domKey === 'isDel'" v-model="scope.row[col.prop]" :true-label="0" :false-label="1" disabled></el-checkbox>
                                                <el-checkbox v-else v-model="scope.row[col.prop]" :true-label="1" :false-label="0" disabled></el-checkbox>
                                            </div>
                                            <div v-else-if="'input' === col.domType">
                                                <el-form-item 
                                                    :prop="col.validate && ('formList.' + supscope.$index + '.' + expandRowProp + '.' + scope.$index + '.' + col.prop) || ''"
                                                    :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                                    :inline-message="true">
                                                    <el-input class="form-width-100" v-model="scope.row[col.prop]" 
                                                        @change="eventBusFun(col.eveName,col.eveParams,$event,scope.row)"
                                                        :placeholder="col.placeholder" :disabled="col.disabled" autocomplete="off">
                                                    </el-input>
                                                </el-form-item>
                                            </div>
                                            <div v-else-if="'input-num' === col.domType">
                                                <el-form-item 
                                                    :prop="col.validate && ('formList.' + supscope.$index + '.' + expandRowProp + '.' + scope.$index + '.' + col.prop) || ''"
                                                    :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                                    :inline-message="true">
                                                    <el-input class="form-width-100" v-model="scope.row[col.prop]" type="number"
                                                        @change="eventBusFun(col.eveName,col.eveParams,$event,scope.row)"
                                                        :placeholder="col.placeholder" :disabled="col.disabled" 
                                                        @mousewheel.native.prevent autocomplete="off">
                                                    </el-input>
                                                </el-form-item>
                                            </div>
                                            <div v-else-if="'input-positive-num' === col.domType">
                                                <el-form-item 
                                                    :prop="col.validate && ('formList.' + supscope.$index + '.' + expandRowProp + '.' + scope.$index + '.' + col.prop) || ''"
                                                    :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                                    :inline-message="true">
                                                    <el-input class="form-width-100" v-model="scope.row[col.prop]" type="number"
                                                        @input="e => (scope.row[col.prop] = positiveNumber(e))"
                                                        @change="eventBusFun(col.eveName,col.eveParams,$event,scope.row)"
                                                        :placeholder="col.placeholder" :disabled="col.disabled" 
                                                        @mousewheel.native.prevent autocomplete="off">
                                                    </el-input>
                                                </el-form-item>
                                            </div>
                                            <span v-else>{{ scope.row[col.prop] }}</span>
                                        </template>
                                        <template v-else>
                                            <slot :name="col.prop.toLowerCase()+'subrow'" v-bind="this"></slot>
                                        </template>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>

                    <el-table-column type="index" label="序号" width="55"></el-table-column>
                    <el-table-column 
                        v-for="(col, index) in supTablehead"
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
                                <div v-if="col.dom === 'checkbox'">
                                    <el-checkbox v-if="col.domKey === 'isDel'" v-model="scope.row[col.prop]" :true-label="0" :false-label="1" disabled></el-checkbox>
                                    <el-checkbox v-else v-model="scope.row[col.prop]" :true-label="1" :false-label="0" disabled></el-checkbox>
                                </div>
                                <div v-else-if="'input' === col.domType">
                                    <el-form-item 
                                        :prop="col.validate && ('formList.' + scope.$index + '.' + col.prop) || ''"
                                        :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                        :inline-message="true">
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" 
                                            @change="eventBusFun(col.eveName,col.eveParams,$event,scope.row)"
                                            :placeholder="col.placeholder" :disabled="col.disabled" autocomplete="off">
                                        </el-input>
                                    </el-form-item>
                                </div>
                                <div v-else-if="'input-num' === col.domType">
                                    <el-form-item 
                                        :prop="col.validate && ('formList.' + scope.$index + '.' + col.prop) || ''"
                                        :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                        :inline-message="true">
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" type="number" 
                                            @change="eventBusFun(col.eveName,col.eveParams,$event,scope.row)"
                                            :placeholder="col.placeholder" :disabled="col.disabled" 
                                            @mousewheel.native.prevent autocomplete="off">
                                        </el-input>
                                    </el-form-item>
                                </div>
                                <div v-else-if="'input-positive-num' === col.domType">
                                    <el-form-item 
                                        :prop="col.validate && ('formList.' + scope.$index + '.' + col.prop) || ''"
                                        :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                        :inline-message="true">
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" type="number"
                                            @input="e => (scope.row[col.prop] = positiveNumber(e))"
                                            @change="eventBusFun(col.eveName,col.eveParams,$event,scope.row)"
                                            :placeholder="col.placeholder" :disabled="col.disabled" 
                                            @mousewheel.native.prevent autocomplete="off">
                                        </el-input>
                                    </el-form-item>
                                </div>
                                <span v-else>{{ scope.row[col.prop] }}</span>
                            </template>
                            <template v-else>
                                <slot :name="col.prop.toLowerCase()+'row'" v-bind="this"></slot>
                            </template>
                        </template>
                    </el-table-column>

                    <slot name="supopertip" v-bind="this">
                        
                    </slot>

                </el-table>
            </div>
        </el-form>

    </div>
</template>
    
<script type="text/javascript">

    Vue.component('tem-el-list-table-expand', {
        template: '#tem-el-list-table-expand',
        props:{
            supTableList:{
                type: Array,
                required: false,
                default: ()=>([])
            },
            supTablehead:{
                type: Array,
                required: false,
                default: ()=>([])
            },
            subTablehead:{
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
            expandRowProp:{
                type: String,
                required: false,
                default: 'subTableList'
            },
            expandIndex:{
                type: Number,
                required: false
            },
            expandUrl:{
                type: String,
                required: false,
                default: ''
            },
            requireExpandEvent:{
                type: Boolean,
                required: false
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
            propFormatter:{
                type: Function,
                required: false
            },
            formEventBus: {
                type: Function,
                required: false,
                default: ()=>{}
            },
            tableBorder:{
                type: Boolean,
                required: false,
                default: false
            },
            selectionShow:{
                type: Boolean,
                required: false,
                default: true
            },
            searchTimes:{
                type: Number,
                required: false,
                default: 0
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
            headerRowClassNameSub:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            headerCellClassNameSub:{
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
            rowClassNameSub:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            cellClassNameSub:{
                type: Function,
                required: false,
                default: ()=>{}
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
        computed: {
            formatterFun: function () {
                if (this.propFormatter && !ComUtil.isEmptyFunction(this.propFormatter)) {
                    return this.propFormatter;
                } else {
                    return this.formatter;
                }
            },
            eventBusFun: function () {
                if (this.formEventBus && !ComUtil.isEmptyFunction(this.formEventBus)) {
                    return this.formEventBus;
                } else {
                    return this.eventBus;
                }
            }
        },
        data() {
            return {
                form: {
                    formList: []
                },
                rules: null,
                expandRowKeys: [],
                defaultExpandTimes: 0
            }
        },
        created() {
            this.rules = this.getRules();
            this.$emit('created',this);
        },
        watch: {
            searchTimes(newVal, oldVal){
                this.searchTimes > 1 && (this.expandRowKeys = []);
            },
            supTableList: {
                handler: function(newVal, oldVal) {
                    this.form.formList = newVal;
                    this.searchTimes
                    //设置默认展开行
                    this.defaultExpandTimes === 0 && this.defaultExpand();
                    this.defaultExpandTimes++;
                },
                deep: true
            }
        },
        mounted() {
            this.$emit('mounted');
        },
        methods: {
            defaultExpand(){
                 if (this.form.formList.length > 0) {
                    if (this.expandIndex || this.expandIndex === 0) {
                        this.expandRowKeys.push(this.form.formList[this.expandIndex][this.rowKey]);
                        this.handleExpandChange(this.form.formList[this.expandIndex]);
                    } else {
                        this.expandRowKeys = [];
                    }
                }
            },
            handleExpandChange(row, expandedRows){
                if (row.expanded) {
                    row.expanded = false;
                    if (expandedRows) {
                        let idx = this.expandRowKeys.findIndex(f => f === row[this.rowKey]);
                        if (idx != -1) {
                            this.expandRowKeys.splice(idx,1);
                        }
                    }
                } else {
                    row.expanded = true;
                    if (!this.expandRowKeys.includes(row[this.rowKey])) {
                        this.expandRowKeys.push(row[this.rowKey]);
                    }
                }

                if (this.requireExpandEvent) {
                    this.$emit('expandchange', {row, expandedRows});
                } else {
                    this.getRowDetail(row);
                }
            },
            getRowDetail(row) {
                if (this.expandUrl) {
                    let params = {
                        [this.rowKey]: OS_EXPS.eq_ + row[this.rowKey]
                    };
                    let queryUrl = tagRnUrl([this.expandUrl]);
                    axios.get(queryUrl,{params})
                    .then((res) => {
                        let ret = JSON.parse(res.data);
                        this.$set(row, this.expandRowProp, ret);
                    })
                    .catch((err) => {
                        console.log(err);
                    });
                }
            },
            positiveNumber(val) {
                val = val.replace(/[^0-9]/gi, "");
                return val;
            },
            eventBus(eveName, eveParams, val, ...params){
                if (eveName) {
                    this.$emit(eveName.toLowerCase(), val, params);
                }
            },
            formatter(row, column, cellValue, index){
                let supObj = this.supTablehead.find(f => f.prop === column.property);
                if (supObj) {
                    if (this[supObj.formatter]) {
                        return this[supObj.formatter](row, column, cellValue, index);
                    } else {
                        console.error("未传入格式化函数: " + supObj.formatter);
                        return row[column.property];
                    }
                } else {
                    let subObj = this.subTablehead.find(f => f.prop === column.property);
                    if (subObj && this[subObj.formatter]) {
                        if (this[subObj.formatter]) {
                            return this[subObj.formatter](row, column, cellValue, index);
                        } else {
                            console.error("未传入格式化函数: " + subObj.formatter);
                            return row[column.property];
                        }
                    }
                }
            },
            getRules(){
                let rules = {};
                for (const it of this.supTablehead) {
                    if (it.validate) {
                        let obj = {};
                        this.$set(obj, 'required', true);
                        this.$set(obj, 'message', it.validMsg);
                        this.$set(obj, 'trigger', it.validTrigger);
                        rules[it.prop] = [obj];
                    }
                }
                for (const it of this.subTablehead) {
                    if (it.validate) {
                        let obj = {};
                        this.$set(obj, 'required', true);
                        this.$set(obj, 'message', it.validMsg);
                        this.$set(obj, 'trigger', it.validTrigger);
                        rules[it.prop] = [obj];
                    }
                }
                return rules;
            }
        }
    });

</script>