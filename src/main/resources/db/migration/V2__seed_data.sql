-- ============================================================================
-- TradeFlow - Seed Data (matches frontend content)
-- ============================================================================

-- ─── Source Regions ───────────────────────────────────────
INSERT INTO source_regions (name, code) VALUES
    ('Taiwan', 'TW'), ('South Korea', 'KR'), ('Germany', 'DE'),
    ('Japan', 'JP'), ('USA', 'US'), ('Brazil', 'BR'),
    ('India', 'IN'), ('Bangladesh', 'BD'), ('Vietnam', 'VN'),
    ('China', 'CN'), ('Malaysia', 'MY'), ('Colombia', 'CO');

-- ─── Tags ─────────────────────────────────────────────────
INSERT INTO tags (name, color) VALUES
    ('ESD Protected', '#4d8bff'),
    ('Climate Controlled', '#00d4ff'),
    ('Heavy Cargo', '#ff8c42'),
    ('Hazmat Certified', '#ff4444'),
    ('Temperature-Controlled', '#00f5d4'),
    ('Oversized Load', '#a855f7'),
    ('Bulk Commodity', '#ffd700'),
    ('Fragile', '#ff8c42'),
    ('Perishable', '#00f5d4'),
    ('Organic Certified', '#00f5d4');

-- ─── Categories ───────────────────────────────────────────
INSERT INTO categories (name, slug, icon_class, description, sort_order) VALUES
    ('Electronics', 'electronics', 'fas fa-microchip', 'Electronic components and devices', 1),
    ('Machinery', 'machinery', 'fas fa-cogs', 'Industrial machinery and equipment', 2),
    ('Agriculture', 'agriculture', 'fas fa-seedling', 'Agricultural products and commodities', 3),
    ('Textiles', 'textiles', 'fas fa-tshirt', 'Textiles and apparel products', 4),
    ('Chemicals', 'chemicals', 'fas fa-flask', 'Specialty and industrial chemicals', 5),
    ('Automotive', 'automotive', 'fas fa-car', 'Automotive parts and components', 6);

-- ─── Products ─────────────────────────────────────────────
INSERT INTO products (category_id, name, slug, description, short_description, icon_class, avg_shipment_value, is_featured, sort_order) VALUES
    (1, 'Semiconductors', 'semiconductors', 'Advanced semiconductor chips and integrated circuits for global electronics manufacturing', 'Advanced chips & ICs', 'fas fa-microchip', '$2.4M', true, 1),
    (2, 'Industrial Machinery', 'industrial-machinery', 'Heavy-duty industrial machinery and precision engineering equipment', 'Heavy-duty machinery', 'fas fa-industry', '$1.8M', true, 2),
    (3, 'Grain & Cereals', 'grain-cereals', 'Bulk grain shipments including wheat, rice, corn and other cereals', 'Bulk grain shipments', 'fas fa-wheat-awn', '$890K', true, 3),
    (4, 'Textiles & Apparel', 'textiles-apparel', 'Premium textiles and ready-made garments for international fashion markets', 'Premium textiles & garments', 'fas fa-vest', '$650K', true, 4),
    (5, 'Specialty Chemicals', 'specialty-chemicals', 'Industrial and specialty chemicals with certified hazmat handling', 'Industrial chemicals', 'fas fa-flask', '$1.2M', true, 5),
    (6, 'Automotive Parts', 'automotive-parts', 'OEM and aftermarket automotive components and assemblies', 'OEM auto components', 'fas fa-gear', '$1.5M', true, 6),
    (1, 'Solar Panels', 'solar-panels', 'High-efficiency photovoltaic modules and solar energy equipment', 'PV modules & solar gear', 'fas fa-solar-panel', '$980K', true, 7),
    (3, 'Coffee & Spices', 'coffee-spices', 'Premium coffee beans and exotic spices from tropical origins', 'Premium coffee & spices', 'fas fa-mug-hot', '$420K', true, 8);

