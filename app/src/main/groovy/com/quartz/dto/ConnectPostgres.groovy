package com.quartz.dto

import com.quartz.model.person.*

import javax.xml.transform.Result
import java.sql.*
import java.time.LocalDate

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
                " ca.country, ca.personal_description from candidates AS ca;"



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
                    LocalDate dateOfBirth = LocalDate.parse(res.getDate(4).toString(), 'yyyy-MM-dd')
                    String email = res.getString(5)
                    String cpf =  res.getString(6)
                    String state = res.getString(7)
                    String cep = res.getString(8)
                    String country = res.getString(9)
                    String description = res.getString(10)




                    String selectSkill = "SELECT string_agg(s.skill_name, ',') from candidates as ca, skills as s, candidates_skills as cs \n" +
                            "WHERE cs.id_candidate = ca.id AND cs.id_skill = s.id AND ca.id = ? GROUP BY\n" +
                            "ca.id ORDER BY ca.id;"

                    PreparedStatement getSkills = conn.prepareStatement(
                            selectSkill,
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY
                    )


                    getSkills.setInt(1,id);

                    ResultSet res2 = getSkills.executeQuery();

                    int qtd2 = getResultSetLength(res2)

                    String skillList = null
                    def skillsListArray = null

                    if (qtd2 >0) {
                        res2.next()
                        skillList = res2.getString(1)

                        skillsListArray = skillList.split(",")
                    }

                    Candidate ca = new Candidate(id: id, name: name, surname: surName, email: email,
                            cpf: cpf, dob: dateOfBirth, state: state, cep: cep,
                            description: description, country: country)

                    if(skillsListArray){
                        ca.skills = new Skills(skills: skillsListArray)
                    }

                    getSkills.close()

                    listOfCandidates << ca
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
        String select_all = "SELECT co.id,co.name, co.cnpj, co.email, co.company_description, co.state, co.cep, co.country FROM companies AS co"
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

                    String selectSkill = "SELECT string_agg(s.skill_name, ',') from companies as co, skills as s, companies_skills as cs\n" +
                            "WHERE cs.id_company = co.id AND cs.id_skill = s.id AND co.id = ? GROUP BY\n" +
                            "co.id ORDER BY co.id;"

                    PreparedStatement getSkills = conn.prepareStatement(
                            selectSkill,
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY
                    )

                    getSkills.setInt(1,id);

                    ResultSet res2 = getSkills.executeQuery();

                    int qtd2 = getResultSetLength(res2)

                    String skillList = null
                    def skillsListArray = null

                    if (qtd2 >0) {
                        res2.next()
                        skillList = res2.getString(1)

                        skillsListArray = skillList.split(",")
                    }

                    Company co = new Company(id: id, name: name, email: email
                            ,cnpj: cnpj, country: country, state: state, cep: cep
                            , description: description)

                    if(skillsListArray){
                        co.skills = new Skills(skills: skillsListArray)
                    }

                    getSkills.close()


                    listOfCompanies << co
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
    Candidate showCandidateByEmail(String candidateEmail) {
        Connection conn = null
        PreparedStatement getCandidate = null
        PreparedStatement getSkills = null
        try{
            conn = connect()

            String singleCandidate = "SELECT ca.* FROM candidates AS ca Where ca.email = ?;"

            getCandidate = conn.prepareStatement(
                    singleCandidate,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            getCandidate.setString(1,candidateEmail)

            ResultSet res = getCandidate.executeQuery()

            int qtd = getResultSetLength(res)

            if (qtd >0){
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


                String selectSkill = "SELECT string_agg(s.skill_name, ',') from candidates as ca, skills as s, candidates_skills as cs \n" +
                        "WHERE cs.id_candidate = ca.id AND cs.id_skill = s.id AND ca.id = ? GROUP BY\n" +
                        "ca.id ORDER BY ca.id;"

                getSkills = conn.prepareStatement(
                        selectSkill,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )

                getSkills.setInt(1,id);

                ResultSet res2 = getSkills.executeQuery();

                int qtd2 = getResultSetLength(res2)

                String skillList = null
                def skillsListArray = null

                if (qtd2 >0) {
                    res2.next()
                    skillList = res2.getString(1)

                    skillsListArray = skillList.split(",")
                }

                Candidate ca = new Candidate(id: id, name: name, surname: surName, email: email,
                        cpf: cpf, dob: dateOfBirth, state: state, cep: cep,
                        description: description, country: country, password: password)

                if(skillsListArray){
                    ca.skills = new Skills(skills: skillsListArray)
                }

                getCandidate.close()
                getSkills.close()
                return ca
            }else {
                println("Candidate not found")
                return null
            }

        }catch(Exception e){
            e.printStackTrace()
            System.err.println("Error on looking for candidate")
            System.exit(-42)
        }
        finally {
            disconnect(conn)
        }
    }

    @Override
    Company showCompanyByEmail(String companyEmail) {

        String selectCompany ="SELECT co.* FROM companies AS co Where co.email = ?"
        Connection conn = null
        Statement getCompany = null
        Statement getSkills = null
        try{
            conn = connect()
            getCompany = conn.prepareStatement(
                    selectCompany,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            )

            getCompany.setString(1,companyEmail)



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

                String selectSkill = "SELECT string_agg(s.skill_name, ',') from companies as co, skills as s, companies_skills as cs\n" +
                        "WHERE cs.id_company = co.id AND cs.id_skill = s.id AND co.id = ? GROUP BY\n" +
                        "co.id ORDER BY co.id;"

                getSkills = conn.prepareStatement(
                        selectSkill,
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY
                )

                getSkills.setInt(1,id);

                ResultSet res2 = getSkills.executeQuery();

                int qtd2 = getResultSetLength(res2)

                String skillList = null
                def skillsListArray = null

                if (qtd2 >0) {
                    res2.next()
                    skillList = res2.getString(1)

                    skillsListArray = skillList.split(",")
                }

                Company co = new Company(id: id, name: name, email: email
                        ,cnpj: cnpj, country: country, state: state, cep: cep
                        , description: description, password: password)

                if(skillsListArray){
                    co.skills = new Skills(skills: skillsListArray)
                }

                getCompany.close()
                getSkills.close()
                return co
            }
            else{
                println("There is no Company with specified e-mail")
                return null
            }


        }catch(Exception e){
            e.printStackTrace()
            System.err.println("Error on looking for Candidate")
            System.exit(-42)
        }
        finally {
            disconnect(conn)
        }
    }

    Skills showAllSkills(){
        Connection conn = null;

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
            System.err.println("Error on Looking for Candidate")
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


            if(qtd > 0){
                res.next()

                int idCandidate = res.getInt(1)
                int idSkill = res.getInt(2)

                String insertSkillRelation = "INSERT INTO candidates_skills (id_skill,id_candidate) VALUES (?,?)"

                PreparedStatement insertRelation = conn.prepareStatement(insertSkillRelation)

                insertRelation.setInt(1, idSkill)
                insertRelation.setInt(2, idCandidate)

                insertRelation.executeUpdate()

                insertRelation.close()

                println("Updated!")

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
                println("Updated!!!")
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

    void insertCompanySkills(Skills skills, String email){
        Connection conn = null

        try{
            String skill= null

            conn = connect()

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

                    insertCompanySkillRelations(email, skill)


                }else {
                    String skillInsert = "INSERT INTO skills (skill_name) VALUES(?)"

                    PreparedStatement insertSkill = conn.prepareStatement(skillInsert)

                    insertSkill.setString(1,skill);

                    insertSkill.executeUpdate();
                    insertCompanySkillRelations(email, skill)

                    insertSkill.close();
                }
                searchSkill.close()
            }

        }
        catch (Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)

        }
        finally{
            disconnect(conn)


        }
    }

    void insertCompanySkillRelations(String email, String skill){
        Connection conn = null
        try{

            conn = connect()

            String searchIDs = "SELECT co.id, s.id FROM companies AS co, skills AS s WHERE co.email = ? AND s.skill_name = ?; "

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
                res.next()

                int idCompany = res.getInt(1)
                int idSkill = res.getInt(2)

                String insertSkillRelation = "INSERT INTO companies_skills (id_company,id_skill) VALUES (?,?)"

                PreparedStatement insertRelation = conn.prepareStatement(insertSkillRelation)

                insertRelation.setInt(1, idCompany)
                insertRelation.setInt(2, idSkill)

                insertRelation.executeUpdate()

                println("UPDATED!!!")
                insertRelation.close()
                selectIDS.close()

            }else{
                println("ID not found. Please check if everything is correct")
            }
        }catch(Exception e){
            e.stackTrace();
            println("Connection not Found")
            System.exit(-42)
        }
        finally {
            disconnect(conn)
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


                if(qtd>0){
                    insertVacancySkillRelations(email, vacancyTitle, skill)

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

    void insertVacancySkillRelations(String email, String  title, String skill){
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
    void updateCandidate(Candidate person) {

        Connection conn = null

        try{

            String updateCandidate = "Update candidates \n"+
                    "set name = ?, surname = ?, birthdate = ?, \n"+
                    "email =?, cpf = ?, state =?, cep =?, country =?, \n"+
                    "personal_description =? WHERE id = ?"

            conn = connect()

            Candidate getCandidate = showCandidateByEmail(person.email);

            person.id = getCandidate.id;

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
            update.setInt(10,person.id);

            update.executeUpdate()
            update.close()



            updateSkillRelation(person)



        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }
        finally {
            disconnect(conn)
        }

    }

    void updateCompany(Company person) {
        Connection conn = null
        Company getCompany = showCompanyByEmail(person.email);
        try{

            String updateCompany = "Update companies \n"+
                    "set name = ?, cnpj = ?, email = ?, \n"+
                    "company_description =?, state = ?, cep =?, country =? \n"+
                    " WHERE id = ?"

            conn = connect()

            person.id = getCompany.id;

            PreparedStatement update = conn.prepareStatement(updateCompany)

            update.setString(1,person.name);
            update.setString(2,person.cnpj);
            update.setString(3,person.email);
            update.setString(4,person.description);
            update.setString(5,person.state);
            update.setString(6,person.cep);
            update.setString(7,person.country);
            update.setInt(8,person.id);

            update.executeUpdate()
            update.close()

            updateSkillRelation(person)

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }finally{
            disconnect(conn)
        }

    }

    void updateSkillRelation(Person person){

        Connection conn = null;

        try{

            conn = connect()

            if(person instanceof Candidate){

                String deleteSkillsFromCandidate= 'DELETE FROM candidates_skills WHERE id_candidate=?'

                PreparedStatement delete = conn.prepareStatement(deleteSkillsFromCandidate);

                delete.setInt(1,person.id)

                delete.executeUpdate();

                delete.close()


                insertCandidateSkills(person.skills, person.email)
            }
            else {
                String deleteSkillsFromCompany= 'DELETE FROM companies_skills WHERE id_company=?;'



                PreparedStatement delete = conn.prepareStatement(deleteSkillsFromCompany);



                delete.setInt(1,person.id)



                delete.executeUpdate();

                insertCompanySkills(person.skills, person.email)
            }

        }catch(Exception e){
            e.stackTrace()
            println("Connection not Found")
            System.exit(-42)
        }finally {
            disconnect(conn)
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
