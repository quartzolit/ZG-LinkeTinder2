package com.quartz.controller

import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonObject
import com.quartz.dto.ConnectPostgres
import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
import com.quartz.model.person.Person
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import java.util.stream.Collectors

@WebServlet("/login")
class LoggedServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BufferedReader result = request.getReader()

        response.addHeader("Access-Control-Allow-Origin", "*")

        ConnectPostgres postgres = new ConnectPostgres()

        String requestData = result.lines().collect(Collectors.joining())


        JsonObject resultJson = Json.parse(requestData).asObject()
        String objectEmail = resultJson.get("userEmail").asString()
        String objectPassword = resultJson.get("userPassword").asString()

        Candidate isCandidate = postgres.showCandidateByEmail(objectEmail)

        println(isCandidate)

        if(isCandidate!= null){
            println("entrei aqui")
            JsonObject jsonRes = null

            if(isCandidate.password == objectPassword ){
                jsonRes = getCandidateJson(isCandidate)

            }
            PrintWriter responseJson = response.getWriter()
            //response.setContentType("application/json")
            //response.getCharacterEncoding("UTF-8")
            responseJson.print(jsonRes)
            responseJson.flush()

        }else{
            println("entrei no lugar certo")

            Company isCompany = postgres.showCompanyByEmail(objectEmail)

            if(isCompany){

                JsonObject jsonRes = null

                if(isCompany.password == objectPassword){
                    jsonRes = getCompanyJson(isCompany)

                }

                PrintWriter responseJson = response.getWriter()
                //response.setContentType("application/json")
                //response.getCharacterEncoding("UTF-8")
                responseJson.print(jsonRes)
                responseJson.flush()
            }else {

                JsonObject jsonRes = null

                PrintWriter responseJson = response.getWriter()
                response.setContentType("application/json")
                response.getCharacterEncoding("UTF-8")
                resultJson.print(jsonRes)
                responseJson.flush()

            }

        }

    }

    public JsonObject getCandidateJson(Candidate candidate){
        JsonObject loggedPerson = new JsonObject()

        loggedPerson.add("type", "candidate")
        loggedPerson.add("email", candidate.email)
        loggedPerson.add("name", candidate.name)
        loggedPerson.add("surName", candidate.surname)
        loggedPerson.add("dob", "${candidate.dob}")
        loggedPerson.add("cpf", candidate.cpf)
        loggedPerson.add("cep", candidate.cep)
        loggedPerson.add("state", candidate.state)
        loggedPerson.add("country", candidate.country)
        loggedPerson.add("description", candidate.description)
        loggedPerson.add("skills", candidate.skills.skills.toString())

    }

    public JsonObject getCompanyJson(Company company){
        JsonObject loggedPerson = new JsonObject()

        loggedPerson.add("type", "company")
        loggedPerson.add("id", company.id)
        loggedPerson.add("email", company.email)
        loggedPerson.add("name", company.name)
        loggedPerson.add("cnpj", company.cnpj)
        loggedPerson.add("cep", company.cep)
        loggedPerson.add("state", company.state)
        loggedPerson.add("country", company.country)
        loggedPerson.add("description", company.description)
        loggedPerson.add("skills", company.skills.skills.toString())

    }
}
