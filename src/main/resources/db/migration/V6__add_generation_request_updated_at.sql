ALTER TABLE generation_requests
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE;

UPDATE generation_requests
SET updated_at = created_at
WHERE updated_at IS NULL;

ALTER TABLE generation_requests
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE generation_requests
    ADD CONSTRAINT chk_generation_requests_status
        CHECK (status IN (
            'ANALYZING',
            'NEEDS_CONFIRMATION',
            'GENERATING',
            'COMPLETED',
            'FAILED'
        ));
