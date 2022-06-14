package com.quartz.dto

import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
import com.quartz.model.person.Skills
import com.quartz.model.person.Vacancy

import java.sql.*
import java.time.LocalDate
import java.util.Date

class ConnectPostgres implements IConnect {

     @Override
    Connection connect() {
        Properties props = new Properties()
        props.setProperty("user", "arthur")
        props.setProperty("password", "123456")
        props.setProperty("ssl", "false")
        String url = "jdbc:postgresql://localhost:5432/linketinder"

        try{
            Class.forName("org.postgresql.Driver")
            return DriverManager.getConnection(url,props)
        }catch(Exception e){
            e.stackTrace()
            if(e instanceof ClassNotFoundException){
                println("Check Driver's connection.")
            }else{
                println("Check if the database is active.")
            }
            println("Nothing found!!!")
            System.exit(-42)
            return null
        }
    }

    @Override
    void disconnect(Connection conn) {
        if(conn != null){
            try {
                conn.close()
            } catch (SQLException e) {
                e.printStackTrace()
            }
        }
    }

    @Override
    List<Candidate> showALLCandidates() {
        String select_all = "SELECT ca.id, ca.name,ca.surname, ca.birthdate,ca.email, ca.cpf, ca.state, ca.cep, \n"+
                " ca.country, ca.personal_description, string_agg(s.skill_name, ',') AS \"Skills List\"  \n"+
                "from candidates AS ca, skills AS s, candidates_skills AS cs \n"+
                "WHERE cs.id_skill = s.id AND cs.id_candidate = ca.id Group By ca.id ORDER BY ca.id"

        Connection conn = null
        Statement candidates = null
        def listOfCandidates = []

        try{
            conn = connect()

            candidates = conn.prepareStatement(
                    select_all,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            ResultSet res = candidates.executeQuery()

            int qtd = getResultSetLength(res)

            if (qtd >0){

                while (res.next()){

                    int id = res.getInt(1)
                    String name = res.getString(2)
                    String surName = res.getString(3)
                    LocalDate dateOfBirth = res.getDate(4)
                    String email = res.getString(5)
                    String cpf =  res.getString(6)
                    String state = res.getString(7)
                    String cep = res.getString(8)
                    String country = res.getString(9)
                    String description = res.getString(10)
                    String skillsList = res.getString(11)

                    //int age = DateManagement.getAge(dateOfBirth)


                    def skillsListArray = skillsList.split(",")


                    listOfCandidates << new Candidate(id: id, name: name, surname: surName, email: email,
                            cpf: cpf, dob: dateOfBirth, state: state, cep: cep,
                            description: description, country: country,
                            skills: new Skills(skills: skillsListArray))
                }
            }
            else{
                println("There is no candidate yet")
            }

            return listOfCandidates

        }catch(Exception e){
            e.stackTrace()
            println("Error searching desired information")
            System.exit(-42)

        }finally{
            candidates.close()
            disconnect(conn)
        }

    }

    List<Company> showALLCompanies() {
        String select_all = "SELECT co.id,co.name, co.cnpj, co.email, co.company_description, co.state, co.cep, co.country, v.title,\n" +
                "string_agg(s.skill_name, ',') AS \"Skills List\"\n" +
                "from companies AS co,\n" +
                "skills AS s,\n" +
                "vacancies_skills AS vs, \n" +
                "vacancies AS v\n" +
                "WHERE vs.id_skill = s.id \n" +
                "AND vs.id_vacancy = v.id\n" +
                "AND v.id_company = co.id  Group By co.id, v.title ORDER BY co.id"
        Connection conn = null
        Statement companies = null
        try{
            List<Candidate> listOfCompanies = []
            conn = connect()
            companies = conn.prepareStatement(
                    select_all,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            ResultSet res = companies.executeQuery()


            int qtd = getResultSetLength(res)


            if (qtd >0){
                while (res.next()){
                    int id = res.getInt(1)
                    String name = res.getString(2)
                    String cnpj = res.getString(3)
                    String email = res.getString(4)
                    String description = res.getString(5)
                    String state = res.getString(6)
                    String cep = res.getString(7)
                    String country = res.getString(8)
                    String title = res.getString(9)
                    String skillList = res.getString(10)



                    def skillsListArray = skillList.split(",")

                    listOfCompanies << new Company(id: id, name: name, email: email
                            ,cnpj: cnpj, country: country, state: state, cep: cep
                            , description: description
                            , skills: new Skills(skills: skillsListArray)
                            , vacancy: new Vacancy(name: title, desiredSkills: new Skills(skills: skillsListArray)))
                }
            }
            else{
                println("There is no candidate yet")
            }

            return listOfCompanies

        }catch(Exception e){
            e.stackTrace()
            println("Error searching desired information")
            System.exit(-42)

        }finally{
            companies.close()
            disconnect(conn)
        }
    }

    List<Vacancy> showALLCompanyVacanciesByCompanyId(int id) {
        String select_all = "SELECT v.id, v.title, string_agg(s.skill_name, ',') AS \"Skills List\"\n" +
                "FROM vacancies AS v,\n" +
                "vacancies_skills AS vs,\n" +
                "skills AS s\n" +
                "WHERE v.id_company = $id\n" +
                "AND vs.id_skill = s.id\n" +
                "AND vs.id_vacancy = v.id\n" +
                "GROUP BY v.id\n" +
                "ORDER BY v.id;"
        Connection conn = null
        Statement vacancies = null
        List<Vacancy> listOfVacancies = []
        try{
            conn = connect()
            vacancies = conn.prepareStatement(
                    select_all,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            ResultSet res = vacancies.executeQuery()

            int qtd = getResultSetLength(res)

            if (qtd >0){
                println("Listing Companies...")
                println("---------------------")

                while (res.next()){
                    int idReceived = res.getInt(1)
                    String title = res.getString(2)
                    String skillList = res.getString(3)

                    skillList = skillList.split(",")

                    def skillsListArray = skillList.split(",")

                    listOfVacancies<< new Vacancy(id: idReceived, name: title, desiredSkills: new Skills(skills: skillsListArray ) )
                }


            }
            else{
                println("There is no candidate yet")
            }



        }catch(Exception e){
            e.stackTrace()
            println("Error searching desired information")
            System.exit(-42)

        }finally{
            vacancies.close()
            disconnect(conn)
        }

    }

    @Override
    Candidate showCandidateByEmail(String candidateEmail, String candidatePassword) {

        Connection conn = null
        PreparedStatement getCandidate = null
        try{
            conn = connect()

            String singleCandidate = "SELECT ca.*, string_agg(s.skill_name, ',') from candidates as ca, skills as s, candidates_skills as cs\n" +
                    "WHERE cs.id_candidate = ca.id AND cs.id_skill = s.id AND ca.email = ? GROUP BY\n" +
                    "ca.id ORDER BY ca.id;"

            getCandidate = conn.prepareStatement(
                    singleCandidate,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            getCandidate.setString(1, candidateEmail)

            ResultSet res = getCandidate.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd>0){
                res.next()

                int id = res.getInt(1)
                String name = res.getString(2)
                String surName = res.getString(3)
                LocalDate dateOfBirth = LocalDate.parse(res.getDate(4).toString(), 'yyyy-MM-dd')
                String email = res.getString(5)
                String cpf =  res.getString(6)
                String state = res.getString(7)
                String cep = res.getString(8)
                String country = res.getString(9)
                String description = res.getString(10)
                String password = res.getString(11)
                String skillsList = res.getString(12)

                def skillsListArray = skillsList.split(",")


                Candidate ca = new Candidate(id: id, name: name, surname: surName, email: email,
                        cpf: cpf, dob: dateOfBirth, state: state, cep: cep,
                        description: description, country: country,
                        skills: new Skills(skills: skillsListArray))

                getCandidate.close()
                disconnect(conn)

                if(!(candidateEmail == email && candidatePassword == password)){
                    println("Email or Password doesn't match")
                    return null
                }

                return ca

            }else {
                println("Candidate not found")
                return null
            }

        }catch(Exception e){
        e.printStackTrace()
        System.err.println("Erro ao encontrar o candidato")
        System.exit(-42)
        }
        finally {
            getCandidate.close()
            disconnect(conn)
        }
    }

    @Override
    Company showCompanyByEmail(String companyEmail, String companyPassword) {

        String select_all = "SELECT co.*, v.title,\n" +
                "string_agg(s.skill_name, ',') AS \"Skills List\"\n" +
                "from companies AS co,\n" +
                "skills AS s,\n" +
                "vacancies_skills AS vs, \n" +
                "vacancies AS v\n" +
                "WHERE vs.id_skill = s.id \n" +
                "AND vs.id_vacancy = v.id\n" +
                "AND v.id_company = co.id \n" +
                "AND co.email = ?  Group By co.id, v.title ORDER BY co.id"
        Connection conn = null
        Statement getCompany = null
        try{
            conn = connect()
            getCompany = conn.prepareStatement(
                    select_all,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            getCompany.setString(1, companyEmail )



            ResultSet res = getCompany.executeQuery()


            int qtd = getResultSetLength(res)


            if (qtd >0){
                res.next()

                int id = res.getInt(1)
                String name = res.getString(2)
                String cnpj = res.getString(3)
                String email = res.getString(4)
                String description = res.getString(5)
                String state = res.getString(6)
                String cep = res.getString(7)
                String country = res.getString(8)
                String password = res.getString(9)
                String title = res.getString(10)
                String skillList = res.getString(11)



                def skillsListArray = skillList.split(",")

                Company co = new Company(id: id, name: name, email: email
                        ,cnpj: cnpj, country: country, state: state, cep: cep
                        , description: description
                        , skills: new Skills(skills: skillsListArray)
                        , vacancy: new Vacancy(name: title, desiredSkills: new Skills(skills: skillsListArray)))


                if(!(email == companyEmail && password == companyPassword)){
                    println("Email or password does not match")
                    return null
                }

                return co
            }
            else{
                println("There is no Company with specified e-mail")
                return null
            }


        }catch(Exception e){
            e.printStackTrace()
            System.err.println("Erro ao encontrar o candidato")
            System.exit(-42)
        }
        finally {
            getCompany.close()
            disconnect(conn)
        }
    }

    @Override
    void insertCandidate(Candidate person, String password) {
        try{
            Connection conn = connect()

            //def dateOfBirth = DateManagement.getDateOfBirth(person.age)


            String searchForCandidate = "SELECT * FROM candidates WHERE email = ?"

            String insertCandidate = "INSERT INTO candidates (name, surname, birthdate, email, cpf, state, cep, country, personal_description, password)\n"+
                    "VALUES (?,?,?,?,?,?,?,?,?,?)"



                PreparedStatement candidateFilter = conn.prepareStatement(
                        searchForCandidate,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )

                candidateFilter.setString(1, person.email)

                ResultSet res = candidateFilter.executeQuery()

                int qtd = getResultSetLength(res)

                if(qtd > 0){
                    println("A user with this e-mail already exists")
                }
                else {

                    PreparedStatement insert = conn.prepareStatement(insertCandidate)

                    insert.setString(1,person.name)
                    insert.setString(2,person.surname)
                    insert.setDate(3,person.dob)
                    insert.setString(4,person.email)
                    insert.setString(5,person.cpf)
                    insert.setString(6,person.state)
                    insert.setString(7,person.cep)
                    insert.setString(8,person.country)
                    insert.setString(9,person.description)
                    insert.setString(10,password)


                    insert.executeUpdate()
                    insert.close()

                }
            candidateFilter.close()
            disconnect(conn)
        }catch(Exception e){
            e.printStackTrace()
            System.err.println("Erro ao inserir o candidato")
            System.exit(-42)
        }

    }

    void insertCandidateSkills(Skills skills, String email){
        try{
            Connection conn = connect()

            String skill= null

            while (skills.skills.size()>0){

                skill = skills.skills.remove(0)
                String searchSkillOnDB = "SELECT * FROM skills WHERE skill_name = ?"

                PreparedStatement searchSkill = conn.prepareStatement(
                        searchSkillOnDB,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )

                searchSkill.setString(1,skill)

                ResultSet res = searchSkill.executeQuery()

                int qtd = getResultSetLength(res)

                if(qtd>0){
                    insertCandidateSkillRelations(email, skill)

                }else {
                    String skillInsert = "INSERT INTO skills (skill_name) VALUES(?)"

                    PreparedStatement insertSkill = conn.prepareStatement(skillInsert)

                    insertSkill.setString(1,skill);

                    insertSkill.executeUpdate();
                    insertCandidateSkillRelations(email, skill)
                    insertSkill.close();
                }
                searchSkill.close()
            }
            disconnect(conn)

        }catch(Exception e){
            e.stackTrace()
            println("Connection error")
            System.exit(-42)
        }


    }

    void insertCandidateSkillRelations(String email, String skill){
        try{

            Connection conn = connect()

            String searchIDs = "SELECT ca.id, s.id FROM candidates AS ca, skills AS s WHERE ca.email = ? AND s.skill_name = ?; "

            PreparedStatement selectIDS = conn.prepareStatement(
                    searchIDs,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            selectIDS.setString(1,email)
            selectIDS.setString(2,skill)

            ResultSet res = selectIDS.executeQuery();

            int qtd = getResultSetLength(res)

            println(qtd + "Numero de encontros")

            if(qtd > 0){
                res.next()

                int idCandidate = res.getInt(1)
                int idSkill = res.getInt(2)

                String insertSkillRelation = "INSERT INTO candidates_skills (id_skill,id_candidate) VALUES (?,?)"

                PreparedStatement insertRelation = conn.prepareStatement(insertSkillRelation)

                insertRelation.setInt(1, idSkill)
                insertRelation.setInt(2, idCandidate)

                insertRelation.executeUpdate()

                println("executou o update")
                insertRelation.close()

                selectIDS.close()
                disconnect(conn)
            }else{
                println("ID not found. Please check if everything is correct")
            }
        }catch(Exception e){
            e.stackTrace();
            println("Connection not Found")
            System.exit(-42)
        }

    }

    void insertCompany(Company person, String password){
        try{
            Connection conn = connect()
            String searchForCompany = "SELECT * FROM companies WHERE email = ?"

            String insertCompany = "INSERT INTO companies (name, cnpj, email, company_description, state, cep, country, password)\n"+
                    "VALUES (?,?,?,?,?,?,?,?)"

            PreparedStatement companyFilter = conn.prepareStatement(
                    searchForCompany,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            companyFilter.setString(1, person.email)

            ResultSet res = companyFilter.executeQuery()

            int qtd = getResultSetLength(res)

            println("peguei a quantidade ${qtd}")

            if(qtd > 0){
                println("A user with this e-mail already exists")
            }
            else {

                PreparedStatement insert = conn.prepareStatement(insertCompany)

                insert.setString(1,person.name)
                insert.setString(2,person.cnpj)
                insert.setString(3,person.email)
                insert.setString(4,person.description)
                insert.setString(5,person.state)
                insert.setString(6,person.cep)
                insert.setString(7,person.country)
                insert.setString(8,password)

                insert.executeUpdate()
                println("executou o update")
                insert.close()
            }

            companyFilter.close()
            disconnect(conn)

        }catch(Exception e){
            e.stackTrace();
            println("Connection problem")
            System.exit(-42)
        }
    }

    void insertVacancy(Vacancy vacancy, String email){
        String searchForCompany = "SELECT id FROM companies WHERE email = ?"

        try {

            Connection conn = connect()
            PreparedStatement searchCreatedCompany = conn.prepareStatement(
                    searchForCompany,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            searchCreatedCompany.setString(1,email)

            ResultSet res = searchCreatedCompany.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd>0){
                    res.next()
                    int id = res.getInt(1)
                    String vacancyCreationSQL = "INSERT INTO vacancies (title, id_company) VALUES(?,?)"
                    PreparedStatement createVacancy = conn.prepareStatement(vacancyCreationSQL)

                    createVacancy.setString(1, vacancy.name)
                    createVacancy.setInt(2, id)

                    createVacancy.executeUpdate();

                    println("cheguei aqui")



                createVacancy.close()
                searchCreatedCompany.close()
                disconnect(conn)
               // insertVacancySkills(vacancy, email, false)
            }
            else {
                println("Account not found")
                searchCreatedCompany.close()
                disconnect(conn)
            }

        }catch(Exception e){
            e.stackTrace();
            println("Error During Connect")
            System.exit(-42)
        }

    }

    void insertVacancySkills(String vacancyTitle, String email, Skills skills){

        try{
            Connection conn = connect()

            String skill
            while (skills.skills.size()>0){

                skill = skills.skills.remove(0)
                String searchSkillOnDB = "SELECT * FROM skills WHERE skill_name = ?"

                PreparedStatement searchSkill = conn.prepareStatement(
                        searchSkillOnDB,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )

                searchSkill.setString(1,skill)

                ResultSet res = searchSkill.executeQuery()

                int qtd = getResultSetLength(res)

                println("encontrei a skill ${skill}")

                if(qtd>0){
                    insertCompanySkillRelations(email, vacancyTitle, skill)

                }else {
                    String skillInsert = "INSERT INTO skills (skill_name) VALUES(?)"

                    PreparedStatement insertSkill = conn.prepareStatement(skillInsert)

                    insertSkill.setString(1,skill);

                    insertSkill.executeUpdate();
                    insertCompanySkillRelations(email, vacancyTitle, skill)
                    insertSkill.close()

                }
                searchSkill.close()
            }
            disconnect(conn)
        }catch(Exception e){
            e.stackTrace()
            println("Connection error")
            System.exit(-42)
        }

    }

    void insertCompanySkillRelations(String email, String  title, String skill){
        try{

            Connection conn = connect()

            String searchIDs = "SELECT v.id, s.id FROM vacancies AS v, skills AS s, companies AS co\n" +
                    "WHERE v.id_company = co.id AND co.email = ?\n" +
                    "AND s.skill_name = ?\n"+
                    "AND v.title = ?"

            PreparedStatement selectIDS = conn.prepareStatement(
                    searchIDs,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            selectIDS.setString(1,email)
            selectIDS.setString(2,skill)
            selectIDS.setString(3,title)

            ResultSet res = selectIDS.executeQuery();

            int qtd = getResultSetLength(res)

            if(qtd > 0){
                res.next()
                int idVacancy = res.getInt(1)
                int idSkill = res.getInt(2)

                println("peguei os valores")

                String insertSkillRelation = "INSERT INTO vacancies_skills (id_skill,id_vacancy) VALUES (?,?)"

                PreparedStatement insertRelation = conn.prepareStatement(insertSkillRelation)

                insertRelation.setInt(1,idSkill)
                insertRelation.setInt(2,idVacancy)

                insertRelation.executeUpdate()
                insertRelation.close()
                selectIDS.close()
                disconnect(conn)
            }else{
                println("ID not found. Please check if everything is correct")
            }
        }catch(Exception e){
            e.stackTrace();
            println("Connection not Found")
            System.exit(-42)
        }

    }

    @Override
    void updateCandidate(Candidate person, int idCandidate) {

        try{

            String searchId = "SELECT * FROM candidates where id = ?"
            String updateCandidate = "Update FROM candidates \n"+
                    "set name = ?, surname = ?, birthdate = ?, \n"+
                    "email =?, cpf = ?, state =?, cep =?, country =?, \n"+
                    "personal_description =? WHERE id = ?"

            Connection conn = connect()

            PreparedStatement search = conn.prepareStatement(
                    searchId,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            search.setInt(1,idCandidate)

            ResultSet res = search.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd>0){
                PreparedStatement update = conn.prepareStatement(updateCandidate)

                update.setString(1,person.name);
                update.setString(2,person.surname);
                update.setDate(3,person.dob);
                update.setString(4,person.email);
                update.setString(5,person.cpf);
                update.setString(6,person.state);
                update.setString(7,person.cep);
                update.setString(8,person.country);
                update.setString(9,person.description);
                update.setInt(10,idCandidate);

                update.executeUpdate()
                update.close()
                search.close()
                disconnect(conn)
            }

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }

    }

    void updateCompany(Company person, int idCompany) {

        try{

            String searchId = "SELECT * FROM companies where id = ?"
            String updateCompany = "Update FROM companies \n"+
                    "set name = ?, cnpj = ?, email = ?, \n"+
                    "company_description =?, state = ?, cep =?, country =?, \n"+
                    " WHERE id = ?"

            Connection conn = connect()

            PreparedStatement search = conn.prepareStatement(
                    searchId,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            search.setInt(1,idCompany)

            ResultSet res = search.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd>0){

                PreparedStatement update = conn.prepareStatement(updateCompany)

                update.setString(1,person.name);
                update.setString(2,person.cnpj);
                update.setString(3,person.email);
                update.setString(4,person.description);
                update.setString(5,person.state);
                update.setString(6,person.cep);
                update.setString(7,person.country);
                update.setInt(8,idCompany);

                update.executeUpdate()
                update.close()
                search.close()
                disconnect(conn)
            }

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }

    }

    void updateVacancy(Vacancy vacancy, int idVacancy, int idCompany) {

        try{

            String searchId = "SELECT * FROM vacancies where id = ?"
            String updateVacancy = "Update FROM companies \n"+
                    "set title = ?, id_company = ? \n"+
                    " WHERE id = ?"

            Connection conn = connect()

            PreparedStatement search = conn.prepareStatement(
                    searchId,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            search.setInt(1,idVacancy)

            ResultSet res = search.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd>0){

                PreparedStatement update = conn.prepareStatement(updateVacancy)

                update.setString(1,vacancy.name);
                update.setInt(2,idCompany);
                update.setInt(3,idVacancy);

                update.executeUpdate()
                update.close()
                search.close()
                disconnect(conn)
            }

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }

    }

    void updateSkill(String skill, int idSkill) {
        try{

            String searchId = "SELECT * FROM skills where id = ?"
            String updateSkill = "Update FROM companies \n"+
                    "set skill_name = ?\n"+
                    " WHERE id = ?"

            Connection conn = connect()

            PreparedStatement search = conn.prepareStatement(
                    searchId,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            search.setInt(1,idSkill)

            ResultSet res = search.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd>0){

                PreparedStatement update = conn.prepareStatement(updateSkill)

                update.setString(1,skill);
                update.setInt(3,idSkill);

                update.executeUpdate()
                update.close()
                search.close()
                disconnect(conn)
            }
        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }
    }

    @Override
    void deleteCandidate(int idCandidate) {
        try{

            String deleteCandidate = "DELETE FROM candidates AS ca, candidates_skills AS cs, \n"+
                    "companies_candidates AS cc, vacancies_candidates AS vc \n"+
                    " WHERE vc.id_candidate = cc.id_candidate AND cc.id_candidate = cs.id_candidate \n"+
                    "AND cs.id_candidate = ca.id AND ca.id = ?"

            Connection conn = connect()

            PreparedStatement delete = conn.prepareStatement(deleteCandidate )

            delete.setInt(1,idCandidate)

            delete.executeUpdate()
            delete.close()
            disconnect(conn)

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }
    }

    @Override
    void deleteCompany(int idCompany) {
        try{

            String deleteCompany = "DELETE FROM company AS co, vacancies AS v, \n"+
                    "companies_candidates AS cc, vacancies_candidates AS vc, vacancies_skills AS vs \n"+
                    " WHERE vs.id_vacancy = v.id AND cc.id_company = co.id AND vc.id_vacancy = v.id \n"+
                    "AND cc.id_company = co.id AND v.id_company = co.id AND co.id = ?"

            Connection conn = connect()

            PreparedStatement delete = conn.prepareStatement(deleteCompany )

            delete.setInt(1,idCompany)

            delete.executeUpdate()
            delete.close()
            disconnect(conn)

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }
    }

    void deleteVacancy(int idVacancy) {
        try{

            String deleteVacancy = "DELETE FROM vacancies AS v, \n"+
                    "vacancies_candidates AS vc, vacancies_skills AS vs \n"+
                    " WHERE vs.id_vacancy = v.id AND vc.id_vacancy = v.id \n"+
                    "AND v.id = ?"

            Connection conn = connect()

            PreparedStatement delete = conn.prepareStatement(deleteVacancy)

            delete.setInt(1,idVacancy)

            delete.executeUpdate()
            delete.close()
            disconnect(conn)

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }
    }

    void deleteSkill(int idSkill) {
        try{

            String deleteSkill = "DELETE FROM skills AS s, \n"+
                    "candidates_skills AS cs, vacancies_skills AS vs \n"+
                    " WHERE vs.id_skill = s.id AND cs.id_skill = s.id \n"+
                    "AND s.id = ?"

            Connection conn = connect()

            PreparedStatement delete = conn.prepareStatement(deleteSkill)

            delete.setInt(1,idSkill)

            delete.executeUpdate()
            delete.close()
            disconnect(conn)

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }
    }

    int getResultSetLength (ResultSet res){

        res.last()
        int qtd = res.getRow()
        res.beforeFirst()
        return qtd
    }
}
