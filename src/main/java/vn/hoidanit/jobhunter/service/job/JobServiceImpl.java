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
            if(company.isPresent()){
                job.setCompany(company.get());
            }
        }
        // create job
        Job currentJob =jobRepository.save(job);
        return currentJob;

    }

    @Override
    public Job handleUpdateJob(Job job, Job jobJnDb) {
        if(job.getSkills()!=null){
            List<Long> idSkills = job.getSkills().stream().map(s->s.getId()).toList();
            List<Skill>  SkillDbs = skillRepository.findByIdIn(idSkills);
            jobJnDb.setSkills(SkillDbs);
        }
        if(job.getCompany() != null){
            Optional<Company> company = companyService.fetchGetCompanyById(job.getCompany().getId());
            if(company.isPresent()){
                jobJnDb.setCompany(company.get());
            }

        }


        jobJnDb.setName(job.getName());
        jobJnDb.setLocation(job.getLocation());
        jobJnDb.setSalary(job.getSalary());
        jobJnDb.setQuantity(job.getQuantity());
        jobJnDb.setLevel(job.getLevel());
        jobJnDb.setDescription(job.getDescription());
        jobJnDb.setStartDate(job.getStartDate());
        jobJnDb.setEndDate(job.getEndDate());
        jobJnDb.setActive(job.isActive());

        Job currentUpdate = jobRepository.save(jobJnDb);
        return     currentUpdate;
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
