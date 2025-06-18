package com.quickhr.service;

import com.quickhr.entity.Permission;
import com.quickhr.repository.PermissonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {

	private final PermissonRepository permissonRepository;


	public Permission save(Permission permission) {
		return permissonRepository.save(permission);
	}

	public Optional<Permission> getPermissionById(Long id) {
		return permissonRepository.findById(id);
	}

}
