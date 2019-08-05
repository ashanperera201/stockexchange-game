// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiUrl: "http://localhost:8080/api/",
  firebase: {
    apiKey: "AIzaSyC9DlJ_WHwfdEyEg-uFtt25SjqUlsIUR6Y",
    authDomain: "stockexchange-d7c52.firebaseapp.com",
    databaseURL: "https://stockexchange-d7c52.firebaseio.com",
    projectId: "stockexchange-d7c52",
    storageBucket: "",
    messagingSenderId: "1006592503985",
    appId: "1:1006592503985:web:40725d60b79428ba"
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
