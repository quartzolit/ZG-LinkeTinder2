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
import java.util.stream.Collectors

@WebServlet("/person")
class SignupServlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        BufferedReader result = request.getReader()

        response.addHeader("Access-Control-Allow-Origin", "*");

        String requestData = result.lines().collect(Collectors.joining())

        JsonObject resultJson = Json.parse(requestData).asObject()

        println(resultJson.toString())

        String objectType = resultJson.get("type").asString()


        if(objectType == "candidate"){
            sendCandidateToDB(resultJson)
        }else{
            sendCompanyToDB(resultJson)
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String type = request.getParameterValues("type")

        response.addHeader("Access-Control-Allow-Origin", "*");

        JsonObject resJson = new JsonObject()

        if(type == "[candidate]"){
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


    public void  sendCandidateToDB(JsonObject result){

        ConnectPostgres postgres = new ConnectPostgres()

        Connection conn = postgres.connect()


        Candidate candidate = new Candidate()
        candidate.name = result.get("name").asString()
        candidate.surname = result.get("name").asString()
        candidate.email= result.get("login").asString()
        candidate.dob = result.get("dob").asInt()
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
        company.email= result.get("login").asString()
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

    public JsonObject getAllCompanies(){
        ConnectPostgres postgres = new ConnectPostgres()

        List<Company> companies = postgres.showALLCompanies()

        JsonObject resJson = new JsonObject()

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

        resJson.add("companies", array)


        return resJson

    }

    public JsonObject getAllCandidates() {
        ConnectPostgres postgres = new ConnectPostgres()

        List<Candidate> candidates = postgres.showALLCandidates()

        JsonObject resJson = new JsonObject()

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

        resJson.add("candidates",array)

        return resJson
    }
}
