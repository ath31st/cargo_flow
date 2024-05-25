CREATE TABLE company_employees
(
    company_employee_id SERIAL PRIMARY KEY,
    user_keycloak_id    VARCHAR(100) NOT NULL,
    role_id             INT,
    company_id          INT REFERENCES companies (company_id),
    CONSTRAINT unique_user_company_employee UNIQUE (user_keycloak_id, company_id)
);