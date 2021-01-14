<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="searchParam.name" :placeholder="$t('system.menu.name')" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('common.search') }}
      </el-button>
      <el-button-group>
        <el-button v-waves :disabled="!isEdit" class="filter-item" style="margin-left: 10px;" :title="$t('common.add')" type="primary" icon="el-icon-plus" @click="handleAdd" />
      </el-button-group>
    </div>
    <el-table
      ref="menuTree"
      v-loading="listLoading"
      :data="datalist"
      row-key="name"
      border
      fit
      highlight-current-row
      stripe
      size="mini"
      style="width: 100%;"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column :label="$t('system.menu.title')" prop="title" align="left" header-align="center" width="220">
        <template slot-scope="{ row }">
          <svg-icon v-if="row.icon" :icon-class="row.icon" />
          <span v-if="!isEdit">{{ $t(row.title) }}</span>
          <span v-else>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.menu.path')" prop="path" align="left" header-align="center">
        <template slot-scope="{ row }">
          <span>{{ row.path }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.menu.role')" prop="role" align="center" width="160">
        <template slot-scope="{ row }">
          <span>{{ row.role }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.menu.sort')" prop="sort" align="center" width="80">
        <template slot-scope="{ row }">
          <span>{{ row.sort }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.menu.hidden')" prop="hidden" align="center" width="80">
        <template slot-scope="{ row }">
          <el-button v-if="row.hidden" :disabled="!isEdit" type="text" size="mini" @click="handleEditHidden(row)">
            <svg-icon icon-class="eye" />
          </el-button>
          <el-button v-else :disabled="!isEdit" type="text" size="mini" @click="handleEditHidden(row)">
            <svg-icon icon-class="eye-open" />
          </el-button>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" prop="status" align="center" width="80">
        <template slot-scope="{ row }">
          <el-switch
            v-model="row.status"
            :disabled="!isEdit"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleEditStatus(row)"
          />
        </template>
      </el-table-column>
      <el-table-column v-if="isEdit" align="center" :label="$t('common.operations')">
        <template slot-scope="{ row }">
          <el-button-group>
            <el-button type="primary" size="mini" icon="el-icon-plus" @click="handleAddChild(row)">
              {{ $t('system.menu.add') }}
            </el-button>
            <el-button type="primary" size="mini" @click="handleEdit(row)">
              <svg-icon icon-class="edit" />
              {{ $t('common.edit') }}
            </el-button>
            <el-button type="danger" size="mini" @click="handleDelete(row)">
              <svg-icon icon-class="trash" />
              {{ $t('common.delete') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="formVisible" width="60%">
      <div slot="title" class="dialog-header">
        <svg-icon icon-class="peoples" />
        {{ getFormTitle() }}
      </div>
      <el-form ref="dataForm" :rules="formRules" :model="formData" label-position="right" label-width="80px" style="width: 680px; margin-left:50px;">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('system.menu.name')" prop="path">
              <el-input v-model="formData.name" :placeholder="$t('system.menu.name')" type="text" maxlength="100" show-word-limit :disabled="formStatus ==='edit'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('system.menu.title')" prop="name">
              <el-input v-model="formData.title" :placeholder="$t('system.menu.title')" type="text" maxlength="20" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.menu.icon')" prop="icon">
              <icon-select v-model="formData.icon" :placeholder="$t('system.menu.icon')" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('system.menu.path')" prop="path">
              <el-input v-model="formData.path" :placeholder="$t('system.menu.path')" type="text" maxlength="100" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="formStatus !=='edit'">
          <el-col :span="24">
            <el-form-item :label="$t('system.menu.parent')" prop="parent">
              <tree-select
                v-model="formData.parent"
                :props="{ value: 'name', label: 'title', children: 'children' }"
                :options="datalist"
                :clearable="true"
                :accordion="true"
                style="width: 100%"
                :disabled="formParentVisible"
              />
              <!--              <el-cascader-->
              <!--                ref="menuTree"-->
              <!--                v-model="formData.parent"-->
              <!--                :placeholder="$t('system.menu.parent')"-->
              <!--                :disabled="formStatus ==='edit'"-->
              <!--                :options="datalist"-->
              <!--                :props="{ checkStrictly: true, value: 'name', label: 'title', emitPath: false }"-->
              <!--                clearable-->
              <!--                style="width: 100%"-->
              <!--                @change="handleChange"-->
              <!--              />-->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-form-item :label="$t('system.menu.role')" prop="role">
              <el-select v-model="formData.role" multiple :placeholder="$t('system.menu.role')" style="width: 100%">
                <el-option
                  v-for="item in roleOptions"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label="$t('system.menu.sort')" prop="sort">
              <el-input v-model="formData.sort" :placeholder="$t('system.menu.sort')" type="text" maxlength="2" :disabled="formStatus ==='edit'" oninput="value = value.replace(/[^\d]/g,'')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchMenus, fetchRoles, submitMenu } from '@/api/system'
import IconSelect from '@/components/IconSelect'
import TreeSelect from '@/components/TreeSelect'
import waves from '@/directive/waves'
import { deepClone } from '@/utils'
import settings from '@/settings'
import { asyncRoutes } from '@/router'
import Sortable from 'sortablejs'

export default {
  components: { TreeSelect, IconSelect },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const _status = status || false
      const statusMap = {
        published: false,
        deleted: true
      }
      return statusMap[_status]
    }
  },
  data() {
    return {
      listLoading: false,
      uploadLoading: false,
      downloadLoading: false,
      submitLoading: false,
      isEdit: settings.system.menu.mode !== 'frontend',
      datalist: [],
      roleOptions: [],
      page: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 0,
        totalRecord: 0
      },
      searchParam: {},
      formStatus: 'add',
      formVisible: false,
      formParentVisible: false,
      formData: {},
      formRules: {
        name: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        title: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        path: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }]
      }
    }
  },
  computed: {
  },
  created() {
    this.getList()
  },
  mounted() {
    const table = document.querySelector('.el-table__body-wrapper tbody')
    const self = this
    const menu = self.$refs.menuTree
    Sortable.create(table, {
      onEnd({ item, oldIndex, newIndex }) {
        console.log(item)
        console.log(menu)
        // const targetRow = self.datalist.splice(oldIndex, 1)[0]
        // self.datalist.splice(newIndex, 0, targetRow)
      }
    })
  },
  methods: {
    async getList() {
      this.listLoading = true
      if (settings.system.menu.mode === 'frontend') {
        const routes = deepClone([...asyncRoutes])
        const filter = function(routes, path, number) {
          const menus = []
          for (let i = 0; i < routes.length; i++) {
            const obj = routes[i]
            if (obj.isMenu !== undefined && obj.isMenu) {
              const menu = { name: obj.name, title: obj.meta ? obj.meta.title : obj.path, hidden: (obj.hidden || false), status: true }
              if (obj.meta) {
                menu.role = obj.meta.role
                menu.icon = obj.meta.icon
              }
              menu.path = (path || '') + (obj.path.charAt(0) === '/' ? '' : '/') + obj.path
              if (obj.children) {
                menu.children = filter(obj.children, menu.path)
              }
              menus.push(menu)
            }
          }
          return menus
        }
        this.datalist = filter(routes)
      } else {
        const { data } = await fetchMenus()
        this.datalist = data.datalist
      }
      this.listLoading = false
    },
    async getRoles() {
      const { data } = await fetchRoles()
      this.roleOptions = data.datalist
    },
    getFormTitle() {
      return this.$t('common.' + this.formStatus) + this.$t('system.Menu')
    },
    handleFilter() {
      this.getList()
    },
    async handleAdd() {
      this.formStatus = 'add'
      this.formData = {}
      this.formParentVisible = false
      this.showMenusForm()
    },
    async handleEdit(row) {
      this.formStatus = 'edit'
      this.formData = deepClone(row)
      this.formData.parent = row.name
      this.formParentVisible = true
      this.showMenusForm()
    },
    async handleAddChild(row) {
      this.formStatus = 'add'
      this.formData = { parent: row.name }
      this.formParentVisible = true
      this.showMenusForm()
    },
    showMenusForm() {
      this.getRoles()
      this.formVisible = true
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].resetFields()
      }
    },
    handleEditHidden(row) {
      row.hidden = !row.hidden
      submitMenu(row).then(_ => {
      })
    },
    handleEditStatus(row) {
      submitMenu(row).then(_ => {
      })
    },
    handleDelete(row) {
      const name = this.$t('system.Menu') + ' ' + row.title
      this.$confirm(this.$t('tips.confirm_delete', { name }), this.$t('tips.confirm_delete_title'), {
        confirmButtonText: this.$t('tips.confirm_delete_confirm_button_text'),
        cancelButtonText: this.$t('tips.confirm_delete_cancel_button_text'),
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: this.$t('tips.delete_success', { name })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('tips.delete_cancel', { name })
        })
      })
    },
    handleChange() {
      if (this.$refs.menuTree) {
        this.$refs.menuTree.dropDownVisible = false
      }
    },
    handleSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          submitMenu(this.formData).then(_ => {
            this.formVisible = false
          })
        }
      })
    }
  }
}
</script>

<style>
</style>
