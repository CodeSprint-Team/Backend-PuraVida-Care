package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    boolean existsByEmail(String email);

    @Query("""
    SELECT u FROM User u JOIN FETCH u.role r
    WHERE r.roleName IN ('CLIENT', 'ADMIN', 'SENIOR')
    OR (
        r.roleName = 'PROVIDER'
        AND u.userState IN ('active', 'inactive')
        AND EXISTS (
            SELECT p FROM ProviderProfile p
            WHERE p.user = u AND p.providerState = 'active'
        )
    )
""")
    List<User> findUsersForAdmin();

    @Modifying
    @Query("UPDATE User u SET u.userState = :state WHERE u.id = :id")
    void updateUserState(@Param("id") Long id, @Param("state") String state);
}
