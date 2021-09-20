import {getCurrentInstance, reactive, ref} from "vue";
import enums from "../../../../utils/enums";
import depApi from "../../../../api/acl/dep";

export default function useForm(dialogData, openType, dialogClose, emit) {
  const globalProperties = getCurrentInstance().appContext.config.globalProperties;

  const refFormMu = ref(null);
  const form = ref({});
  const loading = ref(false);

  const formRow = reactive([
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'depName', label: '部门名称：'},
      domObj: {dom: 'input', type: 'text', model: 'depName'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'depNo', label: '部门编码：'},
      domObj: {model: 'depName', change: 'dep-no-change', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {label: '顺序：'},
      domObj: {model: 'sequence', change: 'dep-no-change', dom: 'input', type: 'text'},
    }]
  ]);
  const rules = ref({
    depName: [{required: true, trigger: 'blur', message: '名称必须输入'}],
    depNo: [{required: true, trigger: 'blur', message: '编码必须输入'}]
  });

  const initData = () => {
    loading.value = false;
    refFormMu.value.refForm.clearValidate();
    form.value = {};
    if (enums.formType.add !== openType.value) {
      depApi.select({id: dialogData.value.id}).then(res => {
        form.value = res.data;
      })
    }
  }

  const updateForm = () => {
    loading.value = true;
    depApi.update(form.value).then(res => {
      if (res.success) {
        emit('after-save');
        globalProperties.$message.success(res.message);
        dialogClose();
      } else {
        globalProperties.$message.error(res.message);
      }
      loading.value = false;
    })
  }

  const saveForm = () => {
    loading.value = true;
    depApi.save(form.value).then(res => {
      if (res.success) {
        emit('after-save');
        globalProperties.$message.success(res.message);
        dialogClose();
      } else {
        globalProperties.$message.error(res.message);
      }
      loading.value = false;
    })
  }

  const onSaveOrUpdate = () => {
    refFormMu.value.refForm.validate(valid => {
      if (valid) {
        if (enums.formType.add === openType.value) {
          saveForm();
        } else if (enums.formType.edit === openType.value) {
          updateForm();
        }
      }
    })
  }

  const onPageClose = () => {
    dialogClose();
  }

  return {
    refFormMu,
    form,
    formRow,
    rules,
    initData,
    onSaveOrUpdate,
    onPageClose
  }
}