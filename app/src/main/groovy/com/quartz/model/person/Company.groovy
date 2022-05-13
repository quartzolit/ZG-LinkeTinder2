package com.quartz.model.person

@groovy.transform.InheritConstructors
@groovy.transform.ToString
class Company extends Person {
    String name;
    String cnpj;
    Vacancy vacancy;

    void createVacancy(String name, Skills skills) {
        this.vacancy = new Vacancy(name: name, desiredSkills: skills);


    }

    void listVacancies() {
        println(vacancy);

    }

    void deleteVacancy(int id) {
        this.vacancy = null

    }


}
