import request from '@/utils/request'

export function fetchSystemNotices(page, searchParam) {
  return request({
    url: '/system/notices',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function fetchUserSystemNotice() {
  return request({
    url: '/common/notices',
    method: 'get'
  })
}

export function fetchSystemNoticeDetail(noticeId) {
  return request({
    url: `/common/notice/${noticeId}`,
    method: 'get'
  })
}

export function fetchNoticeTypes() {
  return request({
    url: '/system/notice/types',
    method: 'get'
  })
}

export function fetchNoticeScopes() {
  return request({
    url: '/system/notice/scopes',
    method: 'get'
  })
}

export function submitSystemNotice(systemNotice) {
  return request({
    url: '/system/notice',
    method: 'post',
    data: {
      data: systemNotice
    }
  })
}

export function deleteSystemNotice(systemNoticeId) {
  return request({
    url: `/system/notice/${systemNoticeId}`,
    method: 'delete'
  })
}

export function fetchOrgInNotice() {
  return request({
    url: '/system/notice/orglist',
    method: 'get'
  })
}
