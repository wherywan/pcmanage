<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select
        v-model="search_param.noticeType"
        :placeholder="$t('system.notice.noticeType')"
        clearable
        style="width: 200px"
        class="filter-item"
      >
        <el-option
          v-for="item in noticeTypeOptions"
          :key="item.key"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-input
        v-model="search_param.key"
        :placeholder="$t('system.notice.titleOrContent')"
        style="width: 300px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="search_param.publishTime"
        :placeholder="$t('system.notice.publishTime')"
        style="width: 200px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button
        v-waves
        :disabled="list_loading"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        {{ $t('common.search') }}
      </el-button>
      <el-button-group style="float: right">
        <el-button
          v-waves
          class="filter-item"
          style="margin-left: 10px"
          :title="$t('common.add')"
          type="primary"
          icon="el-icon-plus"
          :disabled="list_loading"
          @click="handleAdd"
        />
        <el-button
          v-waves
          :loading="uploadLoading"
          class="filter-item"
          :title="$t('common.import')"
          type="primary"
          icon="el-icon-upload2"
          disabled
          @click="handleUpload"
        />
        <el-button
          v-waves
          :loading="downloadLoading"
          class="filter-item"
          :title="$t('common.export')"
          type="primary"
          icon="el-icon-download"
          disabled
          @click="handleDownload"
        />
      </el-button-group>
    </div>
    <el-table
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      highlight-current-row
      stripe
      style="width: 100%"
    >
      <el-table-column
        :label="$t('system.notice.title')"
        prop="title"
        align="center"
        width="260"
      >
        <template slot-scope="{ row }">
          <span>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.notice.noticeType')"
        prop="noticeType"
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <span
            v-if="noticeTypeMap[row.noticeType] !== undefined"
            :style="{color: noticeTypeMap[row.noticeType].color,'font-weight':550}"
          >{{ noticeTypeMap[row.noticeType].text }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.notice.isVisible')"
        prop="isVisible"
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <span>{{ isVisibleMap[row.isVisible] }}</span>
        </template>
      </el-table-column>
      <!--el-table-column
        :label="$t('system.notice.content')"
        align="center"
        width="200"
      >
        <template slot-scope="{ row }">
          <el-popover
            placement="right"
            :title="$t('system.notice.common.noticeContentDetail')"
            trigger="click"
            @show="getDetail(row.id)"
          >
            <div v-loading="detail_loading" v-html="form_data.content"></div>
            <div slot="reference" class="name-wrapper" style="padding:10px">
              <el-badge is-dot class="item">
                <el-tag type="info">{{
                  $t('system.notice.common.showByClick')
                }}</el-tag>
              </el-badge>
            </div>
          </el-popover>
        </template>
      </el-table-column-->
      <el-table-column
        :label="$t('system.notice.createdBy')"
        prop="createdBy"
        align="center"
        width="200"
      >
        <template slot-scope="{ row }">
          <span>{{ row.createdBy }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.notice.publishTime')"
        prop="publishTime"
        align="center"
      >
        <template slot-scope="{ row }">
          <span>{{ row.publishTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('common.operations')"
        width="220"
        fixed="right"
        align="center"
      >
        <template slot-scope="{ row }">
          <el-button-group>
            <el-button
              :disabled="list_loading"
              type="primary"
              size="mini"
              @click="handleEdit(row.id)"
            >
              <svg-icon icon-class="edit" />
              {{ $t('common.edit') }}
            </el-button>
            <el-button
              :disabled="list_loading"
              type="danger"
              size="mini"
              @click="showDeleteConfirm(row)"
            >
              <svg-icon icon-class="trash" />
              {{ $t('common.delete') }}
            </el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="page.totalPage > 0"
      :total="page.totalRecord"
      :page.sync="page.pageNumber"
      :limit.sync="page.pageSize"
      style="padding:0;margin-top:20px"
      @pagination="getList"
    />

    <el-dialog :visible.sync="form_visible" width="60%">
      <div slot="title">
        <svg-icon icon-class="Bell_ringing" />
        {{ getFormTitle() }}
      </div>
      <div v-loading="detail_loading" style="float: left;width: 100%">
        <el-form
          ref="dataForm"
          :rules="formRules"
          :model="form_data"
          label-position="right"
          label-width="80px"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item :label="$t('system.notice.title')" prop="title">
                <el-input
                  v-model="form_data.title"
                  :placeholder="$t('system.notice.title')"
                  type="text"
                  maxlength="50"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="7">
              <el-form-item
                :label="$t('system.notice.noticeType')"
                prop="noticeType"
              >
                <el-select
                  v-model="form_data.noticeType"
                  :placeholder="$t('system.notice.noticeType')"
                  clearable
                  class="filter-item"
                >
                  <el-option
                    v-for="item in noticeTypeOptions"
                    :key="item.key"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>

            <el-col :span="6">
              <el-form-item
                :label="$t('system.notice.isVisible')"
                prop="isVisible"
              >
                <el-select
                  v-model="form_data.isVisible"
                  :placeholder="$t('system.notice.noticeType')"
                  clearable
                  class="filter-item"
                >
                  <el-option label="是" value="Y" />
                  <el-option label="否" value="N" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>

            <el-col :span="7">
              <el-form-item
                :label="$t('system.notice.publishTime')"
                prop="publishTime"
              >
                <el-date-picker
                  v-model="form_data.publishTime"
                  :placeholder="$t('system.notice.publishTime')"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="7">
              <el-form-item :label="$t('system.notice.scope')" prop="scope">
                <el-select
                  v-model="form_data.scope"
                  :placeholder="$t('system.notice.scope')"
                  clearable
                  class="filter-item"
                  @change="noticeScopeChanged()"
                >
                  <el-option
                    v-for="item in noticeScopeOptions"
                    :key="item.key"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="16">
              <el-form-item :label="$t('system.notice.targetId')" prop="targetId">
                <el-select
                  v-if="
                    form_data.scope === undefined ||
                      form_data.scope === null ||
                      form_data.scope === '' ||
                      form_data.scope === 'all'
                  "
                  key="1"
                  v-model="form_data.scope"
                  :placeholder="$t('system.notice.targetId')"
                  clearable
                  class="filter-item"
                  style="width:100%"
                  disabled
                />
                <el-select
                  v-if="form_data.scope === 'role'"
                  key="2"
                  v-model="form_data.targetId_role"
                  multiple
                  value-key="roleId"
                  :placeholder="$t('system.Role')"
                  clearable
                  class="filter-item"
                  style="width:100%"
                  @change="removeChange()"
                >
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                  />
                </el-select>
                <el-select
                  v-if="form_data.scope === 'org'"
                  key="4"
                  v-model="form_data.targetId_org"
                  multiple
                  value-key="key"
                  filterable
                  remote
                  :loading="orgLoading"
                  :remote-method="getOrgOptions"
                  :placeholder="$t('system.Org')"
                  class="filter-item"
                  style="width: 100%"
                  @change="removeChange()"
                >
                  <el-option
                    v-for="item in orgOptions"
                    :key="item.key"
                    :label="item.label"
                    :value="item.key"
                  />
                </el-select>
                <el-select
                  v-if="form_data.scope === 'user'"
                  key="4"
                  v-model="form_data.targetId_user"
                  multiple
                  value-key="key"
                  filterable
                  remote
                  :loading="userLoading"
                  :remote-method="getUserOptions"
                  :placeholder="$t('system.User')"
                  class="filter-item"
                  style="width: 100%"
                  @change="removeChange()"
                >
                  <el-option
                    v-for="item in userOptions"
                    :key="item.key"
                    :label="item.label"
                    :value="item.key"
                  />
                </el-select>
                <el-select
                  v-if="form_data.scope === 'app'"
                  key="5"
                  v-model="form_data.targetId_app"
                  :placeholder="$t('portal.subApplication.subApp')"
                  multiple
                  value-key="id"
                  clearable
                  class="filter-item"
                  style="width:100%"
                  @change="removeChange()"
                >
                  <el-option
                    v-for="item in portalAppOptions"
                    :key="item.id"
                    :label="item.appName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-col :span="24">
            <el-form-item :label="$t('system.notice.content')" prop="content">
              <markdown-editor
                ref="markdownEditor"
                v-model="form_data.content"
                :language="language"
                height="200px"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="form_visible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button
          type="primary"
          :disabled="detail_loading"
          :loading="submitLoading"
          @click="handleSubmit"
        >
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import MarkdownEditor from '@/components/MarkdownEditor'
import clipboard from '@/directive/clipboard/index.js'
import {
  fetchSystemNotices,
  fetchSystemNoticeDetail,
  fetchNoticeTypes,
  fetchNoticeScopes,
  submitSystemNotice,
  deleteSystemNotice,
  fetchOrgInNotice
} from '@/api/system-notices'
import {
  fetchRoles,
  // fetchOrgTree,
  fetchRoleCandidates
} from '@/api/system'
import {
  fetchAllPortalApps
} from '@/api/portal'
// import IconSelect from '@/components/IconSelect'
// import TreeSelect from '@/components/MultiTreeSelect'
// import { validateByReg } from '@/utils/validate'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { deepClone } from '@/utils'

export default {
  name: 'SystemNoticeManagement',
  components: { Pagination, MarkdownEditor },
  // components: { Pagination, IconSelect, TreeSelect, MarkdownEditor },
  directives: { waves, clipboard },
  data() {
    return {
      list_loading: false,
      detail_loading: false,
      uploadLoading: false,
      downloadLoading: false,
      submitLoading: false,
      datalist: [],
      page: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 0,
        totalRecord: 0
      },
      search_param: {},
      noticeTypeOptions: [],
      noticeTypeMap: {},
      noticeScopeOptions: [],
      orgListArray: [],
      noticeScopeMap: {},
      isVisibleMap: { Y: '是', N: '否' },
      roleOptions: [],
      // orgTree: [],
      userLoading: false,
      userOptions: [],
      orgOptions: [],
      orgLoading: false,
      portalAppOptions: [],
      showNewRoles: false,
      formStatus: 'add',
      form_visible: false,
      form_data: {},
      formRules: {
        title: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ],
        content: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ],
        noticeType: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ],
        isVisible: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ],
        publishTime: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ],
        scope: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ],
        targetId: [
          {
            required: true,
            message: this.$t('tips.is_required'),
            trigger: 'blur'
          }
        ]
      },
      html: '',
      languageTypeList: {
        en: 'en_US',
        zh: 'zh_CN',
        es: 'es_ES'
      }
    }
  },
  computed: {
    language() {
      return this.languageTypeList['zh']
    }
  },
  async created() {
    // console.log(this.getCurrentTime());
    await this.getList()
    await this.getNoticeTypes()
    await this.getNoticeScopes()
    await this.getRoles()
    // await this.getOrgTree()
    await this.getPortalApps()
    await this.getOrgsInNotice()
  },
  mounted() {
  },
  methods: {
    getCurrentTime() {
      const date = new Date()
      const Y = date.getFullYear()
      const M = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)
      const D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
      const h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
      const m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
      const s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
      // this.form_data.publishTime = '2020-03-24 12:00:00'
      return Y + '-' + M + '-' + D + ' ' + h + ':' + m + ':' + s
    },
    async getList() {
      this.list_loading = true
      const { data } = await fetchSystemNotices(this.page, this.search_param)
      this.datalist = data.datalist
      this.page = data.page
      this.list_loading = false
    },
    async getNoticeTypes() {
      this.noticeTypeOptions = []
      const { data } = await fetchNoticeTypes()
      this.noticeTypeMap = data
      let i = 1
      for (const key in data) {
        this.noticeTypeOptions.push({ key: i++, value: key, label: data[key].text })
      }
    },
    async getNoticeScopes() {
      this.noticeScopeOptions = []
      const { data } = await fetchNoticeScopes()
      this.noticeScopeMap = data
      let i = 1
      for (const key in data) {
        this.noticeScopeOptions.push({ key: i++, value: key, label: data[key] })
      }
    },
    async getOrgsInNotice() {
      this.orgListArray = []
      const { data } = await fetchOrgInNotice()
      this.orgListArray = data
    },
    async getRoles() {
      const { data } = await fetchRoles()
      this.roleOptions = data.datalist
    },
    async getPortalApps() {
      const { data } = await fetchAllPortalApps()
      this.portalAppOptions = data
    },
    // async getOrgTree() {
    //   const { data } = await fetchOrgTree()
    //   this.orgTree = data
    // },
    async getUserOptions(search) {
      if (search !== '') {
        this.userLoading = true
        const { data } = await fetchRoleCandidates(
          this.form_data.roleId,
          search
        )
        const userOptions = []
        for (let i = 0; i < data.datalist.length; i++) {
          const user = data.datalist[i]
          userOptions.push({
            key: user.sid,
            label: user.nickname + ' (' + user.username + ')'
          })
        }
        this.userOptions = userOptions
        this.userLoading = false
      } else {
        this.userOptions = []
      }
    },
    async getOrgOptions(search) {
      if (search !== '') {
        this.orgLoading = true
        let orgOption = []
        orgOption = this.orgListArray.filter(item => {
          if (item.orgNameShort.indexOf(search) > -1) {
            return item
          }
        })
        this.orgOptions = []
        orgOption.forEach(org => {
          this.orgOptions.push({
            key: org.id,
            label: org.orgNameShort
          })
        })
        this.$forceUpdate()
        this.orgLoading = false
      } else {
        this.orgOptions = []
      }
    },
    getFormTitle() {
      return this.$t('common.' + this.formStatus) + this.$t('system.Notice')
    },
    // setFullName() {
    //   this.form_data.params.FULL_NAME = (this.form_data.params.LAST_NAME || '') + (this.form_data.params.FIRST_NAME || '')
    // },
    handleFilter() {
      this.page.pageNumber = 1
      this.getList()
    },
    handleUpload() {
    },
    handleDownload() {
    },
    async handleEdit(noticeId) {
      this.formStatus = 'edit'
      this.showSystemNoticeForm(noticeId)
    },
    // +添加公告按钮点击事件
    async handleAdd() {
      this.formStatus = 'add'
      this.form_data = {}
      this.showSystemNoticeForm(null)
    },
    // 添加公告、编辑公告方法
    async showSystemNoticeForm(noticeId) {
      this.form_visible = true
      this.form_data.publishTime = this.getCurrentTime() // 设置发布时间为当前日期
      if (noticeId != null) {
        this.detail_loading = true
        const { data } = await fetchSystemNoticeDetail(noticeId)
        const form_data = deepClone(data)
        this.form_data = form_data
        this.detail_loading = false
        this.getTargetId()
      }
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].resetFields()
      }
      this.submitLoading = false
    },
    hideNoticeForm() {
      this.form_visible = false
    },
    showDeleteConfirm(portalApp) {
      this.$confirm(
        this.$t('tips.confirm_delete', { name: portalApp.appName }),
        this.$t('common.tips'),
        {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      )
        .then(async _ => {
          await this.handleDelete(portalApp.id)
        })
        .catch(() => {
        })
    },
    async handleDelete(appId) {
      this.list_loading = true
      await deleteSystemNotice(appId)
      this.getList()
    },
    async handleSubmit() {
      this.setTargetId()
      this.setContent()
      this.$refs['dataForm'].validate(async valid => {
        if (valid) {
          this.submitLoading = true
          const data = deepClone(this.form_data)
          await submitSystemNotice(data)
          this.submitLoading = false
          this.hideNoticeForm()
          this.getList()
        }
      })
    },
    clipboardSuccess() {
      this.$message({
        message: this.$t('portal.copySucc'),
        type: 'success',
        duration: 1500
      })
    },
    noticeScopeChanged() {
      this.form_data.targetId = {}
      // this.update = false
      // setTimeout(() => {
      //   this.update = true
      // })
    },
    removeChange() {
      this.$forceUpdate()
    },
    getTargetId() {
      this.form_data[
        `targetId_${this.form_data.scope}`
      ] = this.form_data.targetId
    },
    setTargetId() {
      if (this.form_data.scope === 'all') this.form_data.targetId = 'all'
      else {
        this.form_data.targetId = this.form_data[
          `targetId_${this.form_data.scope}`
        ]
      }
    },
    setContent() {
      this.form_data.content = this.$refs.markdownEditor.getHtml()
    }
  }
}
</script>
