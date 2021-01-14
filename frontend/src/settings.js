module.exports = {

  /**
   * @type {string} build path
   */
  buildPath: '../src/main/resources/static/',

  /**
   * @type {string} title
   * @description Title of the application
   */
  title: '江苏省建行',

  /**
   * @type {boolean} true | false
   * @description Whether enable multi language support
   */
  enableI18N: true,

  /**
   * @type {boolean} true | false
   * @description Whether enable fullscreen feature
   */
  enableFullScreen: true,

  /**
   * @type {boolean} true | false
   * @description Whether enable global search feature
   */
  enableGlobalSearch: true,

  /**
   * @type {boolean} true | false
   * @description Whether enable global size feature
   */
  enableGlobalSize: true,

  /**
   * @type {boolean} true | false
   * @description Whether enable global messages feature
   */
  enableGlobalMessages: false,

  /**
   * @type {string} '00' ~ '03' ‘99’
   * @description The login strategy of the application
   * 00 - No login
   * 01 - Login with username and password (session)
   * 02 - Login with username and password (JWT)
   * 03 - Login via SSO JWT in header
   * 04 - Login via SSO JWT in cookie
   * 99 - Custom strategy (You might need to implement some logic where with comment 'CUSTOM LOGIN STRATEGY' by yourself)
   */
  loginStrategy: '04',

  /**
   * @type {string | null} 'Login page url' | null
   * Login page will show up if this is not null
   */
  loginPage: null,

  /**
   * @type {string} JWT key in header
   * @description Might be used in login strategy '02' and '03'
   */
  jwtHeaderKey: 'Access-Token',

  /**
   * @type {string} JWT key in cookie
   * @description Might be used in login strategy '04'
   */
  jwtCookieKey: 'ACCESS_TOKEN',

  /**
   * @type {string} JWT key in request param
   * @description Mightn be used in every login strategy involved with JWT
   */
  jwtParamKey: 'token',

  /**
   * @type {string} JWT Key in session storage
   */
  jwtSessionKey: 'ark_token',

  /**
   * @type {integer} Token missing flag
   * @description Login expire response code, same as http status 403
   */
  loginFailFlag: -6,

  /**
   * @type {integer} Token expiration flag
   * @description Login expire response code, same as http status 403
   */
  tokenFailFlag: -9,

  /**
   * @type {boolean} true | false
   * @description Whether use mock token in dev modes
   */
  mockTokenOn: true,

  /**
   * @type {string} Mock token
   */
  //
  mockToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzOTc3MTgyNyIsIm5iZiI6MTU4NTA5MjE1NSwiaXNzIjoiQVJLLUZyYW1ld29yayIsInBlcm1zIjpbIlNVUEVSX0FETUlOIl0sImlhdCI6MTU4NTA5MjE1NSwianRpIjoiT05sN2tOSnEiLCJvcmdJZCI6IjMyMDAwMDAwMCJ9.TA0RLNutHpAU016hqCS2Z_JnYGs0NrwHvS_AFtKB50Q',

  // 10533976
  // mockToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDUzMzk3NiIsIm5iZiI6MTU4NTA5MjE1NSwiaXNzIjoiQVJLLUZyYW1ld29yayIsInBlcm1zIjpbIlNVUEVSX0FETUlOIl0sImlhdCI6MTU4NTA5MjE1NSwianRpIjoiT05sN2tOSnEiLCJvcmdJZCI6IjMyMDAwMDAwMCJ9.MWuCHFz9MlHlFoeYjcJa35c1lAFWwZhk4fhIGLeu0-I',
  // mockToken: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDUzMzk3MSIsIm5iZiI6MTU4NTA5MjE1NSwiaXNzIjoiQVJLLUZyYW1ld29yayIsInBlcm1zIjpbIlNVUEVSX0FETUlOIl0sImlhdCI6MTU4NTA5MjE1NSwianRpIjoiT05sN2tOSnEiLCJvcmdJZCI6IjMyMDAwMDAwMCJ9.Ag6DNZn0rHKtBkjtRaRbflBUsDsWEqh4k6otWWma5S4',

  /**
   * @type {boolean} true | false
   * @description Whether show the settings right-panel
   */
  showSettings: false,

  /**
   * @type {boolean} true | false
   * @description Whether need tagsView
   */
  tagsView: true,

  /**
   * @type {boolean} true | false
   * @description Whether fix the header
   */
  fixedHeader: true,

  /**
   * @type {boolean} true | false
   * @description Whether show the logo in sidebar
   */
  sidebarLogo: true,

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'development',

  showDemoPages: false,

  showBetaPages: false,

  client: {

    timeout: 100000

  },

  system: {

    dashboard: 'blankDashboard',

    user: {

      enableCustomId: true,

      /**
       * @type {string} 'local' | 'remote'
       * @description This determines how to read user data
       * local  - User data save local db
       * remote - Get user data through interface
       */
      mode: 'remote'

    },

    menu: {

      /**
       * @type {string} 'backend' | 'frontend'
       * @description This determines how the application will behave when interactive with menu feature
       * backend  - Items of menu are defined at backend
       *            The menu management will show
       *            App will strictly use data from backend as menu permission tree
       * frontend - Items of menu are defined at frontend, menu management will not show
       *            The menu management will not show
       *            App will use data from routers as menu permission tree
       *            Router must be marked as menu: isMenu = true
       */
      mode: 'frontend'

    },

    perm: {

      /**
       * @type {string} 'none' | 'backend' | 'frontend' | 'mixed'
       * @description This determines how the application will behave when interactive with permission feature
       * none     - Permission mode closed
       *            Permission list will not show in role management
       * backend  - Items of permission are defined at backend
       * frontend - Items of permission are defined at frontend
       * mixed    - Items of permission are defined both at frontend and backend
       */
      mode: 'mixed'

    },

    role: {

      /**
       * @type {boolean} true | false
       * @description Whether you can add user to a role in role management
       */
      userMngOn: true

    },

    org: {

      /**
       * @type {string} 'full' | 'simple'
       * @description The organization manage mode
       * full   - Show detailed fields of organizations
       * simple - Show only the basic fields of organization (orgId, orgName)
       */
      mode: 'full',

      /**
       * @type {string} 'full' | 'simple'
       * @description The organization show name mode
       * full   - Show full name of organization (orgName)
       * simple - Show short name of organization (orgNameShort)
       */
      showName: 'simple',

      /**
       * @type {boolean} true | false
       * @description Whether you can add user to an organization in organization management
       */
      userMngOn: true,

      /**
       * @type {boolean} true | false
       * @description Whether to synchronize remote organization data
       */
      synchronize: true

    }

  }

}
