package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.AuditListener.SubscriberListener;

import java.time.Instant;
import java.util.List;
@Entity
@Table(name = "subscribers")
@Getter
@Setter
@EntityListeners(SubscriberListener.class)
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriber_skill",joinColumns = @JoinColumn(name = "subscriber_id"),inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = "subscribers")
    private List<Skill> skills;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
