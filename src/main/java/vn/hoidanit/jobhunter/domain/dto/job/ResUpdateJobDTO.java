package vn.hoidanit.jobhunter.domain.dto.job;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.contant.LevelEnum;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;
    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private Instant startDate;
    private Instant endDate;
    private boolean isActive;
    private CompanyJob company;
    private List<String> skills;

    private Instant updatedAt;
    private String updatedBy;

    @Getter
    @Setter
    public static class CompanyJob {
        private long id;
        private String name;
    }


}
