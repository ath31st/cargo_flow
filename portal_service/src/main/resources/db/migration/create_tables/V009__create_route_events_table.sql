CREATE TABLE route_events
(
    event_id   SERIAL PRIMARY KEY,
    route_id   INT NOT NULL REFERENCES task_routes (route_id),
    event_type INT NOT NULL,
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

