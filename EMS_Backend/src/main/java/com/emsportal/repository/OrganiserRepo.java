package com.emsportal.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.emsportal.model.OrganiserEntity;

public interface OrganiserRepo extends JpaRepository<OrganiserEntity, Integer>  {
	Optional<OrganiserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
}
