import Layout from '@/layout'

const portalRouter = {
  path: '/portal',
  component: Layout,
  redirect: 'noRedirect',
  name: 'portal',
  isMenu: true,
  alwaysShow: true,
  meta: {
    title: 'menu.Portal',
    icon: 'cog-fill'
  },
  children: [
    {
      path: 'sub-application',
      component: () => import('@/views/portal/SubApplication'),
      name: 'portal-sub-application',
      isMenu: false,
      meta: { title: 'menu.portal.SubApplication', icon: 'application-server' }
    }
  ]
}

export default portalRouter
