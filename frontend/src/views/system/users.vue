<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="search_param.search" :placeholder="$t('system.username') + '/' + $t('system.User') + 'ID'" style="width: 240px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <TreeSelect
        v-model="search_param.orgId"
        :props="{ value: 'id', label: 'orgName', children: 'children', placeholder: $t('system.Org') }"
        :options="orgTree"
        :clearable="true"
        :accordion="true"
        style="width: 120px"
        :disabled="showNewRoles"
        class="filter-item"
      />
      <el-select v-model="search_param.roleId" :placeholder="$t('system.Role')" clearable style="width: 120px" class="filter-item">
        <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId" />
      </el-select>
      <el-button v-waves :disabled="list_loading" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('common.search') }}
      </el-button>
      <el-button-group style="float: right;">
        <el-button v-waves class="filter-item" style="margin-left: 10px;" :title="$t('common.add')" type="primary" icon="el-icon-plus" @click="handleAdd" />
      </el-button-group>
    </div>
    <el-table
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column :label="$t('system.fullname')" prop="username" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.sid')" prop="sid" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.sid }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.username')" prop="username" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.userCode }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.userType')" prop="userType" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ $dicts.getLabel('SYS_USER_TYPE', row.userType) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.userStatus')" prop="userStatus" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ $dicts.getLabel('SYS_USER_STATUS', row.userStatus) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.mobile')" prop="mobile" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.mobile }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.businessLine')" prop="businessLine" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ $dicts.getLabel('DEP', row.depId) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operations')" width="220" fixed="right" align="center">
        <template slot-scope="{row}">
          <el-button-group>
            <el-button :disabled="list_loading" type="primary" size="mini" @click="handleEdit(row)">
              <svg-icon icon-class="edit" />
              {{ $t('common.edit') }}
            </el-button>
            <el-button :disabled="list_loading" type="danger" size="mini" @click="showDeleteConfirm(row)">
              <svg-icon icon-class="trash" />
              {{ $t('common.delete') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.totalPage>0" :total="page.totalRecord" :page.sync="page.pageNumber" :limit.sync="page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getList" />

    <el-dialog :visible.sync="form_visible" width="60%">
      <div slot="title">
        <svg-icon icon-class="peoples" />
        {{ getFormTitle() }}
      </div>
      <div style="float: left; width: 100%;">
        <el-form ref="dataForm" :rules="formRules" :model="form_data" label-position="right" label-width="70px">
          <el-col v-if="$settings.system.user.enableCustomId" :span="12">
            <el-form-item :label="$t('system.sid')" prop="sid">
              <el-input v-model="form_data.sid" :placeholder="$t('system.sid')" type="text" maxlength="20" show-word-limit :disabled="showNewRoles || formStatus ==='edit'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.username')" prop="userCode">
              <el-input v-model="form_data.userCode" :placeholder="$t('system.username')" type="text" maxlength="20" show-word-limit :disabled="showNewRoles || formStatus ==='edit'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.fullname')" prop="username">
              <el-input v-model="form_data.username" :placeholder="$t('system.fullname')" type="text" maxlength="10" show-word-limit :disabled="showNewRoles" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common.mobile')" prop="mobile">
              <el-input v-model="form_data.mobile" :placeholder="$t('common.mobile')" suffix-icon="el-icon-phone-outline" :disabled="showNewRoles" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.Org')">
              <TreeSelect
                v-model="form_data.orgId"
                :props="{ value: 'id', label: 'orgName', children: 'children' }"
                :options="orgTree"
                :clearable="true"
                :accordion="true"
                style="width: 100%"
                :disabled="showNewRoles"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.depId')">
              <el-select v-model="form_data.depId" filterable style="width: 100%">
                <el-option
                  v-for="(name, key) in $dicts.getDict('DEP')"
                  :key="key"
                  :label="name"
                  :value="key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('system.Role')">
              <el-col :span="24">
                <el-select v-model="form_data.roles" style="width: 100%" :disabled="showNewRoles">
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                  />
                </el-select>
              </el-col>
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="form_visible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" :loading="submitLoading" :disabled="showNewRoles" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchRoles, fetchOrgTree, submitRoles, deleteUser, fetchSysUsers, submitSysUser, fetchOneUser } from '@/api/system'
import { validateByReg } from '@/utils/validate'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import TreeSelect from '@/components/TreeSelect'
import { deepClone, getDefaultPage } from '@/utils'

export default {
  name: 'SystemUserList',
  components: { Pagination, TreeSelect },
  directives: { waves },
  data() {
    return {
      list_loading: false,
      uploadLoading: false,
      downloadLoading: false,
      submitLoading: false,
      datalist: [],
      page: getDefaultPage(),
      search_param: {},
      sourceOptions: ['DB'],
      roleOptions: [],
      newRoles: [],
      showNewRoles: false,
      formStatus: 'add',
      form_visible: false,
      form_data: {
        sid: null,
        userCode: null,
        username: null,
        mobile: null,
        orgId: null,
        depId: null,
        roles: ''
      },
      formRules: {
        sid: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }, { validator: this.validateSid, message: this.$t('tips.sid_exists'), trigger: 'blur' }],
        userCode: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }, { validator: this.validateUsername, message: this.$t('tips.username_exists'), trigger: 'blur' }],
        username: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        mobile: [{ validator: validateByReg, regName: 'mobile', message: this.$t('tips.is_mobile'), trigger: 'blur' }]
      },
      orgTree: []
    }
  },
  async created() {
    await this.getList()
    await this.getOrgTree()
    await this.getRoles()
  },
  methods: {
    async getList() {
      this.list_loading = true
      const { data } = await fetchSysUsers(this.page, this.search_param)
      this.datalist = data.datalist
      this.page = data.page
      this.list_loading = false
    },
    async getOrgTree() {
      const { data } = await fetchOrgTree()
      this.orgTree = data
    },
    async getRoles() {
      const { data } = await fetchRoles()
      this.roleOptions = data.datalist
    },
    async validateSid(rule, value, callback) {
      if (this.formStatus === 'edit') {
        callback()
        return
      }
      const { data } = await fetchOneUser(value)
      if (data !== null) {
        callback(new Error(rule.message))
      } else {
        callback()
      }
    },
    async validateUsername(rule, value, callback) {
      if (this.formStatus === 'edit') {
        callback()
        return
      }
      const { data } = await fetchOneUser(value)
      if (data !== null) {
        callback(new Error(rule.message))
      } else {
        callback()
      }
    },
    getFormTitle() {
      return this.$t('common.' + this.formStatus) + this.$t('system.subscriber')
    },
    // setFullName() {
    //   this.form_data.params.FULL_NAME = (this.form_data.params.LAST_NAME || '') + (this.form_data.params.FIRST_NAME || '')
    // },
    // showNewRolesForm() {
    //   this.newRoles.push({})
    //   this.showNewRoles = true
    // },
    hideNewRolesForm() {
      this.newRoles = []
      this.showNewRoles = false
    },
    // deleteNewRole(index) {
    //   const newRoles = deepClone(this.newRoles)
    //   this.$lodash.pullAt(newRoles, index)
    //   this.newRoles = newRoles
    // },
    handleFilter() {
      this.page.pageNumber = 1
      this.getList()
    },
    handleUpload() {},
    handleDownload() {},
    async handleEdit(row) {
      this.formStatus = 'edit'
      const form_data = deepClone(row)
      const { data } = await fetchRoles({ userId: form_data.sid })
      if (data && data.datalist && data.datalist.length > 0) {
        form_data.roles = data.datalist[0].roleId
      }
      this.form_data = form_data
      this.showUserForm()
    },
    async handleAdd() {
      this.formStatus = 'add'
      this.form_data = { params: {}, roles: '', orgId: null }
      this.showUserForm()
    },
    async showUserForm() {
      this.showNewRoles = false
      this.newRoles = []

      this.form_visible = true
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].resetFields()
      }
      this.submitLoading = false
    },
    hideUserForm() {
      this.form_visible = false
    },
    showDeleteConfirm(user) {
      this.$confirm(
        this.$t('tips.confirm_delete', { name: user.nickname }), this.$t('common.tips'), {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      ).then(async _ => {
        await this.handleDelete(user.sid)
      }).catch(() => {})
    },
    async handleDelete(userId) {
      this.list_loading = true
      await deleteUser(userId)
      this.getList()
    },
    async handleSubmit() {
      this.$refs['dataForm'].validate(async valid => {
        if (valid) {
          this.submitLoading = true
          const data = deepClone(this.form_data)
          data.userType = '01'
          data.userStatus = '01'
          await submitSysUser(data)
          this.submitLoading = false
          this.hideUserForm()
          this.getList()
        }
      })
    },
    handleRoleSubmit() {
      submitRoles(this.newRoles).then(response => {
        if (response.data && response.data.datalist) {
          this.roleOptions = response.data.datalist
          for (let i = 0; i < this.newRoles.length; i++) {
            this.form_data.roles.push(this.newRoles[i].code)
          }
        }
        this.hideNewRolesForm()
      })
    }
  }
}
</script>
