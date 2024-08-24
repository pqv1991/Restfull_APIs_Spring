package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class PermissionListener {
    @PrePersist
    private void beforeAnyCreate(Permission permission){
        permission.setCreatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        permission.setCreatedAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(Permission permission){
        permission.setUpdatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        permission.setUpdatedAt(Instant.now());
    }
}
