<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>
<%@ include file='../comparts/tem_el_list_search.jsp'%>
<%@ include file='../comparts/tem_el_list_table.jsp'%>
<%@ include file='../comparts/tem_el_list_table_expand.jsp'%>

<style>
    #one-col-list-div .el-table__fixed-right {
        background-color: #fff;
        z-index: 999;
    }
    #one-col-list-div .el-table__fixed {
        background-color: #fff;
        z-index: 999;
    }
</style>

<template id="one-col-list" >
    <div id="one-col-list-div">

        <slot name="search" v-bind="this">
            <tem-el-list-search
                ref="refTemElListSearch"
                :form-size="formSize"
                :btn-size="btnSize"
                :form-obj="formObj"
                :mix-prop="mixProp"
                :dict-obj="dictObj"
                :form-event-bus="formEventBus"
                @formchange="onFormChange"
                :search-loading="searchLoading"
                :search="onSearch"
                :add="onAdd"
                :edit="onEdit"
                :del="onDelete"
                :print="onPrint"
                :confirm="onConfirm"
                :cancel="onCancel"
                :close="onClose"
                :loading-import-excel="loadingImportExcel"
                :upload-excel-change="onUpdExcelChange"
                :upload-excel-success="onUpdExcelSuccess"
                :upload-excel-error="onUpdExcelError"
                :loading-export-excel="loadingExportExcel"
                :export-excel="onExportExcel"
                :download-excel-psd="onDownloadExcelPsd"
            >
                <template #colslot="cp">
                    <slot name="colslot" v-bind="cp.$parent"></slot>
                </template>
                <template #btndom="cp">
                    <slot name="btndom" v-bind="cp.$parent"></slot>
                </template>
            </tem-el-list-search>

            <div class="line-div-1" style="padding-bottom: 8px;">
                <el-divider></el-divider>
            </div>
        </slot>

        <slot name="table" v-bind="this">
            <div class="list-table" v-if="!formObj.tableType && true || formObj.tableType === 'comlist'">
                <tem-el-list-table
                    ref="refTemElListTable"
                    :header-row-class-name="headerRowClassName"
                    :header-cell-class-name="headerCellClassName"
                    :row-class-name="rowClassName"
                    :cell-class-name="cellClassName"
                    :header-click="headerClick"
                    :table-size='tableSize'
                    :table-border="tableBorder"
                    :table-max-height="tableMaxHeight"
                    :table-list="sourceList"
                    :tablehead="formObj.tablehead"
                    :row-key="formObj.comattr.idcode || ''"
                    :handle-select="handleSelect"
                    :handle-select-all="handleSelectAll"
                    :handle-selection-change="handleSelectionChange"
                    :prop-formatter="propFormatter"
                >
                    <template #selectionslot="cp">
                        <slot name="selectionslot" v-bind="cp.$parent" v-if="selectionShow">
                            <el-table-column type="selection" header-align="center" align="center" fixed="left"></el-table-column>
                        </slot>
                    </template>
                    <template #opertip="cp">
                        <slot name="tableoper" v-bind="cp.$parent" v-if="operationShow">
                            <el-table-column
                                :label="formObj.operation ? (formObj.operation.label || '操作') : '操作'"
                                :fixed="formObj.operation ? (formObj.operation.fixed || 'right') : 'right'"
                                :width="formObj.operation ? (formObj.operation.width || '80') : '80'"
                                header-align="center" align="center">
                                <template slot-scope="scope">
                                    <slot name="operation-com" v-bind="cp.$parent" :scope="scope"></slot>
                                    <slot name="operation-edit-pre" v-bind="cp.$parent" :scope="scope"></slot>
                                    <slot name="operation-edit" v-bind="cp.$parent" :scope="scope">
                                        <el-tooltip v-if="formObj.rowBtnShow ? (formObj.rowBtnShow.includes('edit') && hasAuth(auth.edit)) : hasAuth(auth.edit)" 
                                            class="item" effect="dark" content="编辑" placement="left-start">
                                            <i class="el-icon-edit icon-edit" @click="onEdit(scope.row)"></i>
                                        </el-tooltip>
                                    </slot>
                                    <slot name="operation-edit-suf" v-bind="cp.$parent" :scope="scope"></slot>

                                    <slot name="operation-export-pre" v-bind="cp.$parent" :scope="scope"></slot>
                                    <slot name="operation-export" v-bind="cp.$parent">
                                        <el-tooltip v-if="formObj.rowBtnShow ? (formObj.rowBtnShow.includes('export') && hasAuth(auth.export)) : false" 
                                            class="item" effect="dark" content="导出" placement="left-start">
                                            <i class="icon-add el-icon-bottom-right" v-loading="loadingExportExcel" @click="onRowExportExcel(scope.row)"></i>
                                        </el-tooltip>
                                    </slot>
                                    <slot name="operation-export-suf" v-bind="cp.$parent" :scope="scope"></slot>

                                    <slot name="operation-delete-pre" v-bind="cp.$parent" :scope="scope"></slot>
                                    <slot name="operation-delete" v-bind="cp.$parent" :scope="scope">
                                        <el-tooltip v-if="formObj.rowBtnShow ? (formObj.rowBtnShow.includes('delete') && hasAuth(auth.delete)) : hasAuth(auth.delete)" 
                                            class="item" effect="dark" content="删除" placement="bottom-start">
                                            <i class="el-icon-delete icon-delete" @click="onDelete(scope.row)"></i>
                                        </el-tooltip>
                                    </slot>
                                    <slot name="operation-delete-suf" v-bind="cp.$parent" :scope="scope"></slot>
                                </template>
                            </el-table-column>
                        </slot>
                    </template>
                    <template v-for="name in dynamicSlotName" #[name]="cp">
                        <slot :name="name" v-bind="cp.$parent"></slot>
                    </template>
                </tem-el-list-table>
            </div>

            <div class="list-table" v-else-if="formObj.tableType && formObj.tableType === 'expandlist'">
                <tem-el-list-table-expand
                    ref="refTemElListTable"
                    :header-row-class-name="headerRowClassName"
                    :header-cell-class-name="headerCellClassName"
                    :header-row-class-name-sub="headerRowClassNameSub"
                    :header-cell-class-name-sub="headerCellClassNameSub"
                    :row-class-name="rowClassName"
                    :cell-class-name="cellClassName"
                    :row-class-name-sub="rowClassNameSub"
                    :cell-class-name-sub="cellClassNameSub"
                    :header-click="headerClick"
                    :table-size='tableSize'
                    :table-border="tableBorder"
                    :table-max-height="tableMaxHeight"
                    :search-times="searchTimes"
                    :sup-table-list="sourceList"
                    :sup-tablehead="formObj.tablehead"
                    :sub-tablehead="formObj.subTablehead"
                    :row-key="formObj.comattr.idcode"
                    :expand-url="formObj.comattr.expandurl"
                    :expand-row-prop="formObj.comattr.expandRowProp"
                    :expand-index="formObj.comattr.expandIndex"
                    :require-expand-event="formObj.comattr.requireExpandEvent"
                    @expandchange="onExpandChange"
                    :handle-selection-change="handleSelectionChange"
                    :prop-formatter="propFormatter"
                    :form-event-bus="formEventBus"
                >
                    <template #selectionslot="ex">
                        <slot name="selectionslot" v-bind="ex.$parent" v-if="selectionShow">
                            <el-table-column type="selection" header-align="center" align="center" fixed="left"></el-table-column>
                        </slot>
                    </template>
                    <template #supopertip="ex">
                        <slot name="tableoper" v-bind="ex.$parent" v-if="operationShow">
                            <el-table-column
                                :label="formObj.operation ? (formObj.operation.label || '操作') : '操作'"
                                :fixed="formObj.operation ? (formObj.operation.fixed || 'right') : 'right'"
                                :width="formObj.operation ? (formObj.operation.width || '80') : '80'"
                                header-align="center" align="center">
                                <template slot-scope="scope">
                                    <slot name="operation-com" v-bind="ex.$parent" :scope="scope"></slot>
                                    <slot name="operation-edit-pre" v-bind="ex.$parent" :scope="scope"></slot>
                                    <slot name="operation-edit" v-bind="ex.$parent" :scope="scope">
                                        <el-tooltip v-if="formObj.rowBtnShow ? (formObj.rowBtnShow.includes('edit') && hasAuth(auth.edit)) : hasAuth(auth.edit)" 
                                            class="item" effect="dark" content="编辑" placement="left-start">
                                            <i class="el-icon-edit icon-edit" @click="onEdit(scope.row)"></i>
                                        </el-tooltip>
                                    </slot>
                                    <slot name="operation-edit-suf" v-bind="ex.$parent" :scope="scope"></slot>

                                    <slot name="operation-export-pre" v-bind="ex.$parent" :scope="scope"></slot>
                                    <slot name="operation-export" v-bind="ex.$parent" :scope="scope">
                                        <el-tooltip v-if="formObj.rowBtnShow ? (formObj.rowBtnShow.includes('export') && hasAuth(auth.export)) : false" 
                                            class="item" effect="dark" content="导出" placement="left-start">
                                            <i class="icon-add el-icon-bottom-right" v-loading="loadingExportExcel" @click="onRowExportExcel(scope.row)"></i>
                                        </el-tooltip>
                                    </slot>
                                    <slot name="operation-export-suf" v-bind="ex.$parent" :scope="scope"></slot>

                                    <slot name="operation-delete-pre" v-bind="ex.$parent" :scope="scope"></slot>
                                    <slot name="operation-delete" v-bind="ex.$parent" :scope="scope">
                                        <el-tooltip v-if="formObj.rowBtnShow ? (formObj.rowBtnShow.includes('delete') && hasAuth(auth.delete)) : hasAuth(auth.delete)" 
                                            class="item" effect="dark" content="删除" placement="bottom-start">
                                            <i class="el-icon-delete icon-delete" @click="onDelete(scope.row)"></i>
                                        </el-tooltip>
                                    </slot>
                                    <slot name="operation-delete-suf" v-bind="ex.$parent" :scope="scope"></slot>
                                </template>
                            </el-table-column>
                        </slot>
                    </template>
                    <template v-for="supname in dynamicSlotName" #[supname]="ex">
                        <slot :name="supname" v-bind="ex.$parent"></slot>
                    </template>
                    <template v-for="subname in subDynamicSlotName" #[subname]="ex">
                        <slot :name="subname" v-bind="ex.$parent"></slot>
                    </template>
                </tem-el-list-table-expand>
            </div>
        </slot>

        <slot name="pagination" v-bind="this">
            <div ref="refPagination" v-if="pageShow" class="list-page">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="pageSizes" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                </el-pagination>
            </div>
        </slot>

    </div>
