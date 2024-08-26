package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class SubscriberListener {
    @PrePersist
    private void beforeAnyCreate(Subscriber subscriber){
        subscriber.setCreatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        subscriber.setCreatedAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(Subscriber subscriber){
        subscriber.setUpdatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        subscriber.setUpdatedAt(Instant.now());
    }
}
