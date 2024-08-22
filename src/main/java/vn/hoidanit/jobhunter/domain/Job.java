package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.AuditListener.JobListener;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.contant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@EntityListeners(JobListener.class)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    @Enumerated(EnumType.STRING)
    private LevelEnum level;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @ManyToOne()
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_skill",joinColumns = @JoinColumn(name = "job_id"),inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = {"jobs"})
    private List<Skill> skills;



}
