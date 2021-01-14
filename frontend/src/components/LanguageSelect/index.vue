<template>
  <el-dropdown trigger="click" @command="handleLanguageChange">
    <div>
      <svg-icon class-name="language-icon" icon-class="language" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item v-for="item of languageOptions" :key="item.value" :disabled=" language===item.value" :command="item.value">
        {{ item.label }}
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import i18n from '../../i18n'
import { getLanguage } from '../../api/resources'

export default {
  data() {
    const languageOptions = []

    for (const key in i18n.messages) {
      languageOptions.push({ label: i18n.messages[key].language.name, value: key })
    }
    return {
      languageOptions: languageOptions
    }
  },
  computed: {
    language() {
      return this.$i18n.locale
    }
  },
  methods: {
    handleLanguageChange(locale) {
      this.$i18n.locale = locale
      getLanguage(locale).then(response => {
        const newPack = this.$lodash.assign(this.$i18n.messages[locale], { additional: response.data })
        this.$i18n.setLocaleMessage(locale, newPack)
        this.refreshView()
        this.$message({
          message: this.$t('tips.language_changed_to', { name: this.$t('language.name') }),
          type: 'success'
        })
      })
    },
    refreshView() {
      // In order to make the cached page re-rendered
      this.$store.dispatch('tagsView/delAllCachedViews', this.$route)

      const { fullPath } = this.$route

      this.$nextTick(() => {
        this.$router.replace({
          path: '/redirect' + fullPath
        })
      })
    }
  }

}
</script>
