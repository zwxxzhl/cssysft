import { createStore } from 'vuex'

let files = import.meta.glob('./modules/*.js')
let modules = {};
for (const path in files) {
  files[path]().then((file) => {
      modules[path.replace(/\.\/|modules\/|\.js/g, '')] = file.default
      //加入namespaced:true，用于解决vuex命名冲突
      modules[path.replace(/\.\/|modules\/|\.js/g, '')]['namespaced'] = true
      console.log(modules);
  })
}

export default createStore({
    modules
})
