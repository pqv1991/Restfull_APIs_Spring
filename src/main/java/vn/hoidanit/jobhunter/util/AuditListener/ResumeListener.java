package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class ResumeListener {
    @PrePersist
    private void beforeAnyCreate(Resume resume){
        resume.setCreatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        resume.setCreatedAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(Resume resume){
        resume.setUpdatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        resume.setUpdatedAt(Instant.now());
    }
}
