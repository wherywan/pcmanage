<template>
  <div ref="terminal" />
</template>

<script>
import 'xterm/css/xterm.css'
import { Terminal } from 'xterm'
import { FitAddon } from 'xterm-addon-fit'
// import { AttachAddon } from 'xterm-addon-attach'
import { WebLinksAddon } from 'xterm-addon-web-links'

export default {
  name: 'Terminal',
  props: {
    terminal: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      term: null
    }
  },
  mounted() {
    this.term = new Terminal()
    const fitAddon = new FitAddon()
    this.term.loadAddon(fitAddon)
    this.term.loadAddon(new WebLinksAddon())
    // this.term.loadAddon(new AttachAddon())
    this.term.open(this.$refs.terminal)
    this.term.setOption('fontSize', 14)
    fitAddon.fit()
  },
  beforeDestroy() {}
}
</script>
