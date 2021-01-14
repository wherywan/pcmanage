<template>
  <div class="app-container">
    <el-tabs v-model="activeName" tab-position="right">
      <el-tab-pane name="tab1">
        <span slot="label"><svg-icon icon-class="nested" /> {{ $t('system.Menu') }}</span>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="never">
              <div slot="header" class="clearfix">
                <el-row :gutter="5">
                  <el-col :span="12">
                    <el-input
                      v-model="filterText"
                      size="mini"
                      prefix-icon="el-icon-search"
                      :placeholder="$t('common.search')+$t('system.Menu')"
                    />
                  </el-col>
                  <!--<el-button-group style="float: right">
                    <el-button v-if="mode !== 'frontend'" size="mini" type="default" :disabled="draggingLocked" @click="handleAdd">
                      <i class="el-icon-plus" />
                    </el-button>
                    <el-button size="mini" type="primary" :disabled="draggingLocked">
                      <svg-icon icon-class="submit" />
                    </el-button>
                    <el-button size="mini" type="danger" :disabled="mode === 'frontend'" @click="toggleDraggingLock">
                      <svg-icon v-if="draggingLocked" icon-class="lock" />
                      <svg-icon v-if="!draggingLocked" icon-class="unlock" />
                    </el-button>
                  </el-button-group>-->
                </el-row>
              </div>
              <el-tree
                ref="menuTree"
                node-key="id"
                :data="menuTree"
                :expand-on-click-node="false"
                :props="menuTreePropMap"
                :default-expanded-keys="defaultExpandedKeys"
                :filter-node-method="filterNode"
                highlight-current
                draggable
                :allow-drop="allowDrop"
                :allow-drag="allowDrag"
                @node-click="handleClick"
                @node-drag-start="handleDragStart"
                @node-drag-enter="handleDragEnter"
                @node-drag-leave="handleDragLeave"
                @node-drag-over="handleDragOver"
                @node-drag-end="handleDragEnd"
                @node-drop="handleDrop"
              >
                <span slot-scope="{ node, data }" class="custom-tree-node">
                  <span><svg-icon :icon-class="data.icon" />&nbsp;{{ $t(node.label) }}</span>
                  <span>
                    <el-button
                      type="text"
                      size="mini"
                      :disabled="draggingLocked"
                      :title="$t('system.menu.add')"
                      @click="(e) => appendMenu(data, e)"
                    >
                      <i class="el-icon-plus" />
                    </el-button>
                    <!--                    <el-button-->
                    <!--                      type="text"-->
                    <!--                      size="mini"-->
                    <!--                      :disabled="draggingLocked"-->
                    <!--                      :title="$t('common.status')"-->
                    <!--                      @click="(e) => editMenu(data, e)"-->
                    <!--                    >-->
                    <!--                      <svg-icon v-if="data.status" icon-class="eye-open" />-->
                    <!--                      <svg-icon v-else icon-class="eye" />-->
                    <!--                    </el-button>-->
                    <el-button
                      type="text"
                      size="mini"
                      :disabled="draggingLocked"
                      :title="$t('common.delete')"
                      @click="(e) => deleteMenu(data, e)"
                    >
                      <svg-icon icon-class="trash" />
                    </el-button>
                  </span>
                </span>
              </el-tree>
            </el-card>
          </el-col>
          <el-col :span="16">
            <el-card shadow="never">
              <div slot="header" class="clearfix">
                <span>{{ $t('system.menu.info') }}：{{ current ? current.title : $t('common.no_data') }}</span>
                <el-button-group style="float: right;">
                  <el-button size="mini" type="primary" :disabled="draggingLocked || !current" @click="handleSubmit">
                    <svg-icon icon-class="submit" />
                    {{ $t('common.submit') }}
                  </el-button>
                </el-button-group>
              </div>
              <no-data v-if="!current" />
              <div v-if="current">
                <el-collapse v-model="activeCName">
                  <el-collapse-item title="菜单信息" name="1">
                    <el-form ref="dataForm" :rules="currentRules" :model="current" label-position="right" label-width="80px">
                      <el-row>
                        <el-col :span="12">
                          <el-form-item :label="$t('system.menu.title')" prop="name">
                            <el-input
                              v-model="current.name"
                              :placeholder="$t('system.menu.title')"
                              type="text"
                              maxlength="20"
                              show-word-limit
                            />
                          </el-form-item>
                        </el-col>
                        <el-col :span="12">
                          <el-form-item :label="$t('system.menu.icon')" prop="icon">
                            <icon-select v-model="current.icon" :placeholder="$t('system.menu.icon')" />
                          </el-form-item>
                        </el-col>
                      </el-row>
                      <el-row>
                        <el-col :span="24">
                          <el-form-item :label="$t('system.menu.path')" prop="url">
                            <el-input
                              v-model="current.url"
                              :placeholder="$t('system.menu.path')"
                              type="text"
                              maxlength="100"
                              show-word-limit
                            />
                          </el-form-item>
                        </el-col>
                      </el-row>
                      <el-row>
                        <el-col :span="24">
                          <el-form-item :label="$t('system.menu.parent')" prop="pid">
                            <tree-select
                              v-model="current.pid"
                              :props="{ value: 'id', label: 'name', children: 'children' }"
                              :options="menuTree"
                              :clearable="true"
                              :accordion="true"
                              style="width: 100%"
                              :disabled="formParentVisible"
                            />
                          </el-form-item>
                        </el-col>
                      </el-row>
                    </el-form>
                  </el-collapse-item>
                  <el-collapse-item title="角色信息" name="2">
                    <el-row>
                      <div class="filter-container">
                        <el-row :gutter="10">
                          <el-col :span="22">
                            <el-select
                              v-model="roleValues"
                              size="mini"
                              multiple
                              filterable
                              remote
                              :disabled="currentStatus !== 'edit'"
                              :placeholder="$t('system.menu.role')"
                              :loading="roleLoading"
                              :remote-method="getRoleOptions"
                              class="filter-item"
                              style="width: 100%"
                            >
                              <el-option
                                v-for="item in roleOptions"
                                :key="item.key"
                                :label="item.label"
                                :value="item.key"
                              />
                            </el-select>
                          </el-col>
                          <el-col :span="2">
                            <el-button
                              style="width: 100%"
                              size="mini"
                              type="primary"
                              class="filter-item"
                              :title="$t('common.add')+$t('system.Role')"
                              :disabled="roleValues.length === 0"
                              @click="addMenuRole"
                            >
                              <svg-icon icon-class="floppy" />
                            </el-button>
                          </el-col>
                        </el-row>
                      </div>
                      <el-table size="small" :data="currentRoles" row-key="sid" border>
                        <el-table-column align="center" prop="roleName" :label="$t('common.name')" />
                        <el-table-column align="center" prop="remark" :label="$t('common.desc')" />
                        <el-table-column v-if="currentStatus === 'edit'" fixed="right" align="center" :label="$t('common.operations')" width="120">
                          <template slot-scope="{ row }">
                            <el-button-group>
                              <el-button type="danger" size="mini" @click="deleteMenuRole(row)">
                                <svg-icon icon-class="trash" />
                                {{ $t('system.cache.evict') }}
                              </el-button>
                            </el-button-group>
                          </template>
                        </el-table-column>
                      </el-table>
                      <!--                      <pagination-->
                      <!--                        v-show="currentRolesPage.totalPage > 0"-->
                      <!--                        style="margin: 0; padding: 10px 0;"-->
                      <!--                        :total="currentRolesPage.totalRecord"-->
                      <!--                        :page.sync="currentRolesPage.pageNumber"-->
                      <!--                        :limit.sync="currentRolesPage.pageSize"-->
                      <!--                        @pagination="getCurrentRoles"-->
                      <!--                      />-->
                    </el-row>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane name="tab2">
        <span slot="label"><svg-icon icon-class="dict" /> {{ $t('system.Permission') }}</span>
        <el-table
          ref="codeTable"
          v-loading="listLoading"
          :data="codeList"
          row-key="name"
          border
          fit
          highlight-current-row
          stripe
          style="width: 100%;"
          default-expand-all
        >
          <el-table-column :label="$t('system.permission.code')" prop="code" align="left" header-align="center" width="220">
            <template slot-scope="{ row }">
              <span>{{ row.code }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('system.permission.name')" prop="name" align="left" header-align="center" width="160">
            <template slot-scope="{ row }">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('system.permission.desc')" prop="remark" align="center">
            <template slot-scope="{ row }">
              <span>{{ row.remark }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import NoData from '@/components/NoData'
import IconSelect from '@/components/IconSelect'
import TreeSelect from '@/components/TreeSelect'
// import Pagination from '@/components/Pagination'
import { deleteMenu, submitMenu, fetchRoles, fetchMenuRoles, submitMenuRole, dropMenuRole } from '@/api/system'
import { deepClone } from '@/utils'
import { getMenus } from '@/utils/menu'
import { getPermissions } from '@/utils/permission'
import settings from '@/settings'

export default {
  name: 'SystemResources',
  components: { NoData, IconSelect, TreeSelect },
  data() {
    return {
      mode: settings.system.menu.mode,
      activeName: 'tab1',
      activeCName: ['1', '2'],
      filterText: '',
      draggingLocked: false,
      menuTree: [],
      treeLoading: false,
      menuTreePropMap: {
        label: 'name',
        children: 'children',
        disabled: function(data, node) {
          return node.data.status
        },
        isLeaf: function(data, node) {
          return node.children && node.children.length > 0
        }
      },
      defaultExpandedKeys: [],
      currentStatus: 'add',
      current: null,
      currentRules: {
        title: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        path: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }]
      },
      currentRoles: [],
      currentRolesPage: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 1,
        totalRecord: 0
      },
      formParentVisible: false,
      roleOptions: [],
      roleValues: [],
      roleLoading: false,
      codeList: [],
      listLoading: false
    }
  },
  watch: {
    filterText(val) {
      this.$refs.menuTree.filter(val)
    }
  },
  created: function() {
    this.getTree()
    this.getCode()
  },
  methods: {
    async getTree() {
      this.treeLoading = true
      const menuTree = await getMenus()
      for (let i = 0; i < menuTree.length; i++) {
        this.defaultExpandedKeys.push(menuTree[i].id)
      }
      this.menuTree = menuTree
      this.treeLoading = false
    },
    async getCode() {
      this.listLoading = true
      this.codeList = await getPermissions()
      this.listLoading = false
    },
    isRootNode: function(node) {
      return node.data.pid === null
    },
    stopPropagation(e) {
      e.stopPropagation()
      e.preventDefault()
    },
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },
    handleAdd() {
      this.currentStatus = 'add'
      this.current = {}
      this.formParentVisible = false
    },
    async handleSubmit() {
      this.treeLoading = true
      const menu = this.current
      const { data } = await submitMenu(new Array(menu))
      for (let i = 0; i < data.length; i++) {
        this.defaultExpandedKeys.push(data[i].id)
      }
      this.menuTree = data
      this.treeLoading = false
    },
    toggleDraggingLock() {
      this.draggingLocked = !this.draggingLocked
    },
    allowDrag(draggingNode) {
      return !this.draggingLocked
    },
    allowDrop(draggingNode, dropNode, type) {
      return true
    },
    handleClick(data, node) {
      this.currentStatus = 'edit'
      this.current = deepClone(data)
      this.current.pid = this.isRootNode(node) ? null : node.parent.data.id
      this.formParentVisible = true
      this.currentRoles = []

      this.getCurrentRoles()
    },
    handleDragStart(node, ev) {
      console.log('drag start', node)
    },
    handleDragEnter(draggingNode, dropNode) {
      console.log('tree drag enter: ', dropNode.label)
    },
    handleDragLeave(draggingNode, dropNode) {
      console.log('tree drag leave: ', dropNode.label)
    },
    handleDragOver(draggingNode, dropNode) {
      console.log('tree drag over: ', dropNode.label)
    },
    handleDragEnd(draggingNode, dropNode, dropType) {
      console.log('tree drag end: ', dropNode && dropNode.label, dropType)
    },
    handleDrop(draggingNode, dropNode, dropType) {
      console.log('tree drop: ', dropNode, dropType)
    },
    appendMenu(data, e) {
      this.currentStatus = 'add'
      this.current = {}
      this.current.pid = data.id
      this.formParentVisible = true
      this.currentRoles = []

      this.stopPropagation(e)
    },
    editMenu(data, e) {
      this.$set(data, 'status', !data.status)
      this.stopPropagation(e)
    },
    deleteMenu(data, e) {
      const name = this.$t('system.Menu') + ' ' + data.name
      this.$confirm(this.$t('tips.confirm_delete', { name }), this.$t('tips.confirm_delete_title'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        deleteMenu(data.id).then(response => {
          this.menuTree = response.data
          this.$message({
            type: 'success',
            message: this.$t('tips.delete_success', { name })
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('tips.delete_cancel', { name })
        })
      })
      this.stopPropagation(e)
    },
    async getRoleOptions(query) {
      if (query !== '') {
        this.roleLoading = true
        const { data } = await fetchMenuRoles(this.current.id, query)
        const roleOptions = []
        for (let i = 0; i < data.datalist.length; i++) {
          const role = data.datalist[i]
          roleOptions.push({ key: role.roleId, label: role.roleName })
        }
        this.roleOptions = roleOptions
        this.roleLoading = false
      } else {
        this.roleOptions = []
      }
    },
    async getCurrentRoles() {
      this.roleLoading = true
      const { data } = await fetchRoles({ menuId: this.current.id })
      this.currentRoles = data.datalist
    },
    addMenuRole() {
      const data = this.roleValues.map(value => ({ roleId: value }))
      submitMenuRole(this.current.id, data).then(_ => {
        this.getCurrentRoles()
      })
    },
    deleteMenuRole(row) {
      dropMenuRole(this.current.id, row.roleId).then(_ => {
        this.getCurrentRoles()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 82px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    //box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border: 1px solid rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 5px 0 0 5px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 38px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 14px 14px 14px 0;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 18px;
      }
    }
  }
</style>
