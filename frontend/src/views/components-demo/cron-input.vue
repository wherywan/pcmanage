
<template>
  <div class="app-container">
    <cron-input v-model="value" mode="simple" />
  </div>
</template>

<script>
import CronInput from '@/components/CronInput'

export default {
  name: 'CronInputDemo',
  components: { CronInput },
  data() {
    return {
      value: '* * * * * ? 1234'
    }
  },
  methods: {}
}
</script>
