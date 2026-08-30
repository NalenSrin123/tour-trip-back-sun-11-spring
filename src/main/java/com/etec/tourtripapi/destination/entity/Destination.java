package com.etec.tourtripapi.destination.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "destinations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Long destinationId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
