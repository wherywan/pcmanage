<template>
  <div class="app-container">
    <el-form ref="paramForm" :model="search_param" :rules="formRules" label-position="right" label-width="110px">
      <el-row>
        <el-col :span="5" :offset="2">
          <el-form-item :label="$t('system.report.date_select')" prop="curDate">
            <el-date-picker
              v-model="search_param.curDate"
              :picker-options="pickerOptions0"
              value-format="yyyy-MM-dd"
              size="mini"
              style="width: 130px"
              type="date"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="$t('system.report.type')" prop="type">
            <el-select
              v-model="search_param.type"
              clearable
              size="mini"
              style="width: 180px"
            >
              <el-option :key="1" label="个人存款（含结构性）" value="1" />
              <el-option :key="2" label="核心存款" value="2" />
              <el-option :key="3" label="结构性存款" value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button :loading="list_loading" class="filter-item" type="primary" icon="el-icon-search" size="small" @click="handleFilter">
              {{ $t('common.search') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="5" :offset="5">
          <el-upload
            ref="upload"
            action=""
            multiple
            accept=".xlsx"
            :auto-upload="false"
            :file-list="fileList"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :http-request="uploadFile"
          >
            <el-button slot="trigger" type="primary" icon="el-icon-document" size="mini">
              {{ $t('system.report.select-file') }}
            </el-button>
            <el-button :offset="2" icon="el-icon-upload" size="mini" type="primary" :loading="upload_loading" @click="submitUpload">
              {{ $t('system.report.upload_file') }}
            </el-button>
            <div slot="tip" class="el-upload__tip">{{ $t('system.report.upload-tips') }}</div>
          </el-upload>
        </el-col>
        <el-col :span="2">
          <el-button type="success" icon="el-icon-download" size="mini" @click="submitDownload">
            {{ $t('system.report.download_result') }}
          </el-button>
        </el-col>
      </el-row>
      <el-col :span="5" style="margin-left: 20px; margin-top: 20px; margin-bottom: 10px; font-weight: bold">
        <span>{{ getTitleName() }}</span>
      </el-col>
    </el-form>
    <el-table
      v-if="search_param.type && search_param.type.length > 0"
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      size="small"
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column fixed :label="$t('system.report.org-name')" align="center" width="160" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span v-if="row.orgName">{{ row.orgName }}</span>
          <span v-else>{{ $orgTree.getLabel(row.orgId) }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed :label="$t('system.report.org-id')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.orgId }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.timeYear }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.rank-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.rankYear }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.offset-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.offsetYear }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-cur')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.timeCur }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.rank-cur')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.rankCur }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.offset-cur')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.offsetCur }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-up-than-up')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.timeUpThanUp }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.rank-up-than-up')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.rankUpThanUp }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.change-cur-than-up')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.changeCurThanUp }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.rank-cur-than-up')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.rankCurThanUp }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-year-than-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.timeYearThanYear }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.rank-year-than-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.rankYearThanYear }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.change-cur-than-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.changeCurThanYear }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.rank-cur-than-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.rankCurThanYear }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.totalPage>0&&search_param.type!=''" :total="page.totalRecord" :page.sync="page.pageNumber" :limit.sync="page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getDefaultPage } from '@/utils'
import { batUploadFiles, fetchSameBusiPersonalDeposits } from '@/api/report'
import { downloadFile } from '@/utils/file-request'

export default {
  name: 'PersonalDeposit',
  components: { Pagination },
  data() {
    return {
      list_loading: false,
      submitLoading: false,
      datalist: [],
      page: getDefaultPage(),
      search_param: {
        curDate: '',
        type: ''
      },
      pickerOptions0: {
        disabledDate(time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      },
      props: {
        value: 'id',
        label: 'orgName'
      },
      type: '',
      fileData: new FormData(), // 文件上传数据（多文件合一）
      fileList: [],
      formRules: {
        curDate: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        type: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }]
      },
      upload_loading: false
    }
  },
  computed: {
    timeDefault() {
      const date = new Date()
      const s1 = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + (date.getDate())
      return s1
    }
  },
  mounted() {
    // 初始化查询，默认为前一天
    this.search_param.curDate = this.timeDefault
    this.orgOptions = this.$orgTree
  },
  async created() {
  },
  methods: {
    getTitleName() {
      if (this.search_param.type === '1') {
        return this.$t('system.report.same-title-1')
      } else if (this.search_param.type === '2') {
        return this.$t('system.report.same-title-2')
      } else if (this.search_param.type === '3') {
        return this.$t('system.report.same-title-3')
      } else {
        return ''
      }
    },
    async handleFilter() {
      this.$refs['paramForm'].validate(async valid => {
        if (valid) {
          this.list_loading = true
          try {
            const { data } = await fetchSameBusiPersonalDeposits(this.page, this.search_param)
            this.datalist = data.datalist
            if (this.datalist && this.datalist.length > 0) {
              this.type = this.datalist[0].type
            }
            this.page = data.page
            this.list_loading = false
          } finally {
            this.list_loading = false
          }
        }
      })
    },
    async getList() {
      this.handleFilter()
    },
    // 上传文件
    uploadFile(file) {
      this.fileData.append('files', file.file) // append增加数据
      // this.$message.success(file.fileList.name)
    },
    // 移除
    handleRemove(file, fileList) {
      this.fileList = fileList
      // return this.$confirm(`确定移除 ${ file.name }？`)
    },
    // 监控上传文件列表
    handleChange(file, fileList) {
      const existFile = fileList.slice(0, fileList.length - 1).find(f => f.name === file.name)
      if (existFile) {
        this.$message.error('当前文件已经存在!')
        fileList.pop()
      }
      this.fileList = fileList
    },
    // 上传到服务器
    submitUpload() {
      if (this.fileList.length === 0) {
        this.$message({
          message: '请先选择文件',
          type: 'warning'
        })
      } else {
        this.upload_loading = true
        this.$refs.upload.submit()
        this.fileData.append('transCode', '0004')
        batUploadFiles(this.fileData).then(response => {
          this.upload_loading = false
          this.fileList = []
          this.fileData = new FormData()
        })
      }
    },
    submitDownload() {
      this.$refs['paramForm'].validate(async valid => {
        if (valid) {
          downloadFile('/sameBusi-personal-deposit/download?curDate=' + this.search_param.curDate)
        }
      })
    }
  }
}
</script>

<style>
  .el-upload-list__item {
    font-size: 11px;
  }
</style>
