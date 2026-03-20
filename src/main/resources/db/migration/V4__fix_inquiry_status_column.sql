-- Fix inquiries.status from ENUM to VARCHAR so JPA converter works correctly
ALTER TABLE inquiries MODIFY COLUMN status VARCHAR(15) NOT NULL DEFAULT 'new';
