
CREATE TABLE trips (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    destination_id BINARY(16) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    travel_type VARCHAR(50) NOT NULL,
    companions INT DEFAULT 0,
    notes TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE trip_details (
    id BINARY(16) NOT NULL,
    trip_id BINARY(16) NOT NULL,
    detail_key VARCHAR(100) NOT NULL,
    detail_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_trip FOREIGN KEY (trip_id) REFERENCES trips(id)
);

/*

************************************************************************

Estos son datos de ejemplo para poblar las tablas de viajes y detalles de viajes. Pero no seran validos ya que no conocemos los UUID de los usuarios y destinos. 
Por lo tanto, se recomienda utilizarlos solo como referencia.

************************************************************************

-- Insertar viajes de ejemplo
INSERT INTO trips (id, user_id, destination_id, start_date, end_date, travel_type, companions, notes)
VALUES
(UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), '2026-07-01', '2026-07-10', 'Leisure', 2, 'Vacaciones familiares en la playa'),
(UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), '2026-08-15', '2026-08-20', 'Business', 0, 'Viaje de negocios para asistir a conferencia'),
(UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), '2026-09-05', '2026-09-12', 'Leisure', 4, 'Tour cultural con amigos en Europa');

-- Insertar detalles de viajes de ejemplo
INSERT INTO trip_details (id, trip_id, detail_key, detail_value)
VALUES
(UUID_TO_BIN(UUID()), (SELECT id FROM trips LIMIT 1), 'Transport', 'Avión'),
(UUID_TO_BIN(UUID()), (SELECT id FROM trips LIMIT 1), 'Budget', '2000 USD'),
(UUID_TO_BIN(UUID()), (SELECT id FROM trips LIMIT 2), 'Activity', 'Conferencia de tecnología'),
(UUID_TO_BIN(UUID()), (SELECT id FROM trips LIMIT 3), 'Transport', 'Tren'),
(UUID_TO_BIN(UUID()), (SELECT id FROM trips LIMIT 3), 'Activity', 'Visita a museos');

*/