package vn.hoidanit.jobhunter.domain.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Getter
@Setter
public class ResEmailJobDTO {
    private String name;
    private double salary;

    private CompanyEmail company;
    private List<SkillEmail> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    public  static class CompanyEmail{
        private String name;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static  class SkillEmail{
        private String name;
    }

}
