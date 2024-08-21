package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.company.ResCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.company.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("fetch create company")
    public ResponseEntity<ResCreateCompanyDTO> createCompany(@Valid @RequestBody Company company) throws IdInvalidException {
        if(companyService.isNameExist(company.getName())){
            throw  new IdInvalidException("Tên công ty "+company.getName()+" đã tồn tại, vui lòng chọn tên khác!");
        }
        Company newCompany = companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertToResCompanyDTO.convertToResCreateDTO(newCompany));
    }
    @GetMapping("/companies")
    @ApiMessage("fetch get all company")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@Filter Specification<Company> specification, Pageable pageable){
        return ResponseEntity.ok().body(companyService.fetchGetAllCompanies(specification,pageable));
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("fetch delete company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException{
        if(companyService.fetchGetCompanyById(id) == null){
            throw new IdInvalidException("Công ty với id= "+id+" không tồn tại!");
        }
        companyService.handleDeleteCompany(id);
       return ResponseEntity.ok(null);
    }
    @PutMapping("/companies")
    @ApiMessage("fetch update company")
    public ResponseEntity<ResUpdateCompanyDTO> updateCompany(@Valid @RequestBody Company company) throws IdInvalidException{
        Company companyData = companyService.fetchGetCompanyById(company.getId());
        if(companyData == null){
            throw new IdInvalidException("Công ty với id= "+company.getId()+" không tồn tại!");
        }
        Company companyUpdate = companyService.handleUpdateCompany(company);
        return ResponseEntity.ok().body(ConvertToResCompanyDTO.convertToResUpdateDTO(companyUpdate));
    }
    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch company get id")
    public ResponseEntity<ResCompanyDTO> getCompanyById(@RequestParam("id") long id) throws IdInvalidException{
        Company company = companyService.fetchGetCompanyById(id);
        if(company == null){
            throw new IdInvalidException("Công ty với id= "+company.getId()+" không tồn tại!");
        }
        return ResponseEntity.ok().body(ConvertToResCompanyDTO.convertToResDTO(company));
    }
}
