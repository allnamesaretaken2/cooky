// vue.config.js
module.exports = {
    configureWebpack: {

        devServer: {
            proxy: 'http://localhost:8080',
            port: 3000,
        },
    }
}
