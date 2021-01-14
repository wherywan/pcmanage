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
        :clearable="true"
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
      <el-button-group v-show="false" style="float: right">
        <el-button
          v-waves
          class="filter-item"
          style="margin-left: 10px"
          :title="$t('common.add')"
          disabled
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
      @expand-change="handleEdit"
    >
      <el-table-column type="expand">
        <template slot-scope="{ row }">
          <el-form label-position="left" inline class="table-expand">
            <el-form-item label="消息标题">
              <span>{{ row.title }}</span>
            </el-form-item>
            <el-form-item label="消息详情">
              <span>{{ row.content }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('userMessages.senderId')"
        prop="senderId"
        align="center"
        width="120"
      >
        <template slot-scope="{ row }">
          <span>{{ row.senderId }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('userMessages.messageType')"
        prop="messageType"
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <span>{{ row.messageType }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('userMessages.title')"
        prop="title"
        align="center"
        width="150"
        :show-overflow-tooltip="isShowOverFlow"
      >
        <template slot-scope="{ row }">
          <span>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('userMessages.content')"
        prop="content"
        align="center"
        :show-overflow-tooltip="isShowOverFlow"
      >
        <template slot-scope="{ row }">
          <span>{{ row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('userMessages.isRead')"
        prop="isRead"
        align="center"
        width="100"
      >
        <template slot-scope="{ row }">
          <el-button-group>
            <el-button
              disabled
              size="mini"
              :type="(row.isRead !== '已读')? 'primary' :'success'"
              @click="handleEdit(row)"
            >
              <span>{{ row.isRead }}</span>
            </el-button>
          </el-button-group>
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
          :model="form_data"
          label-position="right"
          label-width="120px"
        >
          <el-col :span="15">
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
          <el-col :span="15">
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
            <el-form-item
              :label="$t('system.messages.receiver')"
              prop="receiver"
            >
              <el-input
                v-model="form_data.target"
                :placeholder="$t('system.messages.orgIdOrRoleId')"
                type="text"
                maxlength="10"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="1">&nbsp;</el-col>
          <el-col :span="10">
            <el-form-item
              :label="$t('system.messages.title')"
              prop="title"
            >
              <el-input
                v-model="form_data.title"
                :placeholder="$t('system.messages.title')"
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
<style>
  .table-expand {
    font-size: 0;
    width: 100%;
  }
  .table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 100%;
  }
</style>
<script>
import { getUserMessagesBySendType, saveUserMessage, saveMessage } from '@/api/system'
import Pagination from '@/components/Pagination/index'
import waves from '@/directive/waves/index'
import { deepClone } from '@/utils/index'

export default {
  name: 'MessagesList',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      show_button: [],
      list_loading: false,
      uploadLoading: false,
      downloadLoading: false,
      submitLoading: false,
      is_disabled: false,
      isShowOverFlow: true,
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
      }
    }
  },
  async created() {
    this.getDicts()
    await this.getListByType()
  },
  methods: {
    async getListByType() {
      this.list_loading = true
      var param = {
        sendType: this.search_param.sendType,
        searchContent: this.search_param.searchContent
      }
      this.message_Param = param
      const { data } = await getUserMessagesBySendType(this.message_Param, this.page)
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
    handleUpload() {
    },
    handleDownload() {
    },
    async handleEdit(row) {
      if (row.isRead !== 'Y' && row.isRead !== '已读') {
        const message_data = deepClone(row)
        message_data.isRead = 'Y'
        row.isRead = '已读'
        await saveUserMessage(message_data)
        this.$forceUpdate()
        // this.getListByType()
      }
    },
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
    showDeleteConfirm(portalApp) {
      this.$confirm(
        this.$t('tips.confirm_delete', { name: portalApp.appName }),
        this.$t('common.tips'),
        {
          type: 'warning',
          confirmButtonText: this.$t('common.confirm'),
          cancelButtonText: this.$t('common.cancel')
        }
      )
        .then(async _ => {
          await this.handleDelete(portalApp.id)
        })
        .catch(() => {
        })
    },
    async handleSubmit() {
      this.$refs['dataForm'].validate(async valid => {
        if (valid) {
          this.submitLoading = true
          const data = deepClone(this.form_data)
          await saveMessage(data)
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
    }
  }
}
</script>
