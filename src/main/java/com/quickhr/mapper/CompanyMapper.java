package com.quickhr.mapper;

import com.quickhr.dto.response.CompanyStateResponseDto;
import com.quickhr.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    List<CompanyStateResponseDto> toStateDtoList(List<Company> companies);
}
