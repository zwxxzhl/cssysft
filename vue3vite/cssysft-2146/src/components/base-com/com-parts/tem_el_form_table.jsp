<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>
<%@ include file='../../../common/components/el_select_page_com.jsp'%>

<style>
    #tem-el-form-table-div input::-webkit-outer-spin-button,
    #tem-el-form-table-div input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    #tem-el-form-table-div input[type="number"]{
        -moz-appearance: textfield;
    }

    #tem-el-form-table-div .el-table__fixed-right {
        background-color: #fff;
        z-index: 999;
    }

    #tem-el-form-table-div .table-btn-layout {
        display:flex;
        align-items: center;
    }

    #tem-el-form-table-div .scan-layout {
        display:flex;
        padding-left: 10px;
    }

    #tem-el-form-table-div .form-width-10 {
        width: 240px;
    }

    #tem-el-form-table-div .span-xin {
        color: #F56C6C;
        font-size: 12px;
        margin-right: 4px;
    }
    
</style>

<template id="tem-el-form-table" >
    <div id="tem-el-form-table-div">
        <el-form class="form-css" :model="form" :rules="rules" ref="ruleForm" :size="size" :disabled="disabled" @submit.native.prevent>

            <slot name="formtablebtn" v-bind="this">
                <div ref="refTableBtnShow" v-if="tableBtnShow" class="table-btn-layout">

                    <el-button v-if="!formObj.tableBtnArr && true || formObj.tableBtnArr.includes('add')" 
                        size="default" class="text-green" type="text" icon="el-icon-plus" @click="onAddRow">添加</el-button>

                    <el-button v-if="!formObj.tableBtnArr && true || formObj.tableBtnArr.includes('addBatch') " 
                        size="default" class="text-aqua-blue" type="text" icon="el-icon-circle-plus-outline" @click="onAddBatch">批量添加</el-button>

                    <el-button v-if="!formObj.tableBtnArr && true || formObj.tableBtnArr.includes('import') " 
                        size="default" class="text-aqua-blue" type="text" icon="el-icon-circle-plus-outline" @click="onAddBatch"
                        :disabled="!(disDynamic === 'all' || disDynamic === 'import')">导入</el-button>

                    <el-button v-if="!formObj.tableBtnArr && true || formObj.tableBtnArr.includes('remove')" 
                        size="default" class="text-orange" type="text" icon="el-icon-minus" @click="onRemoveRow">移除</el-button>

                    <div class="scan-layout" v-if="!formObj.tableBtnArr && true || formObj.tableBtnArr.includes('scaninput')" >
                        <!-- <el-image style="width: 32px; height: 32px" :src="`${Sys_Url}/static/imgs/barcode.png`" fit="contain"></el-image> -->
                        <el-input size="small" class="form-width-10" v-model="scanVal" @keyup.enter.native="scanValChange(scanVal)" suffix-icon="el-icon-full-screen"
                            :placeholder="scanPlaceholder" autocomplete="off" :disabled="!(disDynamic === 'all' || disDynamic === 'scan')">
                        </el-input>
                    </div>

                </div>
                <div v-else style="padding-top: 18px;"></div>
            </slot>

            <div class="list-table form-mini-wh font-size-12">
                <%-- row-class-name="row-customer"
                    :header-cell-style="{color:'#606266',fontWeight:400}" --%>
                <el-table 
                    ref="refTable"
                    :size="size" 
                    :border="true"
                    style="width: 100%"
                    :height="tableHeight"
                    :max-height="tableMaxHeight"
                    :header-row-class-name="headerRowClassName"
                    :header-cell-class-name="headerCellClassName"
                    :row-class-name="rowClassName"
                    :cell-class-name="cellClassName"
                    :data="form.formList" 
                    :show-summary="showSummary"
                    :summary-method="getSummaries"
                    @selection-change="selectionChange"
                >
                    <el-table-column v-if="formObj.talbeSelectionShow === undefined && true || formObj.talbeSelectionShow" type="selection" header-align="center" align="center" width="55"></el-table-column>
                    <el-table-column type="index" label="序号" header-align="center" align="center" width="50"></el-table-column>

                    <el-table-column 
                        v-for="(col, index) in formObj.tablehead"
                        :key="index" 
                        header-align="center" 
                        :align="col.align || 'center'"
                        :width="col.width || ''"
                        :prop="col.prop" 
                        :label="col.label"
                        :column-key="index.toString()"
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
                                <el-form-item 
                                    :prop="col.validate && ('formList.' + scope.$index + '.' + col.prop) || ''"
                                    :rules="col.validate && rules[col.prop] || [{required: false}]" 
                                    :inline-message="true"
                                    >
                                    <div v-if="'el-select-page-com' === col.domType">
                                        <el-select-page-com
                                            :ref="'ref' + col.prop + scope.$index"
                                            v-model="scope.row[col.prop]"
                                            :placeholder="formObj.elSelectPageCom.placeholder" 
                                            :url="formObj.elSelectPageCom.url"
                                            :input-key="formObj.elSelectPageCom.inputKey"
                                            :options-attr="formObj.elSelectPageCom.optionsAttr"
                                            :input-show="formObj.elSelectPageCom.inputShow"
                                            :input-show-fun="formObj.elSelectPageCom.inputShowFun"
                                            :dropdown-show="formObj.elSelectPageCom.dropdownShow"
                                            :dropdown-show-fun="formObj.elSelectPageCom.dropdownShowFun"
                                            :search-params="formObj.elSelectPageCom.selectSearchParams"
                                            :page-size="formObj.elSelectPageCom.pageSize"
                                            @change="selectPageValChange($event,scope.row)"
                                            @listchange.self="selectPageListChange">
                                        </el-select-page-com>
                                    </div>
                                    <div v-else-if="'select-mixProp' === col.domType">
                                        <el-select class="form-width-100" v-model="scope.row[col.prop]"
                                            :ref="'ref' + col.prop + scope.$index"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" 
                                            :clearable="col.clearable" :filterable="col.filterable" :placeholder="col.placeholder" 
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" autocomplete="off">
                                            <el-option
                                                v-for="opt in mixProp[col.options]"
                                                :key="opt[col.key]"
                                                :label="(!col.optionShow || col.optionShow === 'label') && opt[col.opLabel] || col.optionShow === 'label(key)' 
                                                        && (opt[col.opLabel] + '(' + opt[col.key] + ')' ) || (opt[col.key] + '(' + opt[col.opLabel] + ')' )"
                                                :value="opt[col.opValue]">
                                            </el-option>
                                        </el-select>
                                    </div>
                                    <div v-else-if="'select-custom' === col.domType">
                                        <el-select class="form-width-100" v-model="scope.row[col.prop]"
                                            :ref="'ref' + col.prop + scope.$index"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)"
                                            :clearable="col.clearable" :filterable="col.filterable" :placeholder="col.placeholder"
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" autocomplete="off">
                                            <el-option
                                                    v-for="opt in scope.row[col.options]"
                                                    :key="opt[col.key]"
                                                    :label="(!col.optionShow || col.optionShow === 'label') && opt[col.opLabel] || col.optionShow === 'label(key)'
                                                        && (opt[col.opLabel] + '(' + opt[col.key] + ')' ) || (opt[col.key] + '(' + opt[col.opLabel] + ')' )"
                                                    :value="opt[col.opValue]">
                                            </el-option>
                                        </el-select>
                                    </div>
                                    <div v-else-if="'select-dictObj' === col.domType">
                                        <el-select class="form-width-100" v-model="scope.row[col.prop]"
                                            :ref="'ref' + col.prop + scope.$index"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" 
                                            :clearable="col.clearable" :filterable="col.filterable" :placeholder="col.placeholder" 
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" autocomplete="off">
                                            <el-option
                                                v-for="opt in dictObj[col.options]"
                                                :key="opt[col.key]"
                                                :label="(!col.optionShow || col.optionShow === 'label') && opt[col.opLabel] || col.optionShow === 'label(key)' 
                                                        && (opt[col.opLabel] + '(' + opt[col.key] + ')' ) || (opt[col.key] + '(' + opt[col.opLabel] + ')' )"
                                                :value="opt[col.opValue]">
                                            </el-option>
                                        </el-select>
                                    </div>
                                    <div v-else-if="'date-picker' === col.domType">
                                        <el-date-picker class="form-width-100" v-model="scope.row[col.prop]" :type="col.type" 
                                            :ref="'ref' + col.prop + scope.$index"
                                            :value-format="col.valueFormat"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" :placeholder="col.placeholder" 
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" autocomplete="off">
                                        </el-date-picker>
                                    </div>
                                    <div v-else-if="'input-num' === col.domType">
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" type="number" 
                                            :ref="'ref' + col.prop + scope.$index"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" :placeholder="col.placeholder" 
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" 
                                            @mousewheel.native.prevent autocomplete="off">
                                        </el-input>
                                    </div>
                                    <div v-else-if="'input-positive-num' === col.domType">
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" type="number"
                                            :ref="'ref' + col.prop + scope.$index"
                                            @input="e => (scope.row[col.prop] = positiveNumber(e))"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" :placeholder="col.placeholder" 
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" 
                                            @mousewheel.native.prevent autocomplete="off">
                                        </el-input>
                                    </div>
                                    <div v-else-if="'input' === col.domType">
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" 
                                            :ref="'ref' + col.prop + scope.$index"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" :placeholder="col.placeholder"
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" autocomplete="off">
                                        </el-input>
                                    </div>
                                    <div v-else>
                                        <el-input class="form-width-100" v-model="scope.row[col.prop]" 
                                            :ref="'ref' + col.prop + scope.$index"
                                            @change="formEventBus(col.eveName,col.eveParams,$event,scope.row)" :placeholder="col.placeholder" 
                                            :disabled="'disabled' in scope.row && scope.row.disabledPropArr.includes(col.prop) ? scope.row.disabled : col.disabled" autocomplete="off">
                                        </el-input>
                                    </div>
                                </el-form-item>
                            </template>
                            <template v-else>
                                <slot :name="col.prop.toLowerCase()+'row'" v-bind="this"></slot>
                            </template>
                        </template>
                    </el-table-column>

                    <slot name="opertip" v-bind="this">
                        
                    </slot>

                </el-table>
            </div>

        </el-form>
    </div>
