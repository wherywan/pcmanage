<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="searchParam.text" :placeholder="$t('system.operations.text')" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-date-picker
        v-model="searchParam.timerange"
        type="datetimerange"
        class="filter-item"
        :range-separator="$t('system.separator')"
        :start-placeholder="$t('system.startTime')"
        :end-placeholder="$t('system.endTime')"
        @keyup.enter.native="handleFilter"
      />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        {{ $t('common.search') }}
      </el-button>
    </div>
    <el-table
      ref="menuTree"
      v-loading="listLoading"
      :data="datalist"
      row-key="id"
      border
      fit
      highlight-current-row
      stripe
      size="mini"
      style="width: 100%;"
      default-expand-all
    >
      <el-table-column :label="$t('system.operations.datetime')" prop="datetime" align="center" width="220">
        <template slot-scope="{ row }">
          <span>{{ $moment(row.operBgnTs).format('YYYY-MM-DD HH:mm:ss.SSS') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.operations.username')" prop="username" align="center" width="220">
        <template slot-scope="{ row }">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.operations.title')" prop="title" align="center" width="220">
        <template slot-scope="{ row }">
          <span>{{ row.modelName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.operations.content')" prop="content" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.funcName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('system.source')" prop="ip" align="center" width="220">
        <template slot-scope="{ row }">
          <span>{{ row.clientIp }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.totalPage>0" :total="page.totalRecord" :page.sync="page.pageNumber" :limit.sync="page.pageSize" style="padding: 0; margin-top: 20px" @pagination="getList" />
  </div>
</template>

<script>
import { fetchOperations } from '@/api/system'
import Pagination from '@/components/Pagination/index'
import waves from '@/directive/waves'

export default {
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      listLoading: false,
      downloadLoading: false,
      datalist: [],
      page: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 0,
        totalRecord: 0
      },
      searchParam: {}
    }
  },
  computed: {
  },
  created() {
    this.getList()
  },
  mounted() {
  },
  methods: {
    async getList() {
      this.listLoading = true
      const { data } = await fetchOperations(this.page, this.searchParam)
      this.datalist = data.datalist
      this.page = data.page
      this.listLoading = false
    },
    handleFilter() {
      this.page.pageNumber = 1
      this.getList()
    }
  }
}
</script>

<style>
</style>
