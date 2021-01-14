import settings from '../settings'
import Cookies from 'js-cookie'

export function getToken() {
  switch (settings.loginStrategy) {
    case '00':
      return null
    case '01':
      // TODO
      break
    case '02':
      return window.sessionStorage.getItem(settings.jwtSessionKey)
    case '03':
      return window.sessionStorage.getItem(settings.jwtSessionKey)
    case '04':
      return Cookies.get(settings.jwtCookieKey)
    case '99':
      // CUSTOM LOGIN STRAGEGY
  }
}

export function setToken(token) {
  switch (settings.loginStrategy) {
    case '00':
      return null
    case '01':
      // TODO
      break
    case '02':
      return window.sessionStorage.setItem(settings.jwtSessionKey, token)
    case '03':
      return window.sessionStorage.setItem(settings.jwtSessionKey, token)
    case '04':
      return Cookies.set(settings.jwtCookieKey, token)
    case '99':
      // CUSTOM LOGIN STRAGEGY
  }
}

export function removeToken() {
  switch (settings.loginStrategy) {
    case '00':
      return null
    case '01':
      // TODO
      break
    case '02':
      return window.sessionStorage.removeItem(settings.jwtSessionKey)
    case '03':
      return window.sessionStorage.removeItem(settings.jwtSessionKey)
    case '04':
      return Cookies.remove(settings.jwtCookieKey)
    case '99':
      // CUSTOM LOGIN STRAGEGY
  }
}
