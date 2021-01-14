<template>
  <div class="app-container">
    <el-table
      v-loading="list_loading"
      :data="datalist"
      fit
      highlight-current-row
      stripe
      style="width: 100%"
    >
      <el-table-column
        :label="$t('userMessages.lastMessages')"
        prop="unReadNum"
        align="left"
        width="250"
      >
        <template slot-scope="{ row }">
          <span>{{ row.senderId }}</span><span>发来消息：</span>
          <router-link to="/profile/index">
            <span style="color:dodgerblue">{{ row.title }}</span>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <div align="center">
      <router-link to="/profile/index">
        <span style="font-size: 13px; color: gray">
          查看更多消息
        </span>
      </router-link>
    </div>
  </div>
</template>

<script>
import { getUserMessagesBySendType } from '@/api/system'
import waves from '@/directive/waves'

export default {
  name: 'MessagesList',
  directives: { waves },
  data() {
    return {
      sid: 'z2LUYIO6',
      list_loading: false,
      datalist: [],
      search_param: {
        sendType: '',
        searchContent: ''
      },
      message_Param: {},
      sendTypes: [],
      page: {
        pageNumber: 1,
        pageSize: 5,
        totalPage: 0,
        totalRecord: 0
      }
    }
  },
  async created() {
    await this.getListByType()
  },
  methods: {
    async getListByType() {
      this.list_loading = true
      var param = {
        sid: this.sid,
        sendType: this.search_param.sendType,
        searchContent: this.search_param.searchContent
      }
      this.message_Param = param
      const { data } = await getUserMessagesBySendType(this.message_Param, this.page)
      this.datalist = data.datalist
      this.page = data.page
      this.list_loading = false
    }
  }
}
</script>
