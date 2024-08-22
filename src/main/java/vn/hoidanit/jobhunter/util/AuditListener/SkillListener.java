package vn.hoidanit.jobhunter.util.AuditListener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

public class SkillListener {
    @PrePersist
    private void beforeAnyCreate(Skill skill){
        skill.setCreatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        skill.setCreatedAt(Instant.now());

    }
    @PreUpdate
    private void beforeAnyUpdate(Skill skill){
        skill.setUpdatedBy(SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"");
        skill.setUpdatedAt(Instant.now());
    }
}
