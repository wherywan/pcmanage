import settings from '../src/settings'

const tokens = {
  admin: {
    token: 'admin-token'
  },
  editor: {
    token: 'editor-token'
  }
}

const users = {
  'admin-token': {
    roles: ['admin'],
    introduction: 'I am a super administrator',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Super Admin'
  },
  'editor-token': {
    roles: ['editor'],
    introduction: 'I am an editor',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Normal Editor'
  }
}

export default [
  // user login
  {
    url: '/user/login',
    type: 'post',
    response: config => {
      const { username } = config.body
      const token = tokens[username]

      // mock error
      if (!token) {
        return {
          code: -1,
          message: 'Account and password are incorrect.'
        }
      }

      return {
        flag: 0,
        data: null,
        msg: null
      }
    }
  },

  // get user info
  {
    url: '/profile/info\.*',
    type: 'get',
    response: _ => {
      const token = settings.mockToken

      if (!token) {
        return {
          flag: -6,
          msg: 'Login failed, unable to get user details.'
        }
      }

      // return {
      //   flag: 0,
      //   data: {
      //     roles: ['admin'],
      //     introduction: 'I am a super administrator',
      //     avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
      //     name: 'Super Admin'
      //   }
      // }
      return {
        flag: 0,
        data: {
          sid: 'admin',
          username: 'admin',
          nickname: '张三',
          password: null,
          params: {
            'FULL_NAME': '张三',
            'LAST_NAME': '张',
            'FIRST_NAME': '三'
          },
          perms: ['perm-0001', 'perm-0002', 'SUPER_ADMIN'],
          source: 'DB'
        },
        msg: null,
        meta: {
          globalTraceNumber: null,
          version: null,
          requestTime: null,
          responseTime: null
        }
      }
    }
  },

  // user logout
  {
    url: '/user/logout',
    type: 'post',
    response: _ => {
      return {
        flat: 0,
        data: 'success'
      }
    }
  }
]
