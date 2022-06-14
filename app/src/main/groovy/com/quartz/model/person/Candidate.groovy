package com.quartz.model.person

import java.time.LocalDate

@groovy.transform.ToString
@groovy.transform.InheritConstructors
class Candidate extends Person{
    String name;
    String surname;
    LocalDate dob;
    String cpf;




}
