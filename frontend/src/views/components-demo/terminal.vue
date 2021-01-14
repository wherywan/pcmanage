<template>
  <div class="components-container">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        Terminal
      </div>
      <el-row>
        <el-button type="primary" icon="play" @click="toggleAppend">
          Run
        </el-button>
      </el-row>
      <el-row>
        <Terminal ref="console" :terminal="terminal" />
      </el-row>
    </el-card>
  </div>
</template>

<script>
import Terminal from '@/components/Terminal'

export default {
  components: { Terminal },
  data() {
    return {
      terminal: {}
    }
  },
  beforeDestroy() {
    clearInterval(this.toggle)
  },
  methods: {
    toggleAppend() {
      if (this.toggle) {
        clearInterval(this.toggle)
      } else {
        this.toggle = setInterval(() => {
          this.$refs.console.term.writeln('please visit: https://xtermjs.org/docs/api/terminal/classes/terminal/')
        }, 1000)
      }
    }
  }
}
</script>
