package com.quartz.dto

import java.text.SimpleDateFormat
import java.time.Period

class DateManagement {

    public static int getAge (String dateOfBirth){
        def currDate = new Date()

        def dob = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);

        Period period = Period.between(dob, currDate)

        int age = period.getYears()

        return age
    }

    public static Date getDateOfBirth(int age){
        def currDate = new Date()

        def dateofBirth = currDate.minus(age*365+(Math.floor(age/4)));

        return dateofBirth
    }
}
