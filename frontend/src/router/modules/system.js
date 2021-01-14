import Layout from '@/layout'

const systemRouter = {
  path: '/system',
  component: Layout,
  redirect: 'noRedirect',
  name: 'system',
  isMenu: true,
  meta: {
    title: 'menu.System',
    icon: 'cog-fill'
  },
  children: [
    {
      path: 'users',
      component: () => import('@/views/system/users'),
      name: 'system-users',
      isMenu: true,
      meta: { title: 'menu.system.Users', icon: 'peoples' }
    },
    {
      path: 'roles',
      component: () => import('@/views/system/roles'),
      name: 'system-roles',
      isMenu: true,
      meta: { title: 'menu.system.Roles', icon: 'role' }
    },
    {
      path: 'orgs',
      component: () => import('@/views/system/orgs'),
      name: 'system-orgs',
      isMenu: true,
      meta: { title: 'menu.system.Orgs', icon: 'org' }
    },
    {
      path: 'resources',
      component: () => import('@/views/system/resources'),
      name: 'system-resources',
      isMenu: true,
      meta: { title: 'menu.system.Resources', icon: 'nested' }
    },
    // {
    //   path: 'dicts',
    //   component: () => import('@/views/system/dicts'),
    //   name: 'system-discs',
    //   isMenu: true,
    //   meta: { title: 'menu.system.Dict', icon: 'dict' }
    // },
    // {
    //   path: 'caches',
    //   component: () => import('@/views/system/caches'),
    //   name: 'system-caches',
    //   isMenu: true,
    //   meta: { title: 'menu.system.Caches', icon: 'chip' }
    // },
    // {
    //   path: 'schedules',
    //   component: () => import('@/views/system/schedules'),
    //   name: 'system-schedules',
    //   meta: { title: 'menu.system.Crons', icon: 'cron' }
    // },
    // {
    //   path: 'params',
    //   component: () => import('@/views/system/params'),
    //   name: 'system-params',
    //   isMenu: true,
    //   meta: { title: 'menu.system.Params', icon: 'params' }
    // },
    {
      path: 'operations',
      component: () => import('@/views/system/operations'),
      name: 'system-operations',
      isMenu: true,
      meta: { title: 'menu.system.Operations', icon: 'log' }
    },
    // {
    //   path: 'logs',
    //   component: () => import('@/views/system/logs'),
    //   name: 'system-logs',
    //   isMenu: true,
    //   meta: { title: 'menu.system.Logs', icon: 'console' }
    // },
    // {
    //   path: 'messages',
    //   component: () => import('@/views/system/messages'),
    //   name: 'system-messages',
    //   isMenu: true,
    //   meta: { title: 'menu.system.Messages', icon: 'message' }
    // },
    // {
    //   path: 'notices',
    //   component: () => import('@/views/system/notices'),
    //   name: 'system-notices',
    //   isMenu: true,
    //   meta: { title: 'menu.system.Notices', icon: 'notice_center' }
    // }
  ]
}

export default systemRouter
