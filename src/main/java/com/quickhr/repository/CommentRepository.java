package com.quickhr.repository;

import com.quickhr.entity.Comment;
import com.quickhr.enums.company.ECommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Belirli bir şirketin yorumu var mı kontrolü için
    Optional<Comment> findByCompanyId(Long companyId);

    // Belirli bir kullanıcının yorumu var mı kontrolü için yapıyoruz
    Optional<Comment> findByUserId(Long userId);

    List<Comment> findByStatus(ECommentStatus status);
}