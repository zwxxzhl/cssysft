import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'

const files = import.meta.globEager('./modules/*.js')

let modules = {}
for (const path in files) {
  modules[path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')] = files[path].default
  modules[path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')]['namespaced'] = true
}
console.log(modules)

const store = createStore({
  modules,
  plugins: [createPersistedState({
    storage: window.sessionStorage,
    reducer(val) {
      return {
        app: val.user,
        permission: val.permission,
        user: val.user
      }
    }
  })]
})

export default store
