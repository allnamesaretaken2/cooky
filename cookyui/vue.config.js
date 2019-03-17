// vue.config.js
module.exports = {
  configureWebpack: {

	devServer: {
        
        proxy: {
            '/rest/^': {
              target: 'http://localhost:8080',
              secure: false
            }
        },
		port : 3000
      }
  }
}