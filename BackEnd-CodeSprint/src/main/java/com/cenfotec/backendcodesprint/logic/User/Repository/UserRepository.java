package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

