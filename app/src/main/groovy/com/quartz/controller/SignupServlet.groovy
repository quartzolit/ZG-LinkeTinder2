package com.quartz.controller

import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import com.quartz.dto.ConnectPostgres
import com.quartz.dto.factory.PostgresConnect
import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
import com.quartz.model.person.Person
import com.quartz.model.person.Skills
import com.quartz.model.person.Vacancy
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import java.sql.Connection
import java.time.LocalDate
import java.util.stream.Collectors

@WebServlet("/person")
class SignupServlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        BufferedReader result = request.getReader()

        response.addHeader("Access-Control-Allow-Origin", "*");

        String requestData = result.lines().collect(Collectors.joining())

        JsonObject resultJson = Json.parse(requestData).asObject()


        String objectType = resultJson.get("type").asString()

        String objectEmail = resultJson.get("email").asString()

        boolean check = checkingIfAccountAlreadyExist(objectEmail);

        if(!check){
            if(objectType == "candidate"){
                sendCandidateToDB(resultJson)

                response.setStatus(201);


            }else{
                sendCompanyToDB(resultJson)
                response.setStatus(201);
            }
        }else {
            response.setStatus(302)
            PrintWriter writer = response.getWriter()
            writer.print(response.status)
            writer.flush()
        }



    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String type = request.getParameterValues("type")

        response.addHeader("Access-Control-Allow-Origin", "*");

        JsonArray resJson = new JsonArray()

        if(type == "candidate"){
             resJson = getAllCompanies()

        }
        else {
            resJson = getAllCandidates()
        }

        PrintWriter res = response.getWriter()


        //response.setContentType("application/json")
        //response.getCharacterEncoding("UTF-8")
        res.print(resJson)
        res.flush()


    }


    Boolean checkingIfAccountAlreadyExist(String email){
        ConnectPostgres postgres = new ConnectPostgres();

        boolean exist = false;

        Candidate searchCandidate = postgres.showCandidateByEmail(email);
        Company searchCompany = postgres.showCompanyByEmail(email);

        if((searchCandidate) || (searchCompany)){
            exist = true;
        }

        return exist;
    }

    public void  sendCandidateToDB(JsonObject result){

        ConnectPostgres postgres = new ConnectPostgres()

        Connection conn = postgres.connect()


        Candidate candidate = new Candidate()
        candidate.name = result.get("name").asString()
        candidate.surname = result.get("surName").asString()
        candidate.email= result.get("email").asString()
        String dob = result.get("dob").asString()
        println(dob)
        candidate.dob = LocalDate.parse(dob.find(/\d{4}-\d{2}-\d{2}/), 'yyyy-MM-dd')
        candidate.cpf = result.get("cpf").asString()
        candidate.country = result.get("country").asString()
        candidate.cep = result.get("cep").asString()
        candidate.state = result.get("state").asString()
        candidate.description = result.get("description").asString()
        candidate.skills = new Skills()

        String password = result.get("password").asString()





        postgres.insertCandidate(candidate, password)


    }

    public void  sendCompanyToDB(JsonObject result){

        println("entrei no company")
        ConnectPostgres postgres = new PostgresConnect()

        Connection conn = postgres.connect()

        println("conectei")


        Company company = new Company()
        company.name = result.get("name").asString()
        company.email= result.get("email").asString()
        company.cnpj = result.get("cnpj").asInt()
        company.country = result.get("country").asString()
        company.cep = result.get("cep").asString()
        company.state = result.get("state").asString()
        company.description = result.get("description").asString()
        company.skills = new Skills()
        company.vacancy = new Vacancy()

        String password = result.get("password").asString()

        postgres.insertCompany(company, password)

    }

    public JsonArray getAllCompanies(){
        ConnectPostgres postgres = new ConnectPostgres()

        List<Company> companies = postgres.showALLCompanies()

        JsonArray array = new JsonArray()

        for(Company company : companies){
            JsonObject elementJson = new JsonObject()

            elementJson.add("type", "company")
            elementJson.add("email", company.email)
            elementJson.add("name", company.name)
            elementJson.add("cnpj", company.cnpj)
            elementJson.add("cep", company.cep)
            elementJson.add("state", company.state)
            elementJson.add("country", company.country)
            elementJson.add("description", company.description)
            elementJson.add("vacancy", company.vacancy.name)
            elementJson.add("skills", company.skills.skills.toString())



            array.add(elementJson)
        }


        return array

    }

    public JsonArray getAllCandidates() {
        ConnectPostgres postgres = new ConnectPostgres()

        List<Candidate> candidates = postgres.showALLCandidates()

        JsonArray array = new JsonArray()

        for (Candidate candidate : candidates) {
            JsonObject elementJson = new JsonObject()

            elementJson.add("type", "candidate")
            elementJson.add("email", candidate.email)
            elementJson.add("name", candidate.name)
            elementJson.add("surname", candidate.surname)
            elementJson.add("dob", "${candidate.dob}")
            elementJson.add("cpf", candidate.cpf)
            elementJson.add("cep", candidate.cep)
            elementJson.add("state", candidate.state)
            elementJson.add("country", candidate.country)
            elementJson.add("description", candidate.description)
            elementJson.add("skills", candidate.skills.skills.toString())

            array.add(elementJson)
        }


        return array
    }
}
