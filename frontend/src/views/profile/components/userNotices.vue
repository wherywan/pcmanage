<template>
  <div class="app-container">
    <el-table
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      highlight-current-row
      stripe
      style="width: 100%"
      @expand-change="HandleExpandChange"
    >
      <el-table-column type="expand">
        <template slot-scope="{ row }">
          <el-form label-position="left" inline class="table-expand">
            <el-form-item :label="$t('system.notice.title')">
              <span>{{ row.title }}</span>
            </el-form-item>
            <el-form-item :label="$t('system.notice.content')">
              <div v-loading="row.notice_detail_loading" v-html="row.content"></div>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.notice.title')"
        prop="title"
        align="center"
        width="280"
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
          <span v-if="noticeTypeMap[row.noticeType] !== undefined"
                :style="{color: noticeTypeMap[row.noticeType].color,'font-weight':550}">{{ noticeTypeMap[row.noticeType].text }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.notice.createdBy')"
        prop="createdBy"
        align="center"
        width="140"
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
    </el-table>
    <pagination
      v-show="page.totalPage > 0"
      :total="page.totalRecord"
      :page.sync="page.pageNumber"
      :limit.sync="page.pageSize"
      @pagination="getList"
      style="padding:0;margin-top:20px"
    />

  </div>
</template>
<style>
  .table-expand {
    font-size: 0;
    width: 100%;
  }
  .table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 100%;
  }
</style>

<script>
  import MarkdownEditor from '@/components/MarkdownEditor'
  import clipboard from '@/directive/clipboard/index.js'
  import {
    fetchUserSystemNotice,
    fetchSystemNoticeDetail,
    fetchNoticeTypes,
    fetchNoticeScopes,
  } from '@/api/system-notices'
  import {
    fetchRoles,
    fetchOrgTree,
    fetchRoleCandidates
  } from '@/api/system'
  import {
    fetchAllPortalApps
  } from '@/api/portal'
  import IconSelect from '@/components/IconSelect'
  import TreeSelect from '@/components/TreeSelect'
  import {validateByReg} from '@/utils/validate'
  import waves from '@/directive/waves'
  import Pagination from '@/components/Pagination'
  import {deepClone} from '@/utils'

  export default {
    name: 'NoticeList',
    components: {Pagination, IconSelect, TreeSelect, MarkdownEditor},
    directives: {waves, clipboard},
    data() {
      return {
        last_expanded_len :0,
        list_loading: false,
        detail_loading: false,
        uploadLoading: false,
        downloadLoading: false,
        submitLoading: false,
        datalist: [],
        page: {
          pageNumber: 1,
          pageSize: 8,
          totalPage: 0,
          totalRecord: 0
        },
        search_param: {},
        noticeTypeOptions: [],
        noticeTypeMap: {},
        noticeScopeOptions: [],
        noticeScopeMap: {},
        isVisibleMap: {Y: "是", N: "否"},
        roleOptions: [],
        orgTree: [],
        userLoading: false,
        userOptions: [],
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
      await this.getList()
      await this.getNoticeTypes()
      await this.getNoticeScopes()
    },
    methods: {
      async HandleExpandChange(row,expanded) {
        if(expanded.length < this.last_expanded_len) return;
        row.notice_detail_loading = true
        const { data } = await fetchSystemNoticeDetail(row.id)
        const notice_data = data
        row.content = notice_data.content
        row.notice_detail_loading = false
        this.last_expanded_len = expanded.length
      },
      async getList() {
        this.list_loading = true
        const {data} = await fetchUserSystemNotice()
        console.log(data)
        this.datalist = data
        let total_record = data.length
        let page_size = this.page.pageSize
        let start_index = (this.page.pageNumber-1) * page_size
        let len = Math.min(this.datalist.length - start_index,page_size)
        this.datalist = this.datalist.slice((this.page.pageNumber-1) * page_size,start_index + len)
        this.page = {
          pageNumber: this.page.pageNumber,
          pageSize: this.page.pageSize,
          totalPage: Math.ceil(total_record/this.page.pageSize),
          totalRecord: total_record
        }
        this.list_loading = false
      },
      async getNoticeTypes() {
        this.noticeTypeOptions = []
        const {data} = await fetchNoticeTypes()
        this.noticeTypeMap = data
        let i = 1
        for (const key in data) {
          this.noticeTypeOptions.push({key: i++, value: key, label: data[key].text})
        }
      },
      async getNoticeScopes() {
        this.noticeScopeOptions = []
        const {data} = await fetchNoticeScopes()
        this.noticeScopeMap = data
        let i = 1
        for (const key in data) {
          this.noticeScopeOptions.push({key: i++, value: key, label: data[key]})
        }
      },
      async getRoles() {
        const {data} = await fetchRoles()
        this.roleOptions = data.datalist
      },
      async getPortalApps() {
        const {data} = await fetchAllPortalApps()
        this.portalAppOptions = data
      },
      async getOrgTree() {
        const {data} = await fetchOrgTree()
        this.orgTree = data
      },
      async getUserOptions(search) {
        if (search !== '') {
          this.userLoading = true
          const {data} = await fetchRoleCandidates(
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
      async handleAdd() {
        this.formStatus = 'add'
        this.form_data = {}
        this.showSystemNoticeForm(null)
      },
      async showSystemNoticeForm(noticeId) {
        this.form_visible = true
        if (noticeId != null) {
          this.detail_loading = true
          const {data} = await fetchSystemNoticeDetail(noticeId)
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
          this.$t('tips.confirm_delete', {name: portalApp.appName}),
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
      clipboardSuccess() {
        this.$message({
          message: this.$t('portal.copySucc'),
          type: 'success',
          duration: 1500
        })
      },
      noticeScopeChanged() {
        this.form_data.targetId = {}
      },
      getTargetId() {
        this.form_data[
          `targetId_${this.form_data.scope}`
          ] = this.form_data.targetId
      },
      setTargetId() {
        if (this.form_data.scope == 'all') this.form_data.targetId = 'all'
        else
          this.form_data.targetId = this.form_data[
            `targetId_${this.form_data.scope}`
            ]
      },
      setContent() {
        this.form_data.content = this.$refs.markdownEditor.getHtml()
      }
    }
  }
</script>
