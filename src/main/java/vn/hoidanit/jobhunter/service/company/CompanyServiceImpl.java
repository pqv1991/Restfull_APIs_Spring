package vn.hoidanit.jobhunter.service.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.company.ResCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResultPaginationDTO fetchGetAllCompanies(Specification<Company> specification,Pageable pageable) {
        Page<Company> userPage =this.companyRepository.findAll(specification,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new  ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(userPage.getTotalPages());
        mt.setTotal(userPage.getTotalElements());
        rs.setMeta(mt);
        List<ResCompanyDTO> listCompanyDTO = userPage.getContent().stream().map(item-> new ResCompanyDTO(
                        item.getId(),
                        item.getName(),
                        item.getLogo(),
                        item.getDescription(),
                        item.getAddress(),
                        item.getCreatedAt(),
                        item.getUpdatedAt())).collect(Collectors.toList());

        rs.setResult(listCompanyDTO);
        return rs;
    }

    @Override
    public Optional<Company> fetchGetCompanyById(long id) {
        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent()){
            return company;
        }
        return null;
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

    @Override
    public Boolean isNameExist(String name) {
        return companyRepository.existsByName(name);
    }
}
