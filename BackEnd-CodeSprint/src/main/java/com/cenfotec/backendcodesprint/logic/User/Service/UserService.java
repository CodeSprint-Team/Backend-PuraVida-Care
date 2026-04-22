package com.cenfotec.backendcodesprint.logic.User.Service;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.User.DTO.Request.RegisterUserRequestDto;
import com.cenfotec.backendcodesprint.logic.User.DTO.Response.UserResponseDto;
import com.cenfotec.backendcodesprint.logic.User.Mapper.UserMapper;
import com.cenfotec.backendcodesprint.logic.User.Repository.RoleRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // Retorna [User, Boolean isNewUser]
    @Transactional
    public Object[] createOrUpdateGoogleUser(String email, String firstName, String lastName,
                                             String googleId, String photoUrl, Long roleId) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setGoogleId(googleId);
            user.setPhotoUrl(photoUrl);
            user.setProvider("google");
            User saved = userRepository.save(user);
            return new Object[]{ saved, false };
        } else {
            Role role;
            if (roleId != null) {
                role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
            } else {
                role = roleRepository.findByRoleName("CLIENT")
                        .orElseThrow(() -> new RuntimeException("Role 'CLIENT' not found"));
            }

            String randomPassword = passwordEncoder.encode(UUID.randomUUID().toString());

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(firstName);
            newUser.setLastName(lastName);
            newUser.setGoogleId(googleId);
            newUser.setPhotoUrl(photoUrl);
            newUser.setProvider("google");
            newUser.setUserState("active");
            newUser.setRole(role);
            newUser.setPassword(randomPassword);

            User saved = userRepository.save(newUser);
            return new Object[]{ saved, true };
        }
    }

    @Transactional
    public UserResponseDto updateUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        user.setRole(role);
        User saved = userRepository.save(user);
        return userMapper.toResponseDto(saved);
    }

    @Transactional
    public UserResponseDto registerUser(RegisterUserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = new User();
        user.setUserName(requestDto.getUserName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(role);
        user.setUserState("active");
        user.setProvider("local");

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    public User loginWithEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return user;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}