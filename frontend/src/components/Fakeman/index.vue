<template>
  <div>
    <el-row>
      <el-col :span="2">
        <el-select v-model="request.method" :disabled="requesting">
          <el-option v-for="item in methodOptions" :key="item.key" :value="item.key" :label="item.label" />
        </el-select>
      </el-col>
      <el-col :span="20">
        <el-input v-model="request.url" type="text" :disabled="requesting" @blur="syncURLToParams" />
      </el-col>
      <el-col :span="2">
        <el-button type="primary" style="width: 100%;" @click="handleRequest">
          <i v-if="requesting" class="el-icon-loading" />
          <svg-icon v-if="!requesting" icon-class="play" />
        </el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-tabs tab-position="top">
          <el-tab-pane label="Params">
            <el-table ref="paramsTable" :data="request.params" @selection-change="handleParamsSelectionChange" @select-all="handleParamsSelectAll">
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="KEY" align="center">
                <template slot-scope="{ row, $index }">
                  <el-input v-model="row.key" placeholder="key" :disabled="requesting" @blur="handleParamInputBlur(row, $index)" />
                </template>
              </el-table-column>
              <el-table-column label="VALUE" align="center">
                <template slot-scope="{ row, $index }">
                  <el-input v-model="row.value" placeholder="value" :disabled="requesting" @blur="handleParamInputBlur(row, $index)" />
                </template>
              </el-table-column>
              <el-table-column align="center" width="80">
                <template slot-scope="{ row, $index }">
                  <el-button v-if="$index !== request.params.length - 1" type="danger" size="small" @click="handleParamDelete(row, $index)">
                    <i class="el-icon-close" />
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <!-- <el-tab-pane label="Headers">
            <el-table :data="request.headers">
              <el-table-column label="KEY" align="center">
                <template slot-scope="{ row }">
                  <el-input v-model="row.key" placeholder="key" :disabled="requesting" @blur="refreshHeaders" />
                </template>
              </el-table-column>
              <el-table-column label="VALUE" align="center">
                <template slot-scope="{ row }">
                  <el-input v-model="row.value" placeholder="value" :disabled="requesting" @blur="refreshHeaders" />
                </template>
              </el-table-column>
              <el-table-column align="center" width="80">
                <template slot-scope="{ row }">
                  <el-button v-if="row.key || row.value" type="danger">
                    <i class="el-icon-close" />
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane> -->
          <!-- <el-tab-pane label="Body">
            <el-radio v-for="item in bodyTypeOptions" :key="item" v-model="bodyType" :label="item" @change="refreshHeaders">{{ item }}</el-radio>
            <el-select v-if="bodyType==='raw'" v-model="contentType" clearable @change="refreshHeaders">
              <el-option v-for="item in contentTypeOptions" :key="item.key" :value="item.key">{{ item.label }}</el-option>
            </el-select>
            <NoData v-if="bodyType==='none'" />
          </el-tab-pane> -->
        </el-tabs>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-tabs tab-position="top" style="min-height: 200px;">
          <el-tab-pane label="Body">
            <NoData v-if="!response.body" />
          </el-tab-pane>
          <el-tab-pane label="Cookies">
            <NoData v-if="response.cookies.length === 0" />
          </el-tab-pane>
          <el-tab-pane label="Headers">
            <NoData v-if="response.headers.length === 0" />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import url from 'url'
import querystring from 'querystring'
// import axios from 'axios'
import NoData from '@/components/NoData'

const containsKey = function(target, key) {
  if (typeof target !== 'object' || typeof key !== 'string') return false
  return Object.keys(target)
    .some(k => (k === key) || containsKey(target[k], key))
}

export default {
  name: 'FakeMan',
  components: { NoData },
  props: {
    init: {
      type: Object,
      default: () => {
        return {
          url: '',
          method: 'get'
        }
      }
    }
  },
  data() {
    return {
      requesting: false,
      methodOptions: [
        {
          key: 'get',
          label: 'GET'
        }, {
          key: 'post',
          label: 'POST'
        }, {
          key: 'put',
          label: 'PUT'
        }, {
          key: 'delete',
          label: 'DELETE'
        }
      ],
      bodyTypeOptions: [
        'none',
        'form-data',
        'raw'
      ],
      contentTypeOptions: [
        {
          key: 'application/json',
          label: 'JSON(application/json)'
        },
        {
          key: 'text/plain',
          label: 'Text(text/plain)'
        },
        {
          key: 'application/xml',
          label: 'XML(application/xml)'
        },
        {
          key: 'text/xml',
          label: 'XML(text/xml)'
        }
      ],
      request: {
        method: '',
        url: '',
        headers: [],
        headers_selected: [],
        params: [],
        params_selection: []
      },
      contentType: '',
      response: {
        headers: [],
        cookies: [],
        body: ''
      },
      fullURL: ''
    }
  },
  mounted: function() {
    this.request.method = this.init.method || 'get'
    this.request.url = this.init.url || window.location.protocol + '//' + window.location.host + '/test/?ddd=123&ddd=123'
    this.syncURLToParams()
  },
  methods: {
    syncURLToParams() {
      const url_obj = url.parse(this.request.url)
      this.request.hash = url_obj.hash
      this.request.baseURL = url_obj.protocol + '//' + url_obj.host + url_obj.pathname

      const params = querystring.parse(url_obj.query)
      const param_arr = []
      const param_selection_arr = []
      for (const key in params) {
        const param_current = params[key]
        if (param_current instanceof Array) {
          for (let i = 0; i < param_current.length; i++) {
            const value = param_current[i]
            const param = { key, value }
            param_arr.push(param)
            param_selection_arr.push(param)
          }
        } else {
          const param = { key, value: params[key] }
          param_arr.push(param)
          param_selection_arr.push(param)
        }
      }
      param_arr.push({})
      this.request.params = param_arr
      this.request.params_selection = []
      this.$nextTick(() => {
        this.toggleSelection('paramsTable', param_selection_arr)
      })
    },
    syncParamsToURL() {
      const params = {}
      for (let i = 0; i < this.request.params_selection.length; i++) {
        const param = this.request.params_selection[i]
        if (containsKey(params, param.key)) {
          if (params[param.key] instanceof Array) {
            params[param.key].push(param.value)
          } else {
            params[param.key] = [params[param.key], param.value]
          }
        } else {
          params[param.key] = param.value
        }
      }
      this.request.url = this.request.baseURL + ('?' + querystring.encode(params)) + (this.request.hash || '')
    },
    handleParamsSelectionChange(selections) {
      this.request.params_selection = selections
      this.syncParamsToURL()
    },
    handleParamsSelectAll(selection) {
      if (selection.length > 0) {
        this.$refs.paramsTable.toggleRowSelection(selection[selection.length - 1])
      }
    },
    toggleSelection(key, rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs[key].toggleRowSelection(row)
        })
      } else {
        this.$refs[key].clearSelection()
      }
    },
    handleParamInputBlur(row, $index) {
      if ($index === this.request.params.length - 1) {
        if ((row.key !== undefined && row.key !== '') || (row.value !== undefined && row.value !== '')) {
          this.request.params.push({})
          this.toggleSelection('paramsTable', [row])
        }
      }
      this.syncParamsToURL()
    },
    handleParamDelete(row, $index) {
      this.request.params = this.$lodash.without(this.request.params, row)
      this.request.params_selection = this.$lodash.without(this.request.params_selection, row)
      console.log(this.request.params_selection)
    },
    handleRequest() {

    }
  }
}
</script>
