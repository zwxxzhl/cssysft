import {getCurrentInstance, reactive, ref} from "vue";
import Enums from "../../../../utils/enums";
import ComUtils from "../../../../utils/comUtils";
import ComCfg from "../../../../components/base-com/com-config/com-config";
import Exps from "../../../../utils/exps";
import UserApi from "../../../../api/acl/user";
import BusFormApi from "../../../../api/acl/busForm";

export default function useForm(emit) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  const refFormMu = ref(null);
  const form = ref({});
  const loading = ref(false);

  let dialogOpenType = ref(Enums.formType.add);
  let dialogClose = null;

  // 获取用户
  let userList = ref([]);
  const getUserList = () => {
    UserApi.select().then(res => {
      userList.value = res.data.items;
    })
  }
  getUserList();

  const formRow = reactive([
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'title', label: '标题：'},
      domObj: {model: 'title', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'content', label: '内容：'},
      domObj: {model: 'content', dom: 'input', type: 'text'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'deadline', label: '截止日期：'},
      domObj: {model: 'deadline', dom: 'date-picker', type: 'datetime', valueFormat: 'YYYY-MM-DD HH:mm:ss', style: {width: '100%'}},
    }],
    [{
      rowObj: {span: 24},
      domObj: {model: 'content', dom: 'slot', slotName: 'mid-divider'},
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {prop: 'userIds', label: '接收者：'},
      domObj: {
        model: 'userIds', dom: 'select', valueKey: 'id', options: userList, option: {key: 'id', label: 'username', value: 'id'},
        multiple: true, filterable: true, clearable: true, style: {width: '100%'}
      },
    }],
    [{
      rowObj: {span: 24},
      formItemObj: {label: '有效：'},
      domObj: {model: 'isDeleted', dom: 'radio-group', style: {display: 'flex'},
        options: [{label: false, show: '有效'}, {label: true, show: '无效'}]},
    }]
  ]);

  const rules = ref({
    title: [{required: true, trigger: 'blur', message: '标题必须输入'}],
    content: [{required: true, trigger: 'blur', message: '内容必须输入'}],
    userIds: [{required: true, trigger: 'blur', message: '必须选择接收者'}]
  });

  const colList = reactive([
    [
      {
        rowObj: {span: 6, colStyle: {flex: '0 0 89px', margin: '0 10px'}},
        domObj: {
          dom: 'button', label: '关闭', click: 'close-click', type: ComCfg.elButton.close,
          icon: ComCfg.elButton.closeIcon, size: ComCfg.elButton.size
        }
      },
      {
        rowObj: {span: 6, colStyle: {flex: '0 0 89px', margin: '0 10px'}},
        domObj: {
          dom: 'button', label: '保存', click: 'save-click', type: ComCfg.elButton.confirm,
          icon: ComCfg.elButton.confirmIcon, size: ComCfg.elButton.size, loading: loading
        }
      }
    ]
  ]);

  const clearValidate = () => {
    refFormMu.value.refForm.clearValidate();
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
      BusFormApi.select({id: ComUtils.expVal(Exps.eq, 'id', data.value.id)}).then(res => {
        form.value = res.data.items[0];
        clearValidate();
      });
    }
  }

  const updateForm = () => {
    loading.value = true;

    BusFormApi.update(form.value).then(res => {
      if (res.success) {
        emit('after-save');
        dialogClose();
      } else {
        gp.$message.error(res.message);
      }
      loading.value = false;
    });
  }

  const saveForm = () => {
    loading.value = true;

    BusFormApi.save(form.value).then(res => {
      if (res.success) {
        emit('after-save');
        dialogClose();
      } else {
        gp.$message.error(res.message);
      }
      loading.value = false;
    }).catch(err => {
      loading.value = false;
    });
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
    });
  }

  const onAfterFormSave = (call) => {
    // 其它逻辑
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
