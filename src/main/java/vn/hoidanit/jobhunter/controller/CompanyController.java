package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.company.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("fetch create company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company){
        Company newCompany = companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
    @GetMapping("/companies")
    @ApiMessage("fetch get all company")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Company> specification, Pageable pageable){
        return ResponseEntity.ok().body(companyService.handleGetAllCompanies(specification,pageable));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("fetch delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id){
        companyService.handleDeleteCompany(id);
       return ResponseEntity.noContent().build();
    }
    @PutMapping("/companies")
    @ApiMessage("fetch update company")
    public ResponseEntity<ResUpdateCompanyDTO> updateCompany(@Valid @RequestBody Company company){
        Company companyUpdate = companyService.handleUpdateCompany(company);
        ResUpdateCompanyDTO resUpdateCompanyDTO = new ResUpdateCompanyDTO();
        resUpdateCompanyDTO.setId(companyUpdate.getId());
        resUpdateCompanyDTO.setName(companyUpdate.getName());
        resUpdateCompanyDTO.setDescription(companyUpdate.getDescription());
        resUpdateCompanyDTO.setAddress(companyUpdate.getAddress());
        resUpdateCompanyDTO.setLogo(companyUpdate.getLogo());
        resUpdateCompanyDTO.setUpdateAt(companyUpdate.getUpdatedAt());
        resUpdateCompanyDTO.setUpdateBy(companyUpdate.getUpdatedBy());
        return ResponseEntity.ok().body(resUpdateCompanyDTO);
    }
}
