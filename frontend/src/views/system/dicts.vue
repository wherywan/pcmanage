<template>
  <div class="app-container">
    <el-tabs v-model="activeName" tab-position="right">
      <el-tab-pane name="tab1">
        <span slot="label"><svg-icon icon-class="dict" /> 动态字典</span>
        <el-alert title="动态字典是指可以存储于数据库的字典项，可以通过界面修改并立即生效" type="info" style="margin-bottom: 20px" close-text="知道了" show-icon />
        <div class="filter-container">
          <el-input v-model="search_param.search" :placeholder="$t('system.Dict') + $t('common.name')" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
          <el-button v-waves :disabled="search_loading" class="filter-item" type="primary" icon="el-icon-search" @click="getList">
            {{ this.$t('common.search') }}
          </el-button>
          <el-button-group style="float: right">
            <el-button class="filter-item" style="margin-left: 10px;" :title="$t('common.add')" type="primary" icon="el-icon-plus" @click="addDicts" />
            <el-button v-waves :loading="upload_loading" class="filter-item" :title="$t('common.import')" type="primary" icon="el-icon-upload2" disabled @click="handleUpload" />
            <el-button v-waves :loading="download_loading" class="filter-item" :title="$t('common.export')" type="primary" icon="el-icon-download" disabled @click="handleDownload" />
          </el-button-group>
        </div>
        <el-table v-loading="dynamicDictsLoading" :data="dynamicDicts | pagination(dict_page.pageNumber, dict_page.pageSize)" border row-key="_key" :tree-props="{ hasChildren: 'hasChildren', children: 'items' }">
          <el-table-column prop="name" :label="$t('system.dict.name')" width="220" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <svg-icon v-if="row.count !== undefined" icon-class="dict" />
              <svg-icon v-else icon-class="list" />
              {{ row.key || row.name }}
            </template>
          </el-table-column>
          <el-table-column prop="count" :label="$t('system.dict.count')" align="center" width="80" show-overflow-tooltip />
          <el-table-column prop="label" :label="$t('system.dict.label')" align="center" />
          <el-table-column :label="$t('common.operations')" align="center" width="180">
            <template slot-scope="{ row }">
              <el-button-group>
                <el-button v-if="row.count !== undefined" type="primary" size="mini" @click="editDicts(row)">
                  <i class="el-icon-edit" />
                  {{ $t('common.edit') }}
                </el-button>
                <el-button type="danger" size="mini" @click="deleteDict(row)">
                  <svg-icon icon-class="trash" />
                  {{ $t('common.delete') }}
                </el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
        <pagination style="margin: 0; padding: 10px 0;" :page-sizes="dict_page.sizes" :total="dynamicDicts.length" :page.sync="dict_page.pageNumber" :limit.sync="dict_page.pageSize" @pagination="getDicts" />
      </el-tab-pane>
      <el-tab-pane name="tab2">
        <span slot="label"><svg-icon icon-class="dict" /> 静态字典</span>
        <el-alert title="静态字典是指由于有特殊用途，通过枚举等硬编码方式嵌入程序的字典项（静态字典不可编辑）。" type="info" style="margin-bottom: 20px" close-text="知道了" show-icon />
        <el-table v-loading="staticDictsLoading" :data="staticDicts" border row-key="_key" :tree-props="{children: 'items'}">
          <el-table-column prop="name" :label="$t('system.dict.name')" width="220" show-overflow-tooltip>
            <template slot-scope="{ row }">
              <svg-icon v-if="row.count !== undefined" icon-class="dict" />
              <svg-icon v-else icon-class="list" />
              {{ row.key || row.name }}
            </template>
          </el-table-column>
          <el-table-column prop="count" :label="$t('system.dict.count')" align="center" width="80" show-overflow-tooltip />
          <el-table-column prop="label" :label="$t('system.dict.label')" align="center" />
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <el-dialog :visible.sync="form_visible" width="60%">
      <div slot="title" class="dialog-header">
        <svg-icon icon-class="peoples" />
        {{ getFormTitle() }}
      </div>
      <el-form ref="dataForm" :rules="form_rules" :model="form_data" label-position="right" label-width="80px" style="width: 680px; margin-left:50px;">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('system.dict.pCodeType')" prop="pid">
              <el-select
                v-model="form_data.pid"
                filterable
                remote
                clearable
                :remote-method="remoteCodeType"
                :loading="pidLoading"
                style="width: 100%"
                @change="handleChange"
              >
                <el-option v-for="(val, key) in pidOptions" :key="key" :label="val" :value="key" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('system.dict.codeType')" prop="codeType">
              <el-input v-if="!form_data.pid" v-model="form_data.codeType" :placeholder="$t('system.dict.codeType')" maxlength="30" show-word-limit />
              <el-select v-else v-model="form_data.code" clearable style="width: 100%">
                <el-option v-for="(val, key) in this.$dicts[form_data.pid]" :key="key" :label="val" :value="key" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('system.dict.codeTypeName')" prop="codeTypeName">
              <el-input v-model="form_data.codeTypeName" :placeholder="$t('system.dict.codeTypeName')" type="text" maxlength="20" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-table ref="table" v-loading="form_loading" class="sort-table" :data="form_data.codes" border row-key="id">
        <el-table-column prop="code" :label="$t('system.dict.code')" align="center" width="180">
          <template slot-scope="{row}">
            <template v-if="row.edit">
              <el-input v-model="row.code" size="mini" />
            </template>
            <span v-else>{{ row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="codeValue" :label="$t('system.dict.codeValue')" align="center">
          <template slot-scope="{row}">
            <template v-if="row.edit">
              <el-input v-model="row.codeValue" size="mini" />
            </template>
            <span v-else>{{ row.codeValue }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operations')" align="center" width="200">
          <template slot="header">
            <el-button type="warning" size="mini" @click="addDict()">
              <i class="el-icon-plus" />
              {{ $t('common.add') }}
            </el-button>
          </template>
          <template slot-scope="{row, $index}">
            <el-button-group v-if="row.edit">
              <el-button type="warning" size="mini" @click="cancelEdit(row)">
                <i class="el-icon-refresh" />
                {{ $t('common.cancel') }}
              </el-button>
              <el-button type="success" size="mini" @click="confirmEdit(row)">
                <svg-icon icon-class="floppy" />
                {{ $t('common.save') }}
              </el-button>
            </el-button-group>
            <el-button-group v-else>
              <el-button type="primary" size="mini" @click="row.edit=!row.edit">
                <i class="el-icon-edit" />
                {{ $t('common.edit') }}
              </el-button>
              <el-button type="danger" size="mini" @click="handleDelete($index)">
                <svg-icon icon-class="trash" />
                {{ $t('common.delete') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="form_visible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchDicts, fetchDict, fetchType, deleteType, deleteDict, submitDict } from '@/api/system'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { deepClone } from '@/utils'
import Sortable from 'sortablejs'

const mapToDicts = function(data, dict) {
  const dicts = []
  for (const name in data) {
    const label = dict[name]
    if (data.hasOwnProperty(name)) {
      const dict = { name, items: [], _key: name, label }
      let count = 0
      for (const key in data[name]) {
        if (data[name].hasOwnProperty(key)) {
          const item = { name, key, _key: name + '_' + key, label: data[name][key], items: null }
          dict.items.push(item)
          count++
        }
      }
      dict['count'] = count
      dicts.push(dict)
    }
  }
  return dicts
}
const searchDicts = function(data, type, search) {
  const typeObj = searchText(type, search)
  Object.getOwnPropertyNames(data).forEach(name => {
    const obj = searchText(data[name], search)
    if (Object.keys(obj).length !== 0) {
      typeObj[name] = obj
    }
  })
  return deepClone(typeObj)
}
const searchText = function(dict, search) {
  const obj = {}
  Object.getOwnPropertyNames(dict).forEach(key => {
    if (test(search, dict[key]) || test(search, key)) {
      obj[key] = dict[key]
    }
  })
  return obj
}
const test = function(search, value) {
  const reg = new RegExp(['', ...search, ''].join('.*'))
  return reg.test(value)
}

export default {
  directives: { waves },
  components: { Pagination },
  filters: {
    pagination(data, num, size) {
      const offset = (num - 1) * size
      return (offset + size >= data.length) ? data.slice(offset, data.length) : data.slice(offset, offset + size)
    }
  },
  data() {
    return {
      search_param: {},
      search_loading: false,
      upload_loading: false,
      download_loading: false,
      form_visible: false,
      form_status: 'add',
      form_data: {},
      form_rules: {},
      form_loading: false,
      dict_page: {
        pageNumber: 1,
        pageSize: 5,
        sizes: [5, 10, 20, 50]
      },
      pidOptions: {},
      pidLoading: false,
      submitLoading: false,
      activeName: 'tab1',
      dict_data: {},
      dynamicDicts: [],
      dynamicDictsLoading: false,
      staticDicts: [],
      staticDictsLoading: false
    }
  },
  mounted() {
    this.getDynamicDicts()
    this.getStaticDicts()
  },
  methods: {
    async getDynamicDicts() {
      this.dynamicDictsLoading = true
      const { data } = await fetchDicts('dynamic')
      const dicts = this.$dicts['SYS_CODE_TYPE']
      this.dict_data = deepClone(data)
      this.dynamicDicts = mapToDicts(data, dicts)
      this.dynamicDictsLoading = false
    },
    async getStaticDicts() {
      this.staticDictsLoading = true
      const { data } = await fetchDicts('static')
      const dicts = this.$dicts['SYS_CODE_TYPE']
      this.staticDicts = mapToDicts(data, dicts)
      this.staticDictsLoading = false
    },
    getList() {
      this.search_loading = true
      this.dynamicDictsLoading = true
      const text = this.search_param.search
      if (text !== '') {
        const dicts = this.$dicts['SYS_CODE_TYPE']
        const data = deepClone(this.dict_data)
        const _data = searchDicts(data, dicts, text)
        this.dynamicDicts = mapToDicts(_data, dicts)
      } else {
        const dicts = this.$dicts['SYS_CODE_TYPE']
        const data = deepClone(this.dict_data)
        this.dynamicDicts = mapToDicts(data, dicts)
      }
      this.search_loading = false
      this.dynamicDictsLoading = false
    },
    async addDicts() {
      this.form_status = 'add'
      const { data } = await fetchType(undefined, '')
      this.pidOptions = Object.assign({}, { '': this.$t('common.select') }, data)
      this.form_data = {}
      this.form_data['codes'] = []
      this.form_visible = true
    },
    async editDicts(row) {
      this.form_status = 'edit'
      const { data } = await fetchDict(row.name)
      data['codes'].map(v => {
        this.$set(v, 'edit', false)
        v.originalValue = v.codeValue
        v.originalKey = v.code
        v.originalPid = v.pid
        return v
      })
      this.form_data = data
      if (data.pid) {
        this.form_data.code = data.codeType.split('.').pop()
      }
      const response = await fetchType(data.codeType, '')
      this.pidOptions = response.data
      this.form_visible = true
    },
    deleteDict(row) {
      const key = row.key || row.name
      const name = this.$t('system.Dict') + ' ' + key
      this.$confirm(this.$t('tips.confirm_delete', { name }), this.$t('tips.confirm_delete_title'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning'
      }).then(() => {
        if (row.key) {
          deleteDict(row.name, key).then(_ => {
            this.getSuccessMsg(name)
          })
        } else {
          deleteType(key).then(_ => {
            this.getSuccessMsg(name)
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('tips.delete_cancel', { name })
        })
      })
    },
    getSuccessMsg(name) {
      this.$message({
        type: 'success',
        message: this.$t('tips.delete_success', { name })
      })
    },
    getFormTitle() {
      return this.$t('common.' + this.form_status) + this.$t('system.Dict')
    },
    remoteCodeType(query) {
      this.pidLoading = true
      fetchType(this.form_data.codeType, query).then(({ data }) => {
        this.pidLoading = false
        this.pidOptions = Object.assign({}, { '': this.$t('common.select') }, data)
      })
    },
    handleChange() {
      this.form_data.code = ''
    },
    handleSubmit() {
      this.submitLoading = true
      const data = this.form_data
      if (data.pid) {
        data.codeType = data.pid + '.' + data.code
      }
      submitDict(data).then(_ => {
        this.form_visible = false
        this.submitLoading = false
      })
    },
    addDict() {
      const data = {
        code: '',
        codeType: this.form_data['codeType'] || '',
        codeValue: '',
        pid: (this.form_data['codeType'] || '').split('.').pop(),
        edit: true
      }
      const list = this.form_data['codes'] || []
      list.push(data)
    },
    cancelEdit(row) {
      row.codeValue = row.originalValue
      row.code = row.originalKey
      row.pid = row.originalPid
      row.edit = false
      this.$message({
        message: this.$t('tips.value_restored'),
        type: 'warning'
      })
    },
    confirmEdit(row) {
      row.edit = false
      this.$message({
        message: this.$t('tips.submit_success', { name: row.codeValue }),
        type: 'success'
      })
    },
    handleDelete(index) {
      const code = this.form_data['codes'].splice(index, 1)
      const deleteCodes = this.form_data['deleteCodes'] || []
      code.forEach(c => deleteCodes.push(c.code))
      this.form_data['deleteCodes'] = deepClone(deleteCodes)
    },
    getDicts(data) {
      this.dict_page.pageNumber = data.page
      this.dict_page.pageSize = data.limit
    },
    handleUpload() {},
    handleDownload() {},
    move() {
      const table = document.querySelectorAll('.el-table__body-wrapper > table > tbody')[2]
      const self = this
      Sortable.create(table, {
        onEnd({ newIndex, oldIndex }) {
          const targetRow = self.formData['codes'].splice(oldIndex, 1)[0]
          self.formData['codes'].splice(newIndex, 0, targetRow)
        }
      })
    }
  }
}
</script>
