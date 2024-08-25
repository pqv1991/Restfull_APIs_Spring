package vn.hoidanit.jobhunter.domain.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Role;

@Getter
@Setter
public class ResLoginDTO {

    private LoginUser user;
    private String access_token;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginUser{
        private long id;
        private String email;
        private String name;
        private Role role;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetAccount{
        private LoginUser user;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class UserInsideToken{
        private long id;
        private String email;
        private String name;
    }



}
