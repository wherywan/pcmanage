import Vue from 'vue'
import VueI18n from 'vue-i18n'

let i18n = {}

Vue.use(VueI18n)
i18n = new VueI18n({
  locale: 'zh_CN',
  messages: {
    'zh_CN': require('./config/zh_CN.json'),
    'zh_TW': require('./config/zh_TW.json')
  }
})

export default i18n
