//Redirecting If not Login
if(!sessionStorage.loggedPerson){
    location.assign("http://localhost:9000/")

}

//--creating global variables
const peopleList: Person[]=[];
let filteredList: Person[]= [];
let filteredBySkillList: Person[]=[];

let logged: Person;
let currentItem: Person;


loadData();

window.onload = function loadDOM(){

    if(logged.type == "candidate"){
        filteredList = peopleList.filter((item)=>{
            return item.type === "company";
        });

    }
    else{
        filteredList = peopleList.filter((item)=>{
            return item.type === "candidate";
        });
    }

    //Creating DOM variables
   
    const welcome: HTMLSpanElement = document.querySelector(".welcome") as HTMLSpanElement;
    welcome.textContent = `${logged.name}`;

    const addSelect: HTMLSelectElement = document.querySelector("#add-skills-select") as HTMLSelectElement;
    const yourSkillsSelect: HTMLSelectElement = document.querySelector("#your-skills-select") as HTMLSelectElement;
    const addButton: HTMLDivElement = document.querySelector(".add-button") as HTMLDivElement;
    const removeButton: HTMLButtonElement = document.querySelector(".remove-button") as HTMLButtonElement;

    const swipeLeft: HTMLDivElement = document.querySelector(".swipe-left") as HTMLDivElement;
    const swipeRight: HTMLDivElement = document.querySelector(".swipe-right") as HTMLDivElement;

    const slotDiv: HTMLDivElement = document.querySelector(".slot") as HTMLDivElement;
    
    const logoutButton: HTMLDivElement = document.querySelector(".logout-button") as HTMLDivElement


    if(logged.skills!.length>0){
        if(logged.skills){
            for (const skill of logged.skills) {
                creatingOptions(skill)    
            }
        } 
        updateFilteredList();
    }

    
    // event Listeners

    addButton.addEventListener('click', addingSkillstoList);
    removeButton.addEventListener('click', removingSkillstoList);
    swipeLeft.addEventListener("click", disapprovingSlot);
    swipeRight.addEventListener("click", approvingSlot);
    logoutButton.addEventListener("click", saveAndLogout);



    

    //functions


    function resetSwipes(){
        logged.approval = [];
        logged.disapproval = [];
    }

    function checkingIfSKillIsUnique(selectedSkill: string){
        let checkUniqueSkill = false;

        if(logged.skills){
            logged.skills.forEach(e => {
                if(e === selectedSkill){
                    checkUniqueSkill = true
                }
                
            });

            if(!checkUniqueSkill){
                logged.skills.push(selectedSkill)
                creatingOptions(selectedSkill);
                window.alert("Skill added Successfully");
            }
            

        }

    }

    function addingSkillstoList(){
        let selectedSkill = addSelect.value;
        
        if(selectedSkill === "select one"){
            return
        }
        resetSwipes();

        checkingIfSKillIsUnique(selectedSkill);

        updateFilteredList();  
    }

    function creatingOptions(element: string){
      
        const yourNewOption = document.createElement('option');

        yourNewOption.value = element;
        yourNewOption.textContent = element;

        yourSkillsSelect.appendChild(yourNewOption);

        

    }

    function removingSkillstoList(){
        let selectedSkill:string = yourSkillsSelect.value;

        if(selectedSkill === "select one"){
            return
        }
        resetSwipes();

        checkingIfSKillIsUnique(selectedSkill);

        updateFilteredList();

    }
    

    function updateFilteredList(){

        if(logged.skills){
            filteredBySkillList = filteredList.filter(item=>{
                
                return item.skills?.some(skill => logged.skills?.includes(skill.toLowerCase()))

            })
        }  
        showTopItemFromList();
    }

    function showTopItemFromList():void{

        while(slotDiv.firstElementChild){
            slotDiv.removeChild(slotDiv.firstElementChild);
        }

        //filteredBySkillList=validateList(filteredBySkillList);


        if(filteredBySkillList.length>0){
            currentItem = getCurrentItem()!;


            //fieldset DOM
            const vacancyFieldSet = document.createElement("fieldset");
            const vacancyLegend = document.createElement("legend");

            vacancyLegend.textContent = `${currentItem.type}`;

            //label DOM
            const vacancyInfo1Label = document.createElement("label");
            if(currentItem.type === "company"){
                vacancyInfo1Label.textContent = "Country: ";

            }else{
                vacancyInfo1Label.textContent = "Age: ";

            }
            const vacancyStateLabel = document.createElement("label");
            vacancyStateLabel.textContent = "State: ";
            const vacancyDesiredSkillsLabel = document.createElement("label");
            vacancyDesiredSkillsLabel.textContent = "Desired Skills: ";
            const vacancyDescriptionLabel = document.createElement("label");
            vacancyDescriptionLabel.textContent = "Description: ";

            //spans

            const vacancyInfo1Span = document.createElement("span");
            if(currentItem.type === "company"){
                vacancyInfo1Span.textContent = `${currentItem.country}`;

            }else{
                vacancyInfo1Span.textContent = `${currentItem.age}`;
            }
            const vacancyStateSpan = document.createElement("span");
            vacancyStateSpan.textContent = `${currentItem.state}`;
            const vacancyDesiredSkillsSpan = document.createElement("span");

            vacancyDesiredSkillsSpan.textContent = `${currentItem.skills}`;
            const vacancyDescriptionTextArea = document.createElement("textarea");
            vacancyDescriptionTextArea.setAttribute('readonly','readonly');
            vacancyDescriptionTextArea.setAttribute('max-lenght','300');
            vacancyDescriptionTextArea.textContent= `${currentItem.description}`;


            vacancyInfo1Label.appendChild(vacancyInfo1Span);
            vacancyStateLabel.appendChild(vacancyStateSpan);
            vacancyDesiredSkillsLabel.appendChild(vacancyDesiredSkillsSpan);
            vacancyDescriptionLabel.appendChild(vacancyDescriptionTextArea);

            
            vacancyFieldSet.appendChild(vacancyLegend);
            vacancyFieldSet.appendChild(vacancyInfo1Label);
            vacancyFieldSet.appendChild(vacancyStateLabel);
            vacancyFieldSet.appendChild(vacancyDesiredSkillsLabel);
            vacancyFieldSet.appendChild(vacancyDescriptionLabel);

            slotDiv.appendChild(vacancyFieldSet);
    
        }else{
            noMoreItemsFromList();
        }



    }

    function noMoreItemsFromList(){

        while(slotDiv.firstElementChild){
            slotDiv.removeChild(slotDiv.firstElementChild);
        }

        const vacancyFieldSet = document.createElement("fieldset");

        vacancyFieldSet.style.height="max-content"
            const vacancyLegend = document.createElement("legend");

            vacancyLegend.textContent = `${currentItem.type}`;

            const vacancyNoMoreItems = document.createElement("h3");

            vacancyNoMoreItems.textContent = "No Vacancies for your current skills";

            vacancyFieldSet.appendChild(vacancyLegend);
            vacancyFieldSet.appendChild(vacancyNoMoreItems);

            slotDiv.appendChild(vacancyFieldSet);

    }

    function getCurrentItem():Person | undefined{

        if(filteredBySkillList.length>0){
            return filteredBySkillList.shift()!;
        }
        
        return;

    }

    function validateList(list: Person[]):Person[]{

        console.log(logged.approval);
        let checkApproval: boolean = true;
        let checkDisapproval: boolean = true;
        
        let newList: Person[] = list.filter(item=>{

            if(logged.approval){
                if(logged.approval.length>0){
                    for (const approved of logged.approval) {
                        if(approved.login===item.login){
                            checkApproval= false;
                        }  
                        
                    }
                }

                
            }

            if(logged.disapproval){
                    for (const disapproved of logged.disapproval) {
                        if(disapproved.login===item.login){
                            checkDisapproval= false;
                        } 
                        
                        console.log("entrei aqui"); 
                    }
                
            }

            if(checkApproval && checkDisapproval){
                return item;
            }
        })


        console.log(newList)

        return newList;
     

    }

    function disapprovingSlot(){

        logged.disapproval?.push(currentItem);

        if(filteredBySkillList.length>0){
            showTopItemFromList();
        }
        else{
            noMoreItemsFromList();
        }

    }

    function approvingSlot(){

        logged.approval?.push(currentItem);

      //  console.log(disapprovedList);

        if(filteredBySkillList.length>0){
            showTopItemFromList();
        }
        else{
            noMoreItemsFromList();
        }

    }

    function saveAndLogout(){

        for(let i = 0; i< peopleList.length; i++){
            if(peopleList[i].login === logged.login){
                peopleList[i] = logged;
            }
        }

       // console.log(peopleList)

        sessionStorage.setItem("people", JSON.stringify(peopleList));

        sessionStorage.removeItem("loggedPerson");

        location.assign("http://localhost:9000/view-page.html");

    }

    






}



