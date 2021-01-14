<template>
  <div class="dashboard-editor-container">

    <panel-group />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-col :xs="24" :sm="24" :lg="12">
        <realtime-line-chart :chart-data="memory" />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <realtime-line-chart :chart-data="cpus" />
      </el-col>
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <realtime-bar-chart :chart-data="onlines" />
    </el-row>

    <!--
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="8">
      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 12}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:30px;">
        <transaction-table />
      </el-col>
      <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
        <todo-list />
      </el-col>
      <el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">
        <box-card />
      </el-col>
    </el-row> -->
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
// import LineChart from './components/LineChart'
import RealtimeLineChart from './components/RealtimeLineChart'
import RealtimeBarChart from './components/RealtimeBarChart'
import { metric } from '@/api/system'
import { deepClone } from '@/utils'
// import RaddarChart from './components/RaddarChart'
// import PieChart from './components/PieChart'
// import BarChart from './components/BarChart'
// import TransactionTable from './components/TransactionTable'
// import TodoList from './components/TodoList'
// import BoxCard from './components/BoxCard'

// const lineChartData = {
//   newVisitis: {
//     expectedData: [100, 120, 161, 134, 105, 160, 165],
//     actualData: [120, 82, 91, 154, 162, 140, 145]
//   },
//   messages: {
//     expectedData: [200, 192, 120, 144, 160, 130, 140],
//     actualData: [180, 160, 151, 106, 145, 150, 130]
//   },
//   purchases: {
//     expectedData: [80, 100, 121, 104, 105, 90, 100],
//     actualData: [120, 90, 100, 138, 142, 130, 130]
//   },
//   shoppings: {
//     expectedData: [130, 140, 141, 142, 145, 150, 160],
//     actualData: [120, 82, 91, 154, 162, 140, 130]
//   }
// }

export default {
  name: 'DashboardPortal',
  components: {
    PanelGroup,
    RealtimeLineChart,
    RealtimeBarChart
    // RaddarChart,
    // PieChart,
    // BarChart,
    // TransactionTable,
    // TodoList,
    // BoxCard
  },
  data() {
    return {
      timer: null,
      onlines: {
        data: {
          '在线数': [],
          '连接数': []
        },
        'timestamps': []
      },
      cpus: {
        data: {
          'CPU': []
        },
        'timestamps': []
      },
      memory: {
        data: {
          'Total(MB)': [],
          'Used(MB)': []
        },
        'timestamps': []
      }
    }
  },
  created() {
    this.fetchMetrics()
    const that = this
    this.timer = setInterval(function() {
      that.fetchMetrics()
    }, 20000)
  },
  destroyed() {
    clearInterval(this.timer)
  },
  methods: {
    async fetchMetrics() {
      const { data } = await metric('{metric_OnlineCount{value} metric_ConnCount{value} metric_CpuUsage{value} metric_TotalMemory{value} metric_FreeMemory{value}}')
      const timestamp = this.$moment().format('HH:mm:ss')

      const onlines = deepClone(this.onlines)
      onlines.data['在线数'].push(data.metric_OnlineCount.value)
      onlines.data['连接数'].push(data.metric_ConnCount.value)
      onlines['timestamps'].push(timestamp)
      this.onlines = onlines

      const cpus = deepClone(this.cpus)
      cpus.data['CPU'].push(data.metric_CpuUsage.value)
      cpus['timestamps'].push(timestamp)
      this.cpus = cpus

      const memory = deepClone(this.memory)
      memory.data['Total(MB)'].push(data.metric_TotalMemory.value / 1024 / 1024)
      memory.data['Used(MB)'].push((data.metric_TotalMemory.value - data.metric_FreeMemory.value) / 1024 / 1024)
      this.memory = memory
    }
  }
  // methods: {
  //   handleSetLineChartData(type) {
  //     this.lineChartData = lineChartData[type]
  //   }
  // }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
