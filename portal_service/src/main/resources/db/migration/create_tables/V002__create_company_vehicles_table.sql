CREATE TABLE company_vehicles
(
    company_vehicle_id SERIAL PRIMARY KEY,
    vin                VARCHAR(17) UNIQUE NOT NULL,
    year               INT                NOT NULL,
    company_id         INT REFERENCES companies (company_id)
);