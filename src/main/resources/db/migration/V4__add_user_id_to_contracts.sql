ALTER TABLE contracts
    ADD COLUMN user_id UUID;

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE;

CREATE INDEX idx_contracts_user_id
    ON contracts(user_id);