async function loadData(){

    peopleList.push(await JSON.parse(sessionStorage.people)[0]);

    logged = await JSON.parse(sessionStorage.loggedPerson);


    if(peopleList.length < 10){

        const person1: Person = {
            type: "candidate",
            login: "luiz.moura@acelerazg.com.br",
            password: "123456",
            name: "Luiz Arthur Moura",
            cpf: "405.155.608-55",
            age: 30,
            state: "São Paulo",
            cep: "12608-170",
            description: "Cool guy",
            skills:["CSS", "HTML", "JAVA", "GITHUB", "GROOVY"]

        };


        const person2: Person = {
            type: "candidate",
            login: "josue.faria@gmail.com",
            password: "123456",
            name: "Josué Farias",
            cpf: "MG-112.344.566",
            age: 35,
            state: "Minas Gerais",
            cep: "30205-102",
            description: "Eu não faria, mas Josué farias",
            skills:["JAVA", "GROOVY", "BACKEND"]

        };

        const person3: Person = {
            type: "candidate",
            login: "gz.tenorio@uol.com.br",
            password: "123456",
            name: "Gezebel Tenório",
            cpf: "405.155.608-55",
            age: 28,
            state: "São Paulo",
            cep: "11223-278",
            description: "Me passa o gel Gezebel",
            skills:["DATABASE", "HIBERNATE", "REGEX", "GITHUB"]

        };

        const person4: Person = {
            type: "candidate",
            login: "duarte@yahoo.com.br",
            password: "123456",
            name: "Lima Duarte",
            cpf: "055.223.541-27",
            age: 70,
            state: "Rio de Janeiro",
            cep: "21551-003",
            description: "Não me peça para limar. Duarte, Lima",
            skills:["BACKEND", "FRONTEND", "DATABASE"]

        };

        const person5: Person = {
            type: "candidate",
            login: "faroukinho@gmail.com",
            password: "123456",
            name: "Tomás Farouk",
            cpf: "523.844.971-56",
            age: 48,
            state: "Sergipe",
            cep: "49000-200",
            description: "Gosto de cebola",
            skills:["JAVA","GROOVY"]

        };

        const company1: Person = {
            type: "company",
            login: "comercial@zgsolucoes.com.br",
            password: "123456",
            companyName: "Zero Glosa",
            cnpj: "14.488.144/0001",
            country: "Brazil",
            state: "Goiás",
            cep: "74070-040",
            description: "Awesome Company to work",
            skills:["DATABASE", "JAVA", "GROOVY", "GITHUB"]

        };

        const company2: Person = {
            type: "company",
            login: "comercial@petrobras.com.br",
            password: "123456",
            companyName: "Petrobras",
            cnpj: "33.000.167/1049-00",
            country: "Brazil",
            state: "Rio de Janeiro",
            cep: "20.031-912",
            description: "Gas super high price",
            skills:["CSS", "HTML", "BACKEND", "GITHUB"]

        };

        const company3: Person = {
            type: "company",
            login: "comercial@arrozgostoso.com.br",
            password: "123456",
            companyName: "Arroz-Gostoso",
            cnpj: "12.544.231/0011",
            country: "Brazil",
            state: "Mato Grosso do Sul",
            cep: "69512-030",
            description: "Selling good quality rice",
            skills:["DATABASE", "FRONTEND", "REGEX","CSS"]

        };

        const company4: Person = {
            type: "company",
            login: "boliche@imperio.com.br",
            password: "123456",
            companyName: "Império do Boliche",
            cnpj: "84.521.799/0005",
            country: "Brazil",
            state: "Maranhão",
            cep: "81224-103",
            description: "Come play with us",
            skills:["DATABASE", "BACKEND", "FRONTEND", "GITHUB"]

        };

        const company5: Person = {
            type: "company",
            login: "boi@nafonte.com.br",
            password: "123456",
            companyName: "Boi na Fonte",
            cnpj: "87.530.973/0103",
            country: "Brazil",
            state: "Goiás",
            cep: "71522-008",
            description: "Come refresh your bull",
            skills:["HIBERNATE", "JAVA", "GROOVY", "HTML","CSS"]

        };


        peopleList.push(person1);
        peopleList.push(person2);
        peopleList.push(person3);
        peopleList.push(person4);
        peopleList.push(person5);
        peopleList.push(company1);
        peopleList.push(company2);
        peopleList.push(company3);
        peopleList.push(company4);
        peopleList.push(company5);
    }

}















