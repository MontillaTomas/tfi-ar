-- User

CREATE TABLE IF NOT EXISTS user_log (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_user_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO user_log (user_id, email, password, role, deleted, action, action_date)
    VALUES (NEW.id, NEW.email, NEW.password, NEW.role, NEW.deleted, 'I', NOW());
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_user_insert
AFTER INSERT ON user_
FOR EACH ROW
EXECUTE FUNCTION log_user_insert();

-- Sale

CREATE TABLE IF NOT EXISTS sale_log (
    id SERIAL PRIMARY KEY,
    sale_id INTEGER NOT NULL,
    observation VARCHAR(255),
    sale_date TIMESTAMP(6) NOT NULL,
    client_id INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_sale_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO sale_log (sale_id, observation, sale_date, client_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.observation, NEW.sale_date, NEW.client_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_sale_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO sale_log (sale_id, observation, sale_date, client_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.sale_date, NEW.client_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO sale_log (sale_id, observation, sale_date, client_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.sale_date, NEW.client_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_sale_insert
AFTER INSERT ON sale
FOR EACH ROW
EXECUTE FUNCTION log_sale_insert();

CREATE TRIGGER after_sale_update
AFTER UPDATE ON sale
FOR EACH ROW
EXECUTE FUNCTION log_sale_update();

-- Invoice

CREATE TABLE IF NOT EXISTS invoice_log (
    id SERIAL PRIMARY KEY,
    invoice_id INTEGER NOT NULL,
    observation VARCHAR(255),
    due_date DATE NOT NULL,
    issue_date DATE NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    state VARCHAR(255) CHECK (state IN ('PENDING','PAID','CANCELED')),
    sale_id INTEGER NOT NULL,
    deleted boolean not null,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_invoice_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO invoice_log (invoice_id, observation, due_date, issue_date, payment_method, state, sale_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.observation, NEW.due_date, NEW.issue_date, NEW.payment_method, NEW.state, NEW.sale_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_invoice_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO invoice_log (invoice_id, observation, due_date, issue_date, payment_method, state, sale_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.due_date, NEW.issue_date, NEW.payment_method, NEW.state, NEW.sale_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO invoice_log (invoice_id, observation, due_date, issue_date, payment_method, state, sale_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.due_date, NEW.issue_date, NEW.payment_method, NEW.state, NEW.sale_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_invoice_insert
AFTER INSERT ON invoice
FOR EACH ROW
EXECUTE FUNCTION log_invoice_insert();

CREATE TRIGGER after_invoice_update
AFTER UPDATE ON invoice
FOR EACH ROW
EXECUTE FUNCTION log_invoice_update();

-- Invoice Detail

CREATE TABLE IF NOT EXISTS invoice_detail_log (
    id SERIAL PRIMARY KEY,
    invoice_detail_id INTEGER NOT NULL,
    invoice_id INTEGER NOT NULL,
    description VARCHAR(255),
    quantity FLOAT(53) NOT NULL,
    unit_price FLOAT(53) NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_invoice_detail_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO invoice_detail_log (invoice_detail_id, invoice_id, description, quantity, unit_price, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.invoice_id, NEW.description, NEW.quantity, NEW.unit_price, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_invoice_detail_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO invoice_detail_log (invoice_detail_id, invoice_id, description, quantity, unit_price, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.invoice_id, NEW.description, NEW.quantity, NEW.unit_price, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO invoice_detail_log (invoice_detail_id, invoice_id, description, quantity, unit_price, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.invoice_id, NEW.description, NEW.quantity, NEW.unit_price, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_invoice_detail_insert
AFTER INSERT ON invoice_detail
FOR EACH ROW
EXECUTE FUNCTION log_invoice_detail_insert();

CREATE TRIGGER after_invoice_detail_update
AFTER UPDATE ON invoice_detail
FOR EACH ROW
EXECUTE FUNCTION log_invoice_detail_update();

-- Address

CREATE TABLE IF NOT EXISTS address_log (
    id SERIAL PRIMARY KEY,
    address_id INTEGER NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(255) NOT NULL,
    floor VARCHAR(255),
    apartment VARCHAR(255),
    postal_code VARCHAR(255) NOT NULL,
    city_id INTEGER NOT NULL,
    observations VARCHAR(255),
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_address_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO address_log (address_id, street, number, floor, apartment, postal_code, city_id, observations, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.street, NEW.number, NEW.floor, NEW.apartment, NEW.postal_code, NEW.city_id, NEW.observations, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_address_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO address_log (address_id, street, number, floor, apartment, postal_code, city_id, observations, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.street, NEW.number, NEW.floor, NEW.apartment, NEW.postal_code, NEW.city_id, NEW.observations, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO address_log (address_id, street, number, floor, apartment, postal_code, city_id, observations, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.street, NEW.number, NEW.floor, NEW.apartment, NEW.postal_code, NEW.city_id, NEW.observations, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_address_insert
AFTER INSERT ON address
FOR EACH ROW
EXECUTE FUNCTION log_address_insert();

CREATE TRIGGER after_address_update
AFTER UPDATE ON address
FOR EACH ROW
EXECUTE FUNCTION log_address_update();

-- Employee

CREATE TABLE IF NOT EXISTS employee_log (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    dni INTEGER,
    email VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(255),
    birth_date DATE,
    start_date DATE,
    end_date DATE,
    address_id INTEGER NOT NULL,
    user_id INTEGER,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_employee_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO employee_log (employee_id, dni, email, name, phone, birth_date, start_date, end_date, address_id, user_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.dni, NEW.email, NEW.name, NEW.phone, NEW.birth_date, NEW.start_date, NEW.end_date, NEW.address_id, NEW.user_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_employee_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO employee_log (employee_id, dni, email, name, phone, birth_date, start_date, end_date, address_id, user_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.dni, NEW.email, NEW.name, NEW.phone, NEW.birth_date, NEW.start_date, NEW.end_date, NEW.address_id, NEW.user_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO employee_log (employee_id, dni, email, name, phone, birth_date, start_date, end_date, address_id, user_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.dni, NEW.email, NEW.name, NEW.phone, NEW.birth_date, NEW.start_date, NEW.end_date, NEW.address_id, NEW.user_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_employee_insert
AFTER INSERT ON employee
FOR EACH ROW
EXECUTE FUNCTION log_employee_insert();

CREATE TRIGGER after_employee_update
AFTER UPDATE ON employee
FOR EACH ROW
EXECUTE FUNCTION log_employee_update();

-- Client

CREATE TABLE IF NOT EXISTS client_log (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL,
    contact_name VARCHAR(255),
    email VARCHAR(255),
    estimated_transactions_number INTEGER,
    industry VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(255),
    remarks VARCHAR(255),
    technologies_used VARCHAR(255),
    address_id INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_client_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO client_log (client_id, contact_name, email, estimated_transactions_number, industry, name, phone, remarks, technologies_used, address_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.contact_name, NEW.email, NEW.estimated_transactions_number, NEW.industry, NEW.name, NEW.phone, NEW.remarks, NEW.technologies_used, NEW.address_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_client_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO client_log (client_id, contact_name, email, estimated_transactions_number, industry, name, phone, remarks, technologies_used, address_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.contact_name, NEW.email, NEW.estimated_transactions_number, NEW.industry, NEW.name, NEW.phone, NEW.remarks, NEW.technologies_used, NEW.address_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO client_log (client_id, contact_name, email, estimated_transactions_number, industry, name, phone, remarks, technologies_used, address_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.contact_name, NEW.email, NEW.estimated_transactions_number, NEW.industry, NEW.name, NEW.phone, NEW.remarks, NEW.technologies_used, NEW.address_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_client_insert
AFTER INSERT ON client
FOR EACH ROW
EXECUTE FUNCTION log_client_insert();

CREATE TRIGGER after_client_update
AFTER UPDATE ON client
FOR EACH ROW
EXECUTE FUNCTION log_client_update();

-- Client Interaction

CREATE TABLE IF NOT EXISTS client_interaction_log (
    id SERIAL PRIMARY KEY,
    client_interaction_id INTEGER NOT NULL,
    date TIMESTAMP(6) NOT NULL,
    details VARCHAR(255),
    client_id INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_client_interaction_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO client_interaction_log (client_interaction_id, date, details, client_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.date, NEW.details, NEW.client_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_client_interaction_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO client_interaction_log (client_interaction_id, date, details, client_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.date, NEW.details, NEW.client_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO client_interaction_log (client_interaction_id, date, details, client_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.date, NEW.details, NEW.client_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_client_interaction_insert
AFTER INSERT ON client_interaction
FOR EACH ROW
EXECUTE FUNCTION log_client_interaction_insert();

CREATE TRIGGER after_client_interaction_update
AFTER UPDATE ON client_interaction
FOR EACH ROW
EXECUTE FUNCTION log_client_interaction_update();

-- Supplier

CREATE TABLE IF NOT EXISTS supplier_log (
    id SERIAL PRIMARY KEY,
    supplier_id INTEGER NOT NULL,
    cuit VARCHAR(255),
    email VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(255),
    address_id INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_supplier_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO supplier_log (supplier_id, cuit, email, name, phone, address_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.cuit, NEW.email, NEW.name, NEW.phone, NEW.address_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_supplier_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO supplier_log (supplier_id, cuit, email, name, phone, address_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.cuit, NEW.email, NEW.name, NEW.phone, NEW.address_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO supplier_log (supplier_id, cuit, email, name, phone, address_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.cuit, NEW.email, NEW.name, NEW.phone, NEW.address_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_supplier_insert
AFTER INSERT ON supplier
FOR EACH ROW
EXECUTE FUNCTION log_supplier_insert();

CREATE TRIGGER after_supplier_update
AFTER UPDATE ON supplier
FOR EACH ROW
EXECUTE FUNCTION log_supplier_update();

-- Payment Condition

CREATE TABLE IF NOT EXISTS payment_condition_log (
    id SERIAL PRIMARY KEY,
    payment_condition_id INTEGER NOT NULL,
    payment_method VARCHAR(255),
    payment_term_days VARCHAR(255),
    currency VARCHAR(255),
    bank VARCHAR(255),
    account_number VARCHAR(255),
    observation VARCHAR(255),
    supplier_id INTEGER,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_payment_condition_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO payment_condition_log (payment_condition_id, payment_method, payment_term_days, currency, bank, account_number, observation, supplier_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.payment_method, NEW.payment_term_days, NEW.currency, NEW.bank, NEW.account_number, NEW.observation, NEW.supplier_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_payment_condition_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO payment_condition_log (payment_condition_id, payment_method, payment_term_days, currency, bank, account_number, observation, supplier_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.payment_method, NEW.payment_term_days, NEW.currency, NEW.bank, NEW.account_number, NEW.observation, NEW.supplier_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO payment_condition_log (payment_condition_id, payment_method, payment_term_days, currency, bank, account_number, observation, supplier_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.payment_method, NEW.payment_term_days, NEW.currency, NEW.bank, NEW.account_number, NEW.observation, NEW.supplier_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_payment_condition_insert
AFTER INSERT ON payment_condition
FOR EACH ROW
EXECUTE FUNCTION log_payment_condition_insert();

CREATE TRIGGER after_payment_condition_update
AFTER UPDATE ON payment_condition
FOR EACH ROW
EXECUTE FUNCTION log_payment_condition_update();

-- Purchase

CREATE TABLE IF NOT EXISTS purchase_log (
    id SERIAL PRIMARY KEY,
    purchase_id INTEGER NOT NULL,
    total FLOAT(53) NOT NULL,
    purchase_date TIMESTAMP(6),
    observation VARCHAR(255),
    supplier_id INTEGER NOT NULL,
    payment_condition_id INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_purchase_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO purchase_log (purchase_id, total, purchase_date, observation, supplier_id, payment_condition_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.total, NEW.purchase_date, NEW.observation, NEW.supplier_id, NEW.payment_condition_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_purchase_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO purchase_log (purchase_id, total, purchase_date, observation, supplier_id, payment_condition_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.total, NEW.purchase_date, NEW.observation, NEW.supplier_id, NEW.payment_condition_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO purchase_log (purchase_id, total, purchase_date, observation, supplier_id, payment_condition_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.total, NEW.purchase_date, NEW.observation, NEW.supplier_id, NEW.payment_condition_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_purchase_insert
AFTER INSERT ON purchase
FOR EACH ROW
EXECUTE FUNCTION log_purchase_insert();

CREATE TRIGGER after_purchase_update
AFTER UPDATE ON purchase
FOR EACH ROW
EXECUTE FUNCTION log_purchase_update();

-- Purchase Rating

CREATE TABLE IF NOT EXISTS purchase_rating_log (
    id SERIAL PRIMARY KEY,
    purchase_rating_id INTEGER NOT NULL,
    rating INTEGER NOT NULL,
    observation VARCHAR(255),
    purchase_id INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_by INTEGER NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_purchase_rating_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO purchase_rating_log (purchase_rating_id, rating, observation, purchase_id, action_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.rating, NEW.observation, NEW.purchase_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_purchase_rating_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO purchase_rating_log (purchase_rating_id, rating, observation, purchase_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.rating, NEW.observation, NEW.purchase_id, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO purchase_rating_log (purchase_rating_id, rating, observation, purchase_id, action_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.rating, NEW.observation, NEW.purchase_id, NEW.updated_by, NEW.deleted, 'U', NOW());
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_purchase_rating_insert
AFTER INSERT ON purchase_rating
FOR EACH ROW
EXECUTE FUNCTION log_purchase_rating_insert();

CREATE TRIGGER after_purchase_rating_update
AFTER UPDATE ON purchase_rating
FOR EACH ROW
EXECUTE FUNCTION log_purchase_rating_update();