package com.quartz.model.person

class Skills implements ISkills{
    List<String> skills=[];


    @Override
    void addSkillToList(String skill) {
        this.skills << skill
        this.skills = this.skills.unique()
    }

    @Override
    void removeSkillToList(String skill) {
        this.skills.remove(skill)
    }

}
