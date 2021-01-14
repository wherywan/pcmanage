import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data: { data }
  })
}

export function getInfo() {
  return request({
    url: '/profile/info',
    method: 'get'
  })
}

export function saveParams(user) {
  return request({
    url: '/profile/params/save',
    method: 'post',
    data: {
      data: user
    }
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}
