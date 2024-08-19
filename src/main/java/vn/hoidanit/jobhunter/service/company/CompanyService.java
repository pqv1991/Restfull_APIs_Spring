package vn.hoidanit.jobhunter.service.company;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;

public interface CompanyService {
    public Company handleCreateCompany(Company company);
    public ResultPaginationDTO fetchGetAllCompanies(Specification<Company> specification, Pageable pageable);
    public Company fetchGetCompanyById(long id);
    public void handleDeleteCompany(long id);
    public Company handleUpdateCompany(Company company);
    public Boolean isNameExist(String name);
}
