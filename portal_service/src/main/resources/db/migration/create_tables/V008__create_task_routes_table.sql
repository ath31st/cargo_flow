CREATE TABLE task_routes
(
    route_id    SERIAL PRIMARY KEY,
    task_id    INT NOT NULL REFERENCES tasks (task_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_time TIMESTAMP,
    end_time   TIMESTAMP
);
