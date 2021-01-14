<template>
  <div class="app-container" style="min-height: 500px">
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
                  :placeholder="$t('common.search')+$t('system.Org')"
                />
              </el-col>
              <el-button-group style="float: right">
                <el-button v-if="$settings.system.org.synchronize" :loading="syncLoading" size="mini" @click="toggleSync">
                  <svg-icon icon-class="cached" />
                </el-button>
                <el-button size="mini" type="primary" :disabled="draggingLocked">
                  <svg-icon icon-class="submit" />
                </el-button>
                <el-button size="mini" type="danger" :disabled="$settings.system.org.synchronize" @click="toggleDraggingLock">
                  <svg-icon v-if="draggingLocked" icon-class="lock" />
                  <svg-icon v-if="!draggingLocked" icon-class="unlock" />
                </el-button>
              </el-button-group>
            </el-row>
          </div>
          <el-tree
            ref="orgTree"
            node-key="id"
            :data="orgTree"
            :expand-on-click-node="false"
            :props="orgTreePropMap"
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
              <span><svg-icon icon-class="org" />&nbsp;{{ node.label }}</span>
              <span>
                <el-button
                  type="text"
                  size="mini"
                  :disabled="draggingLocked"
                  :title="$t('system.org.addChild')"
                  @click="(e) => addChildOrg(data, e)"
                >
                  <i class="el-icon-plus" />
                </el-button>
                <el-button
                  type="text"
                  size="mini"
                  :disabled="draggingLocked"
                  :title="$t('common.delete')"
                  @click="(e) => removeOrg(data, e)"
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
            <span>机构详情：{{ currentOrg ? currentOrg.orgName : '暂无数据' }}</span>
            <el-button-group style="float: right;">
              <el-button size="mini" type="primary" :disabled="draggingLocked || !currentOrg" @click="handleSubmit">
                <svg-icon icon-class="submit" />
                {{ $t('common.submit') }}
              </el-button>
            </el-button-group>
          </div>
          <NoData v-if="!currentOrg" />
          <div v-if="currentOrg">
            <el-row :gutter="10">
              <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
                <div class="card-panel">
                  <div class="card-panel-icon-wrapper icon-people">
                    <svg-icon icon-class="peoples" class-name="card-panel-icon" />
                  </div>
                  <div class="card-panel-description">
                    <div class="card-panel-text">
                      直属人数
                    </div>
                    <count-to :start-val="0" :end-val="currentOrgStatistics.userCount || 0" :duration="2600" class="card-panel-num" />
                  </div>
                </div>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
                <div class="card-panel">
                  <div class="card-panel-icon-wrapper icon-message">
                    <svg-icon icon-class="peoples" class-name="card-panel-icon" />
                  </div>
                  <div class="card-panel-description">
                    <div class="card-panel-text">
                      子机构人数
                    </div>
                    <count-to :start-val="0" :end-val="currentOrgStatistics.subOrgUserCount || 0" :duration="2600" class="card-panel-num" />
                  </div>
                </div>
              </el-col>
              <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
                <div class="card-panel">
                  <div class="card-panel-icon-wrapper icon-money">
                    <svg-icon icon-class="org" class-name="card-panel-icon" />
                  </div>
                  <div class="card-panel-description">
                    <div class="card-panel-text">
                      子机构数
                    </div>
                    <count-to :start-val="0" :end-val="currentOrgStatistics.subOrgCount || 0" :duration="2600" class="card-panel-num" />
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-collapse v-model="activeNames" accordion>
              <el-collapse-item title="机构信息" name="1">
                <el-form label-position="right" label-width="70px">
                  <el-row>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('system.org.orgName')" prop="orgName">
                        <el-input v-model="currentOrg.orgName" type="text" maxlength="30" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('system.org.orgId')" prop="orgCode">
                        <el-input v-model="currentOrg.orgCode" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row v-if="mode === 'full'">
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('system.org.orgShortName')">
                        <el-input v-model="currentOrg.orgNameShort" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('common.type')">
                        <el-select v-model="currentOrg.orgType" clearable style="width: 100%">
                          <el-option v-for="(val, key) in this.$dicts['ORG_TYPE']" :key="key" :label="val" :value="key" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <!-- <el-col :span="12">
                      <el-form-item size="small" :label="$t('system.org.lawer')">
                        <el-input v-model="currentOrg.lawer" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('system.org.regAddr')">
                        <el-input v-model="currentOrg.regAddr" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col> -->
                    <el-col :span="24">
                      <el-form-item size="small" :label="$t('common.address')">
                        <el-input v-model="currentOrg.officeAddr" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('common.zipcode')">
                        <el-input v-model="currentOrg.officeZip" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('common.tel')">
                        <el-input v-model="currentOrg.officeTel" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item size="small" :label="$t('common.fax')">
                        <el-input v-model="currentOrg.officeFax" type="text" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                    <el-col :span="24">
                      <el-form-item size="small" :label="$t('common.desc')">
                        <el-input v-model="currentOrg.desc" type="textarea" maxlength="20" show-word-limit />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </el-collapse-item>
              <el-collapse-item v-if="userMngOn" title="机构成员" name="2">
                <el-row>
                  <div class="filter-container">
                    <el-row :gutter="10">
                      <el-col :span="22">
                        <el-select
                          v-model="userValues"
                          size="mini"
                          multiple
                          filterable
                          remote
                          :disabled="currentOrgStatus !== 'edit'"
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
                        <el-button
                          style="width: 100%"
                          size="mini"
                          type="primary"
                          class="filter-item"
                          :title="$t('common.add')+$t('system.User')"
                          :disabled="userValues.length === 0"
                          @click="addOrgUser"
                        >
                          <svg-icon icon-class="floppy" />
                        </el-button>
                      </el-col>
                    </el-row>
                  </div>
                  <el-table size="small" :data="currentOrgUsers" border>
                    <el-table-column align="center" prop="sid" :label="$t('common.sid')" width="80" />
                    <el-table-column align="center" prop="nickname" :label="$t('system.nickname')" width="100" />
                    <el-table-column align="center" prop="username" :label="$t('system.username')" />
                    <el-table-column align="center" prop="params.MOBILE" :label="$t('common.mobile')" show-overflow-tooltip />
                    <el-table-column align="center" prop="params.EMAIL" :label="$t('common.email')" show-overflow-tooltip />
                    <el-table-column v-if="currentOrgStatus === 'edit'" fixed="right" align="center" :label="$t('common.operations')" width="120">
                      <template slot-scope="{ row }">
                        <el-button-group>
                          <el-button type="danger" size="mini" @click="deleteOrgUser(row)">
                            <svg-icon icon-class="trash" />
                            {{ $t('system.cache.evict') }}
                          </el-button>
                        </el-button-group>
                      </template>
                    </el-table-column>
                  </el-table>
                  <pagination
                    v-show="currentOrgUserPage.totalPage>0"
                    style="margin: 0; padding: 10px 0;"
                    :total="currentOrgUserPage.totalRecord"
                    :page.sync="currentOrgUserPage.pageNumber"
                    :limit.sync="currentOrgUserPage.pageSize"
                    @pagination="getCurrentOrgUsers"
                  />
                </el-row>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import NoData from '@/components/NoData'
