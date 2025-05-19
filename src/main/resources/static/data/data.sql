INSERT INTO workshop(workshop_location, workshop_name)
VALUES ('BRUSSELS', 'WORKSHOP1'),
       ('MECHELEN', 'WORKSHOP2'),
       ('ANTWERP', 'WORKSHOP3');

INSERT INTO test_bench(workshop_workshop_id)
VALUES ( '1'),
       ('2');

INSERT INTO profile (name,workshop_workshop_id, email, password, approved, dtype)
VALUES ('Admin User',2, 'admin@example.com', '$2a$12$2NJZ9z4jPfozboAh9nk1ceIIG9e726l4.bFdCmHdkSw3bfSGrEoey', false, 'WorkshopAdmin');

INSERT INTO profile (id,name, email, password, approved, dtype, workshop_workshop_id)
VALUES (1000000,'Technician User', 'tech@example.com', '$2a$12$x2HHytbC0J3GZkbYGgKgJuBVtQP9C2r/XLLVDzZkcsCsEjrFXBeDO', true, 'Technician',1);

INSERT INTO profile (id,name, email, password, approved, dtype, workshop_workshop_id)
VALUES (1000007,'Technician User2', 'tech2@example.com', '$2a$12$5bOE6dGaniRAJzf34HwzN..hfJR8oUynxhc.cuFHVlARkrp0fEzBe', true, 'Technician',2); --tech2 password

INSERT INTO profile (name, email, password, approved, dtype, phone_number, registered_by_id)
VALUES ('John Doe', 'customer@email.com', '$2a$12$8kZmT8ZhlW9v3BH8azVAFOYitx65Szf5vRSWTDHWCJJGZ0xEJACF2', true, 'Customer', '+32456789444', 1000000),
        ('Carla Dupont',   'carla.dupont@example.be',   '$2a$12$Zhq6ORJmmjvwAYlB30PcZOTRWaj1vymdmQC6l8R.SP.UDfAfaWNwe', true, 'Customer', '+32472345678',1000000),
        ('Bob Johnson',    'bob.johnson@example.com', '$2a$12$VeI2dwxNGThkQN.pdVtauesYD7iTJJdQuArsMIgxWLfTxcE85nQYS', true, 'Customer', '+32471234567',1000000),
        ('Alice Smith',    'alice.smith@example.com', '$2a$12$MH0FOjaHkavCf5Llgecr9OUQI21nfeQ2qQOEZTMCaYuywU29VJenO', true, 'Customer', '+32470123456',1000000),
        ('David Vermeulen',   'david.vermeulen@example.com',   '$2a$12$mqMwA4.CifWAQEgWuAPp5.ioIOezMHPYxhwu2jO.JeNHW3.Ii9TBm', true, 'Customer', '+32474567890',1000000),
        ('Eva Janssens',      'eva.janssens@example.be',       '$2a$12$4w4pGx69Sr5AYxkENCS6u.7haYlAiG1GfpMX619xNKY3SFA7UlGtG', true, 'Customer', '+32475678901',1000000),
        ('Frederick Peeters', 'frederick.peeters@example.net', '$2a$12$vGHGZahp8KEuQgopdmrXpub9q8CbyTR3P2M3uA3k6JYnKCs0goKZS', true, 'Customer', '+32476789012',1000000),
       ('Gisele De Smet',    'gisele.desmet@example.eu',      '$2a$12$i95HAdiaQ3NS9wedFd/B4.foQWCOTWUbtJrgIREd18GEfDTk3/iZy', true, 'Customer', '+32477890123',1000000),
       ('Martina Letsgo',    'martina.letsgo@example.eu',      '$2a$12$RL/64qCkZo7WcCfmcb4ozO/4GcXzlGEzkNh5U6/ao5snEzW2dDDmO', true, 'Customer', '+32477890125',1000007); --hell2 password

--pass123

INSERT INTO profile (name, email, password, approved, dtype)
VALUES ('Super John', 'superadmin@email.com', '$2a$12$d.M7PL2WzgPBZ7c9yHz9FOYj8pNfFb0mILrqd56Qm5FChA7mQMn/m', true, 'SystemAdmin');

-- Insert into bike_model (must be done before referencing in bike)
INSERT INTO bike_model (
    id, type, brand, gear_type, engine_type, powertrain,
    max_support, engine_power_max, engine_power_nominal, engine_torque
)
VALUES (
           1000000, 'PRO', 'TOYOTA', 'GEAR', 'VOLVO', 'POWERTRAIN',
           300, 400, 300, 500
       );
INSERT INTO bike_model (
    id, type, brand, gear_type, engine_type, powertrain,
    max_support, engine_power_max, engine_power_nominal, engine_torque
)
VALUES (
           1000001, 'PRO', 'RUFINA', 'GEAR', 'RUFINA', 'POWERTRAIN',
           300, 400, 300, 500
       );

-- Insert into bike (frame_number as primary key, link to bike_model and customer)
INSERT INTO bike (
    frame_number, bike_owner_id,bike_size, registration_date, production_date,
    milleage, acc_capacity, bike_model_id
)
VALUES (
           'X45FERF', 2, 1,'2024-01-01 10:00:00', '2024-01-01',
           1234, 250, 1000000
       );
INSERT INTO bike (
    frame_number, bike_owner_id, bike_size, registration_date, production_date,
    milleage, acc_capacity, bike_model_id
)
VALUES (
           'R123456', 2,1, '2024-01-01 10:00:00', '2024-01-01',
           1234, 250, 1000001
       );


INSERT INTO report_setting (technician_id, horizontal_vibration, vertical_vibration)
VALUES
    (2, 1, 1)
