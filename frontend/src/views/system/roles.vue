<template>
  <div>
    <div v-if="!form_visible" class="app-container">
      <div class="filter-container">
        <el-input v-model="search_param.search" :placeholder="$t('system.Role') + $t('common.name')" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
        <TreeSelect
          v-model="search_param.menuId"
          :props="{ value: 'id', label: 'name', children: 'children', placeholder: $t('system.Menu') }"
          :options="menu_list"
          :clearable="true"
          :accordion="true"
          style="width: 120px"
          class="filter-item"
        />
        <el-select v-model="search_param.permKey" :placeholder="$t('system.Permission')" clearable style="width: 120px" class="filter-item">
          <el-option v-for="item in perm_list" :key="item.cone" :label="item.name" :value="item.code" />
        </el-select>
        <el-button v-waves :disabled="list_loading" class="filter-item" type="primary" icon="el-icon-search" @click="getList">
          {{ this.$t('common.search') }}
        </el-button>
        <el-button-group style="float: right">
          <el-button class="filter-item" style="margin-left: 10px;" :title="$t('common.add')" type="primary" icon="el-icon-plus" @click="handleAdd" />
        </el-button-group>
      </div>

      <el-table v-loading="list_loading" :data="datalist" border fit style="width: 100%;">
        <el-table-column align="center" :label="$t('common.name')" width="220">
          <template slot-scope="scope">
            {{ scope.row.roleName }}
          </template>
        </el-table-column>
        <el-table-column align="header-center" :label="$t('common.desc')">
          <template slot-scope="scope">
            {{ scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column align="center" :label="$t('common.operations')" width="220">
          <template slot-scope="{ row }">
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
    </div>

    <div v-if="form_visible" class="app-container">
      <el-card shadow="never">
        <div slot="header">
          {{ getFormTitle() }}
          <el-button-group style="float:right">
            <el-button size="mini" type="default" :disabled="submitting" @click="hideForm">
              <svg-icon icon-class="cancel" />
              {{ this.$t('common.cancel') }}
            </el-button>
            <el-button size="mini" type="primary" :loading="submitting" @click="handleSubmit">
              <svg-icon icon-class="floppy" />
              {{ this.$t('common.submit') }}
            </el-button>
          </el-button-group>
        </div>
        <el-col :span="6">
          <el-steps direction="vertical" :active="activeStep" style="height: 300px">
            <el-step :title="$t('system.role.basic_info')" :description="$t('system.role.basic_info_hint')" :status="activeStep===0 ? 'finish' : 'wait'">
              <svg-icon slot="icon" icon-class="edit" />
            </el-step>
            <el-step :title="$t('system.Menu')" :description="$t('system.role.menu_hint')" :status="activeStep===1 ? 'finish' : 'wait'">
              <svg-icon slot="icon" icon-class="edit" />
            </el-step>
            <el-step v-if="perm_list.length > 0" :title="$t('system.Permission')" :description="$t('system.role.permission_hint')" :status="activeStep===2 ? 'finish' : 'wait'">
              <svg-icon slot="icon" icon-class="edit" />
            </el-step>
            <el-step v-if="userMngOn && form_status==='edit'" :title="$t('system.User')" :description="$t('system.role.user_hint')" :status="activeStep===3 ? 'finish' : 'wait'">
              <svg-icon slot="icon" icon-class="edit" />
            </el-step>
          </el-steps>
        </el-col>
        <el-col :span="18">
          <el-form ref="dataForm" :model="form_data" label-position="right" label-width="70px" style="min-height: 400px; margin-bottom: 20px" :rules="formRules">
            <el-row>
              <el-collapse v-model="activeNames" accordion>
                <el-collapse-item :title="$t('system.role.basic_info')" name="1">
                  <el-row>
                    <el-col :span="24">
                      <el-form-item :label="$t('common.name')" prop="roleName">
                        <el-input v-model="form_data.roleName" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="24">
                      <el-form-item :label="$t('common.desc')">
                        <el-input v-model="form_data.remark" type="textarea" maxlength="256" show-word-limit />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-collapse-item>
                <el-collapse-item :title="$t('system.Menu')" name="2">
                  <el-tree
                    ref="menu_tree"
                    :data="menu_list"
                    show-checkbox
                    default-expand-all
                    node-key="id"
                    highlight-current
                    :default-checked-keys="form_data.menus"
                  >
                    <span slot-scope="{ data }" class="custom-tree-node">
                      <span>
                        <svg-icon :icon-class="data.icon" />
                        {{ $t( data.name ) }}
                      </span>
                    </span>
                  </el-tree>
                </el-collapse-item>
                <el-collapse-item v-if="perm_list.length > 0" :title="$t('system.Permission')" name="3">
                  <el-table
                    ref="perm_table"
                    :data="perm_list"
                    style="width: 100%"
                    row-key="key"
                    @selection-change="handlePermSelectionChange"
                  >
                    <el-table-column type="selection" width="55" :reserve-selection="true" />
                    <el-table-column :label="$t('common.name')" width="220">
                      <template slot-scope="{ row }">{{ row.name }}</template>
                    </el-table-column>
                    <el-table-column :label="$t('common.desc')">
                      <template slot-scope="{ row }">{{ row.remark }}</template>
                    </el-table-column>
                  </el-table>
                </el-collapse-item>
                <el-collapse-item v-if="userMngOn && form_status==='edit'" :title="$t('system.User')" name="4">
                  <el-row>
                    <div class="filter-container">
                      <el-row :gutter="10">
                        <el-col :span="22">
                          <el-select
                            v-model="user_selections"
                            size="mini"
                            multiple
                            filterable
                            remote
                            :loading="userLoading"
                            :remote-method="getUserOptions"
                            class="filter-item"
                            style="width: 100%"
                          >
                            <el-option
                              v-for="item in userOptions"
                              :key="item.key"
                              :label="item.label"
                              :value="item.key"
                            />
                          </el-select>
                        </el-col>
                        <el-col :span="2">
                          <el-button style="width: 100%" size="mini" type="primary" class="filter-item" title="添加用户" :disabled="user_selections.length === 0" @click="handleRoleUsersSubmit">
                            <svg-icon icon-class="floppy" />
                          </el-button>
                        </el-col>
                      </el-row>
                    </div>
                    <el-table size="small" :data="role_users" border>
                      <el-table-column align="center" prop="sid" label="ID" width="100" />
                      <el-table-column align="center" prop="username" label="用户名" width="120" />
                      <el-table-column align="center" prop="nickname" label="用户名" width="120" />
                      <el-table-column align="center" prop="params.MOBILE" label="手机" show-overflow-tooltip />
                      <el-table-column align="center" prop="params.EMAIL" label="邮箱" show-overflow-tooltip />
                      <el-table-column fixed="right" align="center" label="操作" width="120">
                        <template slot-scope="{row}">
                          <el-button-group>
                            <el-button type="danger" size="mini" @click="showRoleUserDeleteConfirm(row)">
                              <svg-icon icon-class="trash" />
                              {{ $t('common.delete') }}
                            </el-button>
                          </el-button-group>
                        </template>
                      </el-table-column>
                    </el-table>
                    <pagination v-show="role_user_page.totalPage>0" style="margin: 0; padding: 10px 0;" :total="role_user_page.totalRecord" :page.sync="role_user_page.pageNumber" :limit.sync="role_user_page.pageSize" @pagination="getCurrentRoleUsers" />
                  </el-row>
                </el-collapse-item>
              </el-collapse>
            </el-row>
          </el-form>
        </el-col>
      </el-card>
    </div>
  </div>
</template>

<script>
import { fetchRoles, fetchRole, fetchUsers, fetchRoleCandidates, submitRole, submitRoleUsers, deleteRole, deleteRoleUser } from '@/api/system'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import TreeSelect from '@/components/TreeSelect'
import { deepClone } from '@/utils'
import { getPermissions } from '@/utils/permission'
import { getMenus } from '@/utils/menu'
import settings from '@/settings'

export default {
  directives: { waves },
  components: { Pagination, TreeSelect },
  data() {
    return {
      list_loading: false,
      uploadLoading: false,
      downloadLoading: false,
      submitting: false,
      datalist: [],
      search_param: {},
      form_visible: false,
      form_status: 'add',
      formRules: {
        roleName: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }]
      },
      default_form_data: {
        roleId: null,
        roleName: null,
        remark: null,
        menus: [],
        perms: []
      },
      form_data: {},
      perm_list: [],
      perm_selections: [],
      menu_list: [],
      menu_selections: [],
      role_users: [],
      role_user_page: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 0,
        totalRecord: 0
      },
      userOptions: [],
      user_selections: [],
      userLoading: false,
      activeNames: ['1']
    }
  },
  computed: {
    activeStep() {
      return parseInt(this.activeNames[0]) - 1
    },
    userMngOn() {
      return settings.system.role.userMngOn
    }
  },
  async created() {
    await this.getList()
    const perms = await getPermissions()
    this.perm_list = perms
    const menus = await getMenus()
    this.menu_list = menus
  },
  methods: {
    async getList() {
      this.list_loading = true
      const { data } = await fetchRoles(this.search_param)
      this.datalist = data.datalist
      this.list_loading = false
    },
    async getCurrentRoleUsers() {
      const { data } = await fetchUsers(this.role_user_page, { roleId: this.form_data.roleId })
      this.role_users = data.datalist
      this.role_user_page = data.page
    },
    async getUserOptions(search) {
      if (search !== '') {
        this.userLoading = true
        const { data } = await fetchRoleCandidates(this.form_data.roleId, search)
        const userOptions = []
        for (let i = 0; i < data.datalist.length; i++) {
          const user = data.datalist[i]
          userOptions.push({ key: user.sid, label: user.nickname + ' (' + user.username + ')' })
        }
        this.userOptions = userOptions
        this.userLoading = false
      } else {
        this.userOptions = []
      }
    },
    getFormTitle() {
      return this.$t('common.' + this.form_status) + this.$t('system.Role')
    },
    showForm() {
      this.activeNames = ['1']
      this.form_visible = true
      this.$nextTick(() => {
        const perms = this.form_data.perms || []
        for (let i = 0; i < this.perm_list.length; i++) {
          for (let j = 0; j < perms.length; j++) {
            if (this.perm_list[i].code === perms[j]) {
              this.$refs.perm_table.toggleRowSelection(this.perm_list[i], true)
              break
            }
          }
        }
      })
    },
    hideForm() {
      this.form_visible = false
    },
    showDeleteConfirm(role) {
      this.$confirm(
        this.$t('tips.confirm_delete', { name: role.roleName }), this.$t('common.tips'), {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      ).then(async _ => {
        await this.handleDelete(role.roleId)
      }).catch(() => {})
    },
    showRoleUserDeleteConfirm(subscriber) {
      this.$confirm(
        this.$t('tips.confirm_delete', { name: subscriber.nickname }), this.$t('common.tips'), {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      ).then(async _ => {
        await this.handleDeleteRoleUser(subscriber.sid)
      }).catch(() => {})
    },
    handleAdd() {
      this.form_status = 'add'
      this.form_data = {
        roleName: null,
        remark: null,
        perms: [],
        menus: []
      }
      this.showForm()
    },
    async handleEdit(row) {
      this.form_status = 'edit'
      const { data } = await fetchRole(row.roleId)
      this.form_data = data

      this.getCurrentRoleUsers()
      this.showForm()
    },
    handleUpload() {},
    handleDownload() {},
    async handleDelete(roleId) {
      this.list_loading = true
      await deleteRole(roleId)
      this.getList()
    },
    handlePermSelectionChange(selections) {
      const perms = []
      for (let i = 0; i < selections.length; i++) {
        const permission = selections[i]
        perms.push(permission.code)
      }
      this.form_data.perms = perms
      this.perm_selections = selections
    },
    async handleRoleUsersSubmit() {
      this.submitting = true
      const roleId = this.form_data.roleId
      const subscribers = []
      for (let i = 0; i < this.user_selections.length; i++) {
        subscribers.push({ sid: this.user_selections[i] })
      }
      await submitRoleUsers(roleId, subscribers)
      this.user_selections = []
      this.getCurrentRoleUsers()
      this.submitting = false
    },
    async handleDeleteRoleUser(userId) {
      this.submitting = true
      await deleteRoleUser(this.form_data.roleId, userId)
      this.getCurrentRoleUsers()
      this.submitting = false
    },
    async handleSubmit() {
      this.$refs.dataForm.validate(async valid => {
        if (valid) {
          const role = deepClone(this.form_data)
          role.menus = this.$refs.menu_tree.getCheckedKeys()
          role.menus = role.menus.concat(this.$refs.menu_tree.getHalfCheckedKeys())

          this.submitting = true
          await submitRole(role)
          this.submitting = false

          this.$message({
            type: 'success',
            message: this.$t('tips.submit_success', { name: this.$t('system.Role') })
          })
          this.hideForm()
          this.getList()
        } else {
          this.activeNames = ['1']
        }
      })
    }
  }
}
</script>

<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
