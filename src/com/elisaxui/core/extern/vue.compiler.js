/** modif dans vues.js ligne 8702 : n utilise plus l 'object document 
 *    faire plutot un proxy de ducument
 * 
 * */

function compileVueJs(t) {
      var t = t.replace(/\s*\n+\s*/g, " "),
            t = t.replace(/>\s+/g, ">").replace(/\s+</g, "<"),
            t = Vue.compile(t),
            t = "{ staticRender : [" + t.staticRenderFns.toString() + "], \nrender : " + t.render.toString().replace(/^function anonymous/, "function") + "}",
            t = js_beautify(t, {
                wrap_line_length: 80,
                break_chained_methods: !0
            }):
            
            return t:
        }