<%@ page language='java' contentType='text/html; charset=UTF-8' pageEncoding='UTF-8'%>

<style>
    #tem-el-form-div input::-webkit-outer-spin-button,
    #tem-el-form-div input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    #tem-el-form-div input[type="number"]{
        -moz-appearance: textfield;
    }
    
    #tem-el-form-div .radio-layout{
        vertical-align: middle;
        padding-top: 6px;
    }

    #tem-el-form-div .col-class{
        
    }

</style>

<template id="tem-el-form" >
    <div id="tem-el-form-div">
        <el-form class="form-css" :model="form" :rules="rules" ref="ruleForm" :size="size" :disabled="disabled">

            <el-row type="flex" class="op-flex-center flex-warp" v-for="(formrow, idx) in formObj.formRowList" :key="idx">
                <el-col v-for="(formitem, index) in formrow" :key="index" :span="formitem.span" class="col-class" v-if="'select-mixProp' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-select class="form-width-100" v-model="form[formitem.model]"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :clearable="formitem.clearable" :filterable="formitem.filterable" :multiple="formitem.multiple"
                            :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
                            <el-option
                                v-for="item in mixProp[formitem.options]"
                                :key="item[formitem.key]"
                                :label="(!formitem.optionShow || formitem.optionShow === 'label') && item[formitem.label] || formitem.optionShow === 'label(key)' 
                                        && (item[formitem.label] + '(' + item[formitem.key] + ')' ) || (item[formitem.key] + '(' + item[formitem.label] + ')' )"
                                :value="item[formitem.value]">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'select-dictObj' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-select class="form-width-100" v-model="form[formitem.model]"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :clearable="formitem.clearable" :filterable="formitem.filterable" :multiple="formitem.multiple" 
                            :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
                            <el-option
                                v-for="item in dictObj[formitem.options]"
                                :key="item[formitem.key]"
                                :label="(!formitem.optionShow || formitem.optionShow === 'label') && item[formitem.label] || formitem.optionShow === 'label(key)' 
                                        && (item[formitem.label] + '(' + item[formitem.key] + ')' ) || (item[formitem.key] + '(' + item[formitem.label] + ')' )"
                                :value="item[formitem.value]">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'cascader-mixProp' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-cascader class="form-width-100" v-model="form[formitem.model]"
                            :options="mixProp[formitem.options]"
                            :props="{ checkStrictly: false, value: formitem.value, label: formitem.label }"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :clearable="formitem.clearable" :filterable="formitem.filterable" :placeholder="formitem.placeholder" 
                            :disabled="formitem.disabled" autocomplete="off">
                        </el-cascader>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'select-0-1' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-select class="form-width-100" v-model="form[formitem.model]"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :placeholder="formitem.placeholder" :clearable="formitem.clearable" 
                            :disabled="formitem.disabled" autocomplete="off">
                            <el-option
                                v-for="item in formitem.options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'date-picker' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-date-picker class="form-width-100" v-model="form[formitem.model]"
                            :type="formitem.type" :value-format="formitem.valueFormat"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
                        </el-date-picker>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'input' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-input class="form-width-100" v-model="form[formitem.model]"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
                        </el-input>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'textarea' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-input class="form-width-100" v-model="form[formitem.model]"
                            :type="formitem.type" :rows="formitem.rows" 
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
                        </el-input>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'checkbox' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <el-checkbox v-if="formitem.domKey === 'isDel'" class="form-width-100" v-model="form[formitem.model]" :true-label="0" :false-label="1">{{ formitem.placeholder }}</el-checkbox>
                        <el-checkbox v-else class="form-width-100" v-model="form[formitem.model]" :true-label="1" :false-label="0">{{ formitem.placeholder }}</el-checkbox>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'radio-mixProp' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <div class="radio-layout">
                            <el-radio-group class="form-width-100" v-model="form[formitem.model]"
                                @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                                :disabled="formitem.disabled" autocomplete="off">
                                <el-radio v-for="item in mixProp[formitem.options]" :key="item[formitem.key]" :label="item[formitem.value]">{{ item[formitem.label] }}</el-radio>
                            </el-radio-group>
                        </div>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else-if="'radio-dictObj' === formitem.domType">
                    <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''" 
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
                        <div class="radio-layout">
                            <el-radio-group class="form-width-100" v-model="form[formitem.model]"
                                @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                                :disabled="formitem.disabled" autocomplete="off">
                                <el-radio v-for="item in dictObj[formitem.options]" :key="item[formitem.key]" :label="item[formitem.value]">{{ item[formitem.label] }}</el-radio>
                            </el-radio-group>
                        </div>
                    </el-form-item>
                </el-col>

                <el-col :span="formitem.span" class="col-class" v-else="'span' === formitem.domType">
                    <el-form-item>
                        <div :class="formitem.class">
                            <i v-if="mixProp[formitem.model]" class="el-icon-info"></i>
                            <span>{{ mixProp[formitem.model] }}</span>
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>

        </el-form>
    </div>
</template>
    
<script type="text/javascript">

    Vue.component('tem-el-form', {
        template: '#tem-el-form',
        props:{
            formObj:{
                type: Object,
                required: true
            },
            size:{
                type: String,
                required: false,
                default: 'medium'
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
            formEventBus: {
                type: Function,
                required: false,
                default: ()=>{}
            }
        },
        computed: {
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
                form: {}
            }
        },
        mounted() {
            
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
            },
            eventBus(eveName, eveParams, val, ...params){
                if (eveName) {
                    this.$emit(eveName.toLowerCase(), val, params);
                }
            },
            clearData(){
                this.form = {};
            },
            clearValidate(){
                this.$refs.ruleForm.clearValidate();
            }
        }
    });

</script>