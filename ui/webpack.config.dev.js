const webpack = require("webpack");
const path = require("path");
const HtmlWebpackPlugin = require("html-webpack-plugin");

process.env.NODE_ENV = "development";

module.exports = {
  mode: "development",
  target: "web",
  devtool: "cheap-module-source-map",
  //entry: "./src/components/login/index",
  entry: {
    login: "./src/components/login/index.js",
    dashboard: "./src/components/dashboard/index.js",
    //member: "./src/components/member/index.js",
    web: "./src/components/webpost/index.js",
    vendors: ["react"]
  },
  output: {
    filename: "./[name].bundle.js"
  },
  /*
  output: {
    path: path.resolve(__dirname, "build"),
    publicPath: "/",
    filename: "bundle.js"
  },
*/
  watch: true,
  devServer: {
    stats: "minimal",
    overlay: true,
    historyApiFallback: true,
    disableHostCheck: true,
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET,POST,DELETE,PUT,OPTIONS,PATCH",
      "Access-Control-Allow-Headers": "*",
      "Access-Control-Allow-Credentials": "true",
      "Access-Control-Max-Age": "1800"
    },
    https: false
  },
  plugins: [
    new webpack.DefinePlugin({
      "process.env.API_URL": JSON.stringify("http://localhost:7072/api/v1"),
      "process.env.SESSION_TIMEOUT": 15 * 60000 //15 minutes
    }),
    /*new HtmlWebpackPlugin({
      filename: 'landing_page.html',
      template: '!!prerender-loader?string!landing_page.html',
    }),*/
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery"
    }),
    new HtmlWebpackPlugin({
      filename: "./login.html",
      template: "./src/components/login/index.html",
      chunks: ["vendor", "login"],
      favicon: "src/resources/favicon.ico",
      hash: true,
      title: "Tracker - Login",
      myPageHeader: "Hello World"
    }),
    new HtmlWebpackPlugin({
      // Also generate a test.html
      filename: "./dashboard.html",
      template: "./src/components/dashboard/index.html",
      chunks: ["vendor", "dashboard"],
      favicon: "src/resources/favicon.ico",
      hash: true,
      title: "Tracker - Dashboard",
      myPageHeader: "Hello World"
    }),
    /*new HtmlWebpackPlugin({
      filename: "./member.html",
      template: "./src/components/member/index.html",
      chunks: ["vendor", "member"],
      favicon: "src/resources/favicon.ico",
      hash: true,
      title: "Tracker - Member",
      myPageHeader: "Hello World"
    }),*/
    new HtmlWebpackPlugin({
      filename: "./web.html",
      template: "./src/components/webpost/index.html",
      chunks: ["vendor", "web"],
      favicon: "src/resources/favicon.ico",
      hash: true,
      title: "Tracker - web",
      myPageHeader: "Hello World"
    })
  ],
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: ["babel-loader", "eslint-loader"]
      },
      {
        test: /(\.css)$/,
        use: ["style-loader", "css-loader"]
      }
    ]
  }
};
