package com.quartz.dto

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period

class DateManagement {

    public static int getAge(Date dateOfBirth){

        def dob = LocalDate.parse(dateOfBirth.toString(), "yyyy-MM-dd").getYear()

        def currYear = LocalDate.now().getYear()

        int age = currYear - dob


        return age
    }

    public static LocalDate getDateOfBirth(int age){

        def dateofBirth = LocalDate.now().minusYears(age)

        return dateofBirth
    }
}
