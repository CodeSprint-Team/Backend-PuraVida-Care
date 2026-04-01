package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "home")
public class Home extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id", nullable = false)
    @NotNull
    private SeniorProfile seniorProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    @NotNull
    private User createdByUser;

    @Column(name = "home_state", nullable = false, length = 50)
    @NotBlank
    private String homeState;
}
