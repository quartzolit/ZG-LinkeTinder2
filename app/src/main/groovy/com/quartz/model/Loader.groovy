package com.quartz.model

import com.quartz.model.person.*

@groovy.transform.ToString
class Loader {

    static List<Person> loadCompanies(){
        Company co1 = new Company(name: "Zero Glosa", email: "comercial@zgsolucoes.com.br"
                ,cnpj: "14.488.144/0001", country: "Brazil", state: "Goiás", cep: "74070-040"
                , description: "Awesome Company to work"
                , skills: new Skills(skills: [EnumSkills.DATABASE, EnumSkills.JAVA, EnumSkills.GROOVY, EnumSkills.GITHUB])
                , vacancy: new Vacancy(name: "dev", desiredSkills: new Skills(skills: [EnumSkills.DATABASE, EnumSkills.JAVA, EnumSkills.GROOVY, EnumSkills.GITHUB])));
        Company co2 = new Company(name: "Petrobras", email: "comercial@petrobras.com.br"
                ,cnpj: "33.000.167/1049-00", country: "Brazil", state: "Rio de Janeiro", cep: "20.031-912"
                , description: "Gas super high price"
                , skills: new Skills(skills: [EnumSkills.CSS, EnumSkills.HTML, EnumSkills.BACKEND, EnumSkills.GITHUB])
                , vacancy: new Vacancy(name: "dev", desiredSkills: new Skills(skills: [EnumSkills.CSS, EnumSkills.HTML, EnumSkills.BACKEND, EnumSkills.GITHUB])));

        Company co3 = new Company(name: "Arroz-Gostoso", email: "comercial@arrozgostoso.com.br"
                ,cnpj: "12.544.231/0011", country: "Brazil", state: "Mato Grosso do Sul", cep: "69512-030"
                , description: "Selling good quality rice"
                , skills: new Skills(skills: [EnumSkills.DATABASE, EnumSkills.FRONTEND, EnumSkills.REGEX, EnumSkills.CSS])
                , vacancy: new Vacancy(name: "dev", desiredSkills: new Skills(skills: [EnumSkills.DATABASE, EnumSkills.FRONTEND, EnumSkills.REGEX, EnumSkills.CSS])));

        Company co4 = new Company(name: "Império do Boliche", email: "boliche@imperio.com.br"
                ,cnpj: "84.521.799/0005", country: "Brazil", state: "Maranhão", cep: "81224-103"
                , description: "Come play with us"
                , skills: new Skills(skills: [EnumSkills.BACKEND, EnumSkills.FRONTEND, EnumSkills.DATABASE, EnumSkills.GITHUB])
                , vacancy: new Vacancy(name: "dev", desiredSkills: new Skills(skills: [EnumSkills.BACKEND, EnumSkills.FRONTEND, EnumSkills.DATABASE, EnumSkills.GITHUB])));

        Company co5 = new Company(name: "Boi na Fonte", email: "boi@nafonte.com.br"
                ,cnpj: "87.530.973/0103", country: "Brazil", state: "Goiás", cep: "71522-008"
                , description: "Come refresh your bull"
                , skills: new Skills(skills: [EnumSkills.HIBERNATE, EnumSkills.JAVA, EnumSkills.GROOVY, EnumSkills.HTML, EnumSkills.CSS])
                , vacancy: new Vacancy(name: "dev", desiredSkills: new Skills(skills: [EnumSkills.HIBERNATE, EnumSkills.JAVA, EnumSkills.GROOVY, EnumSkills.HTML, EnumSkills.CSS])));

        println("Companies co1, co2, co3,co4 and co5 loaded")

        return [co1,co2,co3,co4,co5]
    }

    static List<Person> loadCandidates(){
        Candidate ca1 = new Candidate(name: "Luiz Arthur Moura", email: "luiz.moura@acelerazg.com.br"
                    ,cpf: "405.155.608-55", age: 30, state: "São Paulo", cep: "12608-170"
                    , description: "Cool guy"
                    , skills: new Skills(skills: [EnumSkills.CSS, EnumSkills.HTML, EnumSkills.JAVA, EnumSkills.GITHUB, EnumSkills.GROOVY]));

        Candidate ca2 = new Candidate(name: "Josué Farias", email: "josue.faria@gmail.com"
                    ,cpf: "MG-112.344.566", age: 35, state: "Minas Gerais", cep: "30205-102"
                    , description: "Eu não faria, mas Josué farias"
                    , skills: new Skills(skills: [EnumSkills.JAVA, EnumSkills.GROOVY, EnumSkills.BACKEND]));

        Candidate ca3 = new Candidate(name: "Lima Duarte", email: "duarte@yahoo.com.br"
                    ,cpf: "055.223.541-27", age: 70, state: "Rio de Janeiro", cep: "21551-003"
                    , description: "Não me peça para limar. Duarte, Lima"
                    , skills: new Skills(skills: [EnumSkills.BACKEND, EnumSkills.FRONTEND, EnumSkills.DATABASE]));

        Candidate ca4 = new Candidate(name: "Gezebel Tenório", email: "gz.tenorio@uol.com.br"
                    ,cpf: "926.544.321-98", age: 28, state: "São Paulo", cep: "11223-278"
                    , description: "Me passa o gel Gezebel"
                    , skills:new Skills(skills: [EnumSkills.DATABASE, EnumSkills.HIBERNATE, EnumSkills.REGEX, EnumSkills.GITHUB]));

        Candidate ca5 = new Candidate(name: "Tomás Farouk", email: "faroukinho@gmail.com"
                    ,cpf: "523.844.971-56", age: 48, state: "Sergipe", cep: "49000-200"
                    , description: "Gosto de cebola"
                    , skills: new Skills(skills: [EnumSkills.JAVA, EnumSkills.GROOVY]));

        println("Candidates ca1, ca2, ca3, ca4, ca5 loaded")

        return[ca1,ca2,ca3,ca4,ca5]
    }
}
