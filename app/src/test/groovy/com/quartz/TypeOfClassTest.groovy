package com.quartz

import com.quartz.model.person.Candidate
import com.quartz.model.person.Person
import org.apache.tools.ant.types.resources.selectors.InstanceOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TypeOfClassTest {

    @Test
    void children(){
        Candidate candidate = new Candidate();

        String candidateType = 'Candidate';

        String result = typeOfClass(candidate)
        println(result)

        Assertions.assertTrue(result==candidateType)

    }


    String typeOfClass(Person person){
        if (person instanceof Candidate){
            return 'Candidate'
        }else {
            return 'Company'
        }
    }
}
