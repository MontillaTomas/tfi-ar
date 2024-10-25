-- Sale

CREATE TABLE IF NOT EXISTS sale_log (
    id SERIAL PRIMARY KEY,
    sale_id INTEGER NOT NULL,
    observation VARCHAR(255),
    sale_date TIMESTAMP(6) NOT NULL,
    created_by INTEGER NOT NULL,
    updated_by INTEGER,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_sale_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO sale_log (sale_id, observation, sale_date, created_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.observation, NEW.sale_date, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_sale_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO sale_log (sale_id, observation, sale_date, created_by, updated_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.sale_date, NEW.created_by, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO sale_log (sale_id, observation, sale_date, created_by, updated_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.sale_date, NEW.created_by, NEW.updated_by, NEW.deleted, 'U', NOW());
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
    created_by INTEGER NOT NULL,
    updated_by INTEGER,
    deleted boolean not null,
    action CHAR(1) NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_invoice_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO invoice_log (invoice_id, observation, due_date, issue_date, payment_method, state, sale_id, created_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.observation, NEW.due_date, NEW.issue_date, NEW.payment_method, NEW.state, NEW.sale_id, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_invoice_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO invoice_log (invoice_id, observation, due_date, issue_date, payment_method, state, sale_id, created_by, updated_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.due_date, NEW.issue_date, NEW.payment_method, NEW.state, NEW.sale_id, NEW.created_by, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO invoice_log (invoice_id, observation, due_date, issue_date, payment_method, state, sale_id, created_by, updated_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.observation, NEW.due_date, NEW.issue_date, NEW.payment_method, NEW.state, NEW.sale_id, NEW.created_by, NEW.updated_by, NEW.deleted, 'U', NOW());
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
    created_by INTEGER NOT NULL,
    updated_by INTEGER,
    deleted BOOLEAN NOT NULL,
    action CHAR(1) NOT NULL,
    action_date TIMESTAMP
);

CREATE OR REPLACE FUNCTION log_invoice_detail_insert()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO invoice_detail_log (invoice_detail_id, invoice_id, description, quantity, unit_price, created_by, deleted, action, action_date)
    VALUES (NEW.id, NEW.invoice_id, NEW.description, NEW.quantity, NEW.unit_price, NEW.created_by, NEW.deleted, 'I', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION log_invoice_detail_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.deleted THEN
        INSERT INTO invoice_detail_log (invoice_detail_id, invoice_id, description, quantity, unit_price, created_by, updated_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.invoice_id, NEW.description, NEW.quantity, NEW.unit_price, NEW.created_by, NEW.updated_by, NEW.deleted, 'D', NOW());
    ELSE
        INSERT INTO invoice_detail_log (invoice_detail_id, invoice_id, description, quantity, unit_price, created_by, updated_by, deleted, action, action_date)
        VALUES (NEW.id, NEW.invoice_id, NEW.description, NEW.quantity, NEW.unit_price, NEW.created_by, NEW.updated_by, NEW.deleted, 'U', NOW());
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