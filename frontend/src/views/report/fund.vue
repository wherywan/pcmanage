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
      <el-col :span="16">
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
      <!-- <el-col :span="4" style="float: right;">
        <el-form-item>
          <el-button :loading="list_loading" class="filter-item" type="primary" icon="el-icon-search" size="small" @click="handleFilter">
            {{ $t('common.search') }}
          </el-button>
        </el-form-item>
      </el-col> -->
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
    </el-form>
  </div>
</template>

<script>
import { batUploadFiles } from '@/api/report'
import { downloadFile } from '@/utils/file-request'

export default {
  name: 'Fund',
  data() {
    return {
      list_loading: false,
      submitLoading: false,
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
    // 上传文件
    uploadFile(file) {
      this.fileData.append('files', file.file) // append增加数据
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
        this.fileData.append('transCode', '0002')
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