-- Product-Source Region mappings
INSERT INTO product_source_regions (product_id, source_region_id) VALUES
    (1, 1), (1, 2),       -- Semiconductors: Taiwan, South Korea
    (2, 3), (2, 4),       -- Machinery: Germany, Japan
    (3, 5), (3, 6), (3, 7), -- Grain: USA, Brazil, India
    (4, 8), (4, 9),       -- Textiles: Bangladesh, Vietnam
    (5, 3), (5, 10),      -- Chemicals: Germany, China
    (6, 4), (6, 3), (6, 5), -- Automotive: Japan, Germany, USA
    (7, 10), (7, 11),     -- Solar: China, Malaysia
    (8, 12), (8, 7);      -- Coffee: Colombia, India

-- Product-Tag mappings
INSERT INTO product_tags (product_id, tag_id) VALUES
    (1, 1), (1, 2),       -- Semiconductors: ESD, Climate Controlled
    (2, 3), (2, 6),       -- Machinery: Heavy Cargo, Oversized
    (3, 7),               -- Grain: Bulk Commodity
    (4, 2),               -- Textiles: Climate Controlled
    (5, 4), (5, 5),       -- Chemicals: Hazmat, Temp-Controlled
    (6, 3),               -- Automotive: Heavy Cargo
    (7, 8),               -- Solar: Fragile
    (8, 9), (8, 10);      -- Coffee: Perishable, Organic

-- ─── Carriers ─────────────────────────────────────────────
INSERT INTO carriers (carrier_type_id, name, slug, description, asset_count, asset_label, route_count, route_label, coverage_countries, on_time_rate, rating, sort_order) VALUES
    (1, 'OceanLink Shipping', 'oceanlink-shipping', 'Global maritime logistics leader', 450, 'Vessels', 120, '120 Routes', 85, 98.00, 4.90, 1),
    (2, 'AeroGlobal Freight', 'aeroglobal-freight', 'Premium air freight services worldwide', 85, 'Aircraft', 200, '200+ Destinations', 120, 99.00, 4.80, 2),
    (3, 'TransContinental Logistics', 'transcontinental-logistics', 'Land freight across continents', 3200, 'Trucks', 45, '45 Countries', 45, 97.00, 4.70, 3),
    (1, 'PacificWave Maritime', 'pacificwave-maritime', 'Pacific rim shipping specialist', 280, 'Vessels', 90, '90 Ports', 60, 96.00, 4.70, 4),
    (2, 'EuroConnect Air', 'euroconnect-air', 'European air cargo network', 60, 'Aircraft', 150, '150 Destinations', 80, 99.00, 4.60, 5),
    (4, 'AsiaExpress Rail', 'asiaexpress-rail', 'Trans-Asian rail freight network', 800, 'Rail Cars', 25, '25 Countries', 25, 95.00, 4.60, 6);

INSERT INTO carrier_specializations (carrier_id, name) VALUES
    (1, 'Container Shipping'), (1, 'Bulk Cargo'), (1, 'Refrigerated'),
    (2, 'Express Delivery'), (2, 'Charter Services'), (2, 'Dangerous Goods'),
    (3, 'Full Truckload'), (3, 'Cross-border'), (3, 'Last Mile'),
    (4, 'Trans-Pacific'), (4, 'Breakbulk'), (4, 'RoRo'),
    (5, 'Same-Day Delivery'), (5, 'Pharma Logistics'), (5, 'E-commerce'),
    (6, 'Intermodal'), (6, 'Silk Road Express'), (6, 'Cold Chain');

-- ─── Shipments (sample tracking data) ────────────────────
INSERT INTO shipments (tracking_id, status_id, transport_mode, origin_city, origin_country, dest_city, dest_country, cargo_type, weight_tons, container_count, departure_date, eta) VALUES
    ('TF-2026-48291', 2, 'Maritime', 'Shanghai', 'China', 'Rotterdam', 'Netherlands', 'Electronics', 24500.00, 3, '2026-03-01', '2026-04-02'),
    ('TF-2026-77104', 2, 'Air', 'Dubai', 'UAE', 'New York', 'USA', 'Textiles', 8.20, 0, '2026-03-15', '2026-03-17'),
    ('TF-2026-33059', 4, 'Maritime', 'Mumbai', 'India', 'Hamburg', 'Germany', 'Spices', 12800.00, 2, '2026-02-10', '2026-03-15');

