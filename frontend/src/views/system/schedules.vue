<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <span>刷新时间：{{ refresh_time }}</span> -->
      <el-button-group style="float: right;">
        <el-button v-waves class="filter-item" style="margin-left: 10px;" :title="$t('common.refresh')" type="primary" icon="el-icon-refresh" @click="getList" />
      </el-button-group>
    </div>
    <el-table
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      highlight-current-row
      stripe
      style="width: 100%;"
    >
      <el-table-column label="计划任务" prop="remark">
        <template slot-scope="{row}">
          说明：{{ row.remark }}<br>
          执行器：{{ row.runner }}
        </template>
      </el-table-column>
      <el-table-column label="执行时间" align="center" width="160">
        <template slot-scope="{row}">
          <span>{{ row.cron }}
            <el-button type="text" size="mini" @click="showCronInput(row)">
              <svg-icon icon-class="edit" />
            </el-button>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag v-if="row.status === 'ON'" type="success" effect="dark">{{ '已启用' }}</el-tag>
          <el-tag v-if="row.status === 'RUNNING'" type="warning" effect="dark">{{ '执行中' }}</el-tag>
          <el-tag v-if="row.status === 'OFF'" type="info" effect="dark">{{ '已禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开关" align="center" width="80">
        <template slot-scope="{row}">
          <el-switch
            v-if="row.status !== 'RUNNING'"
            v-model="row.status"
            active-value="ON"
            inactive-value="OFF"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="changeStatus(row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="上次执行" width="240">
        <template slot-scope="{row}">
          上次执行: {{ row.lastRun ? $moment(row.lastRun).format('YYYY-MM-DD HH:mm:ss') : '-' }}<br>
          执行耗时：{{ lastRunLasts(row) }}<br>
          执行结果：{{ lastRunResult(row) }}
          <el-popover
            placement="bottom"
            title="执行报告"
            width="260"
            trigger="hover"
          >
            <div>
              开始时间: {{ row.lastRunReport ? $moment(row.lastRunReport.startTime).format('YYYY-MM-DD HH:mm:ss') : '-' }}<br>
              结束时间: {{ row.lastRunReport ? $moment(row.lastRunReport.endTime).format('YYYY-MM-DD HH:mm:ss') : '-' }}<br>
              备注: <br>
              {{ row.lastRunReport ? row.lastRunReport.remark : '-' }}
            </div>
            <el-link slot="reference">
              <i class="el-icon-question" />
            </el-link>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operations')" width="220" fixed="right" align="center">
        <template slot-scope="{row}">
          <el-button-group>
            <el-tooltip class="item" effect="dark" :content="$t('common.execute')" placement="top">
              <el-button :loading="row.status === 'RUNNING'" type="primary" size="mini" @click="showExecuteConfirm(row)">
                <i v-if="row.status !== 'RUNNING'" class="el-icon-caret-right" />
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" :content="$t('common.reset')" placement="top">
              <el-button :disabled="row.status !== 'RUNNING'" type="primary" size="mini" @click="showResetConfirm(row)">
                <svg-icon icon-class="cancel" />
              </el-button>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" :content="$t('common.history')" placement="top">
              <el-button type="primary" size="mini" @click="showHistoryDialog(row)">
                <svg-icon icon-class="history" />
              </el-button>
            </el-tooltip>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :visible.sync="cron_form_visible" width="75%">
      <div slot="title">
        <svg-icon icon-class="cron" />Cron 表达式
      </div>
      <CronInput ref="cronInput" v-model="current_row.cron" mode="simple" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="cron_form_visible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" :loading="submitting" @click="changeCron">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="cron_history_visible" width="75%">
      <div slot="title">
        <svg-icon icon-class="cron" />计划任务执行历史
      </div>
      <el-table
        v-loading="history_datalist_loading"
        :data="history_datalist"
        border
        fit
        highlight-current-row
        stripe
        style="width: 100%;"
      >
        <el-table-column label="开始时间" width="200" align="center">
          <template slot-scope="{row}">
            {{ $moment(row.startTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" width="200" align="center">
          <template slot-scope="{row}">
            {{ $moment(row.endTime).format('YYYY-MM-DD HH:mm:ss') }}
          </template>
        </el-table-column>
        <el-table-column label="执行结果" width="120" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.result === 'SUCCESS' ? 'success' : 'danger'" effect="dark">{{ $dicts.getLabel('REQUEST_RESULT', row.result) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注">
          <template slot-scope="{row}">
            {{ row.remark }}
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="history_datalist_page.totalPage>0" :total="history_datalist_page.totalRecord" :page.sync="history_datalist_page.pageNumber" :limit.sync="history_datalist_page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getHistoryList" />
    </el-dialog>
  </div>
</template>

<script>
import { fetchSchedules, submitSchedule, resetSchedule, executeSchedule, fetchScheduleHistories } from '@/api/schedule'
import { deepClone, getDefaultPage } from '@/utils'
import CronInput from '@/components/CronInput'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves'
import { addHandler, delHandler, sendMessage } from '@/api/system-ws'

export default {
  name: 'SystemSchedule',
  components: { CronInput, Pagination },
  directives: { waves },
  data() {
    return {
      datalist: [],
      list_loading: false,
      refresh_timer: null,
      refresh_time: null,
      cron_form_visible: false,
      cron_history_visible: false,
      current_row: {},
      submitting: false,
      history_datalist: [],
      history_datalist_loading: false,
      history_datalist_runner: '',
      history_datalist_page: getDefaultPage()
    }
  },
  created() {
    const that = this
    addHandler('sys-schedule', function(msg_pack) {
      that.getList()
    })
    sendMessage('sys-schedule', { on: true })
    this.getList()
    // const that = this
    // this.refresh_timer = setInterval(function() {
    //   that.getList()
    // }, 5000)
  },
  destroyed() {
    // clearInterval(this.refresh_timer)
    sendMessage('sys-schedule', { on: false })
    delHandler('sys-schedule')
  },
  methods: {
    async getList() {
      this.list_loading = true
      const { data } = await fetchSchedules()
      const datalist = data.datalist
      for (let i = 0; i < datalist.length; i++) {
        datalist[i].on = datalist[i].status !== 'STOPPED'
        datalist[i].disabled = datalist[i] === 'RUNNING'
      }
      this.datalist = datalist
      this.refresh_time = this.$moment().format('YYYY-MM-DD HH:mm:ss')
      this.list_loading = false
    },
    async getHistoryList() {
      this.history_datalist_loading = true
      const { data } = await fetchScheduleHistories(this.history_datalist_runner, this.history_datalist_page)
      this.history_datalist = data.datalist
      this.history_datalist_page = data.page
      this.history_datalist_loading = false
    },
    async changeStatus(row) {
      const new_status = row.status === 'OFF' ? 'OFF' : 'ON'
      const new_row = deepClone(row)
      new_row.status = new_status
      await submitSchedule(new_row)
      this.getList()
    },
    showCronInput(row) {
      this.current_row = row
      this.cron_form_visible = true
    },
    async changeCron() {
      const new_cron = this.$refs.cronInput.cron1
      const new_row = deepClone(this.current_row)
      new_row.cron = new_cron
      await submitSchedule(new_row)
      this.getList()
      this.cron_form_visible = false
    },
    lastRunLasts(row) {
      if (row.lastRunReport) {
        const start = this.$moment(row.lastRunReport.startTime)
        const end = this.$moment(row.lastRunReport.endTime)
        return this.$moment.duration(end.diff(start)).humanize()
      }
      return '-'
    },
    lastRunResult(row) {
      if (row.lastRunReport) {
        return this.$dicts.getLabel('REQUEST_RESULT', row.lastRunReport.result)
      }
      return '-'
    },
    async showExecuteConfirm(row) {
      this.$confirm(
        this.$t('tips.confirm_execute_schedule', { name: row.remark }), this.$t('common.tips'), {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      ).then(async _ => {
        await executeSchedule(row.runner)
        this.getList()
      }).catch(() => {})
    },
    async showResetConfirm(row) {
      this.$confirm(
        this.$t('tips.confirm_reset_schedule', { name: row.remark }), this.$t('common.tips'), {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      ).then(async _ => {
        await resetSchedule(row.runner)
        this.getList()
      }).catch(() => {})
    },
    showHistoryDialog(row) {
      this.history_datalist_page.pageNumber = 1
      this.history_datalist = []
      this.history_datalist_runner = row.runner
      this.cron_history_visible = true
      this.getHistoryList()
    }
  }
}
</script>
