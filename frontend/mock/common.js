const locales = {
  'zh_CN': {
    'common': {
      'search': '查询'
    }
  },
  'zh_TW': {
    'common': {
      'search': '查詢'
    }
  }
}

const dicts = {
  'REQUEST_RESULT': {
    'SUCCESS': '成功',
    'FAIL': '失败'
  },
  'SCHEDULE_STATUS': {
    'OFF': '关闭',
    'ON': '开启',
    'RUNNING': '正在运行'
  },
  'COUNTRY.CN': {
    'SH': '上海市',
    'JS': '江苏省'
  },
  'SYS_CODE_TYPE': {
    'COUNTRY.CN.SH.310100': '上海市辖区',
    'COUNTRY': '主体地区',
    'COUNTRY.CN': '中国',
    'COUNTRY.CN.JS.320200': '无锡市',
    'COUNTRY.CN.JS.320100': '南京市',
    'COUNTRY.CN.JS': '江苏省',
    'COUNTRY.CN.SH': '上海市'
  },
  'SUBSCRIBER_SOURCE': {
    'DB': 'DB',
    'LDAP': 'LDAP'
  },
  'COUNTRY.CN.JS.320200': {
    '320201': '无锡市辖区',
    '320205': '锡山区',
    '320206': '惠山区',
    '320211': '滨湖区',
    '320213': '梁溪区',
    '320214': '新吴区',
    '320281': '江阴市',
    '320282': '宜兴市'
  },
  'COUNTRY.CN.JS': {
    '320100': '南京市',
    '320200': '无锡市'
  },
  'COUNTRY.CN.JS.320100': {
    '320101': '南京市辖区',
    '320102': '玄武区',
    '320104': '秦淮区',
    '320105': '建邺区',
    '320106': '鼓楼区',
    '320111': '浦口区',
    '320113': '栖霞区',
    '320114': '雨花台区',
    '320115': '江宁区',
    '320116': '六合区',
    '320117': '溧水区',
    '320118': '高淳区'
  },
  'RESOURCE_TYPE': {
    'M': '菜单',
    'P': '权限'
  },
  'MSG_SEND_TYPE': {
    'ALL': '所有人',
    'ORG': '机构',
    'ROLE': '角色',
    'USER': '用户'
  },
  'ORG_TYPE': {
    'O': '机构',
    'D': '部门',
    'V': '虚拟'
  },
  'COUNTRY.CN.SH.310100': {
    '310101': '黄浦区',
    '310104': '徐汇区',
    '310105': '长宁区',
    '310106': '静安区',
    '310107': '普陀区',
    '310109': '虹口区',
    '310110': '杨浦区',
    '310112': '闵行区',
    '310113': '宝山区',
    '310114': '嘉定区',
    '310115': '浦东新区',
    '310116': '金山区',
    '310117': '松江区',
    '310118': '青浦区',
    '310120': '奉贤区',
    '310151': '崇明区'
  },
  'ORG_STATUS': {
    'PREPARE': '筹建',
    'BUSINESS': '运营',
    'TERMINATE': '终止',
    'TRY': '试营业',
    'FAIL': '已失效'
  },
  'COUNTRY': {
    'CN': '中国'
  },
  'SCHEDULE_RESULT': {
    'SUCCESS': '成功',
    'FAIL': '失败'
  },
  'COUNTRY.CN.SH': {
    '310100': '上海市辖区',
    '310200': '上海市辖县'
  }
}

export default [
  {
    url: '/common/language',
    type: 'get',
    response: config => {
      const { locale } = config.query

      return {
        flag: 0,
        data: locales[locale],
        msg: null
      }
    }
  },
  {
    url: '/common/dicts',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: dicts,
        msg: null
      }
    }
  },
  {
    url: '/common/dict',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {},
        msg: null
      }
    }
  },
  {
    url: '/common/settings',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {},
        msg: null
      }
    }
  }
]
