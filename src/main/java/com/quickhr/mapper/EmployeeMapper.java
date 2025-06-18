package com.quickhr.mapper;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.request.EmployeeUpdateProfileRequestDto;
import com.quickhr.dto.request.EmployeeUpdateRequestDto;
import com.quickhr.dto.request.RegisterRequestDto;
import com.quickhr.dto.response.EmployeeResponseDto;
import com.quickhr.entity.Employee;
import com.quickhr.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeResponseDto toDto(Employee employee);

    Employee fromDto(EmployeeRequestDto dto);

    List<EmployeeResponseDto> toDtoList(List<Employee> employees);

    Employee fromRegisterDto(RegisterRequestDto dto);

    // Çalışanı gelen DTO ile günceller
    @Mapping(target = "id", ignore = true)//id değişmesin
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "companyId", ignore = true)

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromDto(EmployeeUpdateRequestDto dto, @MappingTarget Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromUpdateProfileDto(EmployeeUpdateProfileRequestDto dto, @MappingTarget Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeFromUser(User user, @MappingTarget Employee employee);
}
