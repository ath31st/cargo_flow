CREATE TABLE companies
(
    company_id      SERIAL PRIMARY KEY,
    name    VARCHAR(200)       NOT NULL,
    inn     VARCHAR(12) UNIQUE NOT NULL,
    address VARCHAR(500)       NOT NULL,
    kpp     VARCHAR(9)         NOT NULL,
    ogrn    VARCHAR(13)        NOT NULL
);