<template>
  <div class="dashboard-container">
    <component :is="dashboard_name" />
  </div>
</template>

<script>
import settings from '@/settings'
import adminDashboard from './admin'
import editorDashboard from './editor'
import portalDashboard from './portal'
import blankDashboard from './blank'

export default {
  name: 'Dashboard',
  components: { adminDashboard, editorDashboard, portalDashboard, blankDashboard },
  data() {
    return {
      dashboard_name: settings.system.dashboard
    }
  }
}
</script>
