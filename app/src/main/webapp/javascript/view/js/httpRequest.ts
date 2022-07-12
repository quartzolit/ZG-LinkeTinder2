import {Person} from "./IPerson"

class httpRequests {

    sendPostToServer(url: URL, body: Person){

        let request = new XMLHttpRequest()
    
        request.open("POST", url, true)
       // request.setRequestHeader("Content-Type","application/json")
       // request.setRequestHeader("Access-Control-Allow-Origin","*")
        request.send(JSON.stringify(body))
    
        
    
        request.onload = () =>{
            console.log(request.responseText)
        }
    
        return request.status
    }
    
     async receiveloggedPersonIfExistFromServer(url: URL, email: string, password: string): Promise<string>{

        return new Promise((resolve) =>{
            let request = new XMLHttpRequest()

        request.open("POST", url, true)


        let loginJson = {
            userEmail : `${email}`,
            userPassword: `${password}`
        }

        request.send(JSON.stringify(loginJson))

        request.onreadystatechange= function(){
            if(request.readyState === 4 && request.status === 200){
                resolve(request.responseText)
            }
        }

        })
        

    } 

    async getAllData(type:string): Promise<string>{
        return new Promise((resolve, reject) =>{
            let url = new URL(`http://localhost:8085/ZG-LinkeTinder2/person?type=${type}`)

            let request = new XMLHttpRequest()

            request.open("GET",url, true)

            request.onreadystatechange = function(){
                if(request.readyState == 4 && request.status == 200){
                    resolve(request.responseText)
                }
            }


            request.send()

        })
    
    }

    sendSkillsToServer(url: URL, type:string, email:string, skill:string, vacancy?:string){
        let request = new XMLHttpRequest()

        request.open("POST",url,true)

        let skillDataJson = {}

        if(type === 'candidate'){
            skillDataJson = {
                type: `${type}`,
                email: `${email}`,
                skill: `${skill}`
            }
        }
        else{
            skillDataJson = {
                type: `${type}`,
                email: `${email}`,
                skill: `${skill}`,
                vacancy: `${vacancy}`
            }
        }

        

        request.send(JSON.stringify(skillDataJson))

        return request.status
    }

}



export { httpRequests };