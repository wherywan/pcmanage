import settings from '@/settings'
import { deepClone } from '@/utils'
import { fetchMenus } from '@/api/system'
import { asyncRoutes } from '@/router'
import i18n from '@/i18n'

export async function getMenus() {
  let menu_list = []
  if (settings.system.menu.mode === 'frontend') {
    const routes = deepClone([...asyncRoutes])
    const filter = function(routes, path) {
      const menus = []
      for (let i = 0; i < routes.length; i++) {
        const obj = routes[i]
        if (obj.isMenu !== undefined && obj.isMenu) {
          const menu = { id: obj.name, name: obj.meta ? i18n.t(obj.meta.title) : obj.path, status: true }
          if (obj.meta) {
            menu.perms = obj.meta.role
            menu.icon = obj.meta.icon
          }
          menu.url = (path || '') + (obj.path.charAt(0) === '/' ? '' : '/') + obj.path
          if (obj.children) {
            menu.children = filter(obj.children, menu.url)
          }
          menus.push(menu)
        }
      }
      return menus
    }
    menu_list = filter(routes)
  }
  if (settings.system.menu.mode === 'backend') {
    const { data } = await fetchMenus()
    menu_list = data
  }
  return new Promise((resolve) => {
    resolve(menu_list)
  })
}
