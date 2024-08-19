package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateCompanyDTO {
    private long id;
    private String name;
    private String description;
    private String address;
    private String logo;
    private Instant updateAt;
    private String  updateBy;
}
