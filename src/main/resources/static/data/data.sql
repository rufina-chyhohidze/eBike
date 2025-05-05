INSERT INTO workshop(workshop_location, workshop_name)
VALUES ('BRUSSELS', 'WORKSHOP1'),
       ('MECHELEN', 'WORKSHOP2'),
       ('ANTWERP', 'WORKSHOP3');

INSERT INTO test_bench(workshop_workshop_id)
VALUES ( '1'),
       ('2');

INSERT INTO profile (name,workshop_workshop_id, email, password, approved, dtype)
VALUES ('Admin User',2, 'admin@example.com', '$2a$12$2NJZ9z4jPfozboAh9nk1ceIIG9e726l4.bFdCmHdkSw3bfSGrEoey', false, 'WorkshopAdmin');

INSERT INTO profile (name, email, password, approved, dtype, workshop_workshop_id)
VALUES ('Technician User', 'tech@example.com', '$2a$12$x2HHytbC0J3GZkbYGgKgJuBVtQP9C2r/XLLVDzZkcsCsEjrFXBeDO', true, 'Technician',1);

INSERT INTO profile (name, email, password, approved, dtype, phone_number)
VALUES ('John Doe', 'customer@email.com', '$2a$12$8kZmT8ZhlW9v3BH8azVAFOYitx65Szf5vRSWTDHWCJJGZ0xEJACF2', true, 'Customer', '+32456789444');

INSERT INTO profile (name, email, password, approved, dtype)
VALUES ('Super John', 'superadmin@email.com', '$2a$12$d.M7PL2WzgPBZ7c9yHz9FOYj8pNfFb0mILrqd56Qm5FChA7mQMn/m', true, 'SystemAdmin');

-- Insert into bike_model (must be done before referencing in bike)
INSERT INTO bike_model (
    id, type, brand, bike_size, gear_type, engine_type, powertrain,
    max_support, engine_power_max, engine_power_nominal, engine_torque
)
VALUES (
           1000000, 'PRO', 'TOYOTA', 1, 'GEAR', 'VOLVO', 'POWERTRAIN',
           300, 400, 300, 500
       );

-- Insert into bike (frame_number as primary key, link to bike_model and customer)
INSERT INTO bike (
    frame_number, bike_owner_id, registration_date, production_date,
    milleage, acc_capacity, bike_model_id
)
VALUES (
           'X45FERF', 3, '2024-01-01 10:00:00', '2024-01-01',
           1234, 250, 1000000
       );
INSERT INTO report_setting (technician_id, horizontal_vibration, vertical_vibration)
VALUES
    (2, 1, 1)
