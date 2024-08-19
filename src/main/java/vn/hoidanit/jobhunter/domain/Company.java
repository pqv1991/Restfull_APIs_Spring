package vn.hoidanit.jobhunter.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.AuditListener.AuditTrailListener;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;

@Entity
@Table(name = "companies")
@Getter
@Setter
@EntityListeners(AuditTrailListener.class)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Company name is required!")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
//    @PrePersist
//    public void handleBeforeCreate(){
//        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
//        this.createdAt = Instant.now();
//    }
//    @PreUpdate
//    public void handleBeforeUpdate(){
//        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
//        this.updatedAt = Instant.now();
//    }


}
