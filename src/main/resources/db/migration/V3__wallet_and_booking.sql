-- ============================================================================
-- TradeFlow - Wallet & Booking System
-- ============================================================================

-- ─── WALLET ───────────────────────────────────────────────
CREATE TABLE wallets (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL UNIQUE,
    balance     DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency    VARCHAR(3) DEFAULT 'USD',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_wallet_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE wallet_transactions (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id       BIGINT NOT NULL,
    type            ENUM('CREDIT', 'DEBIT') NOT NULL,
    amount          DECIMAL(15,2) NOT NULL,
    balance_after   DECIMAL(15,2) NOT NULL,
    description     VARCHAR(500) NOT NULL,
    reference_type  VARCHAR(50),
    reference_id    BIGINT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE CASCADE,
    INDEX idx_txn_wallet (wallet_id),
    INDEX idx_txn_created (created_at DESC),
    INDEX idx_txn_ref (reference_type, reference_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ─── BOOKINGS ─────────────────────────────────────────────
CREATE TABLE bookings (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_number  VARCHAR(50) NOT NULL UNIQUE,
    user_id         BIGINT NOT NULL,
    status          ENUM('PENDING', 'CONFIRMED', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',

    -- Cargo details
    cargo_description VARCHAR(500) NOT NULL,
    cargo_type      VARCHAR(100),
    weight_kg       DECIMAL(10,2) NOT NULL,
    quantity        INT NOT NULL DEFAULT 1,

    -- Route
    origin_city     VARCHAR(100) NOT NULL,
    origin_country  VARCHAR(100) NOT NULL,
    dest_city       VARCHAR(100) NOT NULL,
    dest_country    VARCHAR(100) NOT NULL,
    transport_mode  ENUM('Maritime', 'Air', 'Land', 'Rail') NOT NULL,

    -- Pricing
    price           DECIMAL(15,2) NOT NULL,
    currency        VARCHAR(3) DEFAULT 'USD',
    paid            BOOLEAN DEFAULT FALSE,

    -- Dates
    pickup_date     DATE,
    estimated_delivery DATE,
    actual_delivery DATE,
    notes           TEXT,

    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_booking_user (user_id),
    INDEX idx_booking_number (booking_number),
    INDEX idx_booking_status (status),
    INDEX idx_booking_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
