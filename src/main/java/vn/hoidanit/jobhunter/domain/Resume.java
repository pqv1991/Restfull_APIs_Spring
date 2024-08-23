package vn.hoidanit.jobhunter.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.AuditListener.ResumeListener;
import vn.hoidanit.jobhunter.util.contant.ResumeStateEnum;

import java.time.Instant;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@EntityListeners(ResumeListener.class)
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Email khong duoc de trong")
    private String email;
    @NotBlank(message = "url khong duoc de trong")
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
