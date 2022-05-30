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

/***/ "./js/view-page.ts":
/*!*************************!*\
  !*** ./js/view-page.ts ***!
  \*************************/
/***/ (function(__unused_webpack_module, exports) {

eval("\r\nvar __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {\r\n    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }\r\n    return new (P || (P = Promise))(function (resolve, reject) {\r\n        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }\r\n        function rejected(value) { try { step(generator[\"throw\"](value)); } catch (e) { reject(e); } }\r\n        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }\r\n        step((generator = generator.apply(thisArg, _arguments || [])).next());\r\n    });\r\n};\r\nObject.defineProperty(exports, \"__esModule\", ({ value: true }));\r\n//Redirecting If not Login\r\nif (!sessionStorage.loggedPerson) {\r\n    location.assign(\"http://localhost:9000/\");\r\n}\r\n//--creating global variables\r\nconst peopleList = [];\r\nlet filteredList = [];\r\nlet filteredBySkillList = [];\r\nlet logged;\r\nlet currentItem;\r\nloadData();\r\nwindow.onload = function loadDOM() {\r\n    if (logged.type == \"candidate\") {\r\n        filteredList = peopleList.filter((item) => {\r\n            return item.type === \"company\";\r\n        });\r\n    }\r\n    else {\r\n        filteredList = peopleList.filter((item) => {\r\n            return item.type === \"candidate\";\r\n        });\r\n    }\r\n    //Creating DOM variables\r\n    const welcome = document.querySelector(\".welcome\");\r\n    welcome.textContent = `${logged.name}`;\r\n    const addSelect = document.querySelector(\"#add-skills-select\");\r\n    const yourSkillsSelect = document.querySelector(\"#your-skills-select\");\r\n    const addButton = document.querySelector(\".add-button\");\r\n    const removeButton = document.querySelector(\".remove-button\");\r\n    const swipeLeft = document.querySelector(\".swipe-left\");\r\n    const swipeRight = document.querySelector(\".swipe-right\");\r\n    const slotDiv = document.querySelector(\".slot\");\r\n    const logoutButton = document.querySelector(\".logout-button\");\r\n    if (logged.skills.length > 0) {\r\n        if (logged.skills) {\r\n            for (const skill of logged.skills) {\r\n                creatingOptions(skill);\r\n            }\r\n        }\r\n        updateFilteredList();\r\n    }\r\n    // event Listeners\r\n    addButton.addEventListener('click', addingSkillstoList);\r\n    removeButton.addEventListener('click', removingSkillstoList);\r\n    swipeLeft.addEventListener(\"click\", disapprovingSlot);\r\n    swipeRight.addEventListener(\"click\", approvingSlot);\r\n    logoutButton.addEventListener(\"click\", saveAndLogout);\r\n    //functions\r\n    function resetSwipes() {\r\n        logged.approval = [];\r\n        logged.disapproval = [];\r\n    }\r\n    function checkingIfSKillIsUnique(selectedSkill) {\r\n        let checkUniqueSkill = false;\r\n        if (logged.skills) {\r\n            logged.skills.forEach(e => {\r\n                if (e === selectedSkill) {\r\n                    checkUniqueSkill = true;\r\n                }\r\n            });\r\n            if (!checkUniqueSkill) {\r\n                logged.skills.push(selectedSkill);\r\n                creatingOptions(selectedSkill);\r\n                window.alert(\"Skill added Successfully\");\r\n            }\r\n        }\r\n    }\r\n    function addingSkillstoList() {\r\n        let selectedSkill = addSelect.value;\r\n        if (selectedSkill === \"select one\") {\r\n            return;\r\n        }\r\n        resetSwipes();\r\n        checkingIfSKillIsUnique(selectedSkill);\r\n        updateFilteredList();\r\n    }\r\n    function creatingOptions(element) {\r\n        const yourNewOption = document.createElement('option');\r\n        yourNewOption.value = element;\r\n        yourNewOption.textContent = element;\r\n        yourSkillsSelect.appendChild(yourNewOption);\r\n    }\r\n    function removingSkillstoList() {\r\n        let selectedSkill = yourSkillsSelect.value;\r\n        if (selectedSkill === \"select one\") {\r\n            return;\r\n        }\r\n        resetSwipes();\r\n        checkingIfSKillIsUnique(selectedSkill);\r\n        updateFilteredList();\r\n    }\r\n    function updateFilteredList() {\r\n        if (logged.skills) {\r\n            filteredBySkillList = filteredList.filter(item => {\r\n                var _a;\r\n                return (_a = item.skills) === null || _a === void 0 ? void 0 : _a.some(skill => { var _a; return (_a = logged.skills) === null || _a === void 0 ? void 0 : _a.includes(skill.toLowerCase()); });\r\n            });\r\n        }\r\n        showTopItemFromList();\r\n    }\r\n    function showTopItemFromList() {\r\n        while (slotDiv.firstElementChild) {\r\n            slotDiv.removeChild(slotDiv.firstElementChild);\r\n        }\r\n        //filteredBySkillList=validateList(filteredBySkillList);\r\n        if (filteredBySkillList.length > 0) {\r\n            currentItem = getCurrentItem();\r\n            //fieldset DOM\r\n            const vacancyFieldSet = document.createElement(\"fieldset\");\r\n            const vacancyLegend = document.createElement(\"legend\");\r\n            vacancyLegend.textContent = `${currentItem.type}`;\r\n            //label DOM\r\n            const vacancyInfo1Label = document.createElement(\"label\");\r\n            if (currentItem.type === \"company\") {\r\n                vacancyInfo1Label.textContent = \"Country: \";\r\n            }\r\n            else {\r\n                vacancyInfo1Label.textContent = \"Age: \";\r\n            }\r\n            const vacancyStateLabel = document.createElement(\"label\");\r\n            vacancyStateLabel.textContent = \"State: \";\r\n            const vacancyDesiredSkillsLabel = document.createElement(\"label\");\r\n            vacancyDesiredSkillsLabel.textContent = \"Desired Skills: \";\r\n            const vacancyDescriptionLabel = document.createElement(\"label\");\r\n            vacancyDescriptionLabel.textContent = \"Description: \";\r\n            //spans\r\n            const vacancyInfo1Span = document.createElement(\"span\");\r\n            if (currentItem.type === \"company\") {\r\n                vacancyInfo1Span.textContent = `${currentItem.country}`;\r\n            }\r\n            else {\r\n                vacancyInfo1Span.textContent = `${currentItem.age}`;\r\n            }\r\n            const vacancyStateSpan = document.createElement(\"span\");\r\n            vacancyStateSpan.textContent = `${currentItem.state}`;\r\n            const vacancyDesiredSkillsSpan = document.createElement(\"span\");\r\n            vacancyDesiredSkillsSpan.textContent = `${currentItem.skills}`;\r\n            const vacancyDescriptionTextArea = document.createElement(\"textarea\");\r\n            vacancyDescriptionTextArea.setAttribute('readonly', 'readonly');\r\n            vacancyDescriptionTextArea.setAttribute('max-lenght', '300');\r\n            vacancyDescriptionTextArea.textContent = `${currentItem.description}`;\r\n            vacancyInfo1Label.appendChild(vacancyInfo1Span);\r\n            vacancyStateLabel.appendChild(vacancyStateSpan);\r\n            vacancyDesiredSkillsLabel.appendChild(vacancyDesiredSkillsSpan);\r\n            vacancyDescriptionLabel.appendChild(vacancyDescriptionTextArea);\r\n            vacancyFieldSet.appendChild(vacancyLegend);\r\n            vacancyFieldSet.appendChild(vacancyInfo1Label);\r\n            vacancyFieldSet.appendChild(vacancyStateLabel);\r\n            vacancyFieldSet.appendChild(vacancyDesiredSkillsLabel);\r\n            vacancyFieldSet.appendChild(vacancyDescriptionLabel);\r\n            slotDiv.appendChild(vacancyFieldSet);\r\n        }\r\n        else {\r\n            noMoreItemsFromList();\r\n        }\r\n    }\r\n    function noMoreItemsFromList() {\r\n        while (slotDiv.firstElementChild) {\r\n            slotDiv.removeChild(slotDiv.firstElementChild);\r\n        }\r\n        const vacancyFieldSet = document.createElement(\"fieldset\");\r\n        vacancyFieldSet.style.height = \"max-content\";\r\n        const vacancyLegend = document.createElement(\"legend\");\r\n        vacancyLegend.textContent = `${currentItem.type}`;\r\n        const vacancyNoMoreItems = document.createElement(\"h3\");\r\n        vacancyNoMoreItems.textContent = \"No Vacancies for your current skills\";\r\n        vacancyFieldSet.appendChild(vacancyLegend);\r\n        vacancyFieldSet.appendChild(vacancyNoMoreItems);\r\n        slotDiv.appendChild(vacancyFieldSet);\r\n    }\r\n    function getCurrentItem() {\r\n        if (filteredBySkillList.length > 0) {\r\n            return filteredBySkillList.shift();\r\n        }\r\n        return;\r\n    }\r\n    function validateList(list) {\r\n        console.log(logged.approval);\r\n        let checkApproval = true;\r\n        let checkDisapproval = true;\r\n        let newList = list.filter(item => {\r\n            if (logged.approval) {\r\n                if (logged.approval.length > 0) {\r\n                    for (const approved of logged.approval) {\r\n                        if (approved.login === item.login) {\r\n                            checkApproval = false;\r\n                        }\r\n                    }\r\n                }\r\n            }\r\n            if (logged.disapproval) {\r\n                for (const disapproved of logged.disapproval) {\r\n                    if (disapproved.login === item.login) {\r\n                        checkDisapproval = false;\r\n                    }\r\n                    console.log(\"entrei aqui\");\r\n                }\r\n            }\r\n            if (checkApproval && checkDisapproval) {\r\n                return item;\r\n            }\r\n        });\r\n        console.log(newList);\r\n        return newList;\r\n    }\r\n    function disapprovingSlot() {\r\n        var _a;\r\n        (_a = logged.disapproval) === null || _a === void 0 ? void 0 : _a.push(currentItem);\r\n        if (filteredBySkillList.length > 0) {\r\n            showTopItemFromList();\r\n        }\r\n        else {\r\n            noMoreItemsFromList();\r\n        }\r\n    }\r\n    function approvingSlot() {\r\n        var _a;\r\n        (_a = logged.approval) === null || _a === void 0 ? void 0 : _a.push(currentItem);\r\n        //  console.log(disapprovedList);\r\n        if (filteredBySkillList.length > 0) {\r\n            showTopItemFromList();\r\n        }\r\n        else {\r\n            noMoreItemsFromList();\r\n        }\r\n    }\r\n    function saveAndLogout() {\r\n        for (let i = 0; i < peopleList.length; i++) {\r\n            if (peopleList[i].login === logged.login) {\r\n                peopleList[i] = logged;\r\n            }\r\n        }\r\n        // console.log(peopleList)\r\n        sessionStorage.setItem(\"people\", JSON.stringify(peopleList));\r\n        sessionStorage.removeItem(\"loggedPerson\");\r\n        location.assign(\"http://localhost:9000/view-page.html\");\r\n    }\r\n};\r\nfunction loadData() {\r\n    return __awaiter(this, void 0, void 0, function* () {\r\n        peopleList.push(yield JSON.parse(sessionStorage.people)[0]);\r\n        logged = yield JSON.parse(sessionStorage.loggedPerson);\r\n        if (peopleList.length < 10) {\r\n            const person1 = {\r\n                type: \"candidate\",\r\n                login: \"luiz.moura@acelerazg.com.br\",\r\n                password: \"123456\",\r\n                name: \"Luiz Arthur Moura\",\r\n                cpf: \"405.155.608-55\",\r\n                age: 30,\r\n                state: \"São Paulo\",\r\n                cep: \"12608-170\",\r\n                description: \"Cool guy\",\r\n                skills: [\"CSS\", \"HTML\", \"JAVA\", \"GITHUB\", \"GROOVY\"]\r\n            };\r\n            const person2 = {\r\n                type: \"candidate\",\r\n                login: \"josue.faria@gmail.com\",\r\n                password: \"123456\",\r\n                name: \"Josué Farias\",\r\n                cpf: \"MG-112.344.566\",\r\n                age: 35,\r\n                state: \"Minas Gerais\",\r\n                cep: \"30205-102\",\r\n                description: \"Eu não faria, mas Josué farias\",\r\n                skills: [\"JAVA\", \"GROOVY\", \"BACKEND\"]\r\n            };\r\n            const person3 = {\r\n                type: \"candidate\",\r\n                login: \"gz.tenorio@uol.com.br\",\r\n                password: \"123456\",\r\n                name: \"Gezebel Tenório\",\r\n                cpf: \"405.155.608-55\",\r\n                age: 28,\r\n                state: \"São Paulo\",\r\n                cep: \"11223-278\",\r\n                description: \"Me passa o gel Gezebel\",\r\n                skills: [\"DATABASE\", \"HIBERNATE\", \"REGEX\", \"GITHUB\"]\r\n            };\r\n            const person4 = {\r\n                type: \"candidate\",\r\n                login: \"duarte@yahoo.com.br\",\r\n                password: \"123456\",\r\n                name: \"Lima Duarte\",\r\n                cpf: \"055.223.541-27\",\r\n                age: 70,\r\n                state: \"Rio de Janeiro\",\r\n                cep: \"21551-003\",\r\n                description: \"Não me peça para limar. Duarte, Lima\",\r\n                skills: [\"BACKEND\", \"FRONTEND\", \"DATABASE\"]\r\n            };\r\n            const person5 = {\r\n                type: \"candidate\",\r\n                login: \"faroukinho@gmail.com\",\r\n                password: \"123456\",\r\n                name: \"Tomás Farouk\",\r\n                cpf: \"523.844.971-56\",\r\n                age: 48,\r\n                state: \"Sergipe\",\r\n                cep: \"49000-200\",\r\n                description: \"Gosto de cebola\",\r\n                skills: [\"JAVA\", \"GROOVY\"]\r\n            };\r\n            const company1 = {\r\n                type: \"company\",\r\n                login: \"comercial@zgsolucoes.com.br\",\r\n                password: \"123456\",\r\n                companyName: \"Zero Glosa\",\r\n                cnpj: \"14.488.144/0001\",\r\n                country: \"Brazil\",\r\n                state: \"Goiás\",\r\n                cep: \"74070-040\",\r\n                description: \"Awesome Company to work\",\r\n                skills: [\"DATABASE\", \"JAVA\", \"GROOVY\", \"GITHUB\"]\r\n            };\r\n            const company2 = {\r\n                type: \"company\",\r\n                login: \"comercial@petrobras.com.br\",\r\n                password: \"123456\",\r\n                companyName: \"Petrobras\",\r\n                cnpj: \"33.000.167/1049-00\",\r\n                country: \"Brazil\",\r\n                state: \"Rio de Janeiro\",\r\n                cep: \"20.031-912\",\r\n                description: \"Gas super high price\",\r\n                skills: [\"CSS\", \"HTML\", \"BACKEND\", \"GITHUB\"]\r\n            };\r\n            const company3 = {\r\n                type: \"company\",\r\n                login: \"comercial@arrozgostoso.com.br\",\r\n                password: \"123456\",\r\n                companyName: \"Arroz-Gostoso\",\r\n                cnpj: \"12.544.231/0011\",\r\n                country: \"Brazil\",\r\n                state: \"Mato Grosso do Sul\",\r\n                cep: \"69512-030\",\r\n                description: \"Selling good quality rice\",\r\n                skills: [\"DATABASE\", \"FRONTEND\", \"REGEX\", \"CSS\"]\r\n            };\r\n            const company4 = {\r\n                type: \"company\",\r\n                login: \"boliche@imperio.com.br\",\r\n                password: \"123456\",\r\n                companyName: \"Império do Boliche\",\r\n                cnpj: \"84.521.799/0005\",\r\n                country: \"Brazil\",\r\n                state: \"Maranhão\",\r\n                cep: \"81224-103\",\r\n                description: \"Come play with us\",\r\n                skills: [\"DATABASE\", \"BACKEND\", \"FRONTEND\", \"GITHUB\"]\r\n            };\r\n            const company5 = {\r\n                type: \"company\",\r\n                login: \"boi@nafonte.com.br\",\r\n                password: \"123456\",\r\n                companyName: \"Boi na Fonte\",\r\n                cnpj: \"87.530.973/0103\",\r\n                country: \"Brazil\",\r\n                state: \"Goiás\",\r\n                cep: \"71522-008\",\r\n                description: \"Come refresh your bull\",\r\n                skills: [\"HIBERNATE\", \"JAVA\", \"GROOVY\", \"HTML\", \"CSS\"]\r\n            };\r\n            peopleList.push(person1);\r\n            peopleList.push(person2);\r\n            peopleList.push(person3);\r\n            peopleList.push(person4);\r\n            peopleList.push(person5);\r\n            peopleList.push(company1);\r\n            peopleList.push(company2);\r\n            peopleList.push(company3);\r\n            peopleList.push(company4);\r\n            peopleList.push(company5);\r\n        }\r\n    });\r\n}\r\n\n\n//# sourceURL=webpack://ZG-Linketinder/./js/view-page.ts?");

/***/ })

/******/ 	});
/************************************************************************/
/******/ 	
/******/ 	// startup
/******/ 	// Load entry module and return exports
/******/ 	// This entry module is referenced by other modules so it can't be inlined
/******/ 	var __webpack_exports__ = {};
/******/ 	__webpack_modules__["./js/view-page.ts"](0, __webpack_exports__);
/******/ 	
/******/ })()
;