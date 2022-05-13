/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./client/js/view-page.ts":
/*!********************************!*\
  !*** ./client/js/view-page.ts ***!
  \********************************/
/***/ (() => {

eval("\r\n//--creating global variable\r\nconst peopleList = [];\r\nvar test = [];\r\nlet logged;\r\nif (sessionStorage.people) {\r\n    peopleList.push(JSON.parse(sessionStorage.people)[0]);\r\n}\r\nif (sessionStorage.loggedPerson) {\r\n    logged = JSON.parse(sessionStorage.loggedPerson);\r\n}\r\nconst welcome = document.querySelector(\".welcome\");\r\nsetInterval(() => {\r\n    welcome.addEventListener(\"DOMContentLoaded\", function () {\r\n        welcome.textContent = `${logged.name}`;\r\n        console.log(\"Entrei aqui\");\r\n    });\r\n}, 2000);\r\n//EventListener\r\n\n\n//# sourceURL=webpack://ZG-Linketinder/./client/js/view-page.ts?");

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module can't be inlined because the eval devtool is used.
/******/ 	var __webpack_exports__ = {};
/******/ 	__webpack_modules__["./client/js/view-page.ts"]();
/******/ 	
/******/ })()
;