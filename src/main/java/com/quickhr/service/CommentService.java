package com.quickhr.service;

import com.quickhr.dto.request.CommentSaveRequestDto;
import com.quickhr.dto.request.CommentUpdateRequestDto;
import com.quickhr.dto.response.CommentResponseDto;
import com.quickhr.entity.Admin;
import com.quickhr.entity.Comment;
import com.quickhr.entity.Company;
import com.quickhr.entity.User;
import com.quickhr.enums.company.ECommentStatus;
import com.quickhr.enums.user.EUserRole;
import com.quickhr.exception.ErrorType;
import com.quickhr.exception.HRAppException;
import com.quickhr.repository.CommentRepository;
import com.quickhr.repository.CompanyRepository;
import com.quickhr.repository.EmployeeRepository;
import com.quickhr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AdminService adminService;

    public void saveComment(CommentSaveRequestDto dto, String token) {
        User manager = userService.getUserFromToken(token);
        if (!manager.getRole().equals(EUserRole.MANAGER)) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        commentRepository.findByCompanyId(manager.getCompanyId())
                .filter(c -> c.getStatus() == ECommentStatus.APPROVED || c.getStatus() == ECommentStatus.PENDING)
                .ifPresent(c -> {
                    throw new HRAppException(ErrorType.COMMENT_ALREADY_EXITS);
                });

        companyRepository.findById(manager.getCompanyId())
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_FOUND));

        Comment comment = Comment.builder()
                .companyId(manager.getCompanyId())
                .userId(manager.getId())
                .position(dto.position())
                .content(dto.content())
                .status(ECommentStatus.PENDING) // ✅ Yeni yorumlar PENDING olarak başlar
                .build();

        commentRepository.save(comment);
    }

    public void updateComment(CommentUpdateRequestDto dto, String token) {
        User manager = userService.getUserFromToken(token);
        if (!manager.getRole().equals(EUserRole.MANAGER)) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        companyRepository.findById(manager.getCompanyId())
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_FOUND));

        Comment comment = commentRepository.findById(dto.commentId())
                .orElseThrow(() -> new HRAppException(ErrorType.COMMENT_NOT_FOUND));

        if (!comment.getCompanyId().equals(manager.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        comment.setPosition(dto.position());
        comment.setContent(dto.content());
        comment.setStatus(ECommentStatus.PENDING); // ✅ Güncellemeden sonra tekrar onaya düşsün istersen
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, String token) {
        User manager = userService.getUserFromToken(token);
        if (!manager.getRole().equals(EUserRole.MANAGER)) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        companyRepository.findById(manager.getCompanyId())
                .orElseThrow(() -> new HRAppException(ErrorType.COMPANY_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new HRAppException(ErrorType.COMMENT_NOT_FOUND));

        if (!comment.getCompanyId().equals(manager.getCompanyId())) {
            throw new HRAppException(ErrorType.UNAUTHORIZED_OPERATION);
        }

        commentRepository.deleteById(commentId);
    }

    public List<CommentResponseDto> getAllPublishedComments() {
        return commentRepository.findByStatus(ECommentStatus.APPROVED).stream()
                .map(comment -> {
                    User user = userRepository.findById(comment.getUserId()).orElse(null);
                    Company company = companyRepository.findById(comment.getCompanyId()).orElse(null);

                    return new CommentResponseDto(
                            comment.getId(),
                            company != null ? company.getName() : "Bilinmeyen Şirket",
                            company != null ? company.getLogo() : null,
                            user != null ? user.getFirstName() + " " + user.getLastName() : "Anonim",
                            comment.getPosition(),
                            user != null ? user.getAvatar() : null,
                            comment.getContent().substring(0, Math.min(100, comment.getContent().length()))
                    );
                })
                .toList();
    }

    public void changeCommentStatus(Long commentId, ECommentStatus status, String token) {
        adminService.getAdminFromToken(token);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new HRAppException(ErrorType.COMMENT_NOT_FOUND));

        // PENDING'e dönüş yasak (isteğe bağlı koruma)
        if (status == ECommentStatus.PENDING) {
            throw new HRAppException(ErrorType.INVALID_COMMENT_STATUS);
        }

        comment.setStatus(status);
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getAllPendingComments(String token) {
        adminService.getAdminFromToken(token);
        return commentRepository.findByStatus(ECommentStatus.PENDING).stream()
                .map(comment -> {
                    User user = userRepository.findById(comment.getUserId()).orElse(null);
                    Company company = companyRepository.findById(comment.getCompanyId()).orElse(null);

                    return new CommentResponseDto(
                            comment.getId(),
                            company != null ? company.getName() : "Bilinmeyen Şirket",
                            company != null ? company.getLogo() : null,
                            user != null ? user.getFirstName() + " " + user.getLastName() : "Anonim",
                            comment.getPosition(),
                            user != null ? user.getAvatar() : null,
                            comment.getContent().substring(0, Math.min(100, comment.getContent().length()))
                    );
                })
                .toList();
    }


    public List<CommentResponseDto> getAllComments(String token) {
        adminService.getAdminFromToken(token);
        return commentRepository.findAll().stream()
                .map(comment -> {
                    User user = userRepository.findById(comment.getUserId()).orElse(null);
                    Company company = companyRepository.findById(comment.getCompanyId()).orElse(null);

                    return new CommentResponseDto(
                            comment.getId(),
                            company != null ? company.getName() : "Bilinmeyen Şirket",
                            company != null ? company.getLogo() : null,
                            user != null ? user.getFirstName() + " " + user.getLastName() : "Anonim",
                            comment.getPosition(),
                            user != null ? user.getAvatar() : null,
                            comment.getContent().substring(0, Math.min(100, comment.getContent().length()))
                    );
                })
                .toList();
    }
}

