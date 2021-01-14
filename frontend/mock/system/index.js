import Mock from 'mockjs'

function sleep(milliSeconds) {
  const startTime = new Date().getTime()
  while (new Date().getTime() < startTime + milliSeconds) {
    console.log(milliSeconds)
  }
}

const generateUsersData = function(page, searchParam) {
  const finalPage = page || {
    pageNumber: 1,
    pageSize: 20
  }
  return Mock.mock({
    'datalist|10': [{
      'sid|+1': (finalPage.pageNumber - 1) * finalPage.pageSize + 1,
      'username': /\w{5,10}/,
      'nickname': /\w{5,10}/,
      'source': 'LDAP',
      'params': {
        'EMAIL': '@email',
        'MOBILE': /\d{11}/,
        'FULL_NAME': '@cname',
        'LAST_NAME': '@FULL_NAME',
        'FIRST_NAME': ''
      },
      'roles': ['sys-user'],
      'orgId': '1-1-2'
    }],
    'page': {
      pageNumber: finalPage.pageNumber,
      pageSize: finalPage.pageSize,
      totalRecord: 11,
      totalPage: 2
    }
  })
}

const roles = [
  {
    roleId: 'role1',
    roleName: '测试角色1',
    remark: '测试角色说明1',
    menus: null,
    perms: null
  },
  {
    roleId: 'role2',
    roleName: '测试角色2',
    remark: '测试角色说明2',
    menus: null,
    perms: null
  }
]

const org_tree = [{
  id: '1',
  pid: null,
  orgCode: 'org-1',
  orgName: '一级机构1',
  orgNameShort: '机构1',
  children: [{
    id: '1-1',
    pid: null,
    orgCode: 'org-1-1',
    orgName: '二级机构1-1',
    orgNameShort: '机构1-1',
    children: [{
      id: '1-1-1',
      pid: null,
      orgCode: 'org-1-1-1',
      orgName: '三级机构1-1-1',
      orgNameShort: '机构1-1-1'
    }, {
      id: '1-1-2',
      pid: null,
      orgCode: 'org-1-1-2',
      orgName: '三级机构1-1-2',
      orgNameShort: '机构1-1-2'
    }]
  }, {
    id: '1-2',
    pid: null,
    orgCode: 'org-1-2',
    orgName: '二级机构1-2',
    orgNameShort: '机构1-2',
    children: [{
      id: '1-2-1',
      pid: null,
      orgCode: 'org-1-2-1',
      orgName: '三级机构1-2-1',
      orgNameShort: '机构1-2-1'
    }, {
      id: '1-2-2',
      pid: null,
      orgCode: 'org-1-2-2',
      orgName: '三级机构1-2-2',
      orgNameShort: '机构1-2-2'
    }]
  }]
}]

const menus = [
  {
    id: 'system',
    pid: null,
    name: '系统管理',
    status: true,
    icon: 'cog-fill',
    url: '/system',
    children: [
      {
        id: 'system-users',
        pid: 'system',
        name: '用户管理',
        status: true,
        icon: 'peoples',
        url: '/system/users'
      }, {
        id: 'system-roles',
        pid: 'system',
        name: '角色管理',
        status: true,
        icon: 'role',
        url: '/system/roles'
      }, {
        id: 'system-orgs',
        pid: 'system',
        name: '机构管理',
        status: true,
        icon: 'org',
        url: '/system/orgs'
      }, {
        id: 'system-menus',
        pid: 'system',
        name: '菜单管理',
        status: true,
        icon: 'nested',
        url: '/system/menus'
      }, {
        id: 'system-discs',
        pid: 'system',
        name: '字典管理',
        status: true,
        icon: 'dict',
        url: '/system/dicts'
      }, {
        id: 'system-caches',
        pid: 'system',
        name: '缓存管理',
        status: true,
        icon: 'chip',
        url: '/system/caches'
      }, {
        id: 'system-params',
        pid: 'system',
        name: '系统参数管理',
        status: true,
        icon: 'params',
        url: '/system/params'
      }, {
        id: 'system-operations',
        pid: 'system',
        name: '操作日志',
        status: true,
        icon: 'log',
        url: '/system/operations'
      }, {
        id: 'system-logs',
        pid: 'system',
        name: '系统日志',
        status: true,
        icon: 'console',
        url: '/system/logs'
      }
    ]
  }
]

