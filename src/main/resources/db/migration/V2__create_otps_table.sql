-- CREATE OTPS TABLE
CREATE TABLE otps (
    otp_id BIGSERIAL PRIMARY KEY,
    recipient VARCHAR(150) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    expiry_time TIMESTAMP NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    purpose VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_otps_recipient ON otps(recipient);
CREATE INDEX idx_otps_recipient_purpose ON otps(recipient, purpose, is_verified);

-- Seed initial test users if they don't exist yet (password: Password123!)
INSERT INTO users (full_name, email, password_hash, role, status)
VALUES 
    ('System Admin', 'kimlizaset9@gmail.com', '$2a$10$k84ncjUg98hszh5UK6UV7OrK7Syj0bP7gMuZLGWzO441YIud/xBxW', 'ADMIN', 'ACTIVE'),
    ('John Customer', 'customer@example.com', '$2a$10$k84ncjUg98hszh5UK6UV7OrK7Syj0bP7gMuZLGWzO441YIud/xBxW', 'CUSTOMER', 'ACTIVE')
ON CONFLICT (email) DO NOTHING;
