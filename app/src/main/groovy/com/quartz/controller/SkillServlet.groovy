package com.quartz.controller

import com.eclipsesource.json.Json
import com.eclipsesource.json.JsonObject
import com.quartz.dto.ConnectPostgres
import com.quartz.model.person.Skills
import jakarta.servlet.ServletException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import java.util.stream.Collectors

@WebServlet("/skill")
class SkillServlet extends HttpServlet{

    void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        BufferedReader result = request.getReader()

        response.addHeader("Access-Control-Allow-Origin", "*")

        ConnectPostgres postgres = new ConnectPostgres()

        String requestData = result.lines().collect(Collectors.joining())


        JsonObject resultJson = Json.parse(requestData).asObject()
        String type = resultJson.get("type").asString()
        String skillString = resultJson.get("skill").asString()
        String email = resultJson.get("email").asString()

        Skills skill = new Skills()

        skill.addSkillToList(skillString)

        if(type == 'candidate'){
            postgres.insertCandidateSkills(skill, email)
        }
        else if (type == 'company'){
            String title = resultJson.get("vacancy").asString()

            postgres.insertVacancySkills(title, email, skill)
        }



    }

    void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{


    }

}
