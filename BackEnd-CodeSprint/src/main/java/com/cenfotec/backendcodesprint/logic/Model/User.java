package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 100)
    @NotBlank
    private String userName;

    @Column(name = "last_name", nullable = false, length = 100)
    @NotBlank
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @NotBlank
    @Email
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    @NotBlank
    @Pattern(
            regexp = "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$",
            message = "Invalid bcrypt hash"
    )
    private String password;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "user_state", nullable = false, length = 20)
    private String userState = "active";

    // Google OAuth fields
    @Column(name = "google_id", length = 100, unique = true)
    private String googleId;

    @Column(name = "photo_url", columnDefinition = "Text")
    private String photoUrl;

    @Column(name = "provider", length = 20)
    private String provider = "local";
}

