package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.user.ResUserDTO;
import vn.hoidanit.jobhunter.service.user.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Fetch create user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) throws IdInvalidException {
        if(userService.isEmailExist(postManUser.getEmail())){
            throw new IdInvalidException("Email "+ postManUser.getEmail()+" đã tồn tại, vui lòng sử dụng email khác!");
        }
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User ericUser = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertToResUserDTO.convertToResCreateDTO(ericUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Fetch delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException{
        User currentUser = userService.fetchUserById(id);
        if(currentUser == null){
            throw  new IdInvalidException("User vơi id = "+id+" không tồn tại");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);

    }

    // fetch user by id
    @GetMapping("/users/{id}")
    @ApiMessage("Fetch get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws  IdInvalidException{
        User fetchUser = this.userService.fetchUserById(id);
        if(fetchUser == null){
            throw  new IdInvalidException("User vơi id = "+id+" không tồn tại");
        }
        // return ResponseEntity.ok(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(ConvertToResUserDTO.convertToResDTO(fetchUser));
    }

    // fetch all users
    @GetMapping("/users")
    @ApiMessage("Fetch get all user")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> specification, Pageable pageable){
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(specification,pageable));
        }

        @PutMapping("/users")
        @ApiMessage("Fetch update user")
        public ResponseEntity<ResUpdateUserDTO> updateUser (@RequestBody User user) throws IdInvalidException{
            User updateUser = userService.fetchUserById(user.getId());
            if(updateUser == null){
                throw  new IdInvalidException("User vơi id = "+user.getId()+" không tồn tại");
            }
            User ericUser = this.userService.handleUpdateUser(user);
            return ResponseEntity.ok(ConvertToResUserDTO.convertToResUpdateDTO(ericUser));
        }


}
