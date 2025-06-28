package com.quickhr.mapper;

import com.quickhr.dto.request.CreateEmbezzlementRequestDto;
import com.quickhr.dto.response.EmbezzlementProductDetailResponseDto;
import com.quickhr.dto.response.EmbezzlementResponseDto;
import com.quickhr.entity.Embezzlement;
import com.quickhr.service.UserService;
import jakarta.validation.Valid;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmbezzlementMapper {

    EmbezzlementMapper INSTANCE = Mappers.getMapper(EmbezzlementMapper.class);



    @Mapping(source = "id", target = "embezzlementId")
    @Mapping(source = "rejectReason", target = "rejectReason") /////
    EmbezzlementResponseDto toResponseDto(Embezzlement embezzlement);


    Embezzlement fromEmbezzlementRequestDto(@Valid EmbezzlementResponseDto dto);


    EmbezzlementResponseDto toAddEmbezzlementDto(Embezzlement embezzlement);


    @Mapping(source = "id", target = "embezzlementId")
    @Mapping(target = "fullName", expression = "java(getFullName(embezzlement.getUserId(), userService))")
    @Mapping(source = "rejectReason", target = "rejectReason")  ///////
    EmbezzlementProductDetailResponseDto toDetailDto(Embezzlement embezzlement, @Context UserService userService);


    @Mapping(target = "embezzlementId", source = "id")
    @Mapping(target = "fullName", expression = "java(getFullName(embezzlement.getUserId(), userService))")
    @Mapping(source = "rejectReason", target = "rejectReason")    ///////
    EmbezzlementProductDetailResponseDto toEmbezzlementDetails(Embezzlement embezzlement, @Context UserService userService);

    default String getFullName(Long userId, UserService userService) {
        if (userId == null) return null;
        com.quickhr.entity.User user = userService.findById(userId);
        if (user == null) return null;
        return user.getFirstName() + " " + user.getLastName();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    Embezzlement fromCreateDto(CreateEmbezzlementRequestDto dto);

    // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        //void updateEmbezzlementFromDto(@Valid EmbezzlementResponseDto requestDto, @MappingTarget Embezzlement embezzlement);

}
