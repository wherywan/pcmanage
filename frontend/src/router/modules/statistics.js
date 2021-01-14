import Layout from '@/layout'

const sqaRouter = {
  path: '/sqa',
  component: Layout,
  redirect: 'noRedirect',
  name: 'sqa',
  isMenu: true,
  meta: {
    title: 'menu.Statistics',
    icon: 'chart'
  },
  children: [
    {
      path: 'pdeposit',
      component: () => import('@/views/report/personal-deposit'),
      name: 'personal-deposit',
      isMenu: true,
      meta: { title: 'menu.report.Pdeposit', icon: 'deposit' }
    },
    {
      path: 'fund',
      component: () => import('@/views/report/fund'),
      name: 'fund',
      isMenu: true,
      meta: { title: 'menu.report.Fund', icon: 'fund' }
    },
    {
      path: 'sameBusi',
      component: () => import('@/views/report/sameBusi-personal-deposit'),
      name: 'sameBusi',
      isMenu: true,
      meta: { title: 'menu.report.SameBusi', icon: 'samebusi' }
    },
    {
      path: 'insurance',
      component: () => import('@/views/report/personal-deposit'),
      name: 'insurance',
      isMenu: true,
      meta: { title: 'menu.report.Insurance', icon: 'insurance' }
    }
  ]
}

export default sqaRouter
