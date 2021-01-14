<template>
  <div class="app-container">
    <el-form ref="paramForm" :model="search_param" :rules="formRules" label-position="right" label-width="110px">
      <el-col :span="5">
        <el-form-item :label="$t('system.report.date_select')" prop="statDate">
          <el-date-picker
            v-model="search_param.statDate"
            :picker-options="pickerOptions0"
            value-format="yyyy-MM-dd"
            size="mini"
            style="width: 130px"
            type="date"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item :label="$t('system.report.type')" prop="ccy">
          <el-select
            v-model="search_param.ccy"
            clearable
            size="mini"
            style="width: 180px"
          >
            <el-option
              v-for="(name, key) in $dicts.getDict('PRODUCT_TYPE')"
              :key="key"
              :label="name"
              :value="key"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item :label="$t('system.report.org_select')" prop="orgId">
          <el-cascader
            ref="orgCascader"
            v-model="search_param.orgId"
            :props="props"
            :options="orgOptions"
            size="mini"
            style="width: 350px"
            filterable
            clearable
          />
        </el-form-item>
      </el-col>
      <el-col :span="4" style="float: right;">
        <el-form-item>
          <el-button :loading="list_loading" class="filter-item" type="primary" icon="el-icon-search" size="small" @click="handleFilter">
            {{ $t('common.search') }}
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="5" :offset="1">
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
          <el-button icon="el-icon-upload" size="mini" type="primary" :loading="upload_loading" @click="submitUpload">
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
      <el-col :span="20" style="margin-left: 20px; margin-top: 20px; margin-bottom: 10px; font-weight: bold">
        <span>{{ getTitleName() }}</span>
      </el-col>
    </el-form>
    <el-table
      v-if="search_param.ccy && search_param.ccy.length > 0"
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
      <el-table-column :label="$t('system.report.time-than-last-day')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbalThanLastD }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-than-begin-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbalThanBeginY }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-than-last-tenDays')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbalThanTendays }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-than-last-month')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbalThanLastM }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-than-last-season')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbalThanLastS }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-than-last-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbalThanLastY }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.avg-than-begin-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.abalThanBeginY }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.avg-than-last-season')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.abalThanLastS }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.avg-than-last-month')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.abalThanLastM }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.avg-than-last-tenDays')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.abalThanTendays }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.avg-than-last-year')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.abalThanLastY }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.time-bal')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.tbal }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.report.avg-bal')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.abal }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="search_param.ccy && search_param.ccy ==='1'" :label="$t('system.report.price-scissors')" align="center" width="85" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.priceScissors }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.totalPage>0" :total="page.totalRecord" :page.sync="page.pageNumber" :limit.sync="page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getDefaultPage } from '@/utils'
import { batUploadFiles, fetchPersonalDeposits } from '@/api/report'
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
        statDate: ''
      },
      pickerOptions0: {
        disabledDate(time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      },
      orgOptions: [],
      props: {
        value: 'id',
        label: 'orgName'
      },
      ccy: '',
      fileData: new FormData(), // 文件上传数据（多文件合一）
      fileList: [],
      formRules: {
        statDate: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        ccy: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        orgId: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }]
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
    this.search_param.statDate = this.timeDefault
    this.orgOptions = this.$orgTree
  },
  async created() {
  },
  methods: {
    getTitleName() {
      if (this.ccy === '1') {
        return this.$t('system.report.title-1')
      } else if (this.ccy === '2') {
        return this.$t('system.report.title-2')
      } else if (this.ccy === '3') {
        return this.$t('system.report.title-3')
      } else {
        return ''
      }
    },
    async handleFilter() {
      this.$refs['paramForm'].validate(async valid => {
        if (valid) {
          this.list_loading = true
          const orgList = this.search_param.orgId
          try {
            this.search_param.orgId = orgList[this.search_param.orgId.length - 1]
            const { data } = await fetchPersonalDeposits(this.page, this.search_param)
            this.datalist = data.datalist
            if (this.datalist && this.datalist.length > 0) {
              this.ccy = this.datalist[0].ccy
            }
            this.page = data.page
            this.list_loading = false
          } finally {
            this.search_param.orgId = orgList
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
        this.fileData.append('transCode', '0001')
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
          const orgList = this.search_param.orgId
          downloadFile('/personal-deposit/download?statDate=' + this.search_param.statDate + '&orgId=' + orgList[orgList.length - 1])
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
