package com.quickhr.mapper;

import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.entity.PersonalSpending;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonalSpendingMapper {

    PersonalSpendingMapper INSTANCE = Mappers.getMapper(PersonalSpendingMapper.class);

    PersonalSpendingSummaryDto toPersonalSpendingSummaryDto(PersonalSpending personalSpending);
}