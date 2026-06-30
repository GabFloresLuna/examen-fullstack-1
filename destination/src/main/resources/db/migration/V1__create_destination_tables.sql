CREATE TABLE destinations (
    id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    description TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE categories (
    id BINARY(16) NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE destination_categories (
    id BINARY(16) NOT NULL,
    destination_id BINARY(16) NOT NULL,
    category_id BINARY(16) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_destination FOREIGN KEY (destination_id) REFERENCES destinations(id),
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
