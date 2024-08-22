package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class JobListener {
    @PrePersist
    private void beforeAnyCreate(Job job){
        job.setCreatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        job.setCreatedAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(Job job){
        job.setUpdatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        job.setUpdatedAt(Instant.now());
    }
}
