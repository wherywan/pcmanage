import request from '@/utils/request'

export function fetchUsers(page, searchParam) {
  return request({
    url: '/system/subscribers',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function fetchUser(params) {
  return request({
    url: `/system/subscriber`,
    method: 'get',
    params: params
  })
}

export function fetchRoleCandidates(roleId, search) {
  return request({
    url: `/system/role/candidates/${roleId}`,
    method: 'get',
    params: {
      search
    }
  })
}

export function fetchOrgCandidates(orgId) {
  return request({
    url: `/system/org/candidates/${orgId}`,
    method: 'get'
  })
}

export function submitUser(user) {
  return request({
    url: '/system/subscriber/save',
    method: 'post',
    data: {
      data: user
    }
  })
}

export function deleteUser(userId) {
  return request({
    url: `/system/subscriber/${userId}`,
    method: 'delete'
  })
}

export function fetchRoles(searchParam) {
  return request({
    url: '/system/roles',
    method: 'post',
    data: {
      data: searchParam
    }
  })
}

export function fetchRole(roleId) {
  return request({
    url: `/system/role/${roleId}`,
    method: 'get'
  })
}

export function submitRole(role) {
  return request({
    url: '/system/role/save',
    method: 'post',
    data: {
      data: role
    }
  })
}

export function submitRoleUsers(roleId, subscribers) {
  return request({
    url: `/system/role/candidates/${roleId}`,
    method: 'post',
    data: {
      data: subscribers
    }
  })
}

export function deleteRole(roleId) {
  return request({
    url: `/system/role/${roleId}`,
    method: 'delete'
  })
}

export function deleteRoleUser(roleId, userId) {
  return request({
    url: `/system/role/${roleId}/${userId}`,
    method: 'delete'
  })
}

export function fetchOrgTree() {
  return request({
    url: '/system/org-tree',
    method: 'get'
  })
}

export function fetchOrgStatistics(orgId) {
  return request({
    url: `/system/org/statistic-${orgId}`,
    method: 'get'
  })
}

export function fetchOrgUsers(orgId, search) {
  return request({
    url: `/system/org/candidates/${orgId}`,
    method: 'get',
    params: {
      search
    }
  })
}

export function submitOrgUser(orgId, data) {
  return request({
    url: `/system/org/candidates/${orgId}`,
    method: 'post',
    data: {
      data
    }
  })
}

export function deleteOrgUser(orgId, data) {
  return request({
    url: `/system/org/candidates/${orgId}`,
    method: 'delete',
    data: {
      data
    }
  })
}

export function submitOrgTree(data) {
  return request({
    url: '/system/org-tree/save',
    method: 'post',
    data: {
      data
    }
  })
}

export function deleteOrg(id) {
  return request({
    url: `/system/org/${id}`,
    method: 'delete'
  })
}

export function fetchPermissions() {
  return request({
    url: '/system/permissions',
    method: 'get'
  })
}

export function fetchDicts(type) {
  return request({
    url: `/system/dicts/${type}`,
    method: 'get'
  })
}

export function deleteType(type) {
  return request({
    url: `/system/dict/${type}`,
    method: 'delete'
  })
}

export function deleteDict(type, item) {
  return request({
    url: `/system/dict/${type}/${item}`,
    method: 'delete'
  })
}

export function fetchDict(type) {
  return request({
    url: `/system/dict/${type}`,
    method: 'get'
  })
}

export function fetchType(type, search) {
  return request({
    url: `/system/dict/candidates/${type}`,
    method: 'get',
    params: {
      search
    }
  })
}

export function submitDict(data) {
  return request({
    url: '/system/dict',
    method: 'post',
    data: {
      data
    }
  })
}

export function fetchCaches() {
  return request({
    url: '/system/caches',
    method: 'get'
  })
}

export function fetchCacheByName(name) {
  return request({
    url: `/system/caches?name=${name}`,
    method: 'get'
  })
}

export function evictCacheByName(name) {
  return request({
    url: '/system/caches/evict',
    method: 'delete',
    params: {
      cacheName: name
    }
  })
}

export function evictCacheByKey(name, key) {
  return request({
    url: '/system/caches/evict',
    method: 'delete',
    params: {
      cacheName: name,
      cacheKey: key
    }
  })
}

export function fetchUIParams(load) {
  return request({
    url: `/system/params/ui?load=${load}`,
    method: 'get'
  })
}

export function saveUIParam(data) {
  return request({
    url: '/system/param/ui',
    method: 'post',
    data
  })
}

export function fetchParams() {
  return request({
    url: '/system/params',
    method: 'get'
  })
}

export function submitParam(data) {
  return request({
    url: '/system/param',
    method: 'post',
    data: {
      data
    }
  })
}

export function fetchMenus() {
  return request({
    url: '/system/menus',
    method: 'get'
  })
}

export function submitMenu(data) {
  return request({
    url: '/system/menus',
    method: 'post',
    data: { data }
  })
}

export function deleteMenu(id) {
  return request({
    url: `/system/menu/${id}`,
    method: 'delete'
  })
}

export function fetchMenuRoles(id, search) {
  return request({
    url: `/system/menu/candidates/${id}`,
    method: 'get',
    params: {
      search
    }
  })
}

export function submitMenuRole(id, data) {
  return request({
    url: `/system/menu/candidates/${id}`,
    method: 'post',
    data: {
      data
    }
  })
}

export function dropMenuRole(id, roleId) {
  return request({
    url: `/system/menu/${id}/${roleId}`,
    method: 'delete'
  })
}

export function fetchOperations(page, searchParam) {
  return request({
    url: '/system/operation-logs',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function fetchPermission() {
  return request({
    url: '/system/permission',
    method: 'get'
  })
}

export function fetchSendMsg(page, searchParam) {
  return request({
    url: '/system/messages',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function SyncOrg() {
  return request({
    url: '/system/org-sync',
    method: 'post'
  })
}

export function metric(query) {
  return request({
    url: '/system/visualize',
    method: 'post',
    data: {
      data: query
    }
  })
}

export function fetchMessages() {
  return request({
    url: '/system/messages',
    method: 'get'
  })
}

export function fetchMessagesByType(searchParam, page) {
  return request({
    url: '/system/messages/type',
    method: 'post',
    data: {
      data: {
        searchParam,
        page
      }
    }
  })
}

export function saveMessage(Message) {
  return request({
    url: '/system/message',
    method: 'post',
    data: {
      data: Message
    }
  })
}

export function getUserMessagesBySendType(searchParam, page) {
  return request({
    url: '/system/userMessages/type',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function saveUserMessage(userMessage) {
  return request({
    url: '/system/userMessage',
    method: 'post',
    data: {
      data: userMessage
    }
  })
}

export function countUserUnreadMessages() {
  return request({
    url: '/system/userMessages',
    method: 'get'
  })
}

export function fetchSysUsers(page, searchParam) {
  return request({
    url: '/sysCommon/user-list',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function submitSysUser(user) {
  return request({
    url: '/sysCommon/user-save',
    method: 'post',
    data: {
      data: user
    }
  })
}

export function fetchOneUser(id) {
  return request({
    url: `/sysCommon/one-user?id=${id}`,
    method: 'get'
  })
}

