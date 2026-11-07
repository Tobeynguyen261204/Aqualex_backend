ALTER TABLE generation_requests
    DROP CONSTRAINT chk_generation_requests_status;

ALTER TABLE generation_requests
    ADD CONSTRAINT chk_generation_requests_status
        CHECK (status IN (
            'ANALYZING',
            'NEEDS_CONFIRMATION',
            'GENERATING',
            'COMPLETED',
            'FAILED',
            'CANCELLED',
            'FINALIZED'
        ));
