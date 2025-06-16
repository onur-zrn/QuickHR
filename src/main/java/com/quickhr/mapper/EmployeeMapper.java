package com.quickhr.mapper;

import com.quickhr.dto.request.EmployeeRequestDto;
import com.quickhr.dto.request.RegisterRequestDto;
import com.quickhr.dto.response.EmployeeResponseDto;
import com.quickhr.entity.Employee;
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

}
