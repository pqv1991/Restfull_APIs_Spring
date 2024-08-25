package vn.hoidanit.jobhunter.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.user.ResUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.company.CompanyService;
import vn.hoidanit.jobhunter.service.role.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
    }


    @Override
    public User handleCreateUser(User user) {
        if(user.getCompany() != null){
            Optional<Company> company = companyService.fetchGetCompanyById(user.getCompany().getId());
            company.get();
            user.setCompany(company.get());
        }
        // check role
        if(user.getRole() != null){
            Optional<Role> roleOptional = roleService.fetchRoleById(user.getRole().getId());
            roleOptional.get();
            user.setRole(roleOptional.get());
        }
        return this.userRepository.save(user);
    }

    @Override
    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);

    }

    @Override
    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }


    @Override
    public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable) {
        Page<User> userPage =this.userRepository.findAll(specification,pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new  ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(userPage.getTotalPages());
        mt.setTotal(userPage.getTotalElements());
        rs.setMeta(mt);
        List<ResUserDTO> listUserDTO = userPage.getContent().stream().map(item-> ConvertToResUserDTO.convertToResUserDTO(item)).collect(Collectors.toList());
        rs.setResult(listUserDTO);
        return rs;
    }
    @Override
    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setName(reqUser.getName());
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setAge(reqUser.getAge());
            currentUser.setGender(reqUser.getGender());
            if (reqUser.getCompany() !=null) {
                Optional<Company> company = companyService.fetchGetCompanyById(reqUser.getCompany().getId());
                currentUser.setCompany(company.orElse(null));
            }
            if(reqUser.getRole() != null){
                Optional<Role> roleOptional = roleService.fetchRoleById(reqUser.getRole().getId());
                roleOptional.get();
                currentUser.setRole(roleOptional.get());
            }
            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = handleGetUserByUsername(email);
        if(currentUser !=null){
            currentUser.setRefreshToken(token);
            userRepository.save(currentUser);
        }
    }

    @Override
    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return userRepository.findByRefreshTokenAndEmail(refreshToken,email);
    }

    @Override
    public List<User> fetchUsersByCompany(Company company) {
        return userRepository.findByCompany(company);
    }

    @Override
    public void deleteAllUsers(List<User> users) {
        userRepository.deleteAll(users);
    }

}
