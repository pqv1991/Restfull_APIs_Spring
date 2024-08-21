package vn.hoidanit.jobhunter.domain.dto.convertDTO;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.user.ResUserDTO;

public class ConvertToResUserDTO {
    public static ResCreateUserDTO convertToResCreateDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreateAt());

        return res;
    }

    public static ResUpdateUserDTO convertToResUpdateDTO(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public static ResUserDTO convertToResDTO(User user){
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }
}
