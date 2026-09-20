package com.etec.tourtripapi.tour.entity;

import com.etec.tourtripapi.category.entity.Category;
import com.etec.tourtripapi.common.entity.BaseEntity;
import com.etec.tourtripapi.schedule.entity.TourSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tour_id")
    private Long tourId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_image_id")
    private TourImage primaryImage;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_days", nullable = false)
    @Builder.Default
    private Integer durationDays = 1;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal basePrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TourImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TourSchedule> schedules = new ArrayList<>();

    public void addImage(TourImage image) {
        images.add(image);
        image.setTour(this);
    }

    public void removeImage(TourImage image) {
        images.remove(image);
        image.setTour(null);
        if (primaryImage != null && primaryImage.equals(image)) {
            primaryImage = null;
        }
    }
}
