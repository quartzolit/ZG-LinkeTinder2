package com.quartz.controller

import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonObject
import com.quartz.dto.ConnectPostgres
import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
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

@WebServlet("/update/person")
class UpdateServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BufferedReader result = request.getReader()

        response.addHeader("Access-Control-Allow-Origin", "*")

        String requestData = result.lines().collect(Collectors.joining())

        JsonObject resultJson = Json.parse(requestData).asObject()


        String objectType = resultJson.get("type").asString();


        if(objectType == 'candidate'){
            updateCandidate(resultJson)

        }else{
            updateCompany(resultJson)

        }

    }

    public void  updateCandidate(JsonObject result){

        ConnectPostgres postgres = new ConnectPostgres();

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
        def skilled = result.get("skills").asArray()
        ArrayList<String> skillList = new ArrayList<>()
        skilled.forEach((skill)->{
            skillList.add(skill.toString().replaceAll('"',''))
        })
        candidate.skills = new Skills(skills: skillList)

        postgres.updateCandidate(candidate)


    }

    public void updateCompany(JsonObject result){
        ConnectPostgres postgres = new ConnectPostgres();

        Company company = new Company()
        company.name = result.get("name").asString()
        company.email= result.get("email").asString()
        company.cnpj = result.get("cnpj").asInt()
        company.country = result.get("country").asString()
        company.cep = result.get("cep").asString()
        company.state = result.get("state").asString()
        company.description = result.get("description").asString()
        def skilled = result.get("skills").asArray()
        ArrayList<String> skillList = new ArrayList<>()
        skilled.forEach((skill)->{
            skillList.add(skill.toString().replaceAll('"',''))
        })
        company.skills = new Skills(skills: skillList)

        postgres.updateCompany(company)

    }



}
