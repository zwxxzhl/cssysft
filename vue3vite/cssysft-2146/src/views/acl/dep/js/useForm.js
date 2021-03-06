import {getCurrentInstance, reactive, ref} from "vue";
import Enums from "../../../../utils/enums";
import depApi from "../../../../api/acl/dep";
import comCfg from "../../../../components/base-com/com-config/com-config";
import dictItemApi from "../../../../api/acl/dictItem";
import Exps from "../../../../utils/exps";

export default function useForm(emit) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  const refFormMu = ref(null);
  const form = ref({});
  const loading = ref(false);

  let dialogOpenType = ref(Enums.formType.add);
  let dialogClose = null;

  // 获取部门树
  let options = ref([]);
  const depTree = () => {
    depApi.selectTree({}).then(res => {
      options.value = res.data.items;
    })
  }
  depTree();

  // 获取部门类型
  let depTypeList = ref([]);
  const getDictList = () => {
    dictItemApi.listBatch({codes: 'DepType'}).then(res => {
      depTypeList.value = res.data.items.DepType;
    })
  }
  getDictList();

  const formRow = reactive([
    [{
      rowObj: {span: 24},
      formItemObj: {label: '部门层级：'},
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
      formItemObj: {prop: 'type', label: '部门类型：'},
      domObj: {
        model: 'type', dom: 'select', options: depTypeList,
        option: {key: 'id', label: 'name', value: 'code'}, clearable: true,
        style: {width: '100%'}
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
      domObj: {model: 'depNo', dom: 'input', type: 'text'},
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
    type: [{required: true, trigger: 'change', message: '类型必须输入'}],
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
      form.value.isDeleted = false;
      clearValidate();
    } else if (Enums.formType.edit === dialogOpenType.value) {
      depApi.select({id: {[Exps.exp]: Exps.eq, [Exps.prop]: 'id', [Exps.val]: data.value.id}}).then(res => {
        form.value = res.data.items[0];
        if (form.value.relation) {
          form.value.relations = form.value.relation.split(',');
        }
        clearValidate();
      })
    }
  }

  const updateForm = () => {
    loading.value = true;

    if (form.value.relations) {
      form.value.pid = form.value.relations[form.value.relations.length - 1];
      if (form.value.pid === form.value.id) {
        gp.$message.error('不允许把自己做为父级');
        loading.value = false;
        return;
      }
      form.value.relation = form.value.relations.join(",");
    } else {
      form.value.pid = '0';
      form.value.relation = null;
    }

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

    if (form.value.relations) {
      form.value.pid = form.value.relations[form.value.relations.length - 1];
      form.value.relation = form.value.relations.join(",");
    } else {
      form.value.pid = '0';
      form.value.relation = null;
    }

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
  }
}
