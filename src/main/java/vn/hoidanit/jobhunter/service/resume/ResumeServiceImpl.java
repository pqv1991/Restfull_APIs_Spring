package vn.hoidanit.jobhunter.service.resume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Resume> fetchResumeById(long id) {

        return resumeRepository.findById(id);
    }

    @Override
    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if(resume.getUser() == null) return  false;
        Optional<User> optionalUser = userRepository.findById(resume.getUser().getId());
        if(optionalUser.isEmpty()) return false;
        if(resume.getJob() == null) return  false;
        Optional<Job> optionalJob = jobRepository.findById(resume.getJob().getId());
        if(optionalJob.isEmpty()) return false;
        return true;

    }

    @Override
    public Resume handleCreateResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    @Override
    public Resume handleUpdateResume(Resume resume) {
        Optional<Resume>  resumeOptional = fetchResumeById(resume.getId());
        if(resumeOptional.isPresent()){
            Resume resumeUpdate = resumeOptional.get();
            resumeUpdate.setStatus(resume.getStatus());
            resumeRepository.save(resumeUpdate);
            return resumeUpdate;
        }
        return null;
    }

    @Override
    public ResultPaginationDTO fetchAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> resumePage = resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());
        List<ResResumeDTO> resumeDTOList = resumePage.getContent().stream().map(item-> new ResResumeDTO(
                item.getId(),
                item.getEmail(),
                item.getUrl(),
                item.getStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getCreatedBy(),
                item.getUpdatedBy(),
                item.getJob().getCompany().getName(),
                new ResResumeDTO.JobResume(
                        item.getJob() !=null? item.getJob().getId():0,
                        item.getJob() !=null? item.getJob().getName():""
                ),
                new ResResumeDTO.UserResume(
                        item.getUser() != null? item.getUser().getId():0,
                        item.getUser() != null? item.getUser().getName():""
                )
               )).collect(Collectors.toList());
        rs.setMeta(mt);
        rs.setResult(resumeDTOList);
        return rs;
    }

    @Override
    public void handleDeleteResume(long id) {
        resumeRepository.deleteById(id);
    }
}
