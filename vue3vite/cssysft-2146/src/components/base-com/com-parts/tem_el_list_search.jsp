<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>

<style>
    #tem-el-list-search-div input::-webkit-outer-spin-button,
    #tem-el-list-search-div input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    #tem-el-list-search-div input[type="number"]{
        -moz-appearance: textfield;
    }
    
    #tem-el-list-search-div .btn-excel{
        padding: 0 10px 0 10px;
    }
</style>

<template id="tem-el-list-search" >
    <div id="tem-el-list-search-div">

        <el-form class="form-mini-wh" :model="form" ref="ruleForm" :size="formSize" :disabled="disabled" @submit.native.prevent>
            <el-row type="flex" class="op-flex-center flex-warp" style="padding-bottom: 8px">

                <el-col v-for="(search, index) in formObj.search" :key="index" :span="search.span || 6" class="col-padding" v-if="'select-mixProp' === search.domType">
                    <el-form-item :label="search.formLabel" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                        <el-select class="form-width-100" v-model="form[search.model]" 
                            @change="formEventBus(search.eveName,search.eveParams,$event)" 
                            :clearable="search.clearable" :filterable="search.filterable" :placeholder="search.placeholder"
                            :disabled="search.disabled" autocomplete="off">
                            <el-option
                                v-for="item in mixProp[search.options]"
                                :key="item[search.key]"
                                :label="(!search.optionShow || search.optionShow === 'label') && item[search.label] || search.optionShow === 'label(key)' 
                                        && (item[search.label] + '(' + item[search.key] + ')' ) || (item[search.key] + '(' + item[search.label] + ')' )"
                                :value="item[search.value]">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="search.span || 6" class="col-padding" v-else-if="'cascader-mixProp' === search.domType">
                    <el-form-item :label="search.formLabel" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                        <el-cascader class="form-width-100" v-model="form[search.model]"
                            :options="mixProp[search.options]"
                            :props="{ checkStrictly: false, value: search.value, label: search.label }"
                            @change="formEventBus(search.eveName,search.eveParams,$event)"
                            :clearable="search.clearable" :filterable="search.filterable" :placeholder="search.placeholder"
                            :disabled="search.disabled" autocomplete="off">
                        </el-cascader>
                    </el-form-item>
                </el-col>

                <el-col :span="search.span || 6" class="col-padding" v-else-if="'select-dictObj' === search.domType">
                    <el-form-item :label="search.formLabel" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                        <el-select class="form-width-100" v-model="form[search.model]" 
                            @change="formEventBus(search.eveName,search.eveParams,$event)" 
                            :clearable="search.clearable" :filterable="search.filterable" :placeholder="search.placeholder"
                            :disabled="search.disabled" autocomplete="off">
                            <el-option
                                v-for="item in dictObj[search.options]"
                                :key="item[search.key]"
                                :label="(!search.optionShow || search.optionShow === 'label') && item[search.label] || search.optionShow === 'label(key)' 
                                        && (item[search.label] + '(' + item[search.key] + ')' ) || (item[search.key] + '(' + item[search.label] + ')' )"
                                :value="item[search.value]">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="search.span || 6" class="col-padding" v-else-if="'select-0-1' === search.domType">
                    <el-form-item :label="search.formLabel" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                        <el-select class="form-width-100" v-model="form[search.model]" 
                            @change="formEventBus(search.eveName,search.eveParams,$event)"
                            :placeholder="search.placeholder" :clearable="search.clearable"
                            :disabled="search.disabled" autocomplete="off">
                            <el-option
                                v-for="item in search.options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="search.span || 6" class="col-padding" v-else-if="'date-picker-two' === search.domType">
                    <el-form-item :label="search.formLabel" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                        <el-date-picker class="form-width-100" v-model="form[search.model]"
                            @change="formEventBus(search.eveName,search.eveParams,$event)"
                            type="daterange" range-separator="至" :start-placeholder="search.startPlaceholder" :end-placeholder="search.endPlaceholder" 
                            :value-format="search.valueFormat" :disabled="search.disabled" autocomplete="off">
                        </el-date-picker>
                    </el-form-item>
                </el-col>

                <el-col :span="search.span || 6" class="col-padding" v-else-if="'input-two' === search.domType">
                    <el-row>
                        <el-col :span="search.span1 || 11">
                            <el-form-item :label="search.formLabel1" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                                <el-input class="form-width-100" v-model="form[search.model1]"  
                                        @blur="formEventBus(search.eveName,search.eveParams,$event)"
                                        :placeholder="search.placeholder" :disabled="search.disabled" autocomplete="off">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2" class="margin-top10" style="text-align: center;">
                            <span>-</span>
                        </el-col>
                        <el-col :span="search.span2 || 12">
                            <el-form-item :label="search.formLabel2" :label-width="search.formLabelWidth && search.formLabelWidth || '0'">
                                <el-input class="form-width-100" v-model="form[search.model2]"  
                                        @blur="formEventBus(search.eveName,search.eveParams,$event)"
                                        :placeholder="search.placeholder" :disabled="search.disabled" autocomplete="off">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-col>

                <el-col :span="search.span || 6" class="col-padding" v-else> <!-- 'input' === search.domType -->
                    <el-form-item :label="search.formLabel" :label-width="search.formLabelWidth && search.formLabelWidth || formLabelWidthDefault">
                        <el-input class="form-width-100" v-model="form[search.model]"  
                                @blur="formEventBus(search.eveName,search.eveParams,$event)"
                                :placeholder="search.placeholder" :disabled="search.disabled" autocomplete="off">
                        </el-input>
                    </el-form-item>
                </el-col>

                <slot name="colslot" v-bind="this">
                    
                </slot>

                <!-- 偏移用 -->
                <el-col :span="formObj.btnPreOffset && formObj.btnPreOffset || 0"></el-col>
                <span v-if="!formObj.btnPreOffset" style="padding: 0 0 0 50px"></span>

                <slot name="btndom" v-bind="this">

                    <el-button v-if="formObj.btndomArr ? (formObj.btndomArr.includes('search') && hasAuth(auth.search)) : hasAuth(auth.search)" 
                        :size="btnSize" class="btn-search margin-top10" style="" icon="el-icon-search" :loading="searchLoading" @click="search(1)">搜索</el-button>

                    <el-button v-if="formObj.btndomArr ? (formObj.btndomArr.includes('add') && hasAuth(auth.add)) : hasAuth(auth.add)"
                        :size="btnSize" class="btn-add margin-top10" type="primary" icon="el-icon-plus" @click="add">新增</el-button>

                    <el-button v-if="formObj.btndomArr ? (formObj.btndomArr.includes('edit') && hasAuth(auth.edit)) : hasAuth(auth.edit)"
                        :size="btnSize" class="btn-edit margin-top10" type="primary" icon="el-icon-edit" @click="edit()">编辑</el-button>

                    <el-button v-if="formObj.btndomArr ? (formObj.btndomArr.includes('delete') && hasAuth(auth.delete)) : hasAuth(auth.delete)"
                        :size="btnSize" class="btn-delete margin-top10" type="primary" icon="el-icon-delete" @click="del()">删除</el-button>

                    <el-button v-if="formObj.btndomArr ? (formObj.btndomArr.includes('print') && hasAuth(auth.print)) : hasAuth(auth.print)"
                        :size="btnSize" class="btn-print margin-top10" type="primary" icon="el-icon-printer" @click="print()">打印</el-button>

                    <el-button v-if="formObj.btndomArr ? formObj.btndomArr.includes('confirm') : false" 
                        :size="btnSize" class="btn-operation margin-top10" icon="el-icon-check" :loading="confirmLoading" @click="confirm">确认</el-button>

                    <el-button v-if="formObj.btndomArr ? formObj.btndomArr.includes('cancel') : false"
                        :size="btnSize" class="margin-top10" type="info" icon="el-icon-close" @click="cancel">取消</el-button>

                    <el-button v-if="formObj.btndomArr ? formObj.btndomArr.includes('close') : false"
                        :size="btnSize" class="margin-top10" type="info" icon="el-icon-close" @click="close">关闭</el-button>

                    <el-upload v-if="formObj.btndomArr && formObj.btndomArr.includes('import') && hasAuth(auth.import)"
                        class="btn-excel"
                        ref="refExcelUpload"
                        :action="formObj.comattr.excel.importExcelUrl"
                        accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
                        :on-change="uploadExcelChange"
                        :on-success="uploadExcelSuccess"
                        :on-error="uploadExcelError"
                        :show-file-list="false"
                        :auto-upload="false">
                        <el-button :size="btnSize" class="btn-import margin-top10" type="primary" icon="el-icon-top" 
                            :loading="loadingImportExcel">导入Excel</el-button>
                    </el-upload>

                    <el-button v-if="formObj.btndomArr && formObj.btndomArr.includes('export') && hasAuth(auth.export)"
                        :size="btnSize" class="btn-export margin-top10" type="primary" icon="el-icon-bottom-right" 
                        :loading="loadingExportExcel" @click="exportExcel">导出Excel</el-button>

                    <el-button v-if="formObj.btndomArr && formObj.btndomArr.includes('excelPsd') && hasAuth(auth.import)"
                        :size="btnSize" class="btn-download margin-top10" type="primary" icon="el-icon-bottom" 
                        @click="downloadExcelPsd(formObj.comattr.excel.importExcelPsd)">Excel模板</el-button>
                    
                </slot>
                
            </el-row>
        </el-form>

    </div>
