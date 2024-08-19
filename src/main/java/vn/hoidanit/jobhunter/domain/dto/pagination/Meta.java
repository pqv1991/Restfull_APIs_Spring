package vn.hoidanit.jobhunter.domain.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private long total;
}
