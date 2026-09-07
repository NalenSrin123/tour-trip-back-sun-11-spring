package com.etec.tourtripapi.auth.repository;

import com.etec.tourtripapi.auth.entity.User;

public interface RegisterRepository extends UserRepository {
	User save(User user);
}
