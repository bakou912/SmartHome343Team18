# SmartHome Project for SOEN 343 - Team 18

### Requirements

#### Backend

First, make sure you have the `Java JDK 1.8` and `Maven 3.6.*` installed on your machine. 
Also, make sure both installation folders are added to the PATH (JAVA_HOME and M2_HOME).

To install dependencies, compile source code and build the application, run the following in the `backend` directory:
```
mvn clean install
```

To skip tests, simply add the command parameter `-DSkipTests`

To run tests, run the following command in the `backend` directory:
```
mvn clean test
```

To start the app, write the following command in the `backend` directory:
```
mvn clean spring-boot:run
```
Once started, the app will be exposed on `http://localhost:8080`.

#### Frontend

First make sure you have the latest version of `Nodejs` installed on your machine.

## Available Scripts
To install all the dependecies make sure to run the following command in the frontend project directory `frontend`:

### `npm install`

In the project directory `frontend`, you can run:

### `npm start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.<br />
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.
