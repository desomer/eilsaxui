/**
 * 
 */
package com.elisaxui.component.widget.log;

import com.elisaxui.core.xui.xhtml.builder.javascript.jsclass.JSClass;

/**
 * @author gauth
 *   https://stackoverflow.com/questions/6604192/showing-console-errors-and-alerts-in-a-div-inside-the-page
 *   
 */
public interface JSConsole extends JSClass {
  /*
   * var log = document.querySelector('#log');
['log','debug','info','warn','error'].forEach(function (verb) {
    console[verb] = (function (method, verb, log) {
        return function () {
            method.apply(console, arguments);
            var msg = document.createElement('div');
            msg.classList.add(verb);
            msg.textContent = verb + ': ' + Array.prototype.slice.call(arguments).join(' ');
            log.appendChild(msg);
        };
    })(console[verb], verb, log);
});
   */
}
