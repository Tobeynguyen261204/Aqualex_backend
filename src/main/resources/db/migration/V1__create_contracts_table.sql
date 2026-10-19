CREATE TABLE contracts (
    id UUID PRIMARY KEY,
    exporter_name VARCHAR(255) NOT NULL,
    importer_name VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity NUMERIC(15, 3) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    unit_price NUMERIC(15, 2) NOT NULL,
    incoterm VARCHAR(20) NOT NULL,
    departure_port VARCHAR(255) NOT NULL,
    arrival_port VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);