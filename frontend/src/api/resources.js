import request from '@/utils/request'

export function getLanguage(locale) {
  return request({
    url: '/common/language',
    method: 'get',
    params: { locale }
  })
}

export function getDicts() {
  return request({
    url: `/common/dicts`,
    method: 'get'
  })
}

export function getDict(dictName) {
  return request({
    url: `/common/dict/${dictName}`,
    method: 'get'
  })
}

export function getParams() {
  return request({
    url: '/common/settings',
    method: 'get'
  })
}

export function getUserOrg() {
  return request({
    url: '/system/org/dep',
    method: 'get'
  })
}
