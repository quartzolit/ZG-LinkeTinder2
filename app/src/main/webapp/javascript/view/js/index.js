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

/***/ "./js/httpRequest.ts":
/*!***************************!*\
  !*** ./js/httpRequest.ts ***!
  \***************************/
/***/ ((__unused_webpack_module, exports) => {

eval("\r\nObject.defineProperty(exports, \"__esModule\", ({ value: true }));\r\nexports.sendPostToServer = void 0;\r\nfunction sendPostToServer(url, body) {\r\n    let request = new XMLHttpRequest();\r\n    request.open(\"POST\", url, true);\r\n    request.setRequestHeader(\"Content-Type\", \"signup/json\");\r\n    request.send(JSON.stringify(body));\r\n    request.onload = () => {\r\n        console.log(request.responseText);\r\n    };\r\n    return request.responseText;\r\n}\r\nexports.sendPostToServer = sendPostToServer;\r\n\n\n//# sourceURL=webpack://ZG-Linketinder/./js/httpRequest.ts?");

/***/ }),

/***/ "./js/script.ts":
/*!**********************!*\
  !*** ./js/script.ts ***!
  \**********************/
/***/ ((__unused_webpack_module, exports, __webpack_require__) => {

eval("\r\nObject.defineProperty(exports, \"__esModule\", ({ value: true }));\r\nconst httpRequest_1 = __webpack_require__(/*! ./httpRequest */ \"./js/httpRequest.ts\");\r\n//--creating global variables--\r\nconst people = [];\r\nconst nameRegex = /[A-Za-zÀ-ú\\s]{3,}/;\r\nconst emailRegex = /^[A-Za-z][A-Za-z0-9 \\w\\.]+@[A-Za-z0-9_\\-.]{3,}\\.[A-Za-z]{2,3}(\\.[[A-Za-z]])?/;\r\nconst cpfRegex = /(^\\d{3})\\.(\\d{3})\\.(\\d{3})\\-(\\d{2})$/;\r\nconst cnpjRegex = /\\d{2}\\.\\d{3}\\.\\d{3}\\/(0001|0002)\\-\\d{2}/;\r\nconst cepRegex = /(^(\\d{2}\\.\\d{3})|(\\d{5}))\\-\\d{3}$/;\r\nconst ageRegex = /(^1[8-9])|(^[2-8]\\d)/;\r\nconst passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$/;\r\nif (sessionStorage.people) {\r\n    let session = JSON.parse(sessionStorage.people);\r\n    for (const person of session) {\r\n        people.push(person);\r\n    }\r\n    console.log(people);\r\n}\r\nlet loggedPerson;\r\n//--Creating our DOM variables--\r\n//Tag Company - Candidate\r\nconst candidateSection = document.querySelector(\".candidate-section\");\r\n;\r\nconst companySection = document.querySelector(\".company-section\");\r\n//Setting Required\r\nconst candidateElements = document.querySelectorAll(\".candidate\");\r\nconst companyElements = document.querySelectorAll(\".company\");\r\nconst signupButton = document.querySelector(\".signup\");\r\nconst radioTags = document.querySelector(\".signup-select-radio\");\r\nconst radioCandidate = document.querySelector(\".select-candidate\");\r\nconst radioCompany = document.querySelector(\".select-company\");\r\n//Signup Form\r\nconst signupEmail = document.querySelector('[data-type=\"login\"]');\r\nconst signupPassword = document.querySelector('[data-type=\"password\"]');\r\nconst signupConfirmPassword = document.querySelector('[data-type=\"confirm-password\"]');\r\nconst message = document.querySelector(\"#matching-message\");\r\nconst signupName = document.querySelector('[data-type=\"name\"]');\r\nconst signupAge = document.querySelector('[data-type=\"age\"]');\r\nconst signupCpf = document.querySelector('[data-type=\"cpf\"]');\r\nconst signupCompanyName = document.querySelector('[data-type=\"company-name\"]');\r\nconst signupCnpj = document.querySelector('[data-type=\"cnpj\"]');\r\nconst signupCountry = document.querySelector('[data-type=\"country\"]');\r\nconst signupCep = document.querySelector('[data-type=\"cep\"]');\r\nconst signupState = document.querySelector('[data-type=\"state\"]');\r\nconst signupDescription = document.querySelector('textarea');\r\n//login\r\nconst loginEmail = document.querySelector('[data-type=\"email-login\"]');\r\nconst loginPassword = document.querySelector('[data-type=\"password-login\"]');\r\nconst loginButton = document.querySelector('.login-button');\r\n//--creating event Listeners--\r\nsignupConfirmPassword === null || signupConfirmPassword === void 0 ? void 0 : signupConfirmPassword.addEventListener('keyup', validatePassword);\r\nsignupButton.addEventListener('click', createAccount);\r\nradioTags.addEventListener('click', changeSignupInputTagsThroughRadioSelection);\r\nloginButton.addEventListener('click', tryToLogin);\r\n//Functions\r\nfunction changeSignupInputTagsThroughRadioSelection(e) {\r\n    if (e.target.classList.contains('select-company')) {\r\n        candidateSection.style.display = 'none';\r\n        companySection.style.display = 'inline-block';\r\n        companyElements.forEach(e => {\r\n            e.setAttribute(\"required\", \"required\");\r\n        });\r\n        candidateElements.forEach(e => {\r\n            e.removeAttribute(\"required\");\r\n        });\r\n    }\r\n    else if (e.target.classList.contains('select-candidate')) {\r\n        e.target.setAttribute('checked', 'checked');\r\n        companySection.style.display = 'none';\r\n        candidateSection.style.display = 'inline-block';\r\n        candidateElements.forEach(e => {\r\n            e.setAttribute(\"required\", \"required\");\r\n        });\r\n        companyElements.forEach(e => {\r\n            e.removeAttribute(\"required\");\r\n        });\r\n    }\r\n}\r\nfunction createAccount(e) {\r\n    e.preventDefault();\r\n    let url = new URL(\"http://localhost:8085/ZG-LinkeTinder2/person\");\r\n    if (radioCandidate.checked) {\r\n        let check = true;\r\n        let person = {\r\n            type: \"candidate\",\r\n            login: \"\",\r\n            password: \"\",\r\n            name: \"\",\r\n            age: 0,\r\n            cpf: \"\",\r\n            cep: \"\",\r\n            state: \"\",\r\n            description: \"\",\r\n            skills: [],\r\n            approval: [],\r\n            disapproval: [],\r\n        };\r\n        //password\r\n        if (!signupPassword.value.match(passwordRegex)) {\r\n            window.alert(\"Password needs to have a capital Letter, a number, a special charactere and have length between 8 and 20\");\r\n            check = false;\r\n            return;\r\n        }\r\n        else {\r\n            if (signupPassword.value == signupConfirmPassword.value) {\r\n                person.password = signupPassword.value;\r\n            }\r\n            else {\r\n                window.alert(\"Password does not match Confirm Password\");\r\n                check = false;\r\n                return;\r\n            }\r\n        }\r\n        //email\r\n        if (signupEmail.value.match(emailRegex)) {\r\n            if (people.some((person) => {\r\n                person.login.includes(signupEmail.value);\r\n            })) {\r\n                window.alert(\"Email already exist in our database\");\r\n                check = false;\r\n                return;\r\n            }\r\n            person.login = signupEmail.value;\r\n        }\r\n        else {\r\n            window.alert(\"Email doesn't have right format\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //name\r\n        if (signupName.value.match(nameRegex)) {\r\n            person.name = signupName.value;\r\n        }\r\n        else {\r\n            window.alert(\"Name not valid\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //age\r\n        if (signupAge.value.match(ageRegex)) {\r\n            person.age = parseInt(signupAge.value);\r\n        }\r\n        else {\r\n            window.alert(\"Invalid Age. Please insert age between 18-80\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //cpf\r\n        if (signupCpf.value.match(cpfRegex)) {\r\n            person.cpf = signupCpf.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid CPF. Please insert your CPF\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //cep\r\n        if (signupCep.value.match(cepRegex)) {\r\n            person.cep = signupCep.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid Cep. Please insert a valid cep\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //state\r\n        if (signupState.value.match(nameRegex)) {\r\n            person.state = signupState.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid State Name\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //description\r\n        person.description = signupDescription.value;\r\n        //send data\r\n        if (check === true) {\r\n            let resText = (0, httpRequest_1.sendPostToServer)(url, person);\r\n            if (!resText) {\r\n                window.Error('Something went wrong on Request');\r\n                return;\r\n            }\r\n            people.push(person);\r\n            window.alert(` ${resText}!! New Account created. Total Account: ${people.length}`);\r\n            console.log(people.length);\r\n        }\r\n    }\r\n    else {\r\n        let check = true;\r\n        let person = {\r\n            type: \"company\",\r\n            login: \"\",\r\n            password: \"\",\r\n            companyName: \"\",\r\n            cnpj: \"\",\r\n            country: \"\",\r\n            cep: \"\",\r\n            state: \"\",\r\n            description: \"\",\r\n            skills: [],\r\n            approval: [],\r\n            disapproval: []\r\n        };\r\n        //password\r\n        if (!signupPassword.value.match(passwordRegex)) {\r\n            window.alert(\"Password needs to have a capital Letter, a number, a special charactere and have length between 8 and 20\");\r\n            check = false;\r\n            return;\r\n        }\r\n        else {\r\n            if (signupPassword.value == signupConfirmPassword.value) {\r\n                person.password = signupPassword.value;\r\n            }\r\n            else {\r\n                window.alert(\"Password does not match Confirm Password\");\r\n                check = false;\r\n                return;\r\n            }\r\n        }\r\n        //email\r\n        if (signupEmail.value.match(emailRegex)) {\r\n            if (people.some((person) => {\r\n                person.login.includes(signupEmail.value);\r\n            })) {\r\n                window.alert(\"Email already exist in our database\");\r\n                check = false;\r\n                return;\r\n            }\r\n            person.login = signupEmail.value;\r\n        }\r\n        else {\r\n            window.alert(\"Email doesn't have right format\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //company name\r\n        if (signupCompanyName.value.match(nameRegex)) {\r\n            person.companyName = signupCompanyName.value;\r\n        }\r\n        else {\r\n            window.alert(\"Company name not valid\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //cnpj\r\n        if (signupCnpj.value.match(cnpjRegex)) {\r\n            person.cnpj = signupCnpj.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid CNPJ. Please insert age between 18-80\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //country\r\n        if (signupCountry.value.match(nameRegex)) {\r\n            person.country = signupCountry.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid Country formar!\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //cep\r\n        if (signupCep.value.match(cepRegex)) {\r\n            person.cep = signupCep.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid Cep. Please insert a valid cep\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //state\r\n        if (signupState.value.match(nameRegex)) {\r\n            person.state = signupState.value;\r\n        }\r\n        else {\r\n            window.alert(\"Invalid State Name\");\r\n            check = false;\r\n            return;\r\n        }\r\n        //description\r\n        person.description = signupDescription.value;\r\n        //send data\r\n        if (check === true) {\r\n            let resText = (0, httpRequest_1.sendPostToServer)(url, person);\r\n            if (!resText) {\r\n                window.Error('Something went wrong on Request');\r\n                return;\r\n            }\r\n            people.push(person);\r\n            window.alert(` ${resText}!! New Account created. Total Account: ${people.length}`);\r\n            console.log(people.length);\r\n        }\r\n    }\r\n}\r\nfunction validatePassword() {\r\n    signupConfirmPassword.value = this.value;\r\n    if (signupConfirmPassword.value.length > 3) {\r\n        if (signupPassword.value == this.value) {\r\n            message.textContent = \"Password Match\";\r\n            message.style.color = \"green\";\r\n        }\r\n        else {\r\n            message.textContent = \"Password Does Not Match\";\r\n            message.style.color = \"red\";\r\n        }\r\n    }\r\n}\r\nfunction tryToLogin() {\r\n    let check = false;\r\n    for (const person of people) {\r\n        if (person.login === loginEmail.value && person.password === loginPassword.value) {\r\n            sessionStorage.setItem(\"loggedPerson\", JSON.stringify(person));\r\n            sessionStorage.setItem(\"people\", JSON.stringify(people));\r\n            check = true;\r\n            break;\r\n        }\r\n    }\r\n    if (check) {\r\n        location.assign(\"http://localhost:9000/view-page.html\");\r\n    }\r\n    else if (loginEmail.value.length === 0 || loginPassword.value.length === 0) {\r\n        window.alert(\"Please Insert a Login and a Password\");\r\n        return;\r\n    }\r\n    else {\r\n        window.alert(\"Login or password are incorrect\");\r\n        return;\r\n    }\r\n}\r\n\n\n//# sourceURL=webpack://ZG-Linketinder/./js/script.ts?");

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module can't be inlined because the eval devtool is used.
/******/ 	var __webpack_exports__ = __webpack_require__("./js/script.ts");
/******/ 	
/******/ })()
;