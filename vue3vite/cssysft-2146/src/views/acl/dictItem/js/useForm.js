import {getCurrentInstance, reactive, ref} from "vue";
import Enums from "../../../../utils/enums";
import Exps from "../../../../utils/exps";
import dictItemApi from "../../../../api/acl/dictItem";
import comCfg from "../../../../components/base-com/com-config/com-config";

export default function useForm(emit) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  const refFormMu = ref(null);
  const form = ref({});
  const loading = ref(false);

  let dialogOpenType = ref(Enums.formType.add);
  let dialogClose = null;

  const formRow = reactive([
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'code', label: '编码：'},
      domObj: {model: 'code', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'name', label: '名称：'},
      domObj: {model: 'name', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'name1', label: '扩展名1：'},
      domObj: {model: 'name1', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'name2', label: '扩展名2：'},
      domObj: {model: 'name2', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {label: '顺序：'},
      domObj: {model: 'sequence', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {label: '有效：'},
      domObj: {model: 'isDeleted', dom: 'radio-group', style: {display: 'flex'},
        options: [{label: false, show: '有效'}, {label: true, show: '无效'}]},
    }]
  ]);

  const rules = ref({
    name: [{required: true, trigger: 'blur', message: '名称必须输入'}],
    code: [{required: true, trigger: 'blur', message: '编码必须输入'}]
  });

  const colList = reactive([
    [
      {
        rowObj: {span: 6, colStyle: {flex: '0 0 89px', margin: '0 10px'}},
        domObj: {
          dom: 'button', label: '关闭', click: 'close-click', type: comCfg.elButton.close,
          icon: comCfg.elButton.closeIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {span: 6, colStyle: {flex: '0 0 89px', margin: '0 10px'}},
        domObj: {
          dom: 'button', label: '保存', click: 'save-click', type: comCfg.elButton.confirm,
          icon: comCfg.elButton.confirmIcon, size: comCfg.elButton.size, loading: loading
        }
      }
    ]
  ]);

  const clearValidate = () => {
    setTimeout(() => {
      refFormMu.value.refForm.clearValidate();
    }, 0);
  }

  const initData = (data, openType, close) => {
    loading.value = false;

    form.value = {};
    dialogOpenType = openType;
    dialogClose = close;

    if (Enums.formType.add === dialogOpenType.value) {
      form.value.pid = data.value.id;
      form.value.isDeleted = false;
      clearValidate();
    } else if (Enums.formType.edit === dialogOpenType.value) {
      dictItemApi.select({id: {[Exps.exp]: Exps.eq, [Exps.prop]: 'id', [Exps.val]: data.value.id}}).then(res => {
        form.value = res.data.items[0];
        clearValidate();
      })
    }
  }

  const updateForm = () => {
    loading.value = true;
    dictItemApi.update(form.value).then(res => {
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
    dictItemApi.save(form.value).then(res => {
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
        if (Enums.formType.add === dialogOpenType.value) {
          saveForm();
        } else if (Enums.formType.edit === dialogOpenType.value) {
          updateForm();
        }
      }
    })
  }

  const onAfterFormSave = (call) => {
    call();
  }

  const onPageClose = () => {
    dialogClose();
  }

  return {
    refFormMu,
    form,
    formRow,
    rules,
    colList,
    loading,
    dialogOpenType,
    initData,
    onSaveOrUpdate,
    onAfterFormSave,
    onPageClose
  };
}
