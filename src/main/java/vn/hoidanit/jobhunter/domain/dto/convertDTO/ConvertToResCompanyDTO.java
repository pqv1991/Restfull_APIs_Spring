package vn.hoidanit.jobhunter.domain.dto.convertDTO;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.company.ResCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.dto.company.ResUpdateCompanyDTO;

public class ConvertToResCompanyDTO {
    public static ResCreateCompanyDTO convertToResCreateDTO(Company company){
        ResCreateCompanyDTO res = new ResCreateCompanyDTO();
        res.setId(company.getId());
        res.setName(company.getName());
        res.setAddress(company.getAddress());
        res.setDescription(company.getDescription());
        res.setLogo(company.getLogo());
        res.setCreatedAt(company.getCreatedAt());
        return res;
    }

    public static ResUpdateCompanyDTO convertToResUpdateDTO(Company company){
        ResUpdateCompanyDTO res = new ResUpdateCompanyDTO();
        res.setId(company.getId());
        res.setName(company.getName());
        res.setDescription(company.getDescription());
        res.setAddress(company.getAddress());
        res.setLogo(company.getLogo());
        res.setUpdatedAt(company.getUpdatedAt());
        return res;
    }

    public static ResCompanyDTO convertToResCompanyDTO(Company company){
        ResCompanyDTO res = new ResCompanyDTO();
        res.setId(company.getId());
        res.setName(company.getName());
        res.setAddress(company.getAddress());
        res.setDescription(company.getDescription());
        res.setLogo(company.getLogo());
        res.setCreatedAt(company.getCreatedAt());
        res.setUpdatedAt(company.getUpdatedAt());
        return res;
    }


}
