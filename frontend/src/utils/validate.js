/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

const regs = {
  money: /(^[1-9](\d+)?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
  email: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/,
  phone: /^((0\d{2,3}-)|\d{7,8})|(1[3584]\d{9})$/,
  positiveDigit: /^[+]?(0|([1-9]\d*))(\.\d+)?$/,
  negativeDigit: /^[-]?(0|([1-9]\d*))(\.\d+)?$/,
  digit: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
  positiveInteger: /^[+]?(0|([1-9]\d*))$/,
  negativeInteger: /^[-]?(0|([1-9]\d*))$/,
  integer: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
  mobile: /^1\d{10}$/
}

export function validateByReg(rule, value, callback) {
  if (value === '' || value === null || value === undefined) {
    callback()
  }
  const reg = regs[rule.regName]
  if (!reg.test(value)) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}

// const area = { 11: '北京', 12: '天津', 13: '河北', 14: '山西', 15: '内蒙古', 21: '辽宁', 22: '吉林', 23: '黑龙江', 31: '上海', 32: '江苏', 33: '浙江', 34: '安徽', 35: '福建', 36: '江西', 37: '山东', 41: '河南', 42: '湖北', 43: '湖南', 44: '广东', 45: '广西', 46: '海南', 50: '重庆', 51: '四川', 52: '贵州', 53: '云南', 54: '西藏', 61: '陕西', 62: '甘肃', 63: '青海', 64: '宁夏', 65: '新疆', 71: '台湾', 81: '香港', 82: '澳门', 91: '国外' }

// /* 根据出生日期算出年龄*/
// const getAge = function(strBirthday, date) {
//   var returnAge
//   var strBirthdayArr = strBirthday.split('-')
//   var birthYear = strBirthdayArr[0]
//   var birthMonth = strBirthdayArr[1]
//   var birthDay = strBirthdayArr[2]

//   var d = new Date()
//   var nowYear = d.getFullYear()
//   var nowMonth = d.getMonth() + 1
//   var nowDay = d.getDate()

//   if (date) {
//     var dateParts = date.split('-')
//     nowYear = parseInt(dateParts[0])
//     nowMonth = parseInt(dateParts[1])
//     nowDay = parseInt(dateParts[2])
//   }

//   if (nowYear == birthYear) {
//     returnAge = 0// 同年 则为0岁
//   } else {
//     var ageDiff = nowYear - birthYear // 年之差
//     if (ageDiff > 0) {
//       if (nowMonth == birthMonth) {
//         var dayDiff = nowDay - birthDay// 日之差
//         if (dayDiff < 0) {
//           returnAge = ageDiff - 1
//         } else {
//           returnAge = ageDiff
//         }
//       } else {
//         var monthDiff = nowMonth - birthMonth// 月之差
//         if (monthDiff < 0) {
//           returnAge = ageDiff - 1
//         } else {
//           returnAge = ageDiff
//         }
//       }
//     } else {
//       returnAge = -1// 返回-1 表示出生日期输入错误 晚于今天
//     }
//   }

//   return returnAge// 返回周岁年龄
// }

// const idCheck = {
//   check: function(code) {
//     var city = { 11: '北京', 12: '天津', 13: '河北', 14: '山西', 15: '内蒙古', 21: '辽宁', 22: '吉林', 23: '黑龙江 ', 31: '上海', 32: '江苏', 33: '浙江', 34: '安徽', 35: '福建', 36: '江西', 37: '山东', 41: '河南', 42: '湖北 ', 43: '湖南', 44: '广东', 45: '广西', 46: '海南', 50: '重庆', 51: '四川', 52: '贵州', 53: '云南', 54: '西藏 ', 61: '陕西', 62: '甘肃', 63: '青海', 64: '宁夏', 65: '新疆', 71: '台湾', 81: '香港', 82: '澳门', 91: '国外 ' }
//     var tip = ''
//     var pass = true

//     if (!code || !/^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/i.test(code)) {
//     // if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
//       tip = '身份证号格式错误'
//       pass = false
//     } else if (!city[code.substr(0, 2)]) {
//       tip = '身份证地址编码错误'
//       pass = false
//     } else {
//       // 18位身份证需要验证最后一位校验位
//       if (code.length == 18) {
//         code = code.split('')
//         // ∑(ai×Wi)(mod 11)
//         // 加权因子
//         var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
//         // 校验位
//         var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2]
//         var sum = 0
//         var ai = 0
//         var wi = 0
//         for (var i = 0; i < 17; i++) {
//           ai = code[i]
//           wi = factor[i]
//           sum += ai * wi
//         }
//         var last = parity[sum % 11]
//         if (parity[sum % 11] != code[17]) {
//           tip = '身份证校验位错误'
//           pass = false
//         }
//       }
//     }

//     return pass ? '' : tip
//   },

//   getInfo: function(idcard, date) {
//     var info = {
//       province: area[parseInt(idcard.substr(0, 2))],
//       birthday: idcard.substr(6, 4) + '-' + idcard.substr(10, 2) + '-' + idcard.substr(12, 2),
//       gender: (idcard.substr(16, 1) % 2 ? 'M' : 'F')
//     }

//     info.age = getAge(info.birthday, date)

//     return info
//   },

//   getAge: getAge
// }

// export function validateIdCard(rule, value, callback) {
//   if (value === '' || value === null) {
//     callback()
//   }
//   const idCheck = idCheck.check(value)
//   if (idCheck) {
//     callback(new Error(idCheck))
//   }
// }
