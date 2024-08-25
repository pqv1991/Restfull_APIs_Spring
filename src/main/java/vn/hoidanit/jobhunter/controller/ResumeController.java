package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.resume.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
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

        return ResponseEntity.ok().body(this.resumeService.fetchAllResumes(spec, pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("FETCH RESUME BY USER")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }


}
