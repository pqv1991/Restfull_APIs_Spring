package vn.hoidanit.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;
}
