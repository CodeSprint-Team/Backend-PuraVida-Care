package com.cenfotec.backendcodesprint.logic.User.Service;

import com.cenfotec.backendcodesprint.logic.Model.Role;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.Repository.RoleRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User createOrUpdateGoogleUser(String email, String firstName, String lastName,
                                         String googleId, String photoUrl) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setGoogleId(googleId);
            user.setPhotoUrl(photoUrl);
            user.setProvider("google");
            return userRepository.save(user);
        } else {
            Role defaultRole = roleRepository.findByRoleName("cliente")
                    .orElseThrow(() -> new RuntimeException("Role 'cliente' not found"));

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUserName(firstName);
            newUser.setLastName(lastName);
            newUser.setGoogleId(googleId);
            newUser.setPhotoUrl(photoUrl);
            newUser.setProvider("google");
            newUser.setUserState("active");
            newUser.setRole(defaultRole);
            newUser.setPassword("");

            return userRepository.save(newUser);
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}