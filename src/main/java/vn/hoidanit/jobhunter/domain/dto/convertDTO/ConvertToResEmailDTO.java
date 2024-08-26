package vn.hoidanit.jobhunter.domain.dto.convertDTO;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.email.ResEmailJobDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ConvertToResEmailDTO {
    public static ResEmailJobDTO convertResEmailJobDTO(Job job){
        ResEmailJobDTO res = new ResEmailJobDTO();

        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJobDTO.CompanyEmail(job.getCompany().getName()));
        List<Skill>skills = job.getSkills();
        List<ResEmailJobDTO.SkillEmail> s = skills.stream().map(skill -> new ResEmailJobDTO.SkillEmail(skill.getName())).collect(Collectors.toList());
        res.setSkills(s);
        return res;


    }
}
