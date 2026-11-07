ALTER TABLE generation_requests
    ADD COLUMN draft_content TEXT,
    ADD COLUMN draft_language VARCHAR(10),
    ADD COLUMN draft_version INTEGER NOT NULL DEFAULT 1;
