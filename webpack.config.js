module.exports = {
    entry: './assets/javascripts/App.jsx',
    output: {
        path: './public/javascripts',
        publicPath: "/javascripts",
        filename: 'app.bundle.js'
    },
    watch: true,
    module: {
        loaders: [{
            test: /\.jsx?$/,
            exclude: /node_modules/,
            loader: 'babel-loader',
            query: {
                presets: ['react', 'es2015', "stage-0"]
            }
        }]
    }
};