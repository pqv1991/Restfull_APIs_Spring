package vn.hoidanit.jobhunter.service.resume;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;

import java.util.Optional;

public interface ResumeService {

    Optional<Resume> fetchResumeById(long id);
    boolean checkResumeExistByUserAndJob(Resume resume);

    Resume handleCreateResume(Resume resume);
    Resume handleUpdateResume(Resume resume);

    ResultPaginationDTO fetchAllResumes(Specification<Resume> spec, Pageable pageable);

    void handleDeleteResume(long id);
}
