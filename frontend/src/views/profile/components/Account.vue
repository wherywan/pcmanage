<template>
  <el-form ref="dataForm" :rules="form_rules" :model="form_data">
    <el-form-item :label="$t('system.username')" prop="username">
      <el-input v-model.trim="form_data.username" disabled />
    </el-form-item>
    <el-form-item :label="$t('system.nickname')" prop="nickname">
      <el-input v-model.trim="form_data.nickname" disabled />
    </el-form-item>
    <el-form-item :label="$t('system.Org')" prop="params.ORG_NAME">
      <el-input v-model.trim="form_data.params.ORG_NAME" disabled />
    </el-form-item>
    <el-form-item :label="$t('common.mobile')" prop="params.MOBILE">
      <el-input v-model.trim="form_data.params.MOBILE" suffix-icon="el-icon-phone-outline" length="11" />
    </el-form-item>
    <el-form-item :label="$t('common.email')" prop="params.EMAIL">
      <el-input v-model.trim="form_data.params.EMAIL" suffix-icon="el-icon-message" />
    </el-form-item>
    <el-form-item :label="$t('common.birthday')" prop="params.BIRTHDAY">
      <el-date-picker v-model="form_data.params.BIRTHDAY" type="date" :placeholder="$t('common.select')" style="width: 100%" />
    </el-form-item>
    <el-form-item :label="$t('common.sex')" prop="params.SEX">
      <el-select v-model.trim="form_data.params.SEX" clearable style="width: 100%;">
        <el-option v-for="(val, key) in this.$dicts['SEX']" :key="key" :label="val" :value="key" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">{{ $t('common.update') }}</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { saveParams } from '@/api/user'
import { validateByReg } from '@/utils/validate'

export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          sid: '',
          name: '',
          introduction: '',
          roles: [],
          params: {},
          perms: []
        }
      }
    }
  },
  data() {
    return {
      form_data: {
        sid: this.user.sid,
        username: this.user.name,
        nickname: this.user.introduction,
        params: {
          ORG_NAME: this.user.params['ORG_NAME'],
          ORG_ID: this.user.params['ORG_ID'],
          MOBILE: this.user.params['MOBILE'],
          EMAIL: this.user.params['EMAIL'],
          BIRTHDAY: this.user.params['BIRTHDAY'],
          SEX: this.user.params['SEX']
        }
      },
      form_rules: {
        'params.EMAIL': [{ validator: validateByReg, regName: 'email', message: this.$t('tips.is_email'), trigger: 'blur' }],
        'params.MOBILE': [{ validator: validateByReg, regName: 'mobile', message: this.$t('tips.is_mobile'), trigger: 'blur' }]
      }
    }
  },
  methods: {
    submit() {
      this.$refs['dataForm'].validate(async valid => {
        if (valid) {
          await saveParams(this.form_data.params)
          const msg = this.$t('menu.profile.settingsAll')
          this.$message({
            message: this.$t('tips.submit_success', { name: msg }),
            type: 'success'
          })
        }
      })
    }
  }
}
</script>
