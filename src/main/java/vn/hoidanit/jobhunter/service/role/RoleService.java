package vn.hoidanit.jobhunter.service.role;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;

import java.util.Optional;

public interface RoleService {

    Role handleCreateRole(Role role);
    Role handleUpdateRole(Role role);

    Optional<Role> fetchRoleById(long id);
    ResultPaginationDTO fetchAllRoles(Specification<Role> spec, Pageable pageable);
    void deleteRoleById(long id);

    boolean isNameExist(String name);
}
