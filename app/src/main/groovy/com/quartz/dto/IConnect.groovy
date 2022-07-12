package com.quartz.dto

import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
import com.quartz.model.person.Person
import com.quartz.model.person.Skills
import com.quartz.model.person.Vacancy

import java.sql.Connection

interface IConnect {

    Connection connect();

    void disconnect(Connection conn);

    List<Candidate> showALLCandidates();

    List<Company> showALLCompanies();

    List<Vacancy> showALLCompanyVacanciesByCompanyId(int id);

    Candidate showCandidateByEmail(String candidateEmail);

    Company showCompanyByEmail(String companyEmail);

    void insertCandidate(Candidate person, String password);

    void insertCandidateSkills(Skills skills, String email);

    void insertCandidateSkillRelations(String email, String skill)

    void insertCompany(Company person, String password)

    void insertCompanySkills(Skills skills, String email)

    void insertCompanySkillRelations(String email, String skill)

    void insertVacancy(Vacancy vacancy, String email)

    void insertVacancySkills(String title, String email, Skills skills)

    void insertVacancySkillRelations(String email, String title, String skill)

    void updateCandidate(Candidate person);

    void updateSkillRelation(Person person);

    void updateCompany(Company person)



    void updateVacancy(Vacancy vacancy, int idVacancy, int idCompany)

    void updateSkill(String skill, int idSkill)

    void deleteCandidate(int idCandidate);

    void deleteCompany(int idCompany);

    void deleteSkill(int idSkill);

    void deleteVacancy(int idVacancy);


}