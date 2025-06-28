package com.quickhr.service;

import com.quickhr.dto.request.*;
import com.quickhr.dto.response.*;
import com.quickhr.entity.*;
import com.quickhr.enums.embezzlement.*;
import com.quickhr.enums.user.*;
import com.quickhr.exception.*;
import com.quickhr.mapper.*;
import com.quickhr.repository.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmbezzlementService {
    private final EmbezzlementRepository embezzlementRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    //  Giriş yapan kullanıcının(manager) şirketine bağlı olarak yeni bir zimmet kaydı oluşturur
    @Transactional
    public Boolean createEmbezzlement(String token, CreateEmbezzlementRequestDto dto) {
        User userFromToken = userService.getUserFromToken(token);

        if (!userFromToken.getRole().equals(EUserRole.MANAGER)) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_NOT_MANAGER);
        }
        Embezzlement embezzlement = EmbezzlementMapper.INSTANCE.fromCreateDto(dto);
        embezzlement.setCompanyId(userFromToken.getCompanyId());
        embezzlement.setEmbezzlementType(dto.embezzlementType());
        embezzlement.setEmbezzlementState(EEmbezzlementState.PENDING);
        embezzlementRepository.save(embezzlement);
        return true;
    }

    /*
     Bu metot, bir yöneticinin(manager) kendi şirketine ait bir zimmeti
     yine kendi şirketinde çalışan bir kullanıcıya(employee) atamasını sağlar.
     Yetkisizlik, zimmetin ya da çalışanın bulunmaması gibi durumlar hata fırlatır.
     */
    @Transactional
    public void assignEmbezzlementToUser(String token, AssignEmbezzlementRequestDto dto) {
        User manager = userService.getUserFromToken(token);

        // Eğer ilgili ID’ye ait zimmet bulunamazsa özel hata fırlatılır.
        Embezzlement embezzlement = embezzlementRepository.findById(dto.id())
                .orElseThrow(() -> new HRAppException(ErrorType.EMBEZZLEMENT_NOT_FOUND));

        // Atama yapılacak çalışan (employee) bulunur
        User employee = userRepository.findById(dto.userId())
                .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

        // Bu işlem sadece yöneticiler tarafından yapılabilir.
        if (!manager.getRole().equals(EUserRole.MANAGER)){
            throw new HRAppException(ErrorType.UNAUTHORIZED_NOT_MANAGER);
        }
        //Atamayı yapan yönetici, zimmet ve atanacak çalışan aynı şirkete ait değilse işlem geçersiz sayılır.
        if (!manager.getCompanyId().equals(employee.getCompanyId()) ||
                !manager.getCompanyId().equals(embezzlement.getCompanyId()) ||
                !embezzlement.getCompanyId().equals(employee.getCompanyId()))
        {
            throw new HRAppException(ErrorType.UNAUTHORIZED_DIFFERENT_COMPANY);
        }
        /*
        Zimmet ilgili kullanıcıya atanır (userId).
        Şirket bilgisi güncellenir (muhtemelen gerek yok ama yazılmış).
        Atama tarihi olarak o gün atanır.
        Zimmet durumu “onaylandı” olarak işaretlenir.
         */
        embezzlement.setUserId(dto.userId());
        embezzlement.setCompanyId(manager.getCompanyId());
        embezzlement.setAssignedDate(LocalDate.now());
        embezzlement.setEmbezzlementState(EEmbezzlementState.APPROVED);
        embezzlementRepository.save(embezzlement);

        // TODO: E-posta bildirimi - employee.getEmail() adresine mail gönderilebilir (mock)
    }

    /*
    Personel kendisine atanan zimmeti kabul edebilir
    Personel eğer kendisiyle ilgisi olmayan bir ürünse reddedebilir → yöneticisine ret notu gönderir.
     */
    @Transactional
    public Boolean confirmOrRejectEmbezzlement(String token, isConfirmEmbezzlementRequestDto dto) {
        User user = userService.getUserFromToken(token);

        // Eğer ilgili ID’ye ait zimmet bulunamazsa özel hata fırlatılır.
        Embezzlement embezzlement = embezzlementRepository.findById(dto.id())
                .orElseThrow(() -> new HRAppException(ErrorType.EMBEZZLEMENT_NOT_FOUND));

        // Kullanıcı kendi zimmeti olmayan bir kaydı onaylamaya/ret etmeye çalışırsa hata fırlatılır.
        if (!embezzlement.getUserId().equals(user.getId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_EMBEZZLEMENT_OWNER);
        }

        // Sadece henüz kullanıcı tarafından işlem yapılmamış (PENDING) zimmetlere onay/ret işlemi yapılabilir.
        if (!embezzlement.getEmbezzlementState().equals(EEmbezzlementState.PENDING)) {
            throw new HRAppException(ErrorType.EMBEZZLEMENT_STATE_DOESNT_PENDING);
        }

        // Kullanıcının kararına göre işlem yapılır
        if (dto.isConfirm()) {
            embezzlement.setEmbezzlementState(EEmbezzlementState.APPROVED);
            embezzlement.setRejectReason(null); // Önceki ret mesajı varsa temizle
        } else {
            embezzlement.setEmbezzlementState(EEmbezzlementState.REJECTED);
            embezzlement.setRejectReason(dto.rejectReason()); //Ret durumunda rejectReason() girilmesi gerekir.
        }
        return dto.isConfirm();
        // TODO: yöneticisine mail gönderimi — şimdilik log
    }

    // Yöneticinin(manager) sistemdeki mevcut bir zimmet ürününü güncellemesini sağlar.
    /*
       Bu metot, sadece yöneticilerin
	   Kendi çalışanlarına ait olacak şekilde, bir zimmet kaydının açıklama, atanan kişi, tarih ve durum bilgilerini güncelleyebilmesini sağlar.
     */
    @Transactional
    public void updateEmbezzlementProduct(String token,Long embezzlementId, UpdateEmbezzlementProductRequestDto dto) {
        User manager = userService.getUserFromToken(token);

        // Eğer zimmet ID’si geçersizse hata fırlatılır.
        Embezzlement product = embezzlementRepository.findById(embezzlementId)
                .orElseThrow(() -> new HRAppException(ErrorType.EMBEZZLEMENT_NOT_FOUND));

        //Güncelleme yapılacak kullanıcı (employee) bulunur
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new HRAppException(ErrorType.USER_NOT_FOUND));

        // Yalnızca yöneticiler işlem yapabilir
        if (!manager.getRole().equals(EUserRole.MANAGER)) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_NOT_MANAGER);
        }

        // Zimmetin yöneticinin şirketine ait olması gerekir
        // Kullanıcının da aynı şirkete bağlı olması gerekir
        if (!manager.getCompanyId().equals(product.getCompanyId()) ||
                !manager.getCompanyId().equals(user.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_DIFFERENT_COMPANY);
        }

        product.setDescription(dto.description());
        product.setUserId(dto.userId());
        product.setAssignedDate(dto.assignedDate());
        product.setReturnedDate(dto.returnedDate());
        product.setEmbezzlementState(dto.embezzlementState());
        embezzlementRepository.save(product);
        // TODO: Güncellenen kişiye bilgi ver (log veya e-posta)
    }

    /*
    Yöneticinin (manager) bir zimmet ürününü sistemden silmesini sağlar. Ancak bu işlem, hem yetki kontrolü hem de şirket kontrolü yapılarak güvenli şekilde gerçekleştirilir.
    aynı zaman da zimmetin durumu approved(onaylanmış) ise o zimmet silinmez
     */
    @Transactional
    public void deleteEmbezzlementProduct(String token, Long productId) {
        User manager = userService.getUserFromToken(token);

        Embezzlement product = embezzlementRepository.findById(productId)
                .orElseThrow(() -> new HRAppException(ErrorType.EMBEZZLEMENT_NOT_FOUND));

        if (!manager.getRole().equals(EUserRole.MANAGER)) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_NOT_MANAGER);
        }

        if (!manager.getCompanyId().equals(product.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_DIFFERENT_COMPANY);
        }
        // Sadece PENDING veya OTHER durumundaki zimmetler silinebilir
        if (!(product.getEmbezzlementState().equals(EEmbezzlementState.PENDING) ||
                product.getEmbezzlementState().equals(EEmbezzlementState.REJECTED))) {
            throw new HRAppException(ErrorType.CANNOT_DELETE_APPROVED_EMBEZZLEMENT);
        }
        embezzlementRepository.delete(product);

        // TODO: Kullanıcıya bilgi ver (log/e-posta)
    }

    // Giriş yapan kullanıcının(employee) kendisine ait zimmet ürünlerini detaylı şekilde listelemesini sağlar.
    public List<EmbezzlementProductDetailResponseDto> getMyEmbezzlements(String token) {
        User user = userService.getUserFromToken(token);

        return embezzlementRepository.findAllByUserId(user.getId()).stream()
                .map(embezzlement -> EmbezzlementMapper.INSTANCE.toDetailDto(embezzlement, userService))
                .collect(Collectors.toList());
    }
    // Giriş yapan kullanıcının şirketine ait onaylanmış (APPROVED) zimmet kayıtlarını döner.
    public List<Embezzlement> getAssignedEmbezzlement(String token) {
        User userFromToken = userService.getUserFromToken(token);
        return embezzlementRepository.findAllByEmbezzlementState(userFromToken.getCompanyId(), EEmbezzlementState.APPROVED);
    }
    // Giriş yapan kullanıcının şirketine ait onaylanmamış (APPROVED olmayan) zimmet kayıtlarını döner.
    public List<Embezzlement> getUnAssignedEmbezzlement(String token) {
        User user = userService.getUserFromToken(token);
        return embezzlementRepository.findAllByEmbezzlementStateNot(user.getCompanyId(), EEmbezzlementState.APPROVED);
    }
    // Giriş yapan kullanıcının şirketine ait reddedilmiş (REJECTED) zimmet kayıtlarını döner.
    public List<Embezzlement> getRejectedEmbezzlement(String token) {
        User user = userService.getUserFromToken(token);
        return embezzlementRepository.findAllByEmbezzlementState(user.getCompanyId(), EEmbezzlementState.REJECTED);
    }
    // Giriş yapan kullanıcının şirketine ait tüm zimmet kayıtlarını döner.
    public List<EmbezzlementResponseDto> getAllByCompany(String token) {
        User userFromToken = userService.getUserFromToken(token);
        List<Embezzlement> embezzlements = embezzlementRepository.findAllByCompanyId(userFromToken.getCompanyId());
        return embezzlements.stream()
                .map(EmbezzlementMapper.INSTANCE::toResponseDto)
                .collect(Collectors.toList());
    }
    // Belirtilen ID’ye sahip zimmetin detaylarını döner, ancak sadece aynı şirkete aitse izin verir.
    public EmbezzlementProductDetailResponseDto getEmbezzlementDetails(String token, Long embezzlementId) {
        User userFromToken = userService.getUserFromToken(token);

        Embezzlement embezzlement = embezzlementRepository.findById(embezzlementId)
                .orElseThrow(() -> new HRAppException(ErrorType.EMBEZZLEMENT_NOT_FOUND));

        // Farklı şirkete aitse hata ver
        if (!userFromToken.getCompanyId().equals(embezzlement.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_DIFFERENT_COMPANY);
        }
        return EmbezzlementMapper.INSTANCE.toEmbezzlementDetails(embezzlement,userService);
    }
}
