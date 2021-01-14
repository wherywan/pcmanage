<template>
  <el-form ref="dataForm" :rules="form_rules" :model="form_data">
    <div
      v-if="Object.keys(form_data).length === 0"
      class="el-table__empty-block"
    >
      <span class="el-table__empty-text">
        <slot name="empty">{{ $t('common.no_data') }}</slot>
      </span>
    </div>
    <el-form-item v-if="Object.keys(form_data).length !== 0">
      <el-button type="primary" @click="submit">{{ $t('common.update') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          sid: '',
          name: '',
          roles: [],
          params: {},
          perms: []
        }
      }
    }
  },
  data() {
    return {
      form_data: {},
      form_rules: {}
    }
  },
  methods: {
    submit() {
      const msg = this.$t('menu.form_data.settingsApp')
      this.$message({
        message: this.$t('tips.submit_success', { name: msg }),
        type: 'success'
      })
    }
  }
}
</script>
