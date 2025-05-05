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

-- INSERT INTO bike (acc_capacity, bike_size, engine_power_max, engine_power_nominal, engine_torque, max_support, milleage, production_date, bike_owner_id, registration_date, brand, engine_type, frame_number, gear_type, powertrain, type)
-- VALUES (250, 5, 400, 300, 500, 300, 1234, NOW(), 3, NOW(), 'TOYOTA', 'VOLVO', 'X45FERF', 'GEAR', 'POWERTRAIN', 'PRO');

-- Bike 1
INSERT INTO bike (acc_capacity, bike_size, engine_power_max, engine_power_nominal, engine_torque, max_support, milleage, production_date, bike_owner_id, registration_date, brand, engine_type, frame_number, gear_type, powertrain, type)
VALUES (300, 6, 450, 350, 600, 350, 1500, NOW(), 3, NOW(), 'HONDA', 'V6', 'Y67GHTJ', 'AUTO', 'ELECTRIC', 'MTB');

-- Bike 2
INSERT INTO bike (acc_capacity, bike_size, engine_power_max, engine_power_nominal, engine_torque, max_support, milleage, production_date, bike_owner_id, registration_date, brand, engine_type, frame_number, gear_type, powertrain, type)
VALUES (200, 5, 380, 280, 520, 280, 950, NOW(), 3, NOW(), 'BMW', 'INLINE-4', 'A34BHVK', 'SHIFTER', 'HYBRID', 'ROAD');

-- Bike 3
INSERT INTO bike (acc_capacity, bike_size, engine_power_max, engine_power_nominal, engine_torque, max_support, milleage, production_date, bike_owner_id, registration_date, brand, engine_type, frame_number, gear_type, powertrain, type)
VALUES (250, 5, 420, 310, 550, 330, 1200, NOW(), 3, NOW(), 'MERCEDES', 'V8', 'Q78DRFG', 'GEAR', 'ELECTRIC', 'CITY');


INSERT INTO report_setting (technician_id, horizontal_vibration, vertical_vibration)
VALUES
    (2, 1, 1)
