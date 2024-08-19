package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.company.CompanyService;

import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company){
        Company newCompany = companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@RequestParam("current") Optional<String> currentOptional,
                                                               @RequestParam("pageSize") Optional<String> pageSize){
        String sCurrent = currentOptional.orElse("");
        String sPageSize = pageSize.orElse("");
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent)-1, Integer.parseInt(sPageSize));
        return ResponseEntity.ok().body(companyService.handleGetAllCompanies(pageable));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id){
        companyService.handleDeleteCompany(id);
       return ResponseEntity.noContent().build();
    }
    @PutMapping("/companies")
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
