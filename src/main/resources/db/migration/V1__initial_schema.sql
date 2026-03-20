-- ============================================================================
-- TradeFlow - Complete MySQL Schema
-- Version: 1.0
-- Engine: InnoDB | Charset: utf8mb4 | Collation: utf8mb4_unicode_ci
-- Normalization: 3NF+
-- ============================================================================

-- ============================================================================
-- 1. AUTHENTICATION & USER MANAGEMENT
-- ============================================================================

CREATE TABLE roles (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (name, description) VALUES
    ('ROLE_USER', 'Standard platform user'),
    ('ROLE_ADMIN', 'Platform administrator with full access');

CREATE TABLE users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(150) NOT NULL,
    phone           VARCHAR(20),
    company_name    VARCHAR(255),
    country         VARCHAR(100),
    avatar_url      VARCHAR(500),
    email_verified  BOOLEAN DEFAULT FALSE,
    enabled         BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_users_email (email),
    INDEX idx_users_company (company_name),
    INDEX idx_users_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE refresh_tokens (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    token       VARCHAR(500) NOT NULL UNIQUE,
    expires_at  TIMESTAMP NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_refresh_token (token),
    INDEX idx_refresh_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 2. PRODUCT / GOODS CATALOG
-- ============================================================================

CREATE TABLE categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    slug        VARCHAR(100) NOT NULL UNIQUE,
    icon_class  VARCHAR(100),
    description TEXT,
    sort_order  INT DEFAULT 0,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_categories_slug (slug),
    INDEX idx_categories_active (is_active, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE products (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id         BIGINT NOT NULL,
    name                VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL UNIQUE,
    description         TEXT,
    short_description   VARCHAR(500),
    icon_class          VARCHAR(100),
    avg_shipment_value  VARCHAR(50),
    hs_code             VARCHAR(20),
    is_featured         BOOLEAN DEFAULT FALSE,
    is_active           BOOLEAN DEFAULT TRUE,
    sort_order          INT DEFAULT 0,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    INDEX idx_products_category (category_id),
    INDEX idx_products_slug (slug),
    INDEX idx_products_active (is_active, sort_order),
    INDEX idx_products_featured (is_featured, is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE product_images (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT NOT NULL,
    image_url   VARCHAR(500) NOT NULL,
    alt_text    VARCHAR(255),
    is_primary  BOOLEAN DEFAULT FALSE,
    sort_order  INT DEFAULT 0,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product_images_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Source regions for products (M:N — a product can come from multiple regions)
CREATE TABLE source_regions (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL UNIQUE,
    code    VARCHAR(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE product_source_regions (
    product_id      BIGINT NOT NULL,
    source_region_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, source_region_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (source_region_id) REFERENCES source_regions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Handling tags for products (e.g., "Hazmat Certified", "Temperature-Controlled")
CREATE TABLE tags (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL UNIQUE,
    color   VARCHAR(20) DEFAULT '#4d8bff'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE product_tags (
    product_id  BIGINT NOT NULL,
    tag_id      BIGINT NOT NULL,
    PRIMARY KEY (product_id, tag_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 3. CARRIER / FLEET MANAGEMENT
-- ============================================================================

CREATE TABLE carrier_types (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE,
    slug    VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO carrier_types (name, slug) VALUES
    ('Maritime', 'maritime'),
    ('Air Freight', 'air'),
    ('Land', 'land'),
    ('Rail', 'rail');

CREATE TABLE carriers (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrier_type_id     BIGINT NOT NULL,
    name                VARCHAR(255) NOT NULL,
    slug                VARCHAR(255) NOT NULL UNIQUE,
    logo_url            VARCHAR(500),
    description         TEXT,
    asset_count         INT DEFAULT 0,
    asset_label         VARCHAR(50),
    route_count         INT DEFAULT 0,
    route_label         VARCHAR(100),
    coverage_countries  INT DEFAULT 0,
    on_time_rate        DECIMAL(5,2) DEFAULT 0.00,
    rating              DECIMAL(3,2) DEFAULT 0.00,
    is_active           BOOLEAN DEFAULT TRUE,
    sort_order          INT DEFAULT 0,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (carrier_type_id) REFERENCES carrier_types(id) ON DELETE RESTRICT,
    INDEX idx_carriers_type (carrier_type_id),
    INDEX idx_carriers_slug (slug),
    INDEX idx_carriers_active (is_active, sort_order),
    INDEX idx_carriers_rating (rating DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE carrier_specializations (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrier_id  BIGINT NOT NULL,
    name        VARCHAR(100) NOT NULL,

    FOREIGN KEY (carrier_id) REFERENCES carriers(id) ON DELETE CASCADE,
    INDEX idx_carrier_specs (carrier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 4. SHIPMENT TRACKING
-- ============================================================================

CREATE TABLE shipment_statuses (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE,
    color   VARCHAR(20) DEFAULT '#00d4ff'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO shipment_statuses (name, color) VALUES
    ('Pending', '#ffd700'),
    ('In Transit', '#00d4ff'),
    ('Customs Hold', '#ff8c42'),
    ('Delivered', '#00f5d4'),
    ('Cancelled', '#ff4444');

CREATE TABLE shipments (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_id     VARCHAR(50) NOT NULL UNIQUE,
    user_id         BIGINT,
    carrier_id      BIGINT,
    status_id       BIGINT NOT NULL,
    transport_mode  VARCHAR(20) NOT NULL,
    origin_city     VARCHAR(100) NOT NULL,
    origin_country  VARCHAR(100) NOT NULL,
    dest_city       VARCHAR(100) NOT NULL,
    dest_country    VARCHAR(100) NOT NULL,
    cargo_type      VARCHAR(100),
    weight_tons     DECIMAL(10,2),
    container_count INT DEFAULT 0,
    pallet_count    INT DEFAULT 0,
    departure_date  DATE,
    eta             DATE,
    delivered_date  DATE,
    notes           TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (carrier_id) REFERENCES carriers(id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES shipment_statuses(id) ON DELETE RESTRICT,
    INDEX idx_shipments_tracking (tracking_id),
    INDEX idx_shipments_user (user_id),
    INDEX idx_shipments_status (status_id),
    INDEX idx_shipments_dates (departure_date, eta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shipment_milestones (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    shipment_id BIGINT NOT NULL,
    title       VARCHAR(150) NOT NULL,
    location    VARCHAR(200),
    status      ENUM('completed', 'current', 'pending') DEFAULT 'pending',
    milestone_date  TIMESTAMP,
    sort_order  INT DEFAULT 0,

    FOREIGN KEY (shipment_id) REFERENCES shipments(id) ON DELETE CASCADE,
    INDEX idx_milestones_shipment (shipment_id, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 5. INQUIRY / LEAD SYSTEM
-- ============================================================================

CREATE TABLE inquiry_types (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO inquiry_types (name) VALUES
    ('General'), ('Quote Request'), ('Product Inquiry'),
    ('Carrier Inquiry'), ('Partnership'), ('Support');

CREATE TABLE inquiries (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    inquiry_type_id BIGINT NOT NULL,
    user_id         BIGINT,
    name            VARCHAR(150) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    phone           VARCHAR(20),
    company_name    VARCHAR(255),
    subject         VARCHAR(255) NOT NULL,
    message         TEXT NOT NULL,
    product_id      BIGINT,
    carrier_id      BIGINT,
    status          ENUM('new', 'in_progress', 'resolved', 'closed') DEFAULT 'new',
    admin_notes     TEXT,
    resolved_at     TIMESTAMP NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (inquiry_type_id) REFERENCES inquiry_types(id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL,
    FOREIGN KEY (carrier_id) REFERENCES carriers(id) ON DELETE SET NULL,
    INDEX idx_inquiries_status (status),
    INDEX idx_inquiries_user (user_id),
    INDEX idx_inquiries_type (inquiry_type_id),
    INDEX idx_inquiries_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 6. ANALYTICS DATA
-- ============================================================================

CREATE TABLE platform_stats (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    stat_key        VARCHAR(50) NOT NULL UNIQUE,
    stat_value      VARCHAR(50) NOT NULL,
    display_label   VARCHAR(100) NOT NULL,
    icon_class      VARCHAR(100),
    sort_order      INT DEFAULT 0,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Hero stats: Active Shipments, Countries, Carriers, On-Time Rate
INSERT INTO platform_stats (stat_key, stat_value, display_label, icon_class, sort_order) VALUES
    ('active_shipments', '12,500', 'Active Shipments', 'fas fa-shipping-fast', 1),
    ('countries', '185', 'Countries', 'fas fa-globe', 2),
    ('carriers', '340', 'Carriers', 'fas fa-truck', 3),
    ('on_time_rate', '99%', 'On-Time Rate', 'fas fa-check-circle', 4);

CREATE TABLE monthly_analytics (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    year            INT NOT NULL,
    month           INT NOT NULL,
    total_shipments INT DEFAULT 0,
    revenue         DECIMAL(15,2) DEFAULT 0.00,
    avg_transit_days DECIMAL(5,1) DEFAULT 0.0,
    delivery_rate   DECIMAL(5,2) DEFAULT 0.00,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE KEY uk_year_month (year, month),
    INDEX idx_analytics_period (year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE revenue_by_region (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    region_name VARCHAR(100) NOT NULL,
    revenue     DECIMAL(15,2) NOT NULL,
    percentage  DECIMAL(5,2) NOT NULL,
    year        INT NOT NULL,
    color       VARCHAR(20),

    INDEX idx_revenue_region_year (year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE commodity_performance (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id     BIGINT NOT NULL,
    revenue         DECIMAL(15,2) NOT NULL,
    revenue_share   DECIMAL(5,2) NOT NULL,
    growth_percent  DECIMAL(5,2) DEFAULT 0.00,
    year            INT NOT NULL,

    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_commodity_perf_year (year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE trade_routes (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin_city     VARCHAR(100) NOT NULL,
    origin_lat      DECIMAL(10,7),
    origin_lng      DECIMAL(10,7),
    dest_city       VARCHAR(100) NOT NULL,
    dest_lat        DECIMAL(10,7),
    dest_lng        DECIMAL(10,7),
    route_color     VARCHAR(20) DEFAULT '#00d4ff',
    volume          INT DEFAULT 0,
    avg_transit_days DECIMAL(5,1),
    is_active       BOOLEAN DEFAULT TRUE,
    sort_order      INT DEFAULT 0,

    INDEX idx_trade_routes_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 7. CMS - DYNAMIC WEBSITE CONTENT
-- ============================================================================

CREATE TABLE site_settings (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    setting_type ENUM('text', 'html', 'json', 'number', 'boolean', 'url') DEFAULT 'text',
    description VARCHAR(255),
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO site_settings (setting_key, setting_value, setting_type, description) VALUES
    ('site_name', 'TradeFlow', 'text', 'Platform name'),
    ('hero_headline', 'Global Trade, Reimagined', 'text', 'Hero section headline'),
    ('hero_subtext', 'Real-time visibility across your entire supply chain', 'text', 'Hero section subtitle'),
    ('footer_copyright', '2026 TradeFlow. All rights reserved.', 'text', 'Footer copyright text'),
    ('contact_email', 'info@tradeflow.com', 'text', 'Contact email'),
    ('contact_phone', '+1-800-TRADE', 'text', 'Contact phone');

CREATE TABLE banners (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    subtitle        VARCHAR(500),
    image_url       VARCHAR(500),
    cta_text        VARCHAR(100),
    cta_link        VARCHAR(500),
    position        VARCHAR(50) DEFAULT 'hero',
    is_active       BOOLEAN DEFAULT TRUE,
    sort_order      INT DEFAULT 0,
    starts_at       TIMESTAMP NULL,
    ends_at         TIMESTAMP NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_banners_position (position, is_active, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE trusted_partners (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    logo_url    VARCHAR(500) NOT NULL,
    website_url VARCHAR(500),
    sort_order  INT DEFAULT 0,
    is_active   BOOLEAN DEFAULT TRUE,

    INDEX idx_partners_active (is_active, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE globe_city_markers (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    city_name   VARCHAR(100) NOT NULL,
    latitude    DECIMAL(10,7) NOT NULL,
    longitude   DECIMAL(10,7) NOT NULL,
    is_active   BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 8. ORDER SYSTEM (EXPANSION-READY)
-- ============================================================================

CREATE TABLE orders (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number    VARCHAR(50) NOT NULL UNIQUE,
    user_id         BIGINT NOT NULL,
    status          ENUM('draft', 'confirmed', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'draft',
    total_amount    DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency        VARCHAR(3) DEFAULT 'USD',
    shipping_address TEXT,
    billing_address TEXT,
    notes           TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    INDEX idx_orders_user (user_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_number (order_number),
    INDEX idx_orders_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE order_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL DEFAULT 1,
    unit_price  DECIMAL(15,2) NOT NULL,
    total_price DECIMAL(15,2) NOT NULL,

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    INDEX idx_order_items_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 9. AUDIT LOG
-- ============================================================================

CREATE TABLE audit_log (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT,
    action      VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id   BIGINT,
    details     JSON,
    ip_address  VARCHAR(45),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_audit_user (user_id),
    INDEX idx_audit_entity (entity_type, entity_id),
    INDEX idx_audit_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
