CREATE TABLE tasks
(
    task_id            SERIAL PRIMARY KEY,
    start_point        VARCHAR(255) NOT NULL,
    end_point          VARCHAR(255) NOT NULL,
    driver_keycloak_id VARCHAR(40)  NOT NULL,
    cargo_description  TEXT         NOT NULL,
    company_vehicle_id INT          NOT NULL REFERENCES company_vehicles (company_vehicle_id),
    company_id         INT          NOT NULL REFERENCES companies (company_id),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
