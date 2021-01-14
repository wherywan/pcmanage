import Layout from '@/layout'
import settings from '@/settings'

const devtoolsRouter = {
  path: '/devtools',
  component: Layout,
  redirect: 'noRedirect',
  name: 'DevTools',
  alwaysShow: true,
  hidden: !settings.showBetaPages,
  meta: {
    title: 'menu.DevTools',
    icon: 'toolkit'
  },
  children: [
    {
      path: 'apis',
      component: () => import('@/views/blank'),
      name: 'DynamicTable',
      meta: { title: 'menu.devtools.APIs', icon: 'api' }
    },
    {
      path: 'routers',
      component: () => import('@/views/blank'),
      name: 'DynamicTable',
      meta: { title: 'menu.devtools.Routers', icon: 'router' }
    },
    {
      path: 'views',
      component: () => import('@/views/blank'),
      name: 'DynamicTable',
      meta: { title: 'menu.devtools.Views', icon: 'page' }
    },
    {
      path: 'i18n',
      component: () => import('@/views/blank'),
      name: 'DynamicTable',
      meta: { title: 'menu.devtools.I18Ns', icon: 'i18n' }
    }
  ]
}

export default devtoolsRouter
