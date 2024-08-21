package vn.hoidanit.jobhunter.domain.dto.company;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant  updatedAt;
}
