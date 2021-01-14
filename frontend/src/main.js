import Vue from 'vue'
import i18n from './i18n'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log

import * as filters from './filters' // global filters

import lodash from 'lodash'

import wl from 'wl-vue-select'
import 'wl-vue-select/lib/wl-vue-select.css'
Vue.use(wl)

Object.defineProperty(Vue.prototype, '$lodash', { value: lodash })

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */
// import { mockXHR } from '../mock'
import settings from './settings'

let token = null

if (process.env.NODE_ENV === 'development') {
  // Mock Token
  if (settings.mockTokenOn) {
    token = settings.mockToken

    switch (settings.loginStrategy) {
      case '03':
        window.sessionStorage.setItem(settings.jwtSessionKey, token)
        break
      case '04':
        document.cookie = `${settings.jwtCookieKey}=${token}; path=/`
        break
    }
  }

  // mockXHR()
}

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

import { getLanguageJob, getDictsJob, getOrgTree, getSystemParamsJob, getMoment } from './initialJobs'

Promise.all([getLanguageJob, getDictsJob, getOrgTree, getSystemParamsJob, getMoment]).then(() => {
  new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: h => h(App)
  })
}).catch(error => {
  console.log(error)
})

