<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">

        <el-col :span="6" :xs="24">
          <user-card :user="user" @handle-edit="handleEdit" />
        </el-col>

        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="active_tab">
              <el-tab-pane :label="$t('menu.profile.activity')" name="activity">
                <activity :user="user" />
              </el-tab-pane>
              <el-tab-pane v-if="isMessagesEnabled" :label="$t('menu.profile.messages')" name="messages">
                <user-messages />
              </el-tab-pane>
              <!--<el-tab-pane :label="$t('menu.profile.systemNotice')" name="notices">-->
              <!--<user-notices />-->
              <!--</el-tab-pane>-->
              <el-tab-pane :label="$t('menu.profile.settingsAll')" name="account">
                <account :user="user" />
              </el-tab-pane>
              <el-tab-pane :label="$t('menu.profile.settingsApp')" name="settings">
                <app-setting :user="user" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UserCard from './components/UserCard'
import Activity from './components/Activity'
import Account from './components/Account'
import AppSetting from './components/AppSetting'
import userMessages from './components/userMessages'
import settings from '@/settings'

export default {
  name: 'Profile',
  components: { UserCard, Activity, Account, AppSetting, userMessages },
  data() {
    return {
      user: {},
      org_list: [],
      active_tab: this.$route.query.tab || 'activity'
    }
  },
  computed: {
    ...mapGetters([
      'sid',
      'name',
      'introduction',
      'avatar',
      'perms',
      'params'
    ]),
    isMessagesEnabled() {
      return settings.enableGlobalMessages
    }
  },
  watch: {
    '$route'(to) {
      this.active_tab = to.query.tab || 'activity'
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      this.user = {
        sid: this.sid,
        name: this.name,
        introduction: this.introduction,
        roles: this.perms,
        params: this.params,
        avatar: this.avatar
      }
    },
    handleEdit() {
      this.active_tab = 'account'
    }
  }
}
</script>
