package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.resume.ResumeService;
import vn.hoidanit.jobhunter.service.user.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterBuilder filterBuilder;
    private final FilterParser filterParser;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService, FilterBuilder filterBuilder, FilterParser filterParser, FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterParser = filterParser;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    @ApiMessage("FETCH CREATE RESUME")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        if(!resumeService.checkResumeExistByUserAndJob(resume)){
            throw  new IdInvalidException("User id / Job id khong ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertToResResumeDTO.convertToCreateResumeDTO(resumeService.handleCreateResume(resume)));
    }
    @PutMapping("/resumes")
    @ApiMessage("FETCH UPDATE RESUME")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Optional<Resume> optionalResume = resumeService.fetchResumeById(resume.getId());
        if(optionalResume.isEmpty()){
            throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(ConvertToResResumeDTO.convertToUpdateResumeDTO(resumeService.handleUpdateResume(resume)));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("FETCH DELETE RESUME")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> optionalResume = resumeService.fetchResumeById(id);
        if(optionalResume.isEmpty()){
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        resumeService.handleDeleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("FETCH GET RESUME BY ID")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> optionalResume = resumeService.fetchResumeById(id);
        if(optionalResume.isEmpty()){
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(ConvertToResResumeDTO.convertToResumeDTO(optionalResume.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("FETCH ALL RESUME")
    public ResponseEntity<ResultPaginationDTO> fetchAllResume(@Filter Specification<Resume> spec,
            Pageable pageable) {
//                List<Long> arrJobIds = null;
//        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
//                ? SecurityUtil.getCurrentUserLogin().get()
//                : "";
//        User currentUser = userService.handleGetUserByUsername(email);
//        if( currentUser != null){
//            Company userCompany = currentUser.getCompany();
//            if (userCompany !=null) {
//                List<Job> companyJobs = userCompany.getJobs();
//                if(companyJobs != null && companyJobs.size() > 0){
//                    arrJobIds = companyJobs.stream().map(x->x.getId()).collect(Collectors.toList());
//                }
//            }
//        }
//        Specification<Resume> jobJnSpec = filterSpecificationConverter.convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());
//        Specification<Resume> finalSpec = jobJnSpec.and(spec);

        return ResponseEntity.ok().body(this.resumeService.fetchAllResumes(spec, pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("FETCH RESUME BY USER")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }


}
