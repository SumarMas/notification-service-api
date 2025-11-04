-- ============================================================
-- DATABASE: notification_service
-- Description: Schema, audit tables and triggers for notification-service
-- ============================================================

CREATE DATABASE IF NOT EXISTS notification_service CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE notification_service;

-- ============================================================
-- 1) TABLE: notifications
-- ============================================================
CREATE TABLE IF NOT EXISTS notifications (
                                             notification_id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL, -- ej: ORGANIZATION_VERIFIED, DONATION_SUCCESS, etc.
    `read` BOOLEAN DEFAULT FALSE,

    enabled BOOLEAN DEFAULT TRUE,
    created_datetime DATETIME DEFAULT NOW(),
    created_user BINARY(16),
    last_updated_datetime DATETIME DEFAULT NOW(),
    last_updated_user BINARY(16),

    PRIMARY KEY (notification_id),
    KEY idx_notifications_user (user_id),
    KEY idx_notifications_read (`read`),
    KEY idx_notifications_type (type)
    );

-- ============================================================
-- 2) TABLE: notifications_audit (mirror + version)
-- ============================================================
CREATE TABLE IF NOT EXISTS notifications_audit LIKE notifications;
ALTER TABLE notifications_audit
    ADD COLUMN version INT NOT NULL,
DROP PRIMARY KEY,
    ADD PRIMARY KEY (notification_id, version);

-- ============================================================
-- 3) TRIGGERS
--    - after_insert: guarda versión 1 en audit
--    - before_update: mantiene created_*, actualiza last_updated_datetime y guarda nueva versión
-- ============================================================
DELIMITER $$

DROP TRIGGER IF EXISTS after_insert_notifications $$
CREATE TRIGGER after_insert_notifications
    AFTER INSERT ON notifications
    FOR EACH ROW
BEGIN
    INSERT INTO notifications_audit
    SELECT n.*, 1 AS version
    FROM notifications n
    WHERE n.notification_id = NEW.notification_id;
END $$

DROP TRIGGER IF EXISTS before_update_notifications $$
CREATE TRIGGER before_update_notifications
    BEFORE UPDATE ON notifications
    FOR EACH ROW
BEGIN
    DECLARE last_version INT;

    -- Mantener campos de creación originales
    SET NEW.created_user = OLD.created_user;
    SET NEW.created_datetime = OLD.created_datetime;

    -- Actualizar campo de última modificación
    SET NEW.last_updated_datetime = NOW();

    -- Obtener última versión desde audit
    SELECT COALESCE(MAX(na.version), 0)
    INTO last_version
    FROM notifications_audit na
    WHERE na.notification_id = NEW.notification_id;

    -- Insertar snapshot con nueva versión
    INSERT INTO notifications_audit
    SELECT n.*, last_version + 1 AS version
    FROM notifications n
    WHERE n.notification_id = NEW.notification_id;
END $$

DELIMITER ;

-- ============================================================
-- LISTO
-- ============================================================
