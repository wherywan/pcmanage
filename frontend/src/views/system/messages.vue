<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select
        v-model="search_param.sendType"
        :placeholder="$t('system.messages.sendType')"
        clearable
        style="width: 200px"
        class="filter-item"
      >
        <el-option
          v-for="item in sendTypes"
          :key="item.key"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-input
        v-model="search_param.searchContent"
        :placeholder="$t('system.messages.content')"
        style="width: 200px"
        class="filter-item"
      />
      <el-button
        v-waves
        :disabled="list_loading"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        {{ $t('common.search') }}
      </el-button>
      <el-button-group style="float: right">
        <el-button
          v-waves
          class="filter-item"
          style="margin-left: 10px"
          :title="$t('common.add')"
          type="primary"
          icon="el-icon-plus"
          @click="handleAdd"
        />
        <el-button
          v-waves
          :loading="uploadLoading"
          class="filter-item"
          :title="$t('common.import')"
          type="primary"
          icon="el-icon-upload2"
          disabled
          @click="handleUpload"
        />
        <el-button
          v-waves
          :loading="downloadLoading"
          class="filter-item"
          :title="$t('common.export')"
          type="primary"
          icon="el-icon-download"
          disabled
          @click="handleDownload"
        />
      </el-button-group>
    </div>
    <el-table
      v-loading="list_loading"
      :data="datalist"
      border
      fit
      highlight-current-row
      stripe
      style="width: 100%"
    >
      <el-table-column
        :label="$t('system.messages.sendType')"
        prop="sendType"
        align="center"
        width="150"
      >
        <template slot-scope="{ row }">
          <span>{{ row.sendType }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.messages.target')"
        prop="target"
        align="center"
        width="150"
      >
        <template slot-scope="{ row }">
          <span>{{ row.target }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.messages.title')"
        prop="title"
        align="center"
        width="250"
      >
        <template slot-scope="{ row }">
          <span>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.messages.content')"
        prop="content"
        align="center"
      >
        <template slot-scope="{ row }">
          <span>{{ row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('system.messages.validDate')"
        prop="validDate"
        align="center"
        width="200"
      >
        <template slot-scope="{ row }">
          <span>{{ row.validDate }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="page.totalPage>0"
      :total="page.totalRecord"
      :page.sync="page.pageNumber"
      :limit.sync="page.pageSize"
      style="padding: 0; margin-top: 20px"
      @pagination="getListByType"
    />

    <el-dialog :visible.sync="form_visible" width="60%">
      <div slot="title">
        <svg-icon icon-class="message" />
        {{ getFormTitle() }}
      </div>
      <div style="float: left; width: 100%">
        <el-form
          ref="dataForm"
          :rules="formRules"
          :model="form_data"
          label-position="right"
          label-width="120px"
        >
          <el-col :span="10">
            <el-form-item
              :label="$t('system.messages.sendType')"
              prop="sendType"
            >
              <el-select
                v-model="form_data.sendType"
                :placeholder="$t('system.messages.allTypes')"
                clearable
                class="filter-item"
              >
                <el-option
                  v-for="item in sendTypes"
                  :key="item.key"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="1">&nbsp;</el-col>
          <el-col :span="12">
            <el-form-item
              :label="$t('system.messages.validDate')"
              prop="validDate"
            >
              <el-date-picker
                v-model="form_data.validDate"
                style="width: 95%"
                type="datetime"
                :value-format="'yyyy-MM-dd HH:mm:ss'"
                placeholder="选择日期时间"
                align="right"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label="$t('system.messages.select_org')">
              <TreeSelect
                v-model="orgTarget"
                :props="{ value: 'id', label: 'orgName', children: 'children' }"
                :options="orgTree"
                :clearable="true"
                :accordion="true"
                style="width: 100%"
                :disabled="form_data.sendType !== 'org'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="1">&nbsp;</el-col>
          <el-col :span="13">
            <el-form-item :label="$t('system.messages.select_roles')">
              <el-col :span="21">
                <el-select v-model="roleTarget" style="width: 100%" :disabled="form_data.sendType !== 'role'">
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId"
                  />
                </el-select>
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              :label="$t('system.messages.title')"
              prop="title"
            >
              <el-input
                v-model="form_data.title"
                :placeholder="$t('system.messages.title')"
                :clearable="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              :label="$t('system.messages.content')"
              prop="content"
            >
              <el-col :span="24">
                <el-input
                  v-model="form_data.content"
                  :placeholder="$t('system.messages.content')"
                  suffix-icon="el-icon-notebook-1"
                  :clearable="true"
                />
              </el-col>
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="form_visible = false">
          {{ $t('common.cancel') }}
        </el-button>
        <el-button
          type="primary"
          :loading="submitLoading"
          @click="handleSubmit"
        >
          {{ $t('common.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchMessages, fetchMessagesByType, saveMessage, fetchOrgTree, fetchRoles } from '@/api/system'
import Pagination from '@/components/Pagination'
import waves from '@/directive/waves'
import TreeSelect from '@/components/TreeSelect'

export default {
  name: 'MessagesList',
  components: { Pagination, TreeSelect },
  directives: { waves },
  data() {
    return {
      list_loading: false,
      uploadLoading: false,
      downloadLoading: false,
      submitLoading: false,
      datalist: [],
      search_param: {
        sendType: '',
        searchContent: ''
      },
      message_Param: {},
      sendTypes: [],
      page: {
        pageNumber: 1,
        pageSize: 10,
        totalPage: 0,
        totalRecord: 0
      },
      formStatus: 'add',
      form_visible: false,
      form_data: {
        sendType: '',
        target: '',
        title: '',
        content: '',
        validDate: ''
      },
      formRules: {
        sendType: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        title: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }],
        content: [{ required: true, message: this.$t('tips.is_required'), trigger: 'blur' }]
      },
      orgTree: [],
      roleOptions: [],
      orgTarget: '',
      roleTarget: ''
    }
  },
  async created() {
    await this.getListByType()
    this.getDicts()
    await this.getOrgTree()
    await this.getRoles()
  },
  methods: {
    async getList() {
      this.list_loading = true
      const { data } = await fetchMessages()
      this.datalist = data
      this.list_loading = false
    },
    async getListByType() {
      this.list_loading = true
      this.message_Param = this.search_param
      const { data } = await fetchMessagesByType(this.message_Param, this.page)
      this.datalist = data.datalist
      this.page = data.page
      this.list_loading = false
    },
    getFormTitle() {
      return (
        this.$t('common.' + this.formStatus) +
        this.$t('system.messages.appName')
      )
    },
    handleFilter() {
      this.page.pageNumber = 1
      this.getListByType()
    },
    handleUpload() {},
    handleDownload() {},
    async handleEdit(row) {},
    async handleAdd() {
      this.formStatus = 'add'
      this.showMessageForm()
    },
    async showMessageForm() {
      this.form_visible = true
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].resetFields()
      }
      this.submitLoading = false
    },
    hideMessageSendForm() {
      this.form_visible = false
    },
    async handleSubmit() {
      this.$refs['dataForm'].validate(async valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form_data.sendType === 'org') {
            this.form_data.target = this.orgTarget
          } else if (this.form_data.sendType === 'role') {
            this.form_data.target = this.roleTarget
          }
          var message = {
            sendType: this.form_data.sendType,
            validDate: this.form_data.validDate,
            target: this.form_data.target,
            title: this.form_data.title,
            content: this.form_data.content
          }
          await saveMessage(message)
          this.submitLoading = false
          this.hideMessageSendForm()
          this.getListByType()
        }
      })
    },
    getDicts() {
      const dicts = this.$dicts['MessageParam']
      let i = 1
      for (const key in dicts) {
        this.sendTypes.push({ key: i++, value: key, label: dicts[key] })
      }
    },
    async getOrgTree() {
      const { data } = await fetchOrgTree()
      this.orgTree = data
    },
    async getRoles() {
      const { data } = await fetchRoles()
      this.roleOptions = data.datalist
      console.log(this.roleOptions)
    }
  }
}
</script>
