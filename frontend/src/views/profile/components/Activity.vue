<template>
  <div class="user-activity">
    <el-timeline v-loading="list_loading" v-infinite-scroll="load" :infinite-scroll-disabled="list_loading" class="infinite-list" style="overflow:auto">
      <el-timeline-item
        v-for="(data, index) in datalist"
        :key="index"
        :type="data.reqRest | statusFilter"
        :color="data.color"
        size="large"
        placement="top"
        :timestamp="data.timestamp"
      >
        <div class="post">
          <div class="user-block">
            <span class="username text-muted">{{ data.funcName + ' - ' + data.modelName }}</span>
            <span class="description">{{ $moment(data.operBgnTs).format('YYYY-MM-DD HH:mm:ss.SSS') }}</span>
          </div>
        </div>
      </el-timeline-item>
    </el-timeline>
    <p v-if="list_loading" style="text-align: center;line-height: 16px;">{{ $t('common.loading') }}</p>
  </div>
</template>

<script>
import { fetchOperations } from '@/api/system'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        SUCCESS: 'success',
        FAIL: 'danger'
      }
      return statusMap[status]
    }
  },
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          sid: '',
          name: '',
          roles: [],
          params: {},
          perms: []
        }
      }
    }
  },
  data() {
    return {
      list_loading: false,
      page: {
        pageNumber: 0,
        pageSize: 10,
        totalPage: 0,
        totalRecord: 0
      },
      datalist: []
    }
  },
  methods: {
    load() {
      this.list_loading = true
      this.page.pageNumber += 1
      fetchOperations(this.page, { userId: this.user.sid }).then(({ data }) => {
        data.datalist.forEach(node => { this.datalist.push(node) })
        this.page = data.page
        this.list_loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-activity {
  .infinite-list {
    max-height: 530px;
  }

  .user-block {

    .username,
    .description {
      display: block;
      margin-left: 50px;
      padding: 2px 0;
    }

    .username{
      font-size: 16px;
      color: #000;
    }

    :after {
      clear: both;
    }

    .img-circle {
      border-radius: 50%;
      width: 40px;
      height: 40px;
      float: left;
    }

    span {
      font-weight: 500;
      font-size: 12px;
    }
  }

  .post {
    font-size: 14px;
    border-bottom: 1px solid #d2d6de;
    margin-bottom: 15px;
    padding-bottom: 15px;
    color: #666;

    .image {
      width: 100%;
      height: 100%;

    }

    .user-images {
      padding-top: 20px;
    }
  }

  .list-inline {
    padding-left: 0;
    margin-left: -5px;
    list-style: none;

    li {
      display: inline-block;
      padding-right: 5px;
      padding-left: 5px;
      font-size: 13px;
    }

    .link-black {

      &:hover,
      &:focus {
        color: #999;
      }
    }
  }

}

.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
}
</style>
