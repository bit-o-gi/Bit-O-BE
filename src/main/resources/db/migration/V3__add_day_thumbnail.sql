ALTER TABLE `day`
    ADD thumbnail_url VARCHAR(255) NULL;

CREATE TABLE day_file
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    day_id             BIGINT                NULL,
    original_file_name VARCHAR(255)          NULL,
    uuid_file_name     VARCHAR(255)          NULL,
    content_type       VARCHAR(255)          NULL,
    file_size          BIGINT                NOT NULL,
    CONSTRAINT pk_dayfile PRIMARY KEY (id)
);

ALTER TABLE day_file
    ADD CONSTRAINT uc_dayfile_day UNIQUE (day_id);
