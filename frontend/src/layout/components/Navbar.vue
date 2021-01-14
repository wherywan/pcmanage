<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <el-tooltip v-if="isGlobalSearchEnabled" :content="$t('common.globalsearch')" effect="dark" placement="bottom">
          <search id="header-search" class="right-menu-item" />
        </el-tooltip>

        <error-log class="errLog-container right-menu-item hover-effect" />

        <el-tooltip v-if="isFullScreenEnabled" :content="$t('common.fullscreen')" effect="dark" placement="bottom">
          <screenfull id="screenfull" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip v-if="isGlobalSizeEnabled" :content="$t('common.globalsize')" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip v-if="isLanguageSelectEnabled" :content="$t('common.language_select')" effect="dark" placement="bottom">
          <language-select id="language-select" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip v-if="isMessagesEnabled" :content="$t('system.messages.appName')" effect="dark" placement="bottom" style="line-height: 30px">
          <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
            <el-badge :value="unReadNum" style="margin-top: 12px">
              <message-view id="message-view" class="right-menu-item hover-effect" />
            </el-badge>
            <el-dropdown-menu slot="dropdown">
              <message-drop-down id="message-drop-down" class="right-menu-item hover-effect" />
            </el-dropdown-menu>
          </el-dropdown>
        </el-tooltip>
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img v-if="avatar" :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <el-avatar v-else class="user-avatar">
            <svg-icon icon-class="male" />
          </el-avatar>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/profile/index">
            <el-dropdown-item>{{ $t('menu.Profile') }}</el-dropdown-item>
          </router-link>
          <!-- <router-link to="/">
            <el-dropdown-item>{{ $t('menu.Dashboard') }}</el-dropdown-item>
          </router-link>
          <a target="_blank" href="https://github.com/PanJiaChen/vue-element-admin/">
            <el-dropdown-item>Github</el-dropdown-item>
          </a>
          <a target="_blank" href="https://panjiachen.github.io/vue-element-admin-site/#/">
            <el-dropdown-item>Docs</el-dropdown-item>
          </a> -->
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">{{ $t('common.quit') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import LanguageSelect from '@/components/LanguageSelect'
import Search from '@/components/HeaderSearch'
import MessageView from '@/components/MessageView'
import MessageDropDown from '@/components/MessageDropDown'
import settings from '../../settings'
import { countUserUnreadMessages } from '@/api/system'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    SizeSelect,
    LanguageSelect,
    Search,
    MessageView,
    MessageDropDown
  },
  data() {
    return {
      unReadNum: 0
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    isFullScreenEnabled() {
      return settings.enableFullScreen
    },
    isGlobalSizeEnabled() {
      return settings.enableGlobalSize
    },
    isLanguageSelectEnabled() {
      return settings.enableI18N
    },
    isGlobalSearchEnabled() {
      return settings.enableGlobalSearch
    },
    isMessagesEnabled() {
      return settings.enableGlobalMessages
    }
  },
  created() {
    if (settings.enableGlobalMessages) {
      this.getNumsOfUnreadMessage()
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
      window.location = '/'
    },
    async getNumsOfUnreadMessage() {
      const { data } = await countUserUnreadMessages()
      this.unReadNum = data
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
