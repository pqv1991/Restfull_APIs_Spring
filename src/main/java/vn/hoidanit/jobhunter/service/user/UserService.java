package vn.hoidanit.jobhunter.service.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;


public interface UserService {
    public User handleCreateUser(User user);

    public void handleDeleteUser(long id);

    public User fetchUserById(long id);

    public ResultPaginationDTO fetchAllUser(Specification<User> specification, Pageable pageable);

    public User handleUpdateUser(User reqUser);

    public User handleGetUserByUsername(String username);

    public boolean isEmailExist(String email);
    void updateUserToken(String token, String email);
}
