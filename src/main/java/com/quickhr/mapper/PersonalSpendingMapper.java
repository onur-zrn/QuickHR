package com.quickhr.mapper;

import com.quickhr.dto.request.CreatePersonalSpendingRequestDto;
import com.quickhr.dto.request.UpdatePersonalSpendingRequestDto;
import com.quickhr.dto.response.PersonalSpendingDetailResponseDto;
import com.quickhr.dto.response.PersonalSpendingSummaryDto;
import com.quickhr.entity.PersonalSpending;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonalSpendingMapper {

    PersonalSpendingMapper INSTANCE = Mappers.getMapper(PersonalSpendingMapper.class);

    PersonalSpendingSummaryDto toPersonalSpendingSummaryDto(PersonalSpending personalSpending);

    PersonalSpendingDetailResponseDto toPersonalSpendingDetailResponseDto(PersonalSpending personalSpending);

    PersonalSpending toPersonalSpending(CreatePersonalSpendingRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonalSpendingFromDto(UpdatePersonalSpendingRequestDto dto,
                                       @MappingTarget PersonalSpending entity);
}