const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: {
        index:'./js/script.ts',
        viewPage: './js/view-page.ts'
    },
    devServer: {
        static: {
            directory: path.join(__dirname, '')},
        port: 9000,
        hot: true

    },
    output: {
        path: path.join(__dirname,'/js'),
        filename: '[name].js',
    },
    resolve: {
        extensions: [ '.ts', '.js' ]

    },
    module:{
        rules: [{
            test: /\.ts$/,
            use: 'ts-loader',
            exclude: /node_modules/
        },
        {
            test: /\.html$/,
            use:[{
                loader: 'file-loader',
                options: {
                    name: '[name].[ext]',

                }
            }],
            exclude: [path.resolve(__dirname, 'html/index.html'),
                    path.resolve(__dirname,'html/view-page.html')
        ]           
        }
    ]
    },

    plugins: [
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'html/index.html',
            excludeChunks: ['viewPage']
        }),
        new HtmlWebpackPlugin({
            chunks: ['viewPage'],
            filename: 'view-page.html',
            template: 'html/view-page.html',
            excludeChunks: ['index']
        })     
    ]
}