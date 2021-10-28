export default {
  bpmnjs: {
    modeler: 'modeler',
    viewer: 'viewer',
    new: 'new',
    edit: 'edit',
    detail: 'detail',
    detailColor: 'detailColor'
  },
  formType: {
    add: 'add',
    edit: 'edit',
    detail: 'detail'
  },
  exp: {
    eq: 'eq__',
    ne: 'ne__',
    gt: 'gt__',
    ge: 'ge__',
    lt: 'lt__',
    le: 'le__',
    between: 'between__',
    notBetween: 'notBetween__',
    like: 'like__',
    notLike: 'notLike__',
    likeLeft: 'likeLeft__',
    likeRight: 'likeRight__',
    orSame: 'orSame__',
    orDiff: 'orDiff__',
    in: 'in__', // 值逗号分隔
    notIn: 'notIn__',

    // 范围与orSame表达式使用，示例：between__pre_code / orSame__pre_code
    pre: 'pre_',
    suf: 'suf_',
    // orDiff使用，示例：qrDiff__code#name_code / qrDiff__code#name_name
    orJoin: '#',
    midJoin: '_',

    // 排序key
    asc: 'orderByAsc',  // 值逗号分隔
    desc: 'orderByDesc'
  }
}
