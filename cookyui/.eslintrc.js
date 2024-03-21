module.exports = {
    root: true,
    env: {
        // this section will be used to determine which APIs are available to us
        // (i.e are we running in a browser environment or a node.js env)
        node: true,
        browser: true,
    },
    parserOptions: {
        // specifying a module sourcetype prevent eslint from marking import statements as errors
        sourceType: 'module',
    },
    extends: [
        'plugin:vue/recommended',
        '@vue/standard',
    ],
    rules: {

        indent: ['error', 4],
        'comma-dangle': ['error', 'always-multiline'],
        'vue/html-closing-bracket-newline': ['error', {
            singleline: 'never',
            multiline: 'never',
        }],
        'vue/max-attributes-per-line': 0,
        'vue/multiline-html-element-content-newline': 0,
        'vue/html-indent': ['error', 4],
        'vue/singleline-html-element-content-newline': 0,
        'vue/multi-word-component-names' : 'off'

    },
}
