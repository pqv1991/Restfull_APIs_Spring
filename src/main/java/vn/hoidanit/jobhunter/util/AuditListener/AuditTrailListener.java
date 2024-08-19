package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class AuditTrailListener {
    @PrePersist
    private void beforeAnyCreate(Company company){
        company.setCreatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        company.setCreatedAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(Company company){
        company.setUpdatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        company.setUpdatedAt(Instant.now());
    }
}
