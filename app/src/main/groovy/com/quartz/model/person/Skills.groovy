package com.quartz.model.person

class Skills implements ISkills{
    List<String> skills=[];

    @Override
    void addSkillToList(EnumSkills skill) {

        this.skills << skill
        this.skills = this.skills.unique()
    }

    void addSkillToList(String skill) {

        this.skills << skill
        this.skills = this.skills.unique()
    }

    @Override
    void removeSkillToList(String skill) {
        this.skills.remove(skill)
    }

    @Override
    void removeSkillToList(EnumSkills skill) {
        this.skills.remove(skill)
    }
}
