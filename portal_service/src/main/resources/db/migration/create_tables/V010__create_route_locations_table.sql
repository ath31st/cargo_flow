CREATE TABLE route_locations
(
    location_id SERIAL PRIMARY KEY,
    route_id    INT           NOT NULL REFERENCES task_routes (route_id),
    latitude    DECIMAL(9, 6) NOT NULL,
    longitude   DECIMAL(9, 6) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

