package com.quartz

import com.quartz.model.person.EnumSkills
import com.quartz.model.person.Person

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions


class PersonTestGroovy {
    @Test
    void testAddSkills() {
        Person person = new Person();

        EnumSkills expectedResult = EnumSkills.JAVA;

        person.skills.addSkillToList(EnumSkills.JAVA );

        Assertions.assertTrue(person.skills.getSkills().contains(expectedResult))

        println("Add skill test executed")
    }

    @Test
    void testRemoveSkills() {
        Person person = new Person();

        person.skills.removeSkillToList(EnumSkills.HIBERNATE);
        person.skills.addSkillToList(EnumSkills.HTML);

        int expectSize = 1;
        EnumSkills expectedResult = EnumSkills.HTML;

        person.skills.removeSkillToList(EnumSkills.HIBERNATE);

        Assertions.assertTrue(person.skills.getSkills().contains(expectedResult) && person.skills.getSkills().size() == expectSize );
        println("Remove Skill Test Executed")
    }

    @Test
    void testSkillIsUnique(){

        Person person = new Person();

        person.skills.addSkillToList(EnumSkills.HIBERNATE);
        person.skills.addSkillToList(EnumSkills.HTML);
        person.skills.addSkillToList(EnumSkills.HTML);

        int expectedResult = 2

        Assertions.assertEquals(expectedResult, person.skills.getSkills().size())

        println("Unique Skill test executed")

    }
}
