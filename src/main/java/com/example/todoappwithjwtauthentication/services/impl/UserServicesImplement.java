package com.example.todoappwithjwtauthentication.services.impl;

import com.example.todoappwithjwtauthentication.dto.requests.LoginRequest;
import com.example.todoappwithjwtauthentication.dto.requests.SignupRequest;
import com.example.todoappwithjwtauthentication.dto.responses.JwtResponse;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.entites.User;
import com.example.todoappwithjwtauthentication.enums.ERole;
import com.example.todoappwithjwtauthentication.exceptions.DataNotFoundException;
import com.example.todoappwithjwtauthentication.exceptions.InvalidDataException;
import com.example.todoappwithjwtauthentication.repositories.RoleRepository;
import com.example.todoappwithjwtauthentication.repositories.UserRepository;
import com.example.todoappwithjwtauthentication.security.jwt.JwtUtils;
import com.example.todoappwithjwtauthentication.security.services.UserDetailsImpl;
import com.example.todoappwithjwtauthentication.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServicesImplement implements UserServices {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String roles = userDetails.getAuthorities().toString();
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @Override
    public MessageResponse register(SignupRequest signupRequest) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?#&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (signupRequest.getUserName() == null) {
            throw new InvalidDataException("Họ tên không được để trống");
        }
        if (signupRequest.getUserName().equals("")){
            throw new InvalidDataException("Username không được để trống");
        }
        if (userRepository.existsUserByUsername(signupRequest.getUserName())) {
            throw new InvalidDataException("Username đã tồn tại");
        }
        if (signupRequest.getEmail().equals("")){
            throw new InvalidDataException("Email không được để trống");
        }
        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            throw new InvalidDataException("Email đã được sử dụng");
        }
        if (signupRequest.getPassword().matches(regex) == false) {
            throw new InvalidDataException("Mật khẩu phải tối thiểu 8 ký tự, bao gồm chữ số, chữ hoa, chữ thường, ký hiệu đặc biệt");
        }
        User user = new User(signupRequest.getFullName(), signupRequest.getUserName(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        String strRole = signupRequest.getRole();
        if ((strRole != null) && (strRole.equalsIgnoreCase("admin"))) {
            user.setRole(roleRepository.findByeRole(ERole.ROLE_ADMIN).orElseThrow(() -> new DataNotFoundException("Không tìm thấy Role")));
        } else {
            user.setRole(roleRepository.findByeRole(ERole.ROLE_USER).orElseThrow(() -> new DataNotFoundException("Không tìm thấy Role")));
        }
        userRepository.save(user);
        return new MessageResponse("Đăng ký người dùng thành công");
    }
}
