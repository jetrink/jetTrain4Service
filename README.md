Teach 4 Service [![pipeline status](https://gitlab.com/Opus55/Teach4Service/badges/master/pipeline.svg)](https://gitlab.com/Opus55/Teach4Service/commits/master)
---
Online tutoring application.

The backend REST API is...
- written in Java, using the Dropwizard
- built with gradle
- runs without a container (such as tomcat)
 
The frontend web page is...
- written in javascript (ES6), SCSS, and Pug/Jade
- using AngularJs using Angular-Material
- built with webpack and lots plugins

#### Formatting and stuff
- all javascript and SCSS files shall be indented with tabs
- See the intellijSettings.jar for the java backend formatting standards.

#### Setup an IDE (frontend)
I recommend [WebStorm](https://www.jetbrains.com/webstorm/), but you can use whatever text editor you want. Just mind the formatting rules (see above).
If you do in fact choose to use WebStorm, you should import Abrar's formatting config:
- Use Abrar's formatting config
    1. File > Import Settings
    2. Select the `IntellijSettings.jar` from the root of this repo
    3. open File > Settings
    4. On the left, expand `Editor` and then select `Code Style`
    5. On the upper right, click `Manage`
    6. Select `AbrarJava` from the list of schemes
    7. `Control-Alt-L` is the keybinding to auto-format

### Build and Test (frontend)
This project uses NPM and requires that it be installed and accessible from the path. The frontend section of this application is isolated to the `frontend` folder. The `frontend` folder contains a fully functional NPM project. Running `npm run dev` in the `frontend` folder will build the frontend, fire up the webpack dev server, and then watch the `frontend/app` folder for changes. In order for the frontend to actually work, the `/api` routes must be reverse proxied to a running instance of the java backend REST API. THis is configurable in the `webpack.config.js`

Note: Windows Users config autocrlf to true to avoid invalid line breaks error on gulp compilation.
git config --global core.autocrlf "input" and got config --global core.autocrlf true. Do this before cloning repository.

#### Setup an IDE (backend)
1. Get the JDK 8..
2. Get  [Intellij Idea](https://www.jetbrains.com/idea/#chooseYourEdition). You can get a student licence for the ultimate edition, but the community edition is fine. You can technically use eclipse, but Intellij is better.
3. Ensure that you have Mysql or MariaDB setup. You will be unable to run the backend without it. See steps 10 and 11 for migrations.
4. Clone this repository
5. Click import, and select the `build.gradle` file. The default settings are fine.
6. Copy the `config.example.yml` to `config.yml` and set your DB credentials.
7. Use Abrar's formatting config
    1. File > Import Settings
    2. Select the `IntellijSettings.jar` from the root of this repo
    3. open File > Settings
    4. On the left, expand `Editor` and then select `Code Style`
    5. On the upper right, click `Manage`
    6. Select `AbrarJava` from the list of schemes
    7. Control-Alt-L is the keybinding to auto-format
8. Install the Project Lombok plugin
    1. open File > Settings
    2. On the left select `Plugins`
    3. Near the bottom, click `Browse repositories...`
    4. Search for `lombok` and select the `Lombok Plugin`
    5. Click install
    6. Restart Intellij Idea
9. Setup server run configuration
    1. Run > Edit Configurations
    2. click the `+` on the upper left of the dialog, and select `Application`
    3. Main Class is `edu.utdallas.utdesign.teach4service.T4SApplication`
    4. Set the program arguments to `server config.yml`. The config should a derivative the example config.
    5. It runs on port 8080 by default.
10. Setup DB migration run configurations
    1. Run > Edit Configurations
    2. click the `+` on the upper left of the dialog, and select `Application`
    3. Main Class is `edu.utdallas.utdesign.teach4service.T4SApplication`
    4. Set the program arguments to `db migrate config.yml`. The config should a derivative the example config.
    5. May be convenient to create a second DB cleaning configuration with the following arguments: `db clean config.yml`
    6. more info on flyway DB run configs [here](https://github.com/dropwizard/dropwizard-flyway)
11. Before you can run the server, you will need to ensure the DB is setup first.
    1. Do step 10 and setup the DB migration run configurations.
    2. Run the migration. If it fails run the db cleaning configuration and then the migration again.


### Build and Test
This project uses Gradle. If you have Gradle installed, you can use the `gradle` command. Otherwise you must use the gradle wrapper; `./gradlew` on *nix, and `gradlew.bat` on windows. All three of these are functionally equivalent.
- `gradle build` will compile and package the entire application. The compiled jar can be found in `build/libs` and the packaged distributions can be found in `build/distributions`. This includes building the frontend and packaging it with the backend in the same jar.
- `gradle run` will compile and run the application from the command line.

