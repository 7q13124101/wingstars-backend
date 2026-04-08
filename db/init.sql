CREATE TABLE IF NOT EXISTS `roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL, 
    `code` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
    UNIQUE KEY `uk_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `full_name` VARCHAR(255),
    `phone` VARCHAR(20), 
    `gender` ENUM('MALE', 'FEMALE', 'OTHER'), 
    `address` VARCHAR(255),
    `image_url` VARCHAR(500),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `role_id` BIGINT NOT NULL,
    `is_deleted` TINYINT(1) DEFAULT 0,
    PRIMARY KEY(`id`),
    INDEX `idx_role_id` (`role_id`),
    CONSTRAINT `fk_user_role`
        FOREIGN KEY (`role_id`)
        REFERENCES `roles` (`id`)
        ON DELETE RESTRICT
        ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `refresh_tokens` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `token` VARCHAR(512) NOT NULL UNIQUE , 
    `expiry_date` DATETIME NOT NULL,
    `revoked` TINYINT(1) DEFAULT 0, 
    PRIMARY KEY(`id`),
    CONSTRAINT `fk_token_user`
        FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`)
        ON DELETE CASCADE,
    INDEX `idx_expiry_date` (`expiry_date`),
    INDEX `idx_user_revoked` (`user_id`, `revoked`)    
);

CREATE TABLE IF NOT EXISTS `cheerleaders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(255) NOT NULL,
  `avatar_url` VARCHAR(500),
  `jersey_number` VARCHAR(10),
  `facebook_url` VARCHAR(500),
  `instagram_url` VARCHAR(500),
  `exclusive_audio_url` VARCHAR(500),
  `photo_frame_url` VARCHAR(500),
  `about_cheerleader` TEXT,
  `message_to_fans` TEXT,
  `hobbies` VARCHAR(255),
  `height_cm` SMALLINT,
  `weight_kg` SMALLINT,
  `birth_date` DATE,
  `zodiac_sign` VARCHAR(50),
  `blood_type` VARCHAR(10),
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `idx_full_name` (`full_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `cheerleader_ranking_categories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `type_code` VARCHAR(100) UNIQUE,
  `status` TINYINT(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `media_assets` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `file_url` VARCHAR(500) NOT NULL UNIQUE,
  `module_source` VARCHAR(50) COMMENT 'BANNER, CHEERLEADER, NEWS...',
  `title` VARCHAR(255),
  `jump_url` VARCHAR(500),
  `display_order` SMALLINT DEFAULT 0,
  `is_active` TINYINT(1) DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `idx_module_active` (`module_source`, `is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `cheerleader_ranking_entries` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ranking_category_id` BIGINT NOT NULL,
  `cheerleader_id` BIGINT NOT NULL,
  `rank_position` SMALLINT,
  `cheerleader_image_url` VARCHAR(500),
  `score` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_cheerleader` (`ranking_category_id`, `cheerleader_id`),
  INDEX `idx_category` (`ranking_category_id`),
  INDEX `idx_cheerleader` (`cheerleader_id`),
  CONSTRAINT `fk_ranking_category_id`
    FOREIGN KEY (`ranking_category_id`)
    REFERENCES `cheerleader_ranking_categories` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_ranking_cheerleader_id`
    FOREIGN KEY (`cheerleader_id`)
    REFERENCES `cheerleaders` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `banners` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `image_url` VARCHAR(500) NOT NULL COMMENT 'Link from Media Service',
    `link_url` VARCHAR(500) COMMENT 'url to redirect when banner is clicked',
    `position_code` VARCHAR(50) NOT NULL COMMENT 'HOME_TOP, EVENT_POPUP, v.v.',
    `display_order` SMALLINT DEFAULT 0,
    `status` TINYINT(1) DEFAULT 1 COMMENT '1: on, 0: off',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_deleted` TINYINT(1) DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `idx_position_status` (`position_code`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (name, code, description) VALUES
('USER', 'USER', 'Regular user'),
('ADMIN', 'ADMIN', 'System administrator (excluding users)'),
('SUPER_ADMIN', 'SUPER_ADMIN', 'Full access');

