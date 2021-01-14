import store from '@/store'
import { fetchPermissions } from '@/api/system'
import settings from '@/settings'
import $lodash from 'lodash'

/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission(value) {
  if (value && value instanceof Array && value.length > 0) {
    const roles = store.getters && store.getters.roles
    const permissionRoles = value

    const hasPermission = roles.some(role => {
      return permissionRoles.includes(role)
    })

    if (!hasPermission) {
      return false
    }
    return true
  } else {
    console.error(`need roles! Like v-permission="['admin','editor']"`)
    return false
  }
}

export function getFrontPerms() {
  return [
    // {
    //   key: 'fnt-perm-1',
    //   name: '权限f1',
    //   remark: '权限f1说明'
    // }, {
    //   key: 'fnt-perm-2',
    //   name: '权限f2',
    //   remark: '权限f2说明'
    // }
  ]
}

export async function getPermissions() {
  let perm_list = []
  if (settings.system.perm.mode === 'frontend' || settings.system.perm.mode === 'mixed') {
    perm_list = $lodash.union(perm_list, getFrontPerms())
  }
  if (settings.system.perm.mode === 'backend' || settings.system.perm.mode === 'mixed') {
    const { data } = await fetchPermissions()
    perm_list = $lodash.union(perm_list, data.datalist)
  }
  return new Promise((resolve, reject) => {
    resolve(perm_list)
  })
}