const perms = [{
  key: 'bk-perm-1',
  name: '权限1',
  desc: '权限1说明'
}, {
  key: 'bk-perm-2',
  name: '权限2',
  desc: '权限2说明'
}]

const generateCache = function(name, size) {
  return Mock.mock({
    'name': name,
    'size': size,
    'content|12': [{
      'name': name,
      '_key': /\w{5,10}/,
      'key': name + '-@_key',
      'val': /\w{5,10}/
    }]
  })
}

const generateCacheData = function() {
  const data = []
  data.push(generateCache('user', 50))
  data.push(generateCache('login', 12))
  data.push(generateCache('captcha', 20))
  return data
}

const generateDicts = function() {
  const names = Mock.mock({
    'names|50': [{
      'name': /[A-Z]{5,15}/
    }]
  }).names
  const dicts = {}
  for (let i = 0; i < names.length; i++) {
    const name = names[i].name
    const items = Mock.mock({
      'items|4': [{
        'key': /[a-z0-9]{2,5}/,
        'label': /[a-z0-9]{5,15}/
      }]
    }).items
    dicts[name] = {}
    for (let j = 0; j < items.length; j++) {
      const item = items[j]
      dicts[name][item.key] = item.label
    }
  }
  return dicts
}

const generatePermission = function(searchParam) {
  return Mock.mock({
    'datalist|10': [{
      'code': '@domain',
      'name': /\w{5,10}/,
      'desc': '@csentence(10)'
    }]
  })
}

const generateOperations = function(searchParam, page) {
  const finalPage = page || {
    pageNumber: 1,
    pageSize: 20
  }
  return Mock.mock({
    'datalist|10': [{
      'id|+1': (finalPage.pageNumber - 1) * finalPage.pageSize + 1,
      'datetime': '@datetime',
      'userId': '@domain',
      'username': /\w{5,10}/,
      'title': '@csentence(10)',
      'content': '@ctitle(100)',
      'ip': '@ip'
    }],
    'page': {
      pageNumber: finalPage.pageNumber,
      pageSize: finalPage.pageSize,
      totalRecord: 11,
      totalPage: 2
    }
  })
}

const generateOperationLogs = function(searchParam, page) {
  const finalPage = page || {
    pageNumber: 1,
    pageSize: 20
  }
  return Mock.mock({
    'datalist|10': [{
      'id|+1': (finalPage.pageNumber - 1) * finalPage.pageSize + 1,
      'clientIp': '',
      'sid': '',
      'orgId': '',
      'method': '',
      'url': '',
      'operBgnTs': '',
      'operEndTs': '',
      'req': '',
      'res': '',
      'funcName': '',
      'funcDesc': ''
    }],
    'page': {
      pageNumber: finalPage.pageNumber,
      pageSize: finalPage.pageSize,
      totalRecord: 11,
      totalPage: 2
    }
  })
}

const generateMessages = function(searchParams, page) {
  const finalPage = page || {
    pageNumber: 1,
    pageSize: 10
  }
  return Mock.mock({
    'datalist|10': [{
      'id|+1': (finalPage.pageNumber - 1) * finalPage.pageSize + 1,
      'title': '@csentence(10)',
      'content': '@csentence(100)',
      'timestamp': '@datetime',
      'sendType|1': ['all', 'org', 'role', 'user'],
      'target': '',
      'targetName|1-20': [
        {
          'id': '@id',
          'name|+1': [
            '南京分行',
            '苏州分行',
            '徐州分行'
          ]
        }
      ],
      'validDate': '@datetime'
    }],
    'page': {
      pageNumber: finalPage.pageNumber,
      pageSize: finalPage.pageSize,
      totalRecord: 11,
      totalPage: 2
    }
  })
}

