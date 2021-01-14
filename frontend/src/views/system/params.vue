<template>
  <div class="app-container">
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column width="220px" align="center" :label="$t('common.name')">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column align="right" :label="$t('common.code')" width="240">
        <template slot-scope="{row}">
          <span>{{ row.key }}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="300px" :label="$t('common.value')">
        <template slot-scope="{row}">
          <template v-if="row.edit">
            <el-input v-if="!!row.value" v-model="row.value" class="edit-input" size="mini" />
            <el-input v-else v-model="row.defaultValue" class="edit-input" size="mini" />
            <el-button class="cancel-btn" size="mini" type="warning" @click="cancelEdit(row)">
              <i class="el-icon-refresh" />
              {{ $t('common.cancel') }}
            </el-button>
          </template>
          <span v-else>{{ row.value || row.defaultValue }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('common.operations')" width="120">
        <template slot-scope="{row}">
          <el-button v-if="row.edit" type="success" size="mini" @click="confirmEdit(row)">
            <svg-icon icon-class="floppy" />
            {{ $t('common.save') }}
          </el-button>
          <el-button v-else type="primary" size="mini" @click="row.edit=!row.edit">
            <i class="el-icon-edit" />
            {{ $t('common.edit') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { fetchParams, submitParam } from '@/api/system'

export default {
  name: 'InlineEditTable',
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      const { data } = await fetchParams()
      const items = data.datalist
      this.list = items.map(v => {
        this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
        v.originalValue = v.value //  will be used when user click the cancel botton
        return v
      })
      this.listLoading = false
    },
    cancelEdit(row) {
      row.value = row.originalValue
      row.edit = false
      this.$message({
        message: this.$t('tips.value_restored'),
        type: 'warning'
      })
    },
    confirmEdit(row) {
      if (!row.value) {
        row.value = row.defaultValue
      }
      submitParam(row).then(_ => {
        row.edit = false
        row.originalTitle = row.title
        this.$message({
          message: this.$t('tips.submit_success', { name: row.name }),
          type: 'success'
        })
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
