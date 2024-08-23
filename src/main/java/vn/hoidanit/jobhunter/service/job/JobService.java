package vn.hoidanit.jobhunter.service.job;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.dto.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;

import java.util.Optional;

public interface JobService {

    Optional<Job> fetchJobById(long id);

    Job handleCreateJob(Job job);

    Job handleUpdateJob(Job job, Job jobInDb);

    void handleDeleteJob(long id);

    ResultPaginationDTO fetchAllJobs(Specification<Job> specification, Pageable pageable);


}