</template>
    
<script type="text/javascript">

    Vue.component('tem-el-list-search', {
        template: '#tem-el-list-search',
        props:{
            formObj:{
                type: Object,
                required: true
            },
            formLabelWidthDefault:{
                type: String,
                required: false,
                default: '10px'
            },
            formSize:{
                type: String,
                required: false,
                default: ''
            },
            btnSize:{
                type: String,
                required: false,
                default: ''
            },
            disabled:{
                type: Boolean,
                required: false,
                default: false
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
            formEventBus:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            searchLoading:{
                type: Boolean,
                required: false,
                default: false
            },
            search:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            add:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            edit:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            del:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            print:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            confirm:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            confirmLoading:{
                type: Boolean,
                required: false,
                default: false
            },
            cancel:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            close:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            loadingImportExcel:{
                type: Boolean,
                required: false,
                default: false
            },
            uploadExcelChange:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            uploadExcelSuccess:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            uploadExcelError:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            loadingExportExcel:{
                type: Boolean,
                required: false,
                default: false
            },
            exportExcel:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            downloadExcelPsd:{
                type: Function,
                required: false,
                default: ()=>{}
            }
        },
        data() {
            return {
                form: {}
            }
        },
        mounted() {
            
        },
        watch: {
            form: {
                handler(nval,oval){
                    if (Object.keys(nval).length !== 0 && Object.keys(oval).length !== 0) {
                        this.$emit('formchange', nval);
                    }
                },
                deep: true
            }
        },
        methods: {
            getData(){
                return this.form;
            },
            setData(data){
                let keys = Object.keys(data);
                for (const it of keys) {
                    this.$set(this.form, it, data[it]);
                }
            }
        }
    });

</script>