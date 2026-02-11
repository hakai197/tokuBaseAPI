CREATE TABLE series (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        type ENUM('RIDER', 'SENTAI') NOT NULL,
                        year_start INT NOT NULL,
                        year_end INT,
                        description TEXT,
                        UNIQUE(name)
);
CREATE TABLE character (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,
                           series_id BIGINT NOT NULL,
                           role ENUM('MAIN', 'SECONDARY', 'EXTRA') NOT NULL,
                           color VARCHAR(50),
                           FOREIGN KEY (series_id) REFERENCES series(id)
);
CREATE TABLE form (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      character_id BIGINT NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      power_type VARCHAR(100),
                      is_final_form BOOLEAN DEFAULT FALSE,
                      FOREIGN KEY (character_id) REFERENCES character(id)
);
CREATE TABLE episode (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         series_id BIGINT NOT NULL,
                         episode_number INT NOT NULL,
                         title VARCHAR(255) NOT NULL,
                         air_date DATE,
                         FOREIGN KEY (series_id) REFERENCES series(id),
                         UNIQUE(series_id, episode_number)
);
