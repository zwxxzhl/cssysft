import {getCurrentInstance, reactive, ref} from "vue";
import enums from "../../../../utils/enums";
import depApi from "../../../../api/acl/dep";
import comCfg from "../../../../components/base-com/com-config/com-config";

export default function useForm(emit) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  const refFormMu = ref(null);
  const form = ref({});
  const loading = ref(false);

  let dialogOpenType = ref(enums.formType.add);
  let dialogClose = null;

  let options = ref([]);
  const depTree = () => {
    depApi.selectTree({}).then(res => {
      options.value = res.data.items;
    })
  }
  depTree();

  const formRow = reactive([
    [{
      rowObj: {span: 24},
      formItemObj: {label: '层级：'},
      domObj: {
        model: 'relations', dom: 'cascader', options: options, clearable: true,
        style: {width: '100%'},
        props: {
          expandTrigger: 'hover', checkStrictly: true, value: 'id', label: 'depName'
        }
      },
    }],
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
    console.log("更新数据");
    debugger
    if (form.value.relations) {
      form.value.pid = form.value.relations[form.value.relations.length - 1];
      form.value.relation = form.value.relations.join(",");
    } else {
      form.value.pid = null;
      form.value.relation = null;
    }
    console.log(form.value);
    // depApi.update(form.value).then(res => {
    //   if (res.success) {
    //     emit('after-save');
    //     dialogClose();
    //   } else {
    //     gp.$message.error(res.message);
    //   }
    //   loading.value = false;
    // })
  }

  const saveForm = () => {
    loading.value = true;
    console.log("保存数据");
    debugger
    if (form.value.relations) {
      form.value.pid = form.value.relations[form.value.relations.length - 1];
      form.value.relation = form.value.relations.join(",");
    } else {
      form.value.pid = null;
      form.value.relation = null;
    }
    console.log(form.value);
    // depApi.save(form.value).then(res => {
    //   if (res.success) {
    //     emit('after-save');
    //     dialogClose();
    //   } else {
    //     gp.$message.error(res.message);
    //   }
    //   loading.value = false;
    // })
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
    colList,
    loading,
    initData,
    onSaveOrUpdate,
    dialogOpenType,
    onPageClose
  }
}
