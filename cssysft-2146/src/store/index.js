import { createStore } from 'vuex'

const files = import.meta.globEager('./modules/*.js')

let modules = {}
for (const path in files) {
  modules[path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')] = files[path].default
  modules[path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')]['namespaced'] = true
}
console.log(modules)

const store = createStore({
  modules
})

export default store