</template>
    
<script type="text/javascript">

    Vue.component('one-col-list', {
        template: '#one-col-list',
        props:{
            formObj:{
                type: Object,
                required: false
            },
            sourceList:{
                type: Array,
                required: false,
                default: ()=>{[]}
            },
            searchParams:{
                type: Object,
                required: false,
                default: ()=>{{}}
            },
            ifSearch:{
                type: Boolean,
                require: false,
                default: true
            },
            formSize:{
                type: String,
                required: false,
                default: getTopInputAttrSize()
            },
            btnSize:{
                type: String,
                required: false,
                default: getTopBtnAttrSize()
            },
            tableSize:{
                type: String,
                required: false,
                default: ''
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
            operationShow:{
                type: Boolean,
                required: false,
                default: true
            },
            pageShow:{
                type: Boolean,
                required: false,
                default: true
            },
            total:{
                type: Number,
                required: false,
                default: 50
            },
            headerRowClassName:{
                type: Function,
                required: false,
                default: ()=>('row-header')
            },
            headerCellClassName:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            headerRowClassNameSub:{
                type: Function,
                required: false,
                default: ()=>('row-header')
            },
            headerCellClassNameSub:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            rowClassName:{
                type: Function,
                required: false,
                default: ()=>('row-customer')
            },
            cellClassName:{
                type: Function,
                required: false,
                default: ()=>{}
            },
            rowClassNameSub:{
                type: Function,
                required: false,
                default: ()=>('row-customer')
            },
            cellClassNameSub:{
                type: Function,
                required: false,
                default: ()=>{}
            }
        },
        data() {
            return {
                tableMaxHeight: null,
                dialogCom: null,
                dataForOpenPage: null,
                noSinglePage: true,

                mixProp: {},
                dictObj: {},

                multipleSelection: [],
                searchTimes: 0,

                currentPage: 1,
                pageSizes: getPageSizes(),
                pageSize: parseInt(`${Page_Size}`),

                searchLoading: false,
                loadingImportExcel: false,
                loadingExportExcel: false
            }
        },
        computed: {
            dynamicSlotName: function () {
                let arr = [];
                if (this.formObj.tablehead) {
                    for (const iterator of this.formObj.tablehead) {
                        iterator.headerslot && arr.push(iterator.prop.toLowerCase() + 'header');
                        iterator.rowslot && arr.push(iterator.prop.toLowerCase() + 'row');
                    }
                }
                return arr;
            },
            subDynamicSlotName: function () {
                let arr = [];
                if (this.formObj.subTablehead) {
                    for (const iterator of this.formObj.subTablehead) {
                        iterator.headerslot && arr.push(iterator.prop.toLowerCase() + 'subheader');
                        iterator.rowslot && arr.push(iterator.prop.toLowerCase() + 'subrow');
                    }
                }
                return arr;
            }
        },
        created() {
            this.$emit('insidecreated',this);
        },
        mounted() {
            this.$emit('insidemounted');
            this.initData();
        },
        methods: {
            initData() {
                this.searchLoading = false;
                this.loadingImportExcel = false;
                this.loadingExportExcel = false;

                //此组件通过模态框打开时，selfDialogLevel 值等于调用打开模态框时传入参数 level，模态框嵌套时, 打开模态框参数 level 需要不同
                if (this.formObj.selfDialogLevel || this.formObj.selfDialogLevel == 0) {
                    if (this.formObj.selfDialogLevel == 1 || this.formObj.selfDialogLevel == 2) {
                        let key = 'refElDialogCom'.concat(this.formObj.selfDialogLevel);
                        this.dialogCom = parent.doit.$refs[key];
                    } else {
                        this.dialogCom = parent.doit.$refs.refElDialogCom;
                    }

                    this.noSinglePage = this.dialogCom.dialogFormVisible;
                    this.dataForOpenPage = this.dialogCom.dataForOpenPage;
                }


                if (this.noSinglePage) {
                    
                } else {
                    
                }

                this.$emit('initdata', this.dialogCom ? this.dialogCom.parentDom : null);
                if (!this.formObj.comattr || !this.formObj.comattr.idcode) {
                    console.error("未配置必须属性 comattr.idcode");
                }
                this.initTableHeight();
            },
            onFormChange(form){
                this.$emit('formchange', form, this.dialogCom ? this.dialogCom.parentDom : null);
            },
            headerClick(e, type){
                this.$emit('headerclick', e, type);
            },
            handleSelect(val, row){
                this.$emit('handleselect', val, row);
            },
            handleSelectAll(val){
                this.$emit('handleselectall', val);
            },
            handleSelectionChange(val){
                this.multipleSelection = val;
                this.$emit('handleselectionchange', val);
            },
            onExpandChange(val){
                this.$emit('expandchange', val);
            },
            formEventBus(eveName, eveParams, val, ...params){
                if (eveName) {
                    this.$emit(eveName.toLowerCase(), val, params);
                }
            },
            propFormatter(row, column, cellValue, index){
                let supObj = this.formObj.tablehead.find(f => f.prop === column.property);
                if (supObj) {
                    if (this[supObj.formatter]) {
                        return this[supObj.formatter](row, column, cellValue, index);
                    } else {
                        console.error("未传入格式化函数: " + supObj.formatter);
                        return row[column.property];
                    }
                } else {
                    let subObj = this.formObj.subTablehead.find(f => f.prop === column.property);
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
            onPrint() {
                this.$emit('print', this.multipleSelection, this.dialogCom ? this.dialogCom.parentDom : null);
            },
            onConfirm() {
                this.$emit('confirm', this.dialogCom ? this.dialogCom.parentDom : null);
            },
            onCancel(){
                this.$emit('cancel', this.dialogCom ? this.dialogCom.parentDom : null);
            },
            onClose(){
                this.onCloseForm();
                this.$emit('close', this.dialogCom ? this.dialogCom.parentDom : null);
            },
            onSearch(val) {
                if (this.ifSearch) {
                    val && (this.currentPage = 1);

                    let params = {};
                    Object.assign(params, this.searchParams);
                    this.pageShow && Object.assign(params, { pageIndex: this.currentPage, pageSize: this.pageSize });

                    if (this.formObj.comattr.searchurl) {
                        this.searchLoading = true;
                        axios.get(tagRnUrl([this.formObj.comattr.searchurl]), {
                            params
                        }).then(res => {
                            this.$emit('aftersearch', res, this.dialogCom ? this.dialogCom.parentDom : null);
                            this.searchTimes++;
                            this.searchLoading = false;
                        }).catch(err => {
                            console.log(err);
                            this.searchLoading = false;
                        });
                    } else {
                        console.error("未配置属性 comattr.searchurl ");
                    }
                }
            },
            handleSizeChange(val) {
                this.pageSize = val;
                this.onSearch(1);
            },
            handleCurrentChange(val) {
                this.currentPage = val;
                this.onSearch();
            },
            onAdd(){
                if (this.formObj.dialog) {
                    parent.doit.initDialogData(this.formObj.dialog.width, this.formObj.dialog.url, this.formObj.dialog.bottom, this.formObj.dialog.top);
                    parent.doit.openDialog({}, this.formObj.dialog.addtitle, this);
                }else{
                    if (this.formObj.singlePage) {
                        parent.doit.setTabData(null, this.formObj.singlePage.addtitle, this);
                        parent.addTab(this.formObj.singlePage.addtitle, this.formObj.singlePage.addtitle, this.formObj.singlePage.url);
                    } else {
                        console.error("未配置打开方式: dialog 模态框属性 或 singlePage 单页面属性");
                    }
                }
            },
            onEdit(row, type){
                let editData = {};
                var result = false;
                if(typeof(row) == 'object'){
                    editData = JSON.parse(JSON.stringify(row));
                    result = true;
                }else{
                    if(this.multipleSelection.length > 0 && this.multipleSelection.length === 1){
                        editData = JSON.parse(JSON.stringify(this.multipleSelection[0]));
                        result = true;
                    }else{
                        this.$msgForWarning('编辑前，请先单选!');
                    }
                }
                editData.VIEWTYPE = type;

                if(result){
                    if (this.formObj.dialog) {
                        parent.doit.initDialogData(this.formObj.dialog.width, this.formObj.dialog.url, this.formObj.dialog.bottom, this.formObj.dialog.top);
                        parent.doit.openDialog(editData, this.formObj.dialog.edittitle, this);
                    }else{
                        if (this.formObj.singlePage) {
                            parent.doit.setTabData(editData, this.formObj.singlePage.edittitle, this);
                            parent.addTab(this.formObj.singlePage.edittitle, this.formObj.singlePage.edittitle, this.formObj.singlePage.url);
                        } else {
                            console.error("未配置打开方式: dialog 模态框属性 或 singlePage 单页面属性");
                        }
                    }
                }
            },
            onDelete(row){
                var keys = ''
                var result = false;
                if(typeof(row) == 'object'){
                    keys = row[this.formObj.comattr.idcode];
                    result = true;
                }else{
                    if(this.multipleSelection.length > 0){
                        var ids = [];
                        for (var i = 0; i < this.multipleSelection.length; i++) {
                            var element = this.multipleSelection[i];
                            ids.push(element[this.formObj.comattr.idcode]);
                        }
                        
                        if(ids.length > 0){
                            keys = ids.join(',');
                        }
                        result = true;
                    }else{
                        this.$msgForWarning('删除前，请先选择!');
                    }
                }
                
                var delData = {
                    keys: keys
                }
                if(result){
                    parent.doit.$confirm('此操作将删除数据, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        if (this.formObj.comattr.delurl) {
                            let deleteUrl = tagRnUrl([this.formObj.comattr.delurl]);
                            axios.post(deleteUrl, Qs.stringify(delData))
                            .then((res) => {
                                this.onSearch(1);
                                this.$msgForSuccess('删除成功!');
                            }).catch((err) => {
                                this.$msgForError('删除失败!');
                                console.log(err);
                            });
                        } else {
                            console.error("未配置属性 comattr.delurl ");
                        }
                    }).catch(() => {

                    });
                }
            },
            onUpdExcelChange(file, fileList) {
                if (file.status === 'ready') {
                    let fileTemp = file.raw;
                    let fileName = file.raw.name;
                    let fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
                    
                    if (fileTemp) {
                        if ((fileType == 'xlsx') || (fileType == 'xls')) {
                            this.loadingImportExcel = true;
                            this.$refs.refTemElListSearch.$refs.refExcelUpload.submit();
                        } else {
                            this.$msgForError('文件格式错误，只支持.xlsx与.xls格式!');
                            this.$refs.refTemElListSearch.$refs.refExcelUpload.clearFiles();
                        }
                    } else {
                        this.$msgForWarning('导入失败，请重新上传!');
                    }
                }
            },
            onUpdExcelSuccess(response, file, fileList){
                if (response.code === 'success') {
                    this.onSearch(1);
                    this.$msgForSuccess('导入成功!');
                } else {
                    this.$msgForError(response.msg);
                }
                this.$refs.refTemElListSearch.$refs.refExcelUpload.clearFiles();
                this.loadingImportExcel = false;
            },
            onUpdExcelError(err, file, fileList){
                this.$msgForError('导入出错了!');
                this.$refs.refTemElListSearch.$refs.refExcelUpload.clearFiles();
                this.loadingImportExcel = false;
            },
            onExportExcel(){
                if (this.ifSearch) {
                    let params = {};
                    Object.assign(params, this.searchParams);
                    this.pageShow && Object.assign(params, { pageIndex: this.currentPage, pageSize: this.pageSize });

                    if (this.formObj.comattr.excel.exportExcelUrl) {
                        this.loadingExportExcel = true;
                        let exportExcelUrl = tagRnUrl([this.formObj.comattr.excel.exportExcelUrl]);
                        axios.get(exportExcelUrl,{
                            params,
                            responseType: 'blob'
                        }).then((res) => {
                            let blob = new Blob([res.data],{ type: 'application/xlsx' });
                            let fileName = this.formObj.comattr.excel.exportExcelName;
                            let url = URL.createObjectURL(blob);
                            ComUtil.methodsObj.downloadFile(url, fileName + '.xlsx');
                            this.loadingExportExcel = false;
                        }).catch((err) => {
                            this.$msgForError('导出失败!');
                            this.loadingExportExcel = false;
                        });
                    } else {
                        console.error("未配置属性 comattr.excel.exportExcelUrl ");
                    }
                }
            },
            onRowExportExcel(row){
                if (this.formObj.comattr && this.formObj.comattr.excel 
                    && this.formObj.comattr.excel.rowExportExcelUrl && this.formObj.comattr.excel.rowExportExcelName) {
                    
                    this.saveLoading = true;
                    let rowExportExcelUrl = tagRnUrl([this.formObj.comattr.excel.rowExportExcelUrl]);
                    axios.post(rowExportExcelUrl, Qs.stringify(row), { responseType: 'blob' })
                        .then(res => {
                            let blob = new Blob([res.data],{ type: 'application/xlsx' });
                            let fileName = this.formObj.comattr.excel.rowExportExcelName;
                            let url = URL.createObjectURL(blob);
                            ComUtil.methodsObj.downloadFile(url, fileName + '.xlsx');
                            this.saveLoading = false;
                        })
                        .catch(err => {
                            console.log(err);
                            this.$msgForError('导出失败!');
                            this.saveLoading = false;
                        });
                } else {
                    console.error("未配置属性 comattr.excel.rowExportExcelUrl 或 rowExportExcelName ");
                }
            },
            onDownloadExcelPsd(excelName){
                if (excelName) {
                    let str = 'resources/download/' + excelName + '.xlsx';
                    let url = tagBaseUrl([str]);
                    ComUtil.methodsObj.downloadFile(url, excelName + '.xlsx');
                } else {
                    console.error("未配置属性 comattr.excel.importExcelPsd ");
                }
            },
            onCloseForm() {
                if (this.dialogCom && this.dialogCom.dialogFormVisible) {
                    this.dialogCom.hiddenDialog();
                } else {
                    parent.removeTab(this.formObj.singlePage.addtitle);
                    parent.removeTab(this.formObj.singlePage.edittitle);
                }
            },
            initTableHeight(){
                setTimeout(() => {
                    let h1 = this.$refs.refTemElListSearch.$el.clientHeight;
                    let h2 = 0;
                    if (this.pageShow) { h2 = this.$refs.refPagination.clientHeight; }

                    if (this.dialogCom) {
                        this.tableMaxHeight = (parent.innerHeight - parent.innerHeight*this.dialogCom.bottomHeight)-h1-h2-30;
                    } else {
                        this.tableMaxHeight = parent.innerHeight-h1-h2-145;
                    }
                }, 200);
            }
        }
    });

</script>