<template>
  <div class="app-container">
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
      <el-table-column :label="$t('common.operations')" align="center" width="450px">
        <template slot-scope="{row}">
          <el-form>
            <el-col :span="4" :offset="5">
              <el-button type="text" size="mini" @click="downloadTemp(row)">
                <svg-icon icon-class="submit" />
                {{ $t('system.report.download_temp') }}
              </el-button>
            </el-col>
            <el-col :span="4">
              <el-upload
                ref="upload"
                :disabled="row.status === '1'"
                action=""
                multiple
                accept=".xlsx"
                :show-file-list="false"
                :http-request="uploadForm"
              >
                <el-button type="text" size="mini" :disabled="row.status === '1'" @click="attachSet(row)">
                  <svg-icon icon-class="form" />
                  {{ $t('system.report.upload_form') }}
                </el-button>
              </el-upload>
            </el-col>
            <el-col :span="4">
              <el-button v-if="row.viewAuth === '1'" type="text" size="mini" @click="downloadResult(row)">
                <svg-icon icon-class="submit" />
                {{ $t('system.report.download_result') }}
              </el-button>
            </el-col>
          </el-form>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.totalPage>0" :total="page.totalRecord" :page.sync="page.pageNumber" :limit.sync="page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getDefaultPage } from '@/utils'
import { fetchFillForms, uploadFillFile } from '@/api/report'
import { downloadFile } from '@/utils/file-request'

export default {
  name: 'ReportFill',
  components: { Pagination },
  data() {
    return {
      list_loading: false,
      submitLoading: false,
      userLoading: false,
      datalist: [],
      page: getDefaultPage(),
      form_visible: false,
      fillId: '',
      reportId: ''
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
    async getList() {
      const { data } = await fetchFillForms(this.page)
      this.datalist = data.datalist
    },
    async downloadTemp(row) {
      if (row.fileUrl) {
        try {
          downloadFile('/fillform/temp_download?url=' + row.fileUrl)
        } catch (e) {
          this.$message({
            message: '下载模板失败',
            type: 'error',
            showClose: true,
            duration: 1000
          })
        }
      } else {
        this.$message({
          message: '模板文件不存在',
          type: 'error',
          showClose: true,
          duration: 1000
        })
      }
    },
    attachSet(row) {
      this.fillId = row.id
      this.reportId = row.reportId
    },
    uploadForm(item) {
      const fileFormData = new FormData()
      fileFormData.append('fillId', this.fillId)
      fileFormData.append('reportId', this.reportId)
      fileFormData.append('attach', item.file)
      uploadFillFile(fileFormData)
    },
    downloadResult(row) {
      downloadFile('/fillform/result_download?reportId=' + row.reportId)
    }
  }
}
</script>
