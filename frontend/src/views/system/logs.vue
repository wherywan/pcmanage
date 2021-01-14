<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <svg-icon icon-class="console" />
        系统日志
        <el-button-group style="float: right;">
          <el-button type="primary" @click="toggleTail">
            <svg-icon v-if="!tailStatus" icon-class="play" />
            <svg-icon v-if="tailStatus" icon-class="pause" />
          </el-button>
          <el-button type="danger" @click="clear">
            <svg-icon icon-class="trash" />
          </el-button>
        </el-button-group>
      </div>
      <Terminal ref="xterm" :terminal="terminal" />
    </el-card>
  </div>
</template>

<script>
import Terminal from '@/components/Terminal'
import { addHandler, delHandler, sendMessage } from '@/api/system-ws'

export default {
  components: { Terminal },
  data() {
    return {
      terminal: {},
      tailStatus: false
    }
  },
  mounted() {
    const that = this
    addHandler('sys-log', function(msg_pack) {
      const { data } = msg_pack
      const message = `[${data.timestamp}][${data.level}] - ${data.threadName} - [${data.loggerName}] - ${data.message}`
      that.$refs.xterm.term.writeln(message)
    })
    this.toggleTail()
  },
  destroyed() {
    delHandler('sys-log')
    sendMessage('sys-log', { on: false })
  },
  methods: {
    toggleTail() {
      this.tailStatus = !this.tailStatus
      sendMessage('sys-log', { on: this.tailStatus })
    },
    clear() {
      this.$refs.xterm.term.clear()
    }
  }
}
</script>
