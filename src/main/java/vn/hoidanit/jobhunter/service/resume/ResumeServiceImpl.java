package vn.hoidanit.jobhunter.service.resume;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.user.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FilterBuilder filterBuilder;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeServiceImpl(ResumeRepository resumeRepository, UserService userService, JobRepository jobRepository, UserRepository userRepository, FilterBuilder filterBuilder, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.filterBuilder = filterBuilder;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
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
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = userService.handleGetUserByUsername(email);
        if( currentUser != null){
            Company userCompany = currentUser.getCompany();
            if (userCompany !=null) {
                List<Job> companyJobs = userCompany.getJobs();
                if(companyJobs != null && companyJobs.size() > 0){
                    arrJobIds = companyJobs.stream().map(x->x.getId()).collect(Collectors.toList());
                }
            }
        }
        Specification<Resume> jobJnSpec = filterSpecificationConverter.convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());
        Specification<Resume> finalSpec = jobJnSpec.and(spec);
        Page<Resume> resumePage = resumeRepository.findAll(finalSpec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());
        List<ResResumeDTO> resumeDTOList = resumePage.getContent().stream().map(item-> ConvertToResResumeDTO.convertToResumeDTO(item)).collect(Collectors.toList());
        rs.setMeta(mt);
        rs.setResult(resumeDTOList);
        return rs;
    }

    @Override
    public void handleDeleteResume(long id) {
        resumeRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()? SecurityUtil.getCurrentUserLogin().get():"";
        FilterNode filterNode = filterParser.parse("email='"+email+"'");
        FilterSpecification<Resume> specificationConverter = filterSpecificationConverter.convert(filterNode);
        Page<Resume> resumePage = resumeRepository.findAll(specificationConverter,pageable);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(resumePage.getTotalPages());
        mt.setTotal(resumePage.getTotalElements());
        res.setMeta(mt);
        List<ResResumeDTO> resumeDTOList = resumePage.getContent().stream().map(item-> ConvertToResResumeDTO.convertToResumeDTO(item)).collect(Collectors.toList());
        res.setResult(resumeDTOList);

        return res;
    }
}
