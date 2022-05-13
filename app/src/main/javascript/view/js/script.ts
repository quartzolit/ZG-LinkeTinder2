interface Person{
    type: string;
    login: string;
    password: string;
    name?: string;
    age?: number;
    cpf?: string;
    companyName?: string;
    cnpj?: string;
    country?: string;
    cep: string;
    state: string;
    description: string;
    skills?: string[];
    approval?: Person[];
    disapproval?: Person[];
}

//--creating global variables--
const people: Person[]=[];
const nameRegex: RegExp = /[A-Za-zÀ-ú\s]{3,}/;
const emailRegex: RegExp = /^[A-Za-z][A-Za-z0-9 \w\.]+@[A-Za-z0-9_\-.]{3,}\.[A-Za-z]{2,3}(\.[[A-Za-z]])?/;
const cpfRegex: RegExp = /(^\d{3})\.(\d{3})\.(\d{3})\-(\d{2})$/;
const cnpjRegex: RegExp = /\d{2}\.\d{3}\.\d{3}\/(0001|0002)\-\d{2}/;
const cepRegex: RegExp = /(^(\d{2}\.\d{3})|(\d{5}))\-\d{3}$/;
const ageRegex: RegExp = /(^1[8-9])|(^[2-8]\d)/
const passwordRegex: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/




if(sessionStorage.people){
    let session = JSON.parse(sessionStorage.people);
    
    for (const person of session) {

        people.push(person);
    }

    console.log(people)
}

let loggedPerson: Person;

//--Creating our DOM variables--

//Tag Company - Candidate
const candidateSection: HTMLElement = document.querySelector(".candidate-section")  as HTMLElement;;
const companySection: HTMLElement = document.querySelector(".company-section") as HTMLElement;


//Setting Required
const candidateElements: NodeListOf<HTMLElement> = document.querySelectorAll(".candidate") as NodeListOf<HTMLElement>;
const companyElements = document.querySelectorAll(".company") as NodeListOf<HTMLElement>;
const signupButton: HTMLElement = document.querySelector(".signup") as HTMLElement;
const radioTags: HTMLElement = document.querySelector(".signup-select-radio") as HTMLElement;
const radioCandidate: HTMLInputElement = document.querySelector(".select-candidate") as HTMLInputElement;
const radioCompany: HTMLInputElement = document.querySelector(".select-company") as HTMLInputElement;

//Signup Form
const signupEmail: HTMLInputElement = document.querySelector('[data-type="login"]') as HTMLInputElement;
const signupPassword: HTMLInputElement = document.querySelector('[data-type="password"]') as HTMLInputElement;
const signupConfirmPassword: HTMLInputElement = document.querySelector('[data-type="confirm-password"]') as HTMLInputElement;
const message: HTMLSpanElement = document.querySelector("#matching-message") as HTMLSpanElement;
const signupName: HTMLInputElement = document.querySelector('[data-type="name"]') as HTMLInputElement;
const signupAge: HTMLInputElement = document.querySelector('[data-type="age"]') as HTMLInputElement;
const signupCpf: HTMLInputElement = document.querySelector('[data-type="cpf"]') as HTMLInputElement;
const signupCompanyName: HTMLInputElement = document.querySelector('[data-type="company-name"]') as HTMLInputElement;
const signupCnpj: HTMLInputElement = document.querySelector('[data-type="cnpj"]') as HTMLInputElement;
const signupCountry: HTMLInputElement = document.querySelector('[data-type="country"]') as HTMLInputElement;
const signupCep: HTMLInputElement = document.querySelector('[data-type="cep"]') as HTMLInputElement;
const signupState: HTMLInputElement = document.querySelector('[data-type="state"]') as HTMLInputElement;
const signupDescription: HTMLTextAreaElement = document.querySelector('textarea') as HTMLTextAreaElement;


//login

const loginEmail: HTMLInputElement = document.querySelector('[data-type="email-login"]') as HTMLInputElement;
const loginPassword: HTMLInputElement = document.querySelector('[data-type="password-login"]') as HTMLInputElement;
const loginButton: HTMLElement = document.querySelector('.login-button') as HTMLElement;

//--creating event Listeners--

signupConfirmPassword?.addEventListener('keyup', validatePassword);
signupButton.addEventListener('click', createAccount);
radioTags.addEventListener('click', changeSignupInputTagsThroughRadioSelection);
loginButton.addEventListener('click', tryToLogin);



//Functions
function changeSignupInputTagsThroughRadioSelection(e:any) :void{
    if(e.target.classList.contains('select-company')){
        candidateSection.style.display = 'none';
        companySection.style.display = 'inline-block'
        companyElements.forEach(e=>{
            e.setAttribute("required","required");
        })
        candidateElements.forEach(e=>{
            e.removeAttribute("required");
        })

    }

    else if(e.target.classList.contains('select-candidate')){
        e.target.setAttribute('checked','checked');
        companySection.style.display = 'none'
        candidateSection.style.display = 'inline-block';
        candidateElements.forEach(e=>{
            e.setAttribute("required","required");
        })
        companyElements.forEach(e=>{
            e.removeAttribute("required");
        })

    }
}

