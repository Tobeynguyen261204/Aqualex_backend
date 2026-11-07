CREATE TABLE generation_requests (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_generation_requests_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_generation_requests_user_id
    ON generation_requests(user_id);
