ALTER TABLE company_vehicles
    ADD COLUMN license_plate VARCHAR(20);

UPDATE company_vehicles
SET license_plate = 'A' || LPAD(company_vehicle_id::text, 3, '0') || 'AА 001rus';

ALTER TABLE company_vehicles
    ADD CONSTRAINT unique_license_plate UNIQUE (license_plate);
