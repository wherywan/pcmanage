<template>
  <el-card style="margin-bottom:20px;">
    <div class="user-profile">
      <div class="box-center">
        <div class="block">
          <el-avatar v-if="user.avatar" :size="120" :src="user.avatar" />
          <el-avatar v-else :size="120">
            <svg-icon icon-class="male" class-name="big" />
          </el-avatar>
        </div>
      </div>
      <div class="box-center">
        <div class="user-name text-center">{{ user.introduction }}</div>
        <div class="user-role text-center text-muted">
          <span v-for="role in user.roles" :key="role.roleId" style="margin-right: 2px;">{{ role.roleName }}</span>
        </div>
      </div>
    </div>

    <div class="user-bio">
      <div class="user-bio-section">
        <div class="user-bio-section-header"><svg-icon icon-class="org" /><span>{{ $t('system.Org') }}</span></div>
        <div class="user-bio-section-body">
          <div class="text-muted">
            {{ user.params['ORG_NAME'] }}
          </div>
        </div>
      </div>

      <div class="user-bio-section">
        <div class="user-bio-section-header">
          <svg-icon icon-class="skill" />
          <span>{{ $t('common.userInfo') }}</span>
          <el-button type="text" class="fr" style="padding-top: 3px;" @click="handleEdit">
            <svg-icon icon-class="edit" />
          </el-button>
        </div>
        <div class="user-bio-section-body">
          <div class="progress-item">
            <span class="text-title">{{ $t('common.mobile') }}</span>
            <span class="text-muted fr">{{ user.params['MOBILE'] || '无' }}</span>
          </div>
          <div class="progress-item">
            <span class="text-title">{{ $t('common.email') }}</span>
            <span class="text-muted fr">{{ user.params['EMAIL'] || '无' }}</span>
          </div>
          <div class="progress-item">
            <span class="text-title">{{ $t('common.birthday') }}</span>
            <span class="text-muted fr">{{ user.params['BIRTHDAY'] ? $moment(user.params['BIRTHDAY']).format('YYYY-MM-DD') : '无' }}</span>
          </div>
          <div class="progress-item">
            <span class="text-title">{{ $t('common.sex') }}</span>
            <span class="text-muted fr">{{ user.params['SEX'] ? $dicts['SEX'][user.params['SEX']] : '无' }}</span>
          </div>
          <div class="progress-item">
            <span class="text-title">{{ $t('common.lastVisited') }}</span>
            <span class="text-muted fr">{{ $moment(user.params['LAST_VISITED']).format('YYYY-MM-DD HH:mm:ss') }}</span>
          </div>
          <div class="progress-item">
            <span class="text-title">{{ $t('IP') }}</span>
            <span class="text-muted fr">{{ user.params['IP'] || '127.0.0.1' }}</span>
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          introduction: '',
          params: {},
          avatar: '',
          roles: []
        }
      }
    }
  },
  methods: {
    handleEdit() {
      this.$emit('handle-edit')
    }
  }
}
</script>

<style lang="scss" scoped>
.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
}

.big {
  font-size: 40px;
  margin-top: 35px;
}

.user-profile {
  .user-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .user-role {
    padding-top: 10px;
    font-weight: 400;
    font-size: 14px;
  }

  .box-social {
    padding-top: 30px;

    .el-table {
      border-top: 1px solid #dfe6ec;
    }
  }

  .user-follow {
    padding-top: 20px;
  }
}

.user-bio {
  margin-top: 20px;
  color: #606266;

  span {
    padding-left: 4px;
  }

  .user-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .user-bio-section-header {
      border-bottom: 1px solid #dfe6ec;
      padding-bottom: 10px;
      margin-bottom: 10px;
      font-weight: bold;
    }

    .progress-item {
      display: flow-root;
      margin-bottom: 10px;
      font-size: 14px;

      .text-muted {
        margin-right: 15px;
      }
    }
  }
}
</style>
