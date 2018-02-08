"use strict";

let path = require('path');
let HtmlWebpackPlugin = require("html-webpack-plugin");
let ExtractTextPlugin = require("extract-text-webpack-plugin");
let CopyWebpackPlugin = require("copy-webpack-plugin");
let StyleLintPlugin = require('stylelint-webpack-plugin');

// if running via `npm run dev`
const isDev = "run" === process.env.npm_lifecycle_event;

module.exports = () => {
	let config = {
		entry: "./src/app/app.js",
		target: "web",
		output: {
			path: path.resolve(__dirname, 'dist/static'),
			filename: "scripts.[hash].js"
		},
		devServer: {
			contentBase: path.resolve(__dirname, 'dist/static'),
			inline: true,
			historyApiFallback: true,
			port: 3000,
			proxy: {

				"/api": "http://localhost:8080" // local server
				// "/api": "https://t4s.abrarsyed.com/Teach4Service/app" // AWS
			},

		},
		module: {
			rules: [
				{
					test: /\.jpg$/, use: ["file-loader"]
				},
				{
					test: /\.png$/, use: ["url-loader?mimetype=image/png"]
				},
				{
					test: /\.js$/,
					loader: "babel-loader",
					exclude: /node_modules/
				},
				{
					test: /\.(pug|jade)$/,
					loader: ["html-loader", "pug-html-loader"],
					exclude: /node_modules/
				},
				{
					test: /\.html$/,
					loader: "html-loader",
					exclude: /node_modules/
				},
			]
		},
		plugins: [
			new HtmlWebpackPlugin({
				template: "./src/public/index.html",
				inject: "body"
			}),
			new CopyWebpackPlugin([{
				from: __dirname + "/src/public"
			}]),
			new StyleLintPlugin({
				failOnError: !isDev,
				quiet: false,
			}),
		]
	};

	if (isDev) {
		config.devtool = "inline-source-map";
	}
	else {
		config.devtool = "source-map";
	}

	// SASS/SCSS loading and preprocessing
	{
		const extractSass = new ExtractTextPlugin({
			filename: "styles.[contenthash].css",
			disable: isDev
		});

		config.plugins.push(extractSass);
		config.module.rules.push({
			test: /\.s[a|c]ss$/,
			use: extractSass.extract({
				use: [
					{
						loader: "css-loader",
						query: {
							import: false,
							sourceMap: true,
						}
					},
					{
						loader: "postcss-loader",
						options: {
							plugins: (loader) => [
								require("autoprefixer")(),
								require("cssnano")()
							]
						}
					},
					"sass-loader",
				],
				// use style-loader in development
				fallback: "style-loader"
			})
		});
	}

	return config;
};
