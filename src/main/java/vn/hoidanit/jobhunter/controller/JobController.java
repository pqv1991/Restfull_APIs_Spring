package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResJobDTO;
import vn.hoidanit.jobhunter.domain.dto.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.dto.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.job.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/jobs")
    @ApiMessage("CREATE A JOB")
    public ResponseEntity<ResCreateJobDTO> createJob (@Valid @RequestBody Job job){
        Job createJob =  jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertToResJobDTO.convertToCreateJobDto(createJob));
    }

    @PutMapping("/jobs")
    @ApiMessage("UPDATE A JOB")
    public ResponseEntity<ResUpdateJobDTO> updateJob (@Valid @RequestBody Job job) throws IdInvalidException {
        Optional<Job> jobDB = jobService.fetchJobById(job.getId());
        if(jobDB.isEmpty()){
            throw new IdInvalidException("Job not found");
        }
        Job updateJob =  jobService.handleUpdateJob(job,jobDB.get());
        return ResponseEntity.ok().body(ConvertToResJobDTO.convertToUpdateJobDto(updateJob));
    }
    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (currentJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (currentJob.isEmpty()) {
            throw new IdInvalidException("Job not found");
        }

        return ResponseEntity.ok().body(currentJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get job with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.jobService.fetchAllJobs(spec, pageable));
    }





}