INSERT INTO shipment_milestones (shipment_id, title, location, status, milestone_date, sort_order) VALUES
    -- Shanghai → Rotterdam
    (1, 'Cargo Picked Up', 'Shanghai, China', 'completed', '2026-03-01 08:00:00', 1),
    (1, 'Customs Cleared', 'Shanghai Port', 'completed', '2026-03-02 14:00:00', 2),
    (1, 'Departed Origin', 'Shanghai Port', 'completed', '2026-03-03 06:00:00', 3),
    (1, 'In Transit - Indian Ocean', 'At Sea', 'current', '2026-03-15 12:00:00', 4),
    (1, 'Arrive Rotterdam', 'Rotterdam, Netherlands', 'pending', NULL, 5),
    -- Dubai → New York
    (2, 'Cargo Received', 'Dubai, UAE', 'completed', '2026-03-15 10:00:00', 1),
    (2, 'Departed Dubai', 'DXB Airport', 'completed', '2026-03-15 22:00:00', 2),
    (2, 'In Transit', 'Airborne', 'current', '2026-03-16 06:00:00', 3),
    (2, 'Arrive JFK', 'New York, USA', 'pending', NULL, 4),
    -- Mumbai → Hamburg (Delivered)
    (3, 'Cargo Picked Up', 'Mumbai, India', 'completed', '2026-02-10 09:00:00', 1),
    (3, 'Departed Mumbai', 'Nhava Sheva Port', 'completed', '2026-02-12 06:00:00', 2),
    (3, 'Suez Canal Transit', 'Suez, Egypt', 'completed', '2026-02-28 18:00:00', 3),
    (3, 'Arrived Hamburg', 'Hamburg, Germany', 'completed', '2026-03-14 08:00:00', 4),
    (3, 'Delivered', 'Hamburg, Germany', 'completed', '2026-03-15 16:00:00', 5);

-- ─── Globe City Markers ───────────────────────────────────
INSERT INTO globe_city_markers (city_name, latitude, longitude) VALUES
    ('Shanghai', 31.2304000, 121.4737000),
    ('Singapore', 1.3521000, 103.8198000),
    ('Rotterdam', 51.9244000, 4.4777000),
    ('Dubai', 25.2048000, 55.2708000),
    ('Mumbai', 19.0760000, 72.8777000),
    ('Los Angeles', 34.0522000, -118.2437000),
    ('New York', 40.7128000, -74.0060000),
    ('Santos', -23.9608000, -46.3336000),
    ('Tokyo', 35.6762000, 139.6503000),
    ('Hamburg', 53.5511000, 9.9937000),
    ('Hong Kong', 22.3193000, 114.1694000),
    ('Busan', 35.1796000, 129.0756000),
    ('Cape Town', -33.9249000, 18.4241000),
    ('Sydney', -33.8688000, 151.2093000),
    ('London', 51.5074000, -0.1278000),
    ('Jeddah', 21.4858000, 39.1925000),
    ('Lagos', 6.5244000, 3.3792000),
    ('Vancouver', 49.2827000, -123.1207000);

-- ─── Trade Routes ─────────────────────────────────────────
INSERT INTO trade_routes (origin_city, origin_lat, origin_lng, dest_city, dest_lat, dest_lng, route_color, volume, avg_transit_days) VALUES
    ('Shanghai', 31.2304000, 121.4737000, 'Rotterdam', 51.9244000, 4.4777000, '#00d4ff', 4500, 28.0),
    ('Shanghai', 31.2304000, 121.4737000, 'Los Angeles', 34.0522000, -118.2437000, '#00f5d4', 3800, 14.0),
    ('Singapore', 1.3521000, 103.8198000, 'Dubai', 25.2048000, 55.2708000, '#4d8bff', 2900, 8.0),
    ('Mumbai', 19.0760000, 72.8777000, 'Hamburg', 53.5511000, 9.9937000, '#a855f7', 2200, 22.0),
    ('Dubai', 25.2048000, 55.2708000, 'New York', 40.7128000, -74.0060000, '#ffd700', 1800, 2.0),
    ('Santos', -23.9608000, -46.3336000, 'Rotterdam', 51.9244000, 4.4777000, '#ff8c42', 1500, 18.0),
    ('Tokyo', 35.6762000, 139.6503000, 'Vancouver', 49.2827000, -123.1207000, '#00d4ff', 2100, 10.0),
    ('Hong Kong', 22.3193000, 114.1694000, 'London', 51.5074000, -0.1278000, '#4d8bff', 1900, 32.0),
    ('Busan', 35.1796000, 129.0756000, 'Los Angeles', 34.0522000, -118.2437000, '#a855f7', 2500, 12.0),
    ('Cape Town', -33.9249000, 18.4241000, 'Mumbai', 19.0760000, 72.8777000, '#ffd700', 800, 16.0),
    ('Jeddah', 21.4858000, 39.1925000, 'Singapore', 1.3521000, 103.8198000, '#ff8c42', 1200, 10.0),
    ('Lagos', 6.5244000, 3.3792000, 'Santos', -23.9608000, -46.3336000, '#00f5d4', 600, 14.0);

