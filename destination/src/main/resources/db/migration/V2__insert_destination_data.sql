-- Insertar destinos de prueba
INSERT INTO destinations (id, name, country, description) VALUES
(UUID_TO_BIN(UUID()), 'Santiago', 'Chile', 'Capital de Chile, conocida por su vida urbana y cercanía a la cordillera.'),
(UUID_TO_BIN(UUID()), 'Valparaíso', 'Chile', 'Ciudad portuaria famosa por sus cerros coloridos y patrimonio cultural.');

-- Insertar categorías de prueba
INSERT INTO categories (id, name) VALUES
(UUID_TO_BIN(UUID()), 'Montaña'),
(UUID_TO_BIN(UUID()), 'Ciudad'),
(UUID_TO_BIN(UUID()), 'Playa');

-- Relacionar destinos con categorías
INSERT INTO destination_categories (id, destination_id, category_id)
SELECT UUID_TO_BIN(UUID()), d.id, c.id
FROM destinations d, categories c
WHERE d.name = 'Santiago' AND c.name = 'Ciudad';

INSERT INTO destination_categories (id, destination_id, category_id)
SELECT UUID_TO_BIN(UUID()), d.id, c.id
FROM destinations d, categories c
WHERE d.name = 'Valparaíso' AND c.name = 'Playa';
