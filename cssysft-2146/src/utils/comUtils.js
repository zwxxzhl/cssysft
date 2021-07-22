export default {
  /*
      生成table格式化方法 genColumnFormat(listData,'unitIdFormat','mixProp','unitList','unitId','name') 普通数据
                    genColumnFormat(listData,'clearingFormFormat','dictObj','clearingFormList','dictCode','dictName') 字典数据
  */
  genColumnFormat(listData, funName,objKey,listKey,idKey,nameKey){
    let fun = {};
    fun[funName] = function (row, column) {
      let data = row[column.property];
      let ret = data;
      if (listData[objKey][listKey]) {
        let obj = listData[objKey][listKey].filter(f => f[idKey] == ((data != 0 && !data) ? row[idKey] : data));
        obj.length > 0 && (ret = obj[0][nameKey]);
      }
      return ret;
    }
    return fun;
  },
  //日期格式化方法 YYYY-MM-DD
  dateYMDFormat(row,column){
    let data = row[column.property];
    if (data) {
      return moment(data).format('YYYY-MM-DD');
    } else {
      return '';
    }
  },
  //日期格式化方法 YYYY-MM-DD HH:mm:ss
  dateYMDHmsFormat(row,column){
    let data = row[column.property];
    if (data) {
      return moment(data).format('YYYY-MM-DD HH:mm:ss');
    } else {
      return '';
    }
  },
  //获取uuid
  getUuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
  }
}