export default [
  {
    url: '/system/subscribers',
    type: 'post',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: generateUsersData(request_data.page, request_data.searchParam)
      }
    }
  },
  {
    url: '/system/subscribers',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: generateUsersData()
      }
    }
  },
  {
    url: '/system/subscriber',
    type: 'post',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: null,
        msg: null
      }
    }
  },
  {
    url: '/system/roles',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {
          datalist: roles
        }
      }
    }
  },
  {
    url: '/system/roles',
    type: 'post',
    response: _ => {
      const newRoles = roles.concat(_.body.data)
      sleep(2000)
      return {
        flag: 0,
        data: {
          datalist: newRoles
        },
        msg: null
      }
    }
  },
  {
    url: '/system/role',
    type: 'post',
    response: _ => {
      sleep(2000)
      return {
        flag: 0,
        data: null,
        msg: null
      }
    }
  },
  {
    url: '/system/permissions',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {
          datalist: perms
        }
      }
    }
  },
  {
    url: '/system/caches',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {
          datalist: generateCacheData()
        }
      }
    }
  },
  {
    url: '/system/cache',
    type: 'get',
    response: _ => {
      const name = _.query.name
      return {
        flag: 0,
        data: generateCache(name, 10)
      }
    }
  },
  {
    url: '/system/cache-evict',
    type: 'post',
    response: _ => {
      return {
        flag: 0,
        data: null
      }
    }
  },
  {
    url: '/system/org-tree',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: org_tree
      }
    }
  },
  {
    url: '/system/org-tree/save',
    type: 'post',
    response: _ => {
      return {
        flag: 0,
        data: org_tree
      }
    }
  },
  {
    url: '/system/org/[A-Za-z0-9]',
    type: 'delete',
    response: _ => {
      return {
        flag: 0,
        data: org_tree
      }
    }
  },
  {
    url: '/system/org/statistic-[A-Za-z0-9]',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: Mock.mock({
          'userCount|20-50': 1,
          'subOrgUserCount|50-300': 1,
          'subOrgCount|0-12': 1
        })
      }
    }
  },
  {
    url: '/system/org/candidates/[A-Za-z0-9]',
    type: 'get',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: generateUsersData(request_data.page, request_data.orgId)
      }
    }
  },
  {
    url: '/system/org/user',
    type: 'delete',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: null,
        msg: null
      }
    }
  },
  {
    url: '/system/dicts',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: generateDicts(),
        msg: null
      }
    }
  },
  {
    url: '/system/params/ui',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {
          datalist: [
            {
              'key': 'GITLAB_SAMPLE_PROJECTS',
              'name': 'GITLAB_SAMPLE_PROJECTS',
              'desc': 'Gitlab 样例项目（项目ID，字符串数组）',
              'defaultValue': null,
              'value': null
            },
            {
              'key': 'APP_TITLE',
              'name': '应用名称',
              'desc': '应用名称',
              'defaultValue': 'Application Name',
              'value': '持续集成与服务治理平台'
            },
            {
              'key': 'APP_DESC',
              'name': '应用描述',
              'desc': '应用描述',
              'defaultValue': 'Application description. ',
              'value': 'Application description. '
            },
            {
              'key': 'APP_VERSION',
              'name': '应用版本',
              'desc': '应用版本号',
              'defaultValue': 'v-.-.-',
              'value': 'v1.0.0-beta'
            },
            {
              'key': 'ORGANIZATION_NAME',
              'name': '组织名称',
              'desc': '应用所属组织名称',
              'defaultValue': 'ORG',
              'value': 'EBStudio'
            },
            {
              'key': 'DEFAULT_PAGE_SIZE',
              'name': '默认分页大小',
              'desc': '分页查询时默认分页容量',
              'defaultValue': '10',
              'value': 10
            }
          ],
          page: null
        },
        msg: null
      }
    }
  },
  {
    url: '/system/param/ui',
    type: 'post',
    response: _ => {
      return {
        flag: 0,
        msg: null,
        data: null
      }
    }
  },
  {
    url: '/system/params',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: {
          datalist: [
            {
              'key': 'GITLAB_SAMPLE_PROJECTS',
              'name': 'GITLAB_SAMPLE_PROJECTS',
              'desc': 'Gitlab 样例项目（项目ID，字符串数组）',
              'defaultValue': null,
              'value': null
            },
            {
              'key': 'APP_TITLE',
              'name': '应用名称',
              'desc': '应用名称',
              'defaultValue': 'Application Name',
              'value': '持续集成与服务治理平台'
            },
            {
              'key': 'APP_DESC',
              'name': '应用描述',
              'desc': '应用描述',
              'defaultValue': 'Application description. ',
              'value': 'Application description. '
            },
            {
              'key': 'APP_VERSION',
              'name': '应用版本',
              'desc': '应用版本号',
              'defaultValue': 'v-.-.-',
              'value': 'v1.0.0-beta'
            },
            {
              'key': 'ORGANIZATION_NAME',
              'name': '组织名称',
              'desc': '应用所属组织名称',
              'defaultValue': 'ORG',
              'value': 'EBStudio'
            },
            {
              'key': 'DEFAULT_PAGE_SIZE',
              'name': '默认分页大小',
              'desc': '分页查询时默认分页容量',
              'defaultValue': '10',
              'value': 10
            }
          ],
          page: null
        },
        msg: null
      }
    }
  },
  {
    url: '/system/param',
    type: 'post',
    response: _ => {
      return {
        flag: 0,
        msg: null,
        data: null
      }
    }
  },
  {
    url: '/system/menus',
    type: 'get',
    response: _ => {
      return {
        flag: 0,
        data: menus
      }
    }
  },
  {
    url: '/system/menus',
    type: 'post',
    response: _ => {
      return {
        flag: 0,
        data: {},
        msg: null
      }
    }
  },
  {
    url: '/system/menu',
    type: 'delete',
    response: _ => {
      return {
        flag: 0,
        data: menus
      }
    }
  },
  {
    url: '/system/schedules',
    type: 'post',
    response: _ => {
      return {
        flag: 0,
        data: {
          datalist: [{
            id: 'TEST_ID_SCHEDULE_1',
            runner: 'com.enbrau.system.runners.LogCleaner',
            remark: '清理应用日志（保留天数见系统参数配置项 KEEP_DAYS_OF_LOG）',
            cron: '0 15 10 ? * *',
            status: 'ACTIVE'
          }, {
            id: 'TEST_ID_SCHEDULE_2',
            runner: 'com.enbrau.system.runners.LogCleaner',
            remark: '清理应用日志（保留天数见系统参数配置项 KEEP_DAYS_OF_LOG）',
            cron: '0 15 10 ? * *',
            status: 'STOPPED'
          }, {
            id: 'TEST_ID_SCHEDULE_3',
            runner: 'com.enbrau.system.runners.LogCleaner',
            remark: '清理应用日志（保留天数见系统参数配置项 KEEP_DAYS_OF_LOG）',
            cron: '0 15 10 ? * *',
            status: 'RUNNING'
          }]
        },
        msg: null
      }
    }
  },
  {
    url: '/system/operations',
    type: 'get',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: generateOperations(request_data.id, request_data.page),
        msg: null
      }
    }
  },
  {
    url: '/system/permission',
    type: 'get',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: generatePermission(request_data.id),
        msg: null
      }
    }
  },
  {
    url: '/system/operation-logs',
    type: 'post',
    response: _ => {
      const request_data = _.body
      return {
        flag: 0,
        data: generateOperationLogs(request_data.id, request_data.page),
        msg: null
      }
    }
  },
  {
    url: '/system/messages',
    type: 'post',
    response: _ => {
      const request_data = _.body.data
      return {
        flag: 0,
        data: generateMessages(request_data.id, request_data.page),
        msg: null
      }
    }
  }
]
