package com.quickhr.controller;

import com.quickhr.dto.request.AssignEmbezzlementRequestDto;
import com.quickhr.dto.request.CreateEmbezzlementRequestDto;
import com.quickhr.dto.request.UpdateEmbezzlementProductRequestDto;
import com.quickhr.dto.request.isConfirmEmbezzlementRequestDto;
import com.quickhr.dto.response.BaseResponse;
import com.quickhr.dto.response.EmbezzlementProductDetailResponseDto;
import com.quickhr.dto.response.EmbezzlementResponseDto;
import com.quickhr.entity.Embezzlement;
import com.quickhr.service.EmbezzlementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.quickhr.constant.EndPoints.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping(COMPANY)
@CrossOrigin(origins = "*")
public class EmbezzlementController {
    private final EmbezzlementService embezzlementService;


    // Şirkete yeni zimmet eklemek için kullanılır
    @PostMapping(CREATE_EMBEZZLEMENT)
    public ResponseEntity<BaseResponse<Boolean>> addEmbezzlement(@RequestParam String token, @Valid @RequestBody CreateEmbezzlementRequestDto dto) {

        Boolean responseDto = embezzlementService.createEmbezzlement(token, dto);

        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .success(true)
                .message("Zimmet şirkete başarıyla eklendi.")
                .data(responseDto)
                .build());
    }


    // Çalışana zimmet atama
    @PostMapping(ASSIGN_EMBEZZLEMENT)
    public ResponseEntity<BaseResponse<Void>> assignEmbezzlementToUser(@RequestParam String token, @RequestBody AssignEmbezzlementRequestDto dto) {

        embezzlementService.assignEmbezzlementToUser(token, dto);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .code(200)
                .success(true)
                .message("Zimmet başarıyla çalışana atandı.")
                .build());
    }

    // Zimmet Onay / Reddet
    @PutMapping(CONFIRM_REJECT)
    public ResponseEntity<BaseResponse<Boolean>> confirmOrRejectEmbezzlement(@RequestParam String token, isConfirmEmbezzlementRequestDto dto) {

        Boolean result = embezzlementService.confirmOrRejectEmbezzlement(token, dto);
        String message;
        if (result) {
            message = "RequestEmbezzlement Confirmed!";
        } else {
            message = "RequestEmbezzlement Rejected!";
        }
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(result)
                .success(true)
                .message(message)
                .build());
    }

    // ZimmetGüncelleme
    @PutMapping(UPDATE_EMBEZZLEMENT)
    public ResponseEntity<BaseResponse<Void>> updateEmbezzlementProduct(@RequestParam String token,Long embezzlementId , @RequestBody UpdateEmbezzlementProductRequestDto dto) {

        embezzlementService.updateEmbezzlementProduct(token,embezzlementId ,dto);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .code(200)
                .success(true)
                .message("Zimmet başarıyla güncellendi.")
                .build());
    }

    // Zimmet Silme
    @DeleteMapping(DELETE_EMBEZZLEMENT)
    public ResponseEntity<BaseResponse<Void>> deleteEmbezzlementProduct(@RequestParam String token, @RequestParam Long productId) {

        embezzlementService.deleteEmbezzlementProduct(token, productId);

        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .code(200)
                .success(true)
                .message("Zimmet başarıyla silindi.")
                .build());
    }

    // Personele Ait Zimmet Listesini Görüntüleme (Detaylı Bilgiler Dahil)
    @GetMapping(MY_EMBEZZLEMENT_LIST)
    public ResponseEntity<BaseResponse<List<EmbezzlementProductDetailResponseDto>>> getMyEmbezzlement(@RequestParam String token) {

        List<EmbezzlementProductDetailResponseDto> list = embezzlementService.getMyEmbezzlements(token);

        return ResponseEntity.ok(BaseResponse.<List<EmbezzlementProductDetailResponseDto>>builder()
                .code(200)
                .success(true)
                .message("Zimmetler başarıyla listelendi.")
                .data(list)
                .build());
    }

    // Assign durumundaki zimmetleri listeler
    @GetMapping(ASSIGNED_EMBEZZLEMENT_LIST)
    public ResponseEntity<BaseResponse<List<Embezzlement>>> getAssignedEmbezzlement(@RequestParam String token) {

        return ResponseEntity.ok(BaseResponse.<List<Embezzlement>>builder()
                .code(200)
                .success(true)
                .message("Zimmet listesi getirildi.")
                .data(embezzlementService.getAssignedEmbezzlement(token))
                .build());
    }

    // Reject durumundaki zimmetleri listeler
    @GetMapping(REJECTED_EMBEZZLEMENT_LIST)
    public ResponseEntity<BaseResponse<List<Embezzlement>>> getRejectedEmbezzlement(@RequestParam String token) {

        return ResponseEntity.ok(BaseResponse.<List<Embezzlement>>builder()
                .code(200)
                .success(true)
                .message("Zimmet listesi getirildi.")
                .data(embezzlementService.getRejectedEmbezzlement(token))
                .build());
    }

    // Onaylanan zimmetler dışındaki diğer durumları içeren zimmetleri listeler
    @GetMapping(UNASSIGNED_EMBEZZLEMENT_LIST)
    public ResponseEntity<BaseResponse<List<Embezzlement>>> getUnAssignedEmbezzlement(@RequestParam String token) {

        return ResponseEntity.ok(BaseResponse.<List<Embezzlement>>builder()
                .code(200)
                .success(true)
                .message("Zimmet listesi getirildi.")
                .data(embezzlementService.getUnAssignedEmbezzlement(token))
                .build());
    }

    // Şirketteki tüm çalışanların zimmetlerini listeler
    @GetMapping(EMBEZZLEMENT_LIST)
    public ResponseEntity<BaseResponse<List<EmbezzlementResponseDto>>> getAllEmbezzlementByCompany(@RequestParam String token) {
        List<EmbezzlementResponseDto> allByCompany = embezzlementService.getAllByCompany(token);
        return ResponseEntity.ok(BaseResponse.<List<EmbezzlementResponseDto>>builder()
                .code(200)
                .success(true)
                .message("Şirkete ait tüm zimmetler listelendi.")
                .data(allByCompany)
                .build());
    }

    // Şirkette bulunan zimmetlerin detaylı bilgisini tutar
    @GetMapping(EMBEZZLEMENT_DETAILS)
    public ResponseEntity<BaseResponse<EmbezzlementProductDetailResponseDto>> getEmbezzlementDetails(@RequestParam String token, @PathVariable Long embezzlementId) {
        EmbezzlementProductDetailResponseDto embezzlementDetails = embezzlementService.getEmbezzlementDetails(token,embezzlementId);
        return ResponseEntity.ok(BaseResponse.<EmbezzlementProductDetailResponseDto>builder()
                .code(200)
                .success(true)
                .message("Şirkete ait tüm zimmetler listelendi.")
                .data(embezzlementDetails)
                .build());
    }
}
