package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.company.ResCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.company.CompanyService;
import vn.hoidanit.jobhunter.service.user.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;
    private final UserService userService;

    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
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
        try {
            Optional<Company> company = companyService.fetchGetCompanyById(id);
            if(company.isEmpty()){
                throw new IdInvalidException("Công ty với id= "+id+" không tồn tại!");
            }else {
                Company com = company.get();
                List<User> userList = userService.fetchUsersByCompany(com);
                userService.deleteAllUsers(userList);
            }
            companyService.handleDeleteCompany(id);
        }catch (DataIntegrityViolationException ex){
            throw  new IdInvalidException("Không thể xóa công ty vì tồn tại các công việc liên quan");

        }

       return ResponseEntity.ok(null);
    }
    @PutMapping("/companies")
    @ApiMessage("fetch update company")
    public ResponseEntity<ResUpdateCompanyDTO> updateCompany(@Valid @RequestBody Company company) throws IdInvalidException{
        Optional<Company> companyData = companyService.fetchGetCompanyById(company.getId());
        if(companyData.isEmpty()){
            throw new IdInvalidException("Công ty với id= "+company.getId()+" không tồn tại!");
        }
        Company companyUpdate = companyService.handleUpdateCompany(company);
        return ResponseEntity.ok().body(ConvertToResCompanyDTO.convertToResUpdateDTO(companyUpdate));
    }
    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch company get id")
    public ResponseEntity<ResCompanyDTO> getCompanyById(@PathVariable("id") long id) throws IdInvalidException{
        Optional<Company> company = companyService.fetchGetCompanyById(id);
        if(company.isEmpty()){
            throw new IdInvalidException("Công ty với id= "+company+" không tồn tại!");
        }
        return ResponseEntity.ok().body(ConvertToResCompanyDTO.convertToResDTO(company.get()));
    }
}
