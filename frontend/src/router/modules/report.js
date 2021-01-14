import Layout from '@/layout'

const reportRouter = {
  path: '/report',
  component: Layout,
  redirect: 'noRedirect',
  name: 'report',
  isMenu: true,
  meta: {
    title: 'menu.AutoReport',
    icon: 'form'
  },
  children: [
    {
      path: 'publish',
      component: () => import('@/views/report/report-list'),
      name: 'report-publish',
      isMenu: true,
      meta: { title: 'menu.report.Publish', icon: 'page' }
    },
    {
      path: 'edit',
      component: () => import('@/views/report/fillform-list'),
      name: 'report-edit',
      isMenu: true,
      meta: { title: 'menu.report.Edit', icon: 'edit' }
    }
  ]
}

export default reportRouter