import CountTo from 'vue-count-to'
import Pagination from '@/components/Pagination'
import { fetchOrgTree, fetchOrgStatistics, fetchUsers, fetchOrgUsers, submitOrgTree, deleteOrg, submitOrgUser, deleteOrgUser, SyncOrg } from '@/api/system'
import { deepClone } from '@/utils'
import settings from '@/settings'

export default {
  name: 'SystemOrgs',
  components: { NoData, CountTo, Pagination },
  data() {
    return {
      mode: settings.system.org.mode,
      userMngOn: settings.system.org.userMngOn,
      userOptions: [],
      userValues: [],
      userLoading: false,
      activeNames: ['1'],
      draggingLocked: true,
      syncLoading: false,
      filterText: '',
      orgTree: [],
      orgTreePropMap: {
        label: this.$settings.system.org.showName === 'full' ? 'orgName' : 'orgNameShort',
        children: 'children',
        disabled: function(data, node) {
          return true
        },
        isLeaf: function(data, node) {
          return node.children && node.children.length > 0
        }
      },
      defaultExpandedKeys: [],
      currentOrgStatus: '',
      currentOrg: null,
      currentOrgStatistics: {},
      currentOrgUsers: [],
      currentOrgUserPage: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 1,
        totalRecord: 0
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.orgTree.filter(val)
    }
  },
  created: function() {
    this.getTree()
  },
  methods: {
    async getTree() {
      fetchOrgTree().then(response => {
        this.getOrgTree(response)
      })
    },
    async getCurrentOrgStatistics() {
      const { data } = await fetchOrgStatistics(this.currentOrg.id)
      this.currentOrgStatistics = data
    },
    async getCurrentOrgUsers() {
      const { data } = await fetchUsers(this.currentOrgUserPage, { orgId: this.currentOrg.id })
      this.currentOrgUsers = data.datalist
      this.currentOrgUserPage = data.page
    },
    async getUserOptions(query) {
      if (query !== '') {
        this.userLoading = true
        const { data } = await fetchOrgUsers(this.currentOrg.id, query)
        const userOptions = []
        for (let i = 0; i < data.datalist.length; i++) {
          const user = data.datalist[i]
          userOptions.push({ key: user.sid, label: user.username + '(' + user.nickname + ')' })
        }
        this.userOptions = userOptions
        this.userLoading = false
      } else {
        this.userOptions = []
      }
    },
    async toggleSync() {
      this.syncLoading = true
      SyncOrg().then(_ => {
        this.getTree()
        this.syncLoading = false
      }).catch(_ => {
        this.syncLoading = false
      })
    },
    getOrgTree: function(response) {
      const orgTree = response.data
      for (let i = 0; i < orgTree.length; i++) {
        this.defaultExpandedKeys.push(orgTree[i].id)
      }
      this.orgTree = orgTree
    },
    stopPropagation(e) {
      e.stopPropagation()
      e.preventDefault()
    },
    filterNode: function(value, data) {
      if (!value) return true
      return data.orgName.indexOf(value) !== -1
    },
    isRootNode: function(node) {
      return node.data.id === null
    },
    handleClick: function(data, node) {
      this.currentOrgStatus = 'edit'
      this.currentOrgStatistics = []
      this.currentOrgUsers = []
      this.userValues = []
      this.currentOrgUserPage.pageNumber = 1
      this.currentOrg = deepClone(data)
      this.currentOrg.pid = this.isRootNode(node) ? null : node.parent.data.id

      this.getCurrentOrgStatistics()
      this.getCurrentOrgUsers()
    },
    addChildOrg: function(data, e) {
      this.currentOrgStatus = 'add'
      this.currentOrgStatistics = []
      this.currentOrgUsers = []
      this.userValues = []
      this.currentOrgUserPage.pageNumber = 1
      this.currentOrg = {}
      this.currentOrg.pid = data.id
      this.stopPropagation(e)
    },
    removeOrg: function(data, e) {
      const name = this.$t('system.Org') + ' ' + data.orgName
      this.$confirm(this.$t('tips.confirm_delete', { name }), this.$t('tips.confirm_delete_title'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        deleteOrg(data.id).then(response => {
          this.getOrgTree(response)
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
    addOrgUser() {
      const list = this.userValues.map(value => ({ sid: value }))
      submitOrgUser(this.currentOrg.id, list).then(_ => {
        this.getCurrentOrgUsers()
      })
    },
    deleteOrgUser(data) {
      deleteOrgUser(this.currentOrg.id, new Array(data)).then(_ => {
        this.getCurrentOrgUsers()
      })
    },
    handleDragStart(node, ev) {
      console.log('drag start', node)
    },
    handleDragEnter(draggingNode, dropNode, ev) {
      console.log('tree drag enter: ', dropNode.label)
    },
    handleDragLeave(draggingNode, dropNode, ev) {
      console.log('tree drag leave: ', dropNode.label)
    },
    handleDragOver(draggingNode, dropNode, ev) {
      console.log('tree drag over: ', dropNode.label)
    },
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      console.log('tree drag end: ', dropNode && dropNode.label, dropType)
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log('tree drop: ', dropNode.label, dropType)
    },
    allowDrop(draggingNode, dropNode, type) {
      return true
    },
    allowDrag(draggingNode) {
      return !this.draggingLocked
    },
    toggleDraggingLock() {
      this.draggingLocked = !this.draggingLocked
    },
    async handleSubmit() {
      const org = this.currentOrg
      submitOrgTree(new Array(org)).then(response => {
        this.getOrgTree(response)
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
