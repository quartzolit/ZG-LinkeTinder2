package com.quartz.dto

import com.quartz.model.person.Candidate
import com.quartz.model.person.Company
import com.quartz.model.person.Skills
import com.quartz.model.person.Vacancy

import java.sql.*
import java.util.Date

class ConnectPostgres implements IConnect {

    @Override
    Connection connect() {
        String url = "jdbc:postgresql://localhost:5432/linketinder"
        String driver = ''

        try{
            return DriverManager.getConnection(url,"arthur","123456")
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
        List<Candidate> listOfCandidates = []

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
                    Date dateOfBirth = res.getDate(4)
                    String email = res.getString(5)
                    String cpf =  res.getString(6)
                    String state = res.getString(7)
                    String cep = res.getString(8)
                    String country = res.getString(9)
                    String description = res.getString(10)
                    String skillsList = res.getString(11)

                    int age = DateManagement.getAge(dateOfBirth)

                    skillsList = skillsList.split(",")

                    listOfCandidates << new Candidate(id: id, name: "$name $surName", email: email
                            ,cpf: cpf, age: age, state: state, cep: cep
                            , description: description, country: country
                            , skills: new Skills(skills: skillsList))
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
                "string_agg(s.skill_name, ', ') AS \"Skills List\"\n" +
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

                    skillList = skillList.split(",")

                    listOfCompanies << new Company(id: id, name: name, email: email
                            ,cnpj: cnpj, country: country, state: state, cep: cep
                            , description: description
                            , skills: new Skills(skills: skillList)
                            ,vacancies: new Vacancy(name: title, desiredSkills: new Skills(skills: skillList)))
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

                    listOfVacancies<< new Vacancy(id: idReceived, name: title, desiredSkills: new Skills(skills: skillList ) )
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
    void insertCandidate(Candidate person, String password) {
        try{
            Connection conn = connect()

            def dateOfBirth = DateManagement.getDateOfBirth(person.age)


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
                    return
                }
                else {
                    PreparedStatement insert = conn.prepareStatement(insertCandidate)

                    insert.setString(person.name)
                    insert.setString(person.surname)
                    insert.setDate(dateOfBirth)
                    insert.setString(person.email)
                    insert.setString(person.cpf)
                    insert.setString(person.state)
                    insert.setString(person.cep)
                    insert.setString(person.country)
                    insert.setString(person.description)
                    insert.setString(password)

                    insert. executeUpdate()
                    insert.close()

                    disconnect(conn)
                }
        }catch(Exception e){
            e.printStackTrace()
            System.err.println("Erro ao inserir o produto")
            System.exit(-42)
        }

    }

    void insertCandidateSkills(String[] skills, String email){
        try{
            Connection conn = connect()

            String skill
            while (skills.size()>0){

                skill = skills[0]
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
                    searchSkill.close()
                    disconnect(conn)
                    continue;
                }else {
                    String skillInsert = "INSERT INTO skills (skill_name) VALUES(?)"

                    PreparedStatement insertSkill = conn.prepareStatement(skillInsert)

                    insertSkill.setString(1,skill);

                    insertSkill.executeUpdate();
                    insertCandidateSkillRelations(email, skill)
                    insertSkill.close();
                    disconnect(conn)

                }

                skills = skills.drop(1)

            }
        }catch(Exception e){
            e.stackTrace()
            println("Connection error")
            System.exit(-42)
        }


    }

    void insertCandidateSkillRelations(String email, String skill){
        try{

            Connection conn = connect()

            String searchIDs = "SELECT ca.id, s.id FROM candidates AS ca, skills AS s WHERE ca.email = ?\n" +
                    "AND s.skill_name = ?; "

            PreparedStatement selectIDS = conn.prepareStatement(
                    searchIDs,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            selectIDS.setString(1,email)
            selectIDS.setString(2,skill)

            ResultSet res = selectIDS.executeQuery();

            int qtd = getResultSetLength(res)

            if(qtd > 0){
                int idCandidate = res.getInt(1)
                int idSkill = res.getInt(2)

                String insertSkillRelation = "INSERT INTO candidates_skills (id_skill,id_candidate) VALUES (?,?)"

                PreparedStatement insertRelation = conn.prepareStatement(insertSkillRelation)

                insertRelation.setInt(1,idSkill)
                insertRelation.setInt(2,idCandidate)

                insertRelation.executeUpdate()
                insertRelation.close()
                selectIDS.close()
                disconnect(conn)
                return

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

            String insertCompany = "INSERT INTO candidates (name, cnpj, email, company_description, state, cep, country, password)\n"+
                    "VALUES (?,?,?,?,?,?,?,?,?,?)"

            PreparedStatement companyFilter = conn.prepareStatement(
                    searchForCompany,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            companyFilter.setString(1, person.email)

            ResultSet res = companyFilter.executeQuery()

            int qtd = getResultSetLength(res)

            if(qtd > 0){
                println("A user with this e-mail already exists")
                return
            }
            else {
                PreparedStatement insert = conn.prepareStatement(insertCompany)

                insert.setString(person.name)
                insert.setString(person.cnpj)
                insert.setString(person.email)
                insert.setString(person.description)
                insert.setString(person.state)
                insert.setString(person.cep)
                insert.setString(person.country)

                insert.setString(password)

                insert. executeUpdate()
                insert.close()

                disconnect(conn)
            }
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
                int id = res.getInt(1)

                searchCreatedCompany.close()
                String vacancyCreationSQL = "INSERT INTO vacancies (title, id_company) VALUES(?,?)"
                PreparedStatement createVacancy = conn.prepareStatement(vacancyCreationSQL)

                createVacancy.setString(1, vacancy.name)
                createVacancy.setInt(2, id)

                createVacancy.executeUpdate();

                createVacancy.close()

                disconnect(conn)

                insertVacancySkills(vacancy, email, false)

                return

            }
            else {
                println("Account not found")
                return
            }

        }catch(Exception e){
            e.stackTrace();
            println("Error During Connect")
            System.exit(-42)
        }

    }

    void insertVacancySkills(Vacancy vacancy, String email){

        try{
            Connection conn = connect()
            Skills[] skills = vacancy.desiredSkills.getSkills()

            String skill
            while (skills.size()>0){

                skill = skills[0]
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
                    insertCompanySkillRelations(email, vacancy.name, skill)
                    searchSkill.close()
                    disconnect(conn)
                    continue;
                }else {
                    String skillInsert = "INSERT INTO skills (skill_name) VALUES(?)"

                    PreparedStatement insertSkill = conn.prepareStatement(skillInsert)

                    insertSkill.setString(1,skill);

                    insertSkill.executeUpdate();
                    insertCompanySkillRelations(email, vacancy.name, skill)
                    insertSkill.close();
                    disconnect(conn)

                }

                skills = skills.drop(1)

            }
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
                int idVacancy = res.getInt(1)
                int idSkill = res.getInt(2)

                String insertSkillRelation = "INSERT INTO candidates_skills (id_skill,id_vacancy) VALUES (?,?)"

                PreparedStatement insertRelation = conn.prepareStatement(insertSkillRelation)

                insertRelation.setInt(1,idSkill)
                insertRelation.setInt(2,idVacancy)

                insertRelation.executeUpdate()
                insertRelation.close()
                selectIDS.close()
                disconnect(conn)
                return

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

            Date dob = DateManagement.getDateOfBirth(person.age)

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
                update.setDate(3,dob);
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
