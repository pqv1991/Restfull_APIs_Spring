package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class UserListener {
    @PrePersist
    private void beforeAnyCreate(User user){
        user.setCreateBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        user.setCreateAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(User user){
        user.setUpdateBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        user.setUpdateAt(Instant.now());
    }
}
