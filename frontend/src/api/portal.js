import request from '@/utils/request'

export function fetchPortalApps(page, searchParam) {
  return request({
    url: '/portal/apps',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function fetchAllPortalApps() {
  return request({
    url: '/portal/app-all',
    method: 'get'
  })
}

export function fetchAppAuthTypes(searchParam) {
  return request({
    url: '/portal/app/authtypes',
    method: 'get'
  })
}

export function submitSubApp(subApp) {
  return request({
    url: '/portal/app/save',
    method: 'post',
    data: {
      data: subApp
    }
  })
}

export function deleteSubApp(subAppId) {
  return request({
    url: `/portal/app/${subAppId}`,
    method: 'delete'
  })
}
