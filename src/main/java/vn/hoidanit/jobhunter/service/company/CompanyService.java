package vn.hoidanit.jobhunter.service.company;

import vn.hoidanit.jobhunter.domain.Company;

import java.util.List;

public interface CompanyService {
    public Company handleCreateCompany(Company company);
    public List<Company> handleGetAllCompanies();
    public void handleDeleteCompany(long id);
    public Company handleUpdateCompany(Company company);
}
