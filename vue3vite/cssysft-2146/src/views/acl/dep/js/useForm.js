import {getCurrentInstance, reactive, ref} from "vue";
import enums from "../../../../utils/enums";
import depApi from "../../../../api/acl/dep";

export default function useForm(emit) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  const refFormMu = ref(null);
  const form = ref({});
  const loading = ref(false);

  let dialogOpenType = ref(enums.formType.add);
  let dialogClose = null;

  const formRow = reactive([
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'depName', label: '部门名称：'},
      domObj: {model: 'depName', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'depNo', label: '部门编码：'},
      domObj: {model: 'depNo', change: 'dep-no-change', dom: 'input', type: 'text'},
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

  const initData = (dialogData, openType, close) => {
    loading.value = false;
    refFormMu.value.refForm.clearValidate();

    form.value = {};
    dialogOpenType = openType;
    dialogClose = close;

    if (enums.formType.add !== dialogOpenType.value) {
      depApi.select({id: dialogData.value.id}).then(res => {
        form.value = res.data.items[0];
      })
    }
  }

  const updateForm = () => {
    loading.value = true;
    depApi.update(form.value).then(res => {
      if (res.success) {
        emit('after-save');
        dialogClose();
      } else {
        gp.$message.error(res.message);
      }
      loading.value = false;
    })
  }

  const saveForm = () => {
    loading.value = true;
    depApi.save(form.value).then(res => {
      if (res.success) {
        emit('after-save');
        dialogClose();
      } else {
        gp.$message.error(res.message);
      }
      loading.value = false;
    })
  }

  const onSaveOrUpdate = () => {
    refFormMu.value.refForm.validate(valid => {
      if (valid) {
        if (enums.formType.add === dialogOpenType.value) {
          saveForm();
        } else if (enums.formType.edit === dialogOpenType.value) {
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
    loading,
    initData,
    onSaveOrUpdate,
    dialogOpenType,
    onPageClose
  }
}
