import request from '@/utils/request'
import { upload } from '@/utils/file-request'

export function getOrgList(data) {
  return request({
    url: `/sysCommon/org-tree?busiScene=${data}`,
    method: 'get'
  })
}

export function deleteReport(id) {
  return request({
    url: `/report/delete?id=${id}`,
    method: 'get'
  })
}

export function fetchReports(page) {
  return request({
    url: '/report/list',
    method: 'post',
    data: {
      data: {
        page
      }
    }
  })
}

export function fetchPersonalDeposits(page, searchParam) {
  return request({
    url: '/personal-deposit/list',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}
export function fetchSameBusiPersonalDeposits(page, searchParam) {
  return request({
    url: '/sameBusi-personal-deposit/list',
    method: 'post',
    data: {
      data: {
        page,
        searchParam
      }
    }
  })
}

export function fetchFillForms(page) {
  return request({
    url: '/fillform/list',
    method: 'post',
    data: {
      data: {
        page
      }
    }
  })
}

export function uploadFile(data) {
  return upload({
    url: '/attach/upload',
    data: data
  })
}

export function uploadFillFile(data) {
  return upload({
    url: '/fillform/upload-form',
    data: data
  })
}

export function batUploadFiles(data) {
  return upload({
    url: '/source-files/upload',
    data: data
  })
}

export function reportCommit(data) {
  return request({
    url: '/report/commit',
    method: 'post',
    data: data
  })
}
