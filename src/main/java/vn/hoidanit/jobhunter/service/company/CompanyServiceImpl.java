package vn.hoidanit.jobhunter.service.company;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    @Override
    public Company handleCreateCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public List<Company> handleGetAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void handleDeleteCompany(long id) {
        companyRepository.deleteById(id);

    }



    @Override
    public Company handleUpdateCompany(Company company) {
        Optional<Company> com = companyRepository.findById(company.getId());
        if (com.isPresent()) {
            Company companyUpdate = com.get();
            companyUpdate.setAddress(company.getAddress());
            companyUpdate.setDescription(company.getDescription());
            companyUpdate.setLogo(company.getLogo());
            companyUpdate.setName(company.getName());
            return companyRepository.save(companyUpdate);
        }
        return null;
    }
}
