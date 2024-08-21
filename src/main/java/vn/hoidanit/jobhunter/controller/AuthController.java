package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.login.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.dto.login.ResLoginDTO;
import vn.hoidanit.jobhunter.service.user.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Fetch login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //create a token
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User userData = userService.handleGetUserByUsername(loginDto.getUsername());
        if(userData != null){
            ResLoginDTO.LoginUser loginUser = new ResLoginDTO.LoginUser(userData.getId(), userData.getEmail(), userData.getName());
            resLoginDTO.setUser(loginUser);
        }
        String access_token= securityUtil.createAccessToken(authentication.getName(),resLoginDTO);
        resLoginDTO.setAccess_token(access_token);
        String refresh_token = securityUtil.createRefreshToken(loginDto.getUsername(),resLoginDTO );
        //update refresh_token for user
        userService.updateUserToken(refresh_token,loginDto.getUsername());

        //set cookies
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token",refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(resLoginDTO);
    }


    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.LoginUser userLogin = new ResLoginDTO.LoginUser();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }
    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO>  getRefreshToken( @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException{
        //check valid
      Jwt decodedToken= securityUtil.checkValidRefreshToken(refresh_token);
      String email = decodedToken.getSubject();
        //check user by token email
        User currentUser = userService.getUserByRefreshTokenAndEmail(refresh_token,email);
        if(currentUser == null){
            throw new IdInvalidException("Refresh token khong hop le");
        }
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User userData = userService.handleGetUserByUsername(email);
        if(userData != null){
            ResLoginDTO.LoginUser loginUser = new ResLoginDTO.LoginUser(userData.getId(), userData.getEmail(), userData.getName());
            resLoginDTO.setUser(loginUser);
        }
        String access_token= securityUtil.createAccessToken(email,resLoginDTO);
        resLoginDTO.setAccess_token(access_token);
        String new_refresh_token = securityUtil.createRefreshToken(email,resLoginDTO );
        //update refresh_token for user
        userService.updateUserToken(new_refresh_token,email);

        //set cookies
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token",new_refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(resLoginDTO);
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() throws IdInvalidException{
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
        if(email.equals("")){
            throw  new IdInvalidException("Acccess token khong hop le");
        }
        // update refresh token null
        userService.updateUserToken(null,email);
        //remove refresh token cookies
        ResponseCookie deleteCookie = ResponseCookie
                .from("refresh_token",null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,deleteCookie.toString()).body(null);
    }
}
