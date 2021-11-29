import comCfg from "../../../../components/base-com/com-config/com-config";
import Exps from "../../../../utils/exps";

export default function useSearch(searchRow, searchExp, search, searchLoading, onSearch) {
  searchExp[Exps.desc] = { [Exps.exp]: Exps.desc, [Exps.prop]: 'sequence' };

  search.isDeleted = '0';
  searchRow.value = [
    [
      {
        rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 0px'}},
        formItemObj: {labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
        domObj: {model: 'depName', placeholder: '部门名称', change: 'dep-name-change', dom: 'input', type: 'text'},
        searchObj: {[Exps.exp]: Exps.like}
      },
      {
        rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 5px'}},
        formItemObj: {labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
        domObj: {model: 'depNo', placeholder: '部门编码', dom: 'input', type: 'text'},
        searchObj: {[Exps.exp]: Exps.eq}
      },
      {
        rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 5px'}},
        formItemObj: {labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
        domObj: {
          model: 'isDeleted', placeholder: '有效', dom: 'select', clearable: true,
          options: [{id: 'a', code: '0', name: '有效'}, {id: 'b', code: '1', name: '无效'}],
          option: {key: 'id', label: 'name', value: 'code'}
        },
        searchObj: {[Exps.exp]: Exps.eq}
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 15px'}},
        domObj: {
          dom: 'button', label: '搜索', click: 'search-click', type: comCfg.elButton.search,
          icon: comCfg.elButton.searchIcon, size: comCfg.elButton.size, loading: searchLoading
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '新增', click: 'add-click', type: comCfg.elButton.add,
          icon: comCfg.elButton.addIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '编辑', click: 'edit-click', type: comCfg.elButton.edit,
          icon: comCfg.elButton.editIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '删除', click: 'delete-click', type: comCfg.elButton.delete,
          icon: comCfg.elButton.deleteIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '测试', click: 'ceshi-click', type: comCfg.elButton.delete,
          icon: comCfg.elButton.deleteIcon, size: comCfg.elButton.size
        }
      }
    ]
  ];

  const onDepNameChange = (val) => {
    console.log("搜索条件 DepName 改变")
    console.log(val);
  }

  return {
    onDepNameChange
  };
}
