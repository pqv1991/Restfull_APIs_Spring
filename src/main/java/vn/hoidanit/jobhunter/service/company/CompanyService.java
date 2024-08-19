package vn.hoidanit.jobhunter.service.company;

import org.springframework.data.domain.Pageable;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;

import java.util.List;

public interface CompanyService {
    public Company handleCreateCompany(Company company);
    public ResultPaginationDTO handleGetAllCompanies(Pageable pageable);
    public void handleDeleteCompany(long id);
    public Company handleUpdateCompany(Company company);
}
