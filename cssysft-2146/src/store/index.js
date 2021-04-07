import { createStore } from 'vuex'

const files = import.meta.glob('./modules/*.js')
let modules = {}
for (const path in files) {
  files[path]().then((file) => {
    modules[path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')] = file.default
    modules[path.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')]['namespaced'] = true
    console.log(path, file)
    console.log(modules)
  })
}
const store = createStore({
  modules
})
console.log(store);

export default store
