ALTER TABLE generation_requests
    ADD COLUMN extracted_data TEXT,
    ADD COLUMN missing_fields TEXT,
    ADD COLUMN warnings TEXT;
