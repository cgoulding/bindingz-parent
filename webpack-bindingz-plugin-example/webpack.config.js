'use strict';

var BindingzPlugin = require('./bindingz-plugin');

module.exports = function makeWebpackConfig() {
  let config = {
    module: {},
    node: {
      fs: 'empty'
    }
  };
  config.plugins = [/** Other build plugins **/]
  config.plugins.push(new BindingzPlugin());
  return config;
}();
