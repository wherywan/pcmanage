<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin-left: 10px;" :title="$t('common.add')" type="primary" size="medium" icon="el-icon-plus" @click="handleAdd" />
    </div>
    <el-table
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      size="small"
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column :label="$t('system.report.title')" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.reportTitle }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.publish_time')" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.createTime ? $moment(row.createTime).format('YYYY-MM-DD HH:mm:ss') : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.done_num')" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.completeStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.fill_user')" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.fillUser }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operations')" align="center" width="350px">
        <template slot-scope="{row}">
          <el-button type="text" size="mini" @click="deleteRow(row)">
            <svg-icon icon-class="trash" />
            {{ $t('common.delete') }}
          </el-button>
          <el-button type="text" size="mini" @click="downloadResult(row)">
            <svg-icon icon-class="submit" />
            {{ $t('common.download') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.totalPage>0" :total="page.totalRecord" :page.sync="page.pageNumber" :limit.sync="page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getList" />

    <el-dialog :visible.sync="form_visible" width="50%" :modal-append-to-body="false">
      <div slot="title">
        {{ $t('system.report.create_task') }}
      </div>
      <div style="float: left; width: 100%;">
        <el-form ref="dataForm" :rules="formRules" :model="form_data" label-position="right" label-width="110px">
          <el-col :span="24">
            <el-form-item :label="$t('system.report.task_title')" prop="reportTitle">
              <el-input v-model="form_data.reportTitle" :placeholder="$t('system.report.task_title')" type="text" maxlength="60" size="mini" style="width: 400px" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('system.report.template')" prop="fileName">
              <el-input v-model="form_data.fileName" type="text" size="mini" disabled show-overflow-tooltip />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-upload
              ref="upload"
              class="upload-demo"
              multiple
              action=""
              accept=".xlsx"
              :show-file-list="false"
              :on-change="handleChange"
              :file-list="fileList"
              :http-request="toUpload"
            >
              <el-tooltip class="item" effect="dark" :content="$t('system.report.upload_content')" placement="right-start">
                <el-button style="margin-left: 20px" size="small" type="text">{{ $t('system.report.upload_file') }}</el-button>
              </el-tooltip>
            </el-upload>
          </el-col>
          <el-col :span="20">
            <el-form-item :label="$t('system.report.fill_form_user')" prop="fillFormName">
              <el-button type="primary" size="small" @click="selectFillForm">
                {{ $t('common.add') }}
              </el-button>
              <el-button type="primary" size="small" @click="deleteFillUser">
                {{ $t('common.delete') }}
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form ref="fillForm" style="margin-left: 40px">
              <el-table
                ref="multipleTable"
                :data="tableData"
                border
                fit
                size="small"
                tooltip-effect="dark"
                style="width: 546px"
                :row-class-name="tableRowClassName"
                @selection-change="handleSelectionChange"
              >
                <el-table-column
                  type="selection"
                  align="center"
                  width="55px"
                />
                <el-table-column :label="$t('system.report.fill_form_user')" align="center" show-overflow-tooltip width="400px">
                  <template slot-scope="{row}">
                    <span>{{ row.fillFormName }}</span>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('system.report.visible')" align="center" width="90px">
                  <template slot-scope="{row}">
                    <el-checkbox v-model="row.viewAuth" />
                  </template>
                </el-table-column>
              </el-table>
            </el-form>
          </el-col>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSubmit">
          {{ $t('system.report.publish') }}
        </el-button>
        <el-button @click="form_visible = false">
          {{ $t('common.cancel') }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="user_form_visible" width="50%">
      <el-form ref="userdataForm" :rules="formRules" :model="user_form_data" label-position="right" label-width="140px" style="height: 200px">
        <el-form-item :label="$t('system.report.org_select')" prop="org">
          <el-cascader
            ref="orgCascader"
            v-model="user_form_data.org"
            :props="props"
            :options="orgOptions"
            size="mini"
            style="width: 450px"
            :show-all-levels="false"
            filterable
            clearable
            @change="getCurrentOrgUsers"
          />
        </el-form-item>
        <el-form-item :label="$t('system.report.user_select')" prop="user">
          <el-select
            v-model="user_form_data.userValues"
            size="mini"
            multiple
            filterable
            remote
            collapse-tags
            :remote-method="getUserOptions"
            class="filter-item"
            style="width: 450px"
            @change="changeLocationValue"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.key"
              :label="item.label"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
        <el-col :span="8" :offset="9">
          <el-button type="primary" :loading="submitLoading" @click="addUserSubmit">
            {{ $t('system.report.add_user') }}
          </el-button>
          <el-button type="info" @click="user_form_visible = false">
            {{ $t('common.cancel') }}
          </el-button>
        </el-col>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getDefaultPage } from '@/utils'
import { getOrgList, uploadFile, reportCommit, fetchReports, deleteReport } from '@/api/report'
import { fetchUsers } from '@/api/system'
import uuid from 'uuid/v4'
import { downloadFile } from '@/utils/file-request'

export default {
  name: 'ReportPublish',
  components: { Pagination },
  data() {
    return {
      list_loading: false,
      submitLoading: false,
      userLoading: false,
      datalist: [],
      orgOptions: [],
      userOptions: [],
      page: getDefaultPage(),
      form_visible: false,
      user_form_visible: false,
      formStatus: '',
      form_data: {},
      user_form_data: {
        userValues: []
      },
      fileList: [],
      tableData: [],
      props: {
        value: 'id',
        label: 'orgName'
      },
      currentOrgUserPage: {
        pageNumber: 1,
        pageSize: 20,
        totalPage: 1,
        totalRecord: 0
      },
      userIds: [],
      userNames: [],
      orgName: '',
      file: {
        attach: ''
      },
      formRules: {
        reportTitle: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        fileName: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        fillFormName: [{ validator: this.validateFillForm, message: this.$t('tips.fill_is_required'), trigger: 'blur' }]
      },
      multipleSelection: []
    }
  },
  computed: {
  },
  watch: {
  },
  async created() {
    this.getList()
  },
  methods: {
    async deleteRow(report) {
      this.$confirm(
        this.$t('tips.confirm_delete', { name: report.reportTitle }), this.$t('common.tips'), {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      ).then(async _ => {
        deleteReport(report.id).then(response => {
          if (response.flag === 0) {
            this.$message({
              message: '任务删除成功',
              type: 'success',
              showClose: true,
              duration: 1000
            })
          } else {
            this.$message({
              message: '任务删除失败',
              type: 'error',
              showClose: true,
              duration: 1000
            })
          }
        }).catch(() => {
          this.$message({
            message: '任务删除失败',
            type: 'error',
            showClose: true,
            duration: 1000
          })
        })
        this.getList()
      }).catch(() => {})
    },
    async getList() {
      const { data } = await fetchReports(this.page)
      this.datalist = data.datalist
    },
    changeLocationValue(val) {
      this.userIds = []
      this.userNames = []
      for (let i = 0; i <= val.length - 1; i++) {
        this.userOptions.find(item => {
          if (item.key === val[i]) {
            this.userIds.push(item.key)
            this.userNames.push(item.label)
          }
        })
      }
    },
    async validateFillForm(rule, value, callback) {
      if (this.tableData.length === 0) {
        callback(new Error(rule.message))
      } else {
        callback()
      }
    },
    async addUserSubmit() {
      for (let i = 0; i < this.userIds.length; i++) {
        const tableRow = {}
        tableRow.fillFormId = this.userIds[i]
        tableRow.fillFormName = this.userNames[i]
        tableRow.viewAuth = false
        this.tableData.push(tableRow)
      }
      this.user_form_visible = false
    },
    async getOrgTree() {
      const { data } = await getOrgList()
      this.orgOptions = data
    },
    async getCurrentOrgUsers() {
      this.orgName = ''
      const orgTree = this.$refs['orgCascader'].getCheckedNodes()[0].pathLabels
      for (let i = 0; i < orgTree.length; i++) {
        this.orgName += orgTree[i] + '-'
      }
      this.orgName = this.orgName.substr(0, this.orgName.length - 1)
      this.user_form_data.userValues = []
      const orgId = this.user_form_data.org[this.user_form_data.org.length - 1]
      const { data } = await fetchUsers(this.currentOrgUserPage, { orgId: orgId })
      this.userOptionsFormat(data.datalist)
    },
    async getUserOptions(query) {
      if (query !== '' && this.user_form_data.org.length > 1) {
        const searchParam = {}
        searchParam.orgId = this.user_form_data.org[this.user_form_data.org.length - 1]
        searchParam.search = query
        const { data } = await fetchUsers(this.currentOrgUserPage, searchParam)
        this.userOptionsFormat(data.datalist)
      }
    },
    async userOptionsFormat(data) {
      const userOptions = []
      for (let i = 0; i < data.length; i++) {
        const user = data[i]
        let label = user.nickname ? user.nickname + ' ' + user.username : user.username
        label += '(' + this.orgName + ')'
        userOptions.push({ key: user.sid, label: label })
      }
      this.userOptions = userOptions
    },
    async handleAdd() {
      this.formStatus = 'add'
      this.getOrgTree()
      this.form_data = {}
      this.tableData = []
      this.showForm()
    },
    async showForm() {
      this.form_visible = true
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].resetFields()
      }
      this.submitLoading = false
    },
    handleChange(file, fileList) {
      if (fileList.length > 0) {
        fileList[0].name = file.name
        this.fileList = fileList.splice(0, 1)
      }
    },
    toUpload(item) {
      this.file.attach = item.file
      const fileFormData = new FormData()
      let attachId
      if (this.form_data.fileUrl) {
        attachId = this.form_data.fileUrl
      } else {
        attachId = uuid()
      }
      fileFormData.append('batchId', attachId)
      fileFormData.append('attachId', attachId)
      fileFormData.append('attach', this.file.attach)

      uploadFile(fileFormData).then(response => {
        this.form_data.fileUrl = attachId
        this.$set(this.form_data, 'fileName', this.fileList[0].name)
      })
    },
    handleSubmit() {
      this.$refs['dataForm'].validate(async valid => {
        if (valid) {
          this.submitLoading = true
          this.form_data.fillUsers = this.tableData
          reportCommit(this.form_data).then(response => {
            this.submitLoading = false
            this.form_visible = false
            if (response.flag === 0) {
              this.$message({
                message: '任务发布成功',
                type: 'success',
                showClose: true,
                duration: 1000
              })
            }
          }).catch(() => {
            this.submitLoading = false
          })
          this.getList()
        }
      })
    },
    async selectFillForm() {
      this.user_form_data.org = ''
      this.user_form_data.userValues = []
      this.tableData = []
      this.user_form_visible = true
    },
    async deleteFillUser() {
      this.multipleSelection.forEach(select => {
        this.tableData.splice(select.index, 1)
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    tableRowClassName({ row, rowIndex }) {
      // 把每一行的索引放进row
      row.index = rowIndex
    },
    downloadResult(row) {
      downloadFile('/fillform/result_download?reportId=' + row.id)
    }
  }
}
</script>
