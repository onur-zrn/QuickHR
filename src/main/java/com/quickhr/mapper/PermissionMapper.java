package com.quickhr.mapper;

import com.quickhr.dto.request.CreateLeaveRequestDto;
import com.quickhr.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "permissionState", ignore = true) // Serviste PENDING olarak atanacak
    Permission toPermission(CreateLeaveRequestDto dto, Long userId);
}
