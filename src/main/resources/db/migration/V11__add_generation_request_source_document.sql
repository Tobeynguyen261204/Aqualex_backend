ALTER TABLE generation_requests
    ADD COLUMN source_file_name VARCHAR(255),
    ADD COLUMN source_text TEXT;
