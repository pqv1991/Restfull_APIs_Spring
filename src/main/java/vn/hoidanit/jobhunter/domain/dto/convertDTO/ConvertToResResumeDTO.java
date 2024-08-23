package vn.hoidanit.jobhunter.domain.dto.convertDTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.dto.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.util.contant.ResumeStateEnum;

import java.time.Instant;

public class ConvertToResResumeDTO {

    public static ResCreateResumeDTO convertToCreateResumeDTO(Resume resume){
        return new ResCreateResumeDTO(resume.getId(),resume.getCreatedAt(),resume.getCreatedBy());
    }

    public static ResUpdateResumeDTO convertToUpdateResumeDTO(Resume resume){
        return new ResUpdateResumeDTO(resume.getUpdatedAt(),resume.getUpdatedBy());
    }
    public static ResResumeDTO convertToResumeDTO(Resume resume){
        ResResumeDTO res = new ResResumeDTO();
        ResResumeDTO.JobResume jobResume = new ResResumeDTO.JobResume();
        ResResumeDTO.UserResume userResume = new ResResumeDTO.UserResume();
            res.setId(resume.getId());
            res.setEmail(resume.getEmail());
            res.setUrl(resume.getUrl());
            res.setStatus(resume.getStatus());
            res.setCreatedAt(resume.getCreatedAt());
            res.setCreatedBy(resume.getCreatedBy());
            res.setUpdatedAt(resume.getUpdatedAt());
            res.setUpdatedBy(resume.getUpdatedBy());
            if(resume.getJob() != null){
                res.setCompanyName(resume.getJob().getCompany().getName());
            }
        if(resume.getJob() !=null) {
            jobResume.setId(resume.getJob().getId());
            jobResume.setName(resume.getJob().getName());
        }
        res.setJob(jobResume);
        if(resume.getUser() !=null) {
            userResume.setId(resume.getUser().getId());
            userResume.setName(resume.getUser().getName());
        }
        res.setUser(userResume);
        return res;
    }
}