function createAccount(e:any){

    

    e.preventDefault();


    
    if(radioCandidate.checked){

        let check = true

        let person: Person= {
                type: "candidate",
                login: "",
                password: "",
                name: "",
                age:0,
                cpf: "",
                cep:"",
                state: "",
                description:"",
                skills:[],
                approval:[],
                disapproval:[],
            }

            //password

            if(!signupPassword.value.match(passwordRegex)){
                window.alert("Password needs to have a capital Letter, a number, a special charactere and have length between 8 and 20");
                check = false;
                return;
                

            }else{
                if(signupPassword.value == signupConfirmPassword.value){
                    person.password = signupPassword.value;
                }
                else{
                    window.alert("Password does not match Confirm Password");
                    check = false;
                    return;
                }
            }

            //email

            if(signupEmail.value.match(emailRegex)){

                if(people.some((person)=>{
                    person.login.includes(signupEmail.value)
                })){
                    window.alert("Email already exist in our database");
                    check = false;
                    return;
                }        
                person.login = signupEmail.value;

            }else{
                window.alert("Email doesn't have right format");
                check = false;
                return;
            }

            //name

            if(signupName.value.match(nameRegex)){
                person.name = signupName.value;

            }else{
                window.alert("Name not valid");
                check = false;
                return;

            }

            //age

            if(signupAge.value.match(ageRegex)){
                person.age = parseInt(signupAge.value);

            }else{
                window.alert("Invalid Age. Please insert age between 18-80");
                check = false;
                return;

            }

            //cpf
            if(signupCpf.value.match(cpfRegex)){
                person.cpf = signupCpf.value;
  

            }else{
                window.alert("Invalid CPF. Please insert your CPF");
                check = false;
                return;

            }

            //cep
            if(signupCep.value.match(cepRegex)){
                person.cep = signupCep.value;
  

            }else{
                window.alert("Invalid Cep. Please insert a valid cep");
                check = false;
                return;

            }

            //state
            if(signupState.value.match(nameRegex)){
                person.state = signupState.value;
  

            }else{
                window.alert("Invalid State Name");
                check = false;
                return;

            }
            
            //description
            person.description = signupDescription.value;


            //send data
            if(check === true){
                people.push(person);
                window.alert(`New Account created. Total Account: ${people.length}`)
                console.log(people.length)
            }
           
            
    }
    else{
        let check = true;
        let person: Person= {
                type: "company",
                login: "",
                password: "",
                companyName: "",
                cnpj:"",
                country: "",
                cep:"",
                state: "",
                description:"",
                skills:[],
                approval:[],
                disapproval:[]
            }

            //password
            if(!signupPassword.value.match(passwordRegex)){
                window.alert("Password needs to have a capital Letter, a number, a special charactere and have length between 8 and 20");
                check = false;
                return;
                

            }else{
                if(signupPassword.value == signupConfirmPassword.value){
                    person.password = signupPassword.value;
                }
                else{
                    window.alert("Password does not match Confirm Password");
                    check = false;
                    return;
                }
            }

            //email
            if(signupEmail.value.match(emailRegex)){

                if(people.some((person)=>{
                    person.login.includes(signupEmail.value)
                })){
                    window.alert("Email already exist in our database");
                    check = false;
                    return;
                }        
                person.login = signupEmail.value;

            }else{
                window.alert("Email doesn't have right format");
                check = false;
                return;
            }

            //company name
            if(signupCompanyName.value.match(nameRegex)){
                person.companyName = signupCompanyName.value;

            }else{
                window.alert("Company name not valid");
                check = false;
                return;

            }

            //cnpj
            if(signupCnpj.value.match(cnpjRegex)){
                person.cnpj = signupCnpj.value;

            }else{
                window.alert("Invalid CNPJ. Please insert age between 18-80");
                check = false;
                return;

            }

            //country
            if(signupCountry.value.match(nameRegex)){
                person.country = signupCountry.value;
  

            }else{
                window.alert("Invalid Country formar!");
                check = false;
                return;

            }

            //cep
            if(signupCep.value.match(cepRegex)){
                person.cep = signupCep.value;
  

            }else{
                window.alert("Invalid Cep. Please insert a valid cep");
                check = false;
                return;

            }

            //state
            if(signupState.value.match(nameRegex)){
                person.state = signupState.value;
  

            }else{
                window.alert("Invalid State Name");
                check = false;
                return;

            }
            
            //description
            person.description = signupDescription.value;

            //send data
            if(check === true){

                people.push(person);
                window.alert(`New Account created. Total Account: ${people.length}`)
            }
                

    }

}

function validatePassword(this: HTMLInputElement){


    signupConfirmPassword.value = this.value;

    if(signupConfirmPassword.value.length > 3){

        if(signupPassword.value == this.value){
            message.textContent = "Password Match";
            message.style.color = "green";
        }
        else{
            message.textContent = "Password Does Not Match";
            message.style.color = "red";

        }

    }

}

function tryToLogin(this: any){


    let check: boolean = false;

    for (const person of people) {

        if(person.login === loginEmail.value && person.password === loginPassword.value){
            sessionStorage.setItem("loggedPerson",JSON.stringify(person));
            sessionStorage.setItem("people", JSON.stringify(people));

            check = true;
        }
        
    }

    if(check){

        location.assign("http://localhost:9000/view-page.html")

    }
    else if(loginEmail.value.length === 0 || loginPassword.value.length === 0 ){
        window.alert("Please Insert a Login and a Password")
        return
    }
    else{
        window.alert("Login or password are incorrect")
        return;

    }


}







