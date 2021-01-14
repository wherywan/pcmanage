import Vue from 'vue'
import _ from 'lodash'
import moment from 'moment'

import i18n from './i18n'
import { getLanguage, getDicts, getParams } from './api/resources'
import { getOrgList } from './api/report'
import settings from '@/settings'

export const getLanguageJob = new Promise((resolve, reject) => {
  const locale = i18n.locale
  getLanguage(locale).then((response) => {
    const newPack = _.assign(i18n.messages[locale], { additional: response.data || {}})
    i18n.setLocaleMessage(locale, newPack)
    resolve()
  })
})

export const getDictsJob = new Promise((resolve, reject) => {
  getDicts().then((response) => {
    const dicts = _.assign({}, response.data)
    dicts.getDict = function(dictName) {
      return dicts[dictName] ? dicts[dictName] : []
    }
    dicts.getLabel = function(dictName, key) {
      if (dicts[dictName] && dicts[dictName][key]) {
        return dicts[dictName][key]
      }
      return key
    }
    Object.defineProperty(Vue.prototype, '$dicts', { value: dicts })
    resolve()
  })
})

export const getOrgTree = new Promise((resolve, reject) => {
  getOrgList('SQA').then((response) => {
    const orgTree = response.data
    orgTree.getLabel = function(key) {
      const tmp = orgTree.filter(org => {
        if (org.id === key) {
          return org
        }
      })
      return tmp[0].orgName
    }
    Object.defineProperty(Vue.prototype, '$orgTree', { value: orgTree })
    resolve()
  })
})

export const getSystemParamsJob = new Promise((resolve, reject) => {
  getParams().then((response) => {
    const system_settings = _.assign(settings, response.data)
    Object.defineProperty(Vue.prototype, '$settings', { value: system_settings })
    window._title = response.data['SYSTEM_TITLE']
    resolve()
  })
})

export const getMoment = new Promise((resolve, reject) => {
  moment.locale('zh-CN')
  Object.defineProperty(Vue.prototype, '$moment', { value: moment })
  resolve()
})
