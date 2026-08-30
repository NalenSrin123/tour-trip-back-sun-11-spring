-- ========================================================
-- Flyway Database Migration: V1__init_tour_trip_schema.sql
-- Tour Trip API Database Schema based on ERD
-- ========================================================

-- 1. CATEGORIES TABLE
CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. DESTINATIONS TABLE
CREATE TABLE destinations (
    destination_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100),
    country VARCHAR(100),
    cover_image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. DESTINATION_IMAGES TABLE
CREATE TABLE destination_images (
    image_id BIGSERIAL PRIMARY KEY,
    destination_id BIGINT NOT NULL,
    image_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_dest_images_destination FOREIGN KEY (destination_id) REFERENCES destinations(destination_id) ON DELETE CASCADE
);

-- 4. GUIDES TABLE
CREATE TABLE guides (
    guide_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE,
    phone VARCHAR(50),
    guide_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. TOURS TABLE
CREATE TABLE tours (
    tour_id BIGSERIAL PRIMARY KEY,
    category_id BIGINT,
    tour_image_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    duration_days INT NOT NULL DEFAULT 1,
    base_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tours_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- 6. TOUR_IMAGES TABLE
CREATE TABLE tour_images (
    tour_image_id BIGSERIAL PRIMARY KEY,
    tour_id BIGINT NOT NULL,
    image_url TEXT NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tour_images_tour FOREIGN KEY (tour_id) REFERENCES tours(tour_id) ON DELETE CASCADE
);

-- Circular FK link from tours to primary tour_image if needed
ALTER TABLE tours 
    ADD CONSTRAINT fk_tours_primary_image 
    FOREIGN KEY (tour_image_id) REFERENCES tour_images(tour_image_id) ON DELETE SET NULL;

-- 7. TOUR_SCHEDULES TABLE
CREATE TABLE tour_schedules (
    schedule_id BIGSERIAL PRIMARY KEY,
    tour_id BIGINT NOT NULL,
    guide_id BIGINT,
    departure_date TIMESTAMP NOT NULL,
    return_date TIMESTAMP NOT NULL,
    max_capacity INT NOT NULL DEFAULT 0,
    available_slots INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedules_tour FOREIGN KEY (tour_id) REFERENCES tours(tour_id) ON DELETE CASCADE,
    CONSTRAINT fk_schedules_guide FOREIGN KEY (guide_id) REFERENCES guides(guide_id) ON DELETE SET NULL
);

-- 8. USERS TABLE
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    user_profile TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    avatar_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. BOOKINGS TABLE
CREATE TABLE bookings (
    booking_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,
    booking_number VARCHAR(100) NOT NULL UNIQUE,
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    booking_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_schedule FOREIGN KEY (schedule_id) REFERENCES tour_schedules(schedule_id) ON DELETE RESTRICT
);

-- 10. PAYMENTS TABLE
CREATE TABLE payments (
    payment_id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    paid_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- 11. BOOKING_TRAVELERS TABLE
CREATE TABLE booking_travelers (
    traveler_id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    traveler_type VARCHAR(50) DEFAULT 'ADULT',
    special_request TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_travelers_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE
);

-- 12. REVIEWS TABLE
CREATE TABLE reviews (
    review_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tour_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_tour FOREIGN KEY (tour_id) REFERENCES tours(tour_id) ON DELETE CASCADE
);
