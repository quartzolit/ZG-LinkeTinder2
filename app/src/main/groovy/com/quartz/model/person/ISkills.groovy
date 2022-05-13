package com.quartz.model.person

interface ISkills{

    void addSkillToList(String skill)

    void addSkillToList(EnumSkills skill)

    void removeSkillToList(String skill)

    void removeSkillToList(EnumSkills skill)

}