</template>
    
<script type="text/javascript">

    Vue.component('tem-el-form-table', {
        template: '#tem-el-form-table',
        props:{
            formType: {
                type: String,
                required: false,
                default: 'order'
            },
            formObj:{
                type: Object,
                required: true
            },
            disDynamic:{
                type: String,
                required: false,
                default: 'all'
            },
            size:{
                type: String,
                required: false,
                default: 'default'
            },
            disabled:{
                type: Boolean,
                required: false,
                default: false
            },
            rules:{
                type: Object,
                required: false,
                default: ()=>({})
            },
            mixProp:{
                type: Object,
                required: false,
                default: ()=>({})
            },
            dictObj:{
                type: Object,
                required: false,
                default: ()=>({})
            },
            showSummary:{
                type: Boolean,
                required: false,
                default: false
            },
            summariesArr:{
                type: Array,
                required: false,
                default: ()=>([])
            },
            tableBtnShow:{
                type: Boolean,
                required: false,
                default: true
            },
            selectPageValChange:{
                type: Function,
                required: false,
                default: ()=>()=>{}
            },
            selectPageListChange:{
                type: Function,
                required: false,
                default: ()=>()=>{}
            },
            batchSelectedCallback:{
                type: Function,
                required: false,
                default: ()=>()=>{}
            },
            formEventBus:{
                type: Function,
                required: false,
                default: ()=>()=>{}
            },
            scanPlaceholder:{
                type: String,
                required: false,
                default: '使用扫描枪扫描'
            },
            scanValChange:{
                type: Function,
                required: false,
                default: ()=>()=>{}
            },
            controlOpenDialog:{
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
            headerClick:{
                type: Function,
                required: false,
                default: ()=>()=>{}
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
        data() {
            return {
                form: {
                    formList: []
                },
                selection: [],
                initFormListRow: {},

                scanVal: ''
            }
        },
        mounted() {
            this.initData();
        },
        watch: {
            'form.formList': {
                handler: function(newVal, oldVal) {
                    this.$emit('listchange', newVal);
                }
            }
        },
        methods: {
            beforeInitData(){
                for (const it of this.formObj.tablehead) {
                    this.$set(this.initFormListRow, it.prop, null);
                }
            },
            initData(){
                this.beforeInitData();
                if (!this.disabled && this.formType === 'order') {
                    this.form.formList.push({...this.initFormListRow});
                }
            },
            getData(){
                return this.form;
            },
            setData(data){
                this.form.formList = data;
            },
            onAddRow(){
                this.$emit('addrow', {...this.initFormListRow});
                this.form.formList.push({...this.initFormListRow});
            },
            onAddBatch(){
                if (this.controlOpenDialog) {
                    this.$emit("opendialog");
                } else {
                    parent.doit.initDialogData(
                        this.formObj.addBatchDialog.width,
                        this.formObj.addBatchDialog.url,
                        this.formObj.addBatchDialog.bottom,
                        this.formObj.addBatchDialog.top
                    );
                    parent.doit.openDialog(this.formObj.addBatchObj, this.formObj.addBatchObj.title, this);
                }
            },
            onRemoveRow(){
                if (this.selection.length === 0) {
                    this.$msgForWarning('请先选择需移除的行');
                    return;
                }

                this.$emit('removerow', this.selection);
                for (const it of this.selection) {
                    let idx = this.form.formList.findIndex(f => f === it);
                    idx !== -1 && this.form.formList.splice(idx,1);
                }
            },
            selectionChange(val) {
                this.selection = val;
            },
            positiveNumber(val) {
                val = val.replace(/[^0-9]/gi, "");
                return val;
            },
            getSummaries(param) {
                this.$nextTick(() => {
                    this.$refs.refTable.doLayout();
                });
                const { columns, data } = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '合计';
                        return;
                    }
                    if (this.summariesArr.includes(column.property)) {
                        const values = data.map(item => Number(item[column.property]));
                        if (!values.every(value => isNaN(value))) {
                            sums[index] = ComUtil.numberRound(values.reduce((prev, curr) => {
                                const value = Number(curr);
                                if (!isNaN(value)) {
                                    return prev + curr;
                                } else {
                                    return prev;
                                }
                            }, 0), 2);
                            sums[index] += '';
                        } else {
                            sums[index] = '';
                        }
                    }else{
                        sums[index] = '';
                    }
                });
                return sums;
            }
        }
    });

</script>