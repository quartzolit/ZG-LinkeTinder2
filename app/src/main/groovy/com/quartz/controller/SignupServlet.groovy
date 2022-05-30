package com.quartz.controller

import com.eclipsesource.json.Json
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

    public void  sendCandidateToDB(JsonObject result){

        ConnectPostgres postgres = new ConnectPostgres()

        Connection conn = postgres.connect()


        Candidate candidate = new Candidate()
        candidate.name = result.get("name").asString()
        candidate.surname = result.get("name").asString()
        candidate.email= result.get("login").asString()
        candidate.age = result.get("age").asInt()
        candidate.cpf = result.get("cpf").asString()
        candidate.country = "Brazil"
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

}
