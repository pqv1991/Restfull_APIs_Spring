package vn.hoidanit.jobhunter.service.job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.company.CompanyService;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService{
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyService companyService;
    public JobServiceImpl(JobRepository jobRepository, SkillRepository skillRepository, CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyService = companyService;
    }


    @Override
    public Optional<Job> fetchJobById(long id) {

        return jobRepository.findById(id);
    }

    @Override
    public Job handleCreateJob(Job job) {
        //check skill
        if(job.getSkills()!=null){
            List<Long> idSkills = job.getSkills().stream().map(s->s.getId()).toList();
            List<Skill>  SkillDbs = skillRepository.findByIdIn(idSkills);
            job.setSkills(SkillDbs);
        }
        if(job.getCompany() != null){
            Optional<Company> company = companyService.fetchGetCompanyById(job.getCompany().getId());
            job.setCompany(company.orElse(null));
        }
        // create job
        Job currentJob =jobRepository.save(job);
        return currentJob;

    }

    @Override
    public Job handleUpdateJob(Job job) {
        if(job.getSkills()!=null){
            List<Long> idSkills = job.getSkills().stream().map(s->s.getId()).toList();
            List<Skill>  SkillDbs = skillRepository.findByIdIn(idSkills);
            job.setSkills(SkillDbs);
        }

        Optional<Job> jobOptional = fetchJobById(job.getId());
        Job currentJob = jobOptional.get();
        currentJob.setName(job.getName());
        currentJob.setLocation(job.getLocation());
        currentJob.setSalary(job.getSalary());
        currentJob.setQuantity(job.getQuantity());
        currentJob.setLevel(job.getLevel());
        currentJob.setDescription(job.getDescription());
        currentJob.setStartDate(job.getStartDate());
        currentJob.setEndDate(job.getEndDate());
        currentJob.setActive(job.isActive());
        currentJob.setCompany(job.getCompany());
        currentJob.setSkills(job.getSkills());
        if(job.getCompany() != null){
            Optional<Company> company = companyService.fetchGetCompanyById(job.getCompany().getId());
            currentJob.setCompany(company.orElse(null));
        }
        return jobRepository.save(currentJob);
    }

    @Override
    public void handleDeleteJob(long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO fetchAllJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobPage = jobRepository.findAll(specification,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(jobPage.getTotalPages());
        mt.setTotal(jobPage.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(jobPage.getContent());

        return rs;

    }


}
