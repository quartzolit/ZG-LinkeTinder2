package com.quartz.model.person

@groovy.transform.InheritConstructors
@groovy.transform.ToString
class Company extends Person {
    String name;
    String cnpj;
    List<Vacancy> vacancy;

    void createVacancy(String name, Skills skills) {
        this.vacancy<< new Vacancy(name: name, desiredSkills: skills);


    }

    void listVacancies() {
        println(vacancy.toString());

    }

    void deleteVacancy(int id) {
        this.vacancy.remove(id);

    }


}
