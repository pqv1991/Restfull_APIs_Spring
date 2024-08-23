package vn.hoidanit.jobhunter.domain.dto.resume;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.util.contant.ResumeStateEnum;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeDTO {
    private long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private String companyName;
    private JobResume job;
    private UserResume user;





    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class JobResume{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class UserResume{
        private long id;
        private String name;
    }
}
