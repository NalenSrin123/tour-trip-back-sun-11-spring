package com.etec.tourtripapi.guide.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="Guides")
public class Guides {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="guide_id")
    private Long id;

    @Column(name="full_name", nullable=false, length=255)
    private String fullName;

    @Column(unique=true, nullable=false, length=255,name="email")
    private String email;

    @Column(unique=true, nullable=false, length=20,name="phone")
    private String phoneNumber;

    @Column(name="guide_url", nullable=false, length=255)
    private String guideUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
