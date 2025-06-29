package com.quickhr.mapper;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.service.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;
import java.util.*;
import java.util.stream.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShiftMapper {
    ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

    ShiftResponseDto toDto(Shift shift);

    @Mapping(target = "fullNames", expression = "java(getFullNames(shift.getAssignedUserIds(), userService))")
    ShiftDetailsResponseDto toDetailsDto(Shift shift, @Context UserService userService);

    default Set<String> getFullNames(Set<Long> userIds, UserService userService) {
        if (userIds == null || userIds.isEmpty()) return Set.of();

        return userIds.stream()
                .map(userId -> userService.findUserById(userId)
                        .map(user -> user.getFirstName() + " " + user.getLastName())
                        .orElse("Unknown"))
                .collect(Collectors.toSet());
    }

    MyShiftResponseDto toMyShiftDto(Shift shift);

    default PersonalShiftDetailsResponseDto toPersonalShiftDetailsDto(Shift shift, User user) {
        if (shift == null || user == null) return null;

        return new PersonalShiftDetailsResponseDto(
                user.getFirstName() + " " + user.getLastName(),
                shift.getShiftId(),
                shift.getShiftName(),
                shift.getBeginHour(),
                shift.getEndHour(),
                shift.getDescription()
        );
    }

    @Mapping(target = "assignedUserIds", expression = "java(getSortedUserIds(shift.getAssignedUserIds()))")
    @Mapping(target = "fullNames", expression = "java(getSortedFullNames(shift.getAssignedUserIds(), userService))")
    ShiftSortedDetailsResponseDto toSortedDetailsDto(Shift shift, @Context UserService userService);

    default List<Long> getSortedUserIds(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return List.of();
        return userIds.stream().sorted().toList();
    }

    default List<String> getSortedFullNames(Set<Long> userIds, UserService userService) {
        if (userIds == null || userIds.isEmpty()) return List.of();

        return userIds.stream()
                .sorted()
                .map(userId -> userService.findUserById(userId)
                        .map(user -> user.getFirstName() + " " + user.getLastName())
                        .orElse("Unknown"))
                .toList();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateShiftFromDto(ShiftUpdateRequestDto dto, @MappingTarget Shift shift);

    @Mapping(target = "shiftId", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "assignedUserIds", ignore = true)
    Shift fromCreateDto(CreateShiftRequestDto dto);

}
