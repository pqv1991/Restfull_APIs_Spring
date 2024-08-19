package vn.hoidanit.jobhunter.service.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
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
    public ResultPaginationDTO handleGetAllCompanies(Specification<Company> specification,Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(specification,pageable);
        return new ResultPaginationDTO(
                new Meta(pageable.getPageNumber()+1,pageable.getPageSize(),companyPage.getTotalPages(),companyPage.getTotalElements())
                ,companyPage.getContent()
        ) ;
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
