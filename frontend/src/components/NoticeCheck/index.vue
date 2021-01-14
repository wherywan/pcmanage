<template>
  <el-popover
    placement="bottom"
    :title="$t('system.Notice')"
    width="260"
    @show="GetUserSystemNotices()"
  >
    <div slot="reference">
      <svg-icon icon-class="alert" />
    </div>
    <div v-loading="list_loading" style="margin-top:12px;margin-bottom:12px">
      <div v-for="item in datalist.slice(0, 5)" :key="item.id">
        <div class="notice-info-box">
          <div class="notice-info-option">
            <el-button
              plain
              circle
              size="mini"
              icon="el-icon-search"
              @click="CheckNoticeInfoDetail(item.id)"
            />
          </div>
          <div class="notice-info-title">{{ item.title }}</div>
          <div class="notice-info-publish-time">{{ item.publishTime }}</div>
        </div>
      </div>
    </div>
    <div style="text-align:center">
      <el-button type="info" @click="CheckNoticesInfo()">{{
        $t('system.notice.showAll')
      }}</el-button>
    </div>
    <el-dialog
      title="公告详情"
      :visible.sync="notice_info_detail_dialog_visible"
      append-to-body
      class="detail-dialog"
    >
      <div v-loading="detail_loading" style="margin-bottom:50px">
        <h2 style="text-align:center">{{ cur_notice.title }}</h2>
        <div
          style="margin-top:20px; margin-bottom:20px"
          v-html="cur_notice.content"
        />
        <div style="float:right;font-size:90%;color:grey">
          <div>发布时间：{{ cur_notice.publishTime }}</div>
          <div>发布者：{{ cur_notice.createdBy }}</div>
        </div>
      </div>
    </el-dialog>
  </el-popover>
</template>

<style scoped>
.notice-info-box {
  padding: 12px 20px;
  border-bottom: 0.5px solid rgba(128, 128, 128, 0.8);
}
.notice-info-box:hover {
  background: #f0f0f0;
}
.notice-info-box:hover .notice-info-option {
  display: inherit;
  float: right;
  margin-top: 6px;
}
.notice-info-option {
  display: none;
}
.notice-info-title {
  margin-bottom: 6px;
}
.notice-info-publish-time {
  font-size: 80%;
  color: grey;
  font-weight: 400;
}
.detail-dialog >>> .el-dialog__body {
  padding-top: 0px;
}
</style>

<script>
import {
  fetchUserSystemNotice,
  fetchSystemNoticeDetail
} from '@/api/system-notices'
export default {
  data() {
    return {
      notice_info_detail_dialog_visible: false,
      notices_info_dialog_visible: false,
      list_loading: false,
      datalist: [],
      detail_loading: false,
      cur_notice: {}
    }
  },
  methods: {
    async GetUserSystemNotices() {
      this.list_loading = true
      const { data } = await fetchUserSystemNotice()
      this.datalist = data
      this.list_loading = false
    },
    async CheckNoticeInfoDetail(noticeId) {
      this.notice_info_detail_dialog_visible = true
      this.GetNoticeDetail(noticeId)
    },
    async GetNoticeDetail(noticeId) {
      this.detail_loading = true
      const { data } = await fetchSystemNoticeDetail(noticeId)
      const notice_data = data
      this.cur_notice = notice_data
      this.detail_loading = false
    },
    async CheckNoticesInfo() {
      this.$nextTick(() => {
        this.$router.push({ path: '/profile/index', query: { tab: 'notices' }})
      })
    },
    CollapseChanged(noticeId) {
      if (noticeId !== '' && noticeId !== undefined && noticeId !== null) {
        this.GetNoticeDetail(noticeId)
      }
    }
  }
}
</script>