-- ─── Trusted Partners ─────────────────────────────────────
INSERT INTO trusted_partners (name, logo_url, website_url, sort_order) VALUES
    ('Google', '/images/partners/google.svg', 'https://google.com', 1),
    ('Microsoft', '/images/partners/microsoft.svg', 'https://microsoft.com', 2),
    ('Amazon', '/images/partners/amazon.svg', 'https://amazon.com', 3),
    ('Apple', '/images/partners/apple.svg', 'https://apple.com', 4),
    ('Meta', '/images/partners/meta.svg', 'https://meta.com', 5),
    ('Samsung', '/images/partners/samsung.svg', 'https://samsung.com', 6);

-- ─── Monthly Analytics (2025 & 2026) ─────────────────────
INSERT INTO monthly_analytics (year, month, total_shipments, revenue, avg_transit_days, delivery_rate) VALUES
    (2025, 1, 8200, 145000000.00, 12.5, 97.80),
    (2025, 2, 8500, 152000000.00, 12.3, 97.90),
    (2025, 3, 9100, 163000000.00, 12.1, 98.00),
    (2025, 4, 9400, 170000000.00, 11.9, 98.10),
    (2025, 5, 9800, 178000000.00, 11.8, 98.20),
    (2025, 6, 10200, 185000000.00, 11.6, 98.30),
    (2025, 7, 10500, 190000000.00, 11.5, 98.40),
    (2025, 8, 10800, 195000000.00, 11.4, 98.50),
    (2025, 9, 11100, 200000000.00, 11.3, 98.60),
    (2025, 10, 11400, 208000000.00, 11.2, 98.70),
    (2025, 11, 11800, 215000000.00, 11.1, 98.80),
    (2025, 12, 12100, 222000000.00, 11.0, 98.90),
    (2026, 1, 9500, 168000000.00, 11.8, 98.50),
    (2026, 2, 9900, 175000000.00, 11.5, 98.70),
    (2026, 3, 10500, 188000000.00, 11.2, 98.90);

-- ─── Revenue by Region ────────────────────────────────────
INSERT INTO revenue_by_region (region_name, revenue, percentage, year, color) VALUES
    ('Asia Pacific', 820000000.00, 35.20, 2026, '#00d4ff'),
    ('Europe', 580000000.00, 24.90, 2026, '#4d8bff'),
    ('North America', 490000000.00, 21.00, 2026, '#a855f7'),
    ('Middle East', 210000000.00, 9.00, 2026, '#ffd700'),
    ('South America', 140000000.00, 6.00, 2026, '#ff8c42'),
    ('Africa', 90000000.00, 3.90, 2026, '#00f5d4');

-- ─── Commodity Performance ────────────────────────────────
INSERT INTO commodity_performance (category_id, revenue, revenue_share, growth_percent, year) VALUES
    (1, 480000000.00, 26.30, 14.00, 2026),
    (2, 350000000.00, 19.20, 8.00, 2026),
    (6, 310000000.00, 17.00, 5.00, 2026),
    (5, 280000000.00, 15.40, 2.00, 2026),
    (3, 220000000.00, 12.10, 11.00, 2026),
    (4, 190000000.00, 10.00, 3.00, 2026);

-- ─── Banners ──────────────────────────────────────────────
INSERT INTO banners (title, subtitle, cta_text, cta_link, position, is_active, sort_order) VALUES
    ('Global Trade, Reimagined', 'Real-time visibility across your entire supply chain. Track, manage, and optimize shipments worldwide.', 'Explore Routes', '#tracking', 'hero', true, 1);
