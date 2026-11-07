ALTER TABLE generation_requests
    ADD COLUMN review_result TEXT,
    ADD COLUMN review_at TIMESTAMP WITH TIME ZONE;