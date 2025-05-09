ALTER TABLE schedule
    ADD color VARCHAR(255) NULL;

ALTER TABLE "day"
    DROP COLUMN test;

ALTER TABLE bit_o_user
    DROP COLUMN platform;

ALTER TABLE bit_o_user
    ADD platform VARCHAR(255) NULL;
