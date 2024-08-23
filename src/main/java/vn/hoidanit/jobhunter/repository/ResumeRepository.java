package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.hoidanit.jobhunter.domain.Resume;

@ResponseBody
public interface ResumeRepository extends JpaRepository<Resume,Long>, JpaSpecificationExecutor<Resume> {
}
