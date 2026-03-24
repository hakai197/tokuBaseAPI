-- TokuBase Database Schema
-- Table names match JPA @Table annotations: series, characters, forms, episodes

CREATE TABLE IF NOT EXISTS series (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL UNIQUE,
    type            ENUM('RIDER','SENTAI') NOT NULL,
    year_start      INT NOT NULL,
    year_end        INT,
    description     TEXT,
    -- Image fields
    logo_base64          LONGTEXT,
    poster_base64        LONGTEXT,
    banner_base64        LONGTEXT,
    series_image_base64  LONGTEXT,
    thumbnail_base64     LONGTEXT,
    series_image_url     VARCHAR(500),
    INDEX idx_series_type (type),
    INDEX idx_series_year (year_start)
);

-- NOTE: 'character' is a reserved word in MySQL → table named 'characters'
CREATE TABLE IF NOT EXISTS characters (
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                    VARCHAR(255) NOT NULL,
    series_id               BIGINT NOT NULL,
    role                    ENUM('MAIN','SECONDARY','EXTRA','SIXTH') NOT NULL,
    color                   VARCHAR(50),
    -- Image fields
    character_image_base64  LONGTEXT,
    portrait_base64         LONGTEXT,
    thumbnail_base64        LONGTEXT,
    character_image_url     VARCHAR(500),
    CONSTRAINT fk_character_series FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE CASCADE,
    INDEX idx_character_series (series_id),
    INDEX idx_character_role   (role)
);

CREATE TABLE IF NOT EXISTS forms (
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    character_id        BIGINT NOT NULL,
    name                VARCHAR(255) NOT NULL,
    power_type          VARCHAR(100),
    is_final_form       BOOLEAN DEFAULT FALSE,
    -- Image fields
    form_image_base64   LONGTEXT,
    icon_base64         LONGTEXT,
    thumbnail_base64    LONGTEXT,
    form_image_url      VARCHAR(500),
    CONSTRAINT fk_form_character FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE,
    INDEX idx_form_character   (character_id),
    INDEX idx_form_final_form  (is_final_form)
);

CREATE TABLE IF NOT EXISTS episodes (
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    series_id               BIGINT NOT NULL,
    episode_number          INT NOT NULL,
    title                   VARCHAR(255) NOT NULL,
    air_date                DATE,
    -- Image fields
    episode_image_base64    LONGTEXT,
    thumbnail_base64        LONGTEXT,
    episode_image_url       VARCHAR(500),
    CONSTRAINT fk_episode_series FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE CASCADE,
    UNIQUE KEY uq_series_episode (series_id, episode_number),
    INDEX idx_episode_series   (series_id),
    INDEX idx_episode_air_date (air_date)
);

