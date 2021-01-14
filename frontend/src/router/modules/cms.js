import Layout from '@/layout'
import settings from '@/settings'

const cmsRouter = {
  path: '/cms',
  component: Layout,
  redirect: 'noRedirect',
  name: 'CMS',
  alwaysShow: true,
  hidden: !settings.showBetaPages,
  meta: {
    title: 'menu.CMS',
    icon: 'documentation'
  },
  children: [
    {
      path: 'articles',
      component: () => import('@/views/table/complex-table'),
      name: 'DynamicTable',
      meta: { title: 'menu.cms.Articles', icon: 'documentation' }
    }
  ]
}

export default cmsRouter
