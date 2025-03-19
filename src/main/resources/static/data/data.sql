INSERT INTO workshop(workshop_location, workshop_name)
VALUES ('BRUSSELS', 'WORKSHOP1'),
       ('MECHELEN', 'WORKSHOP2'),
       ('ANTWERP', 'WORKSHOP3');

INSERT INTO profile (name,workshop_workshop_id, email, password, approved, dtype)
VALUES ('Admin User',2, 'admin@example.com', '$2y$10$Ztympp4dswJV4XoYXx1UZeb0ebJ0.zaulW9cBApWAX7LZe6mrDicG', false, 'WorkshopAdmin');

INSERT INTO profile (name, email, password, approved, dtype)
VALUES ('Technician User', 'tech@example.com', '$2y$10$MzHvzeMB8q3MBAxxvPDtC.MkhilhjTOreMdvled9d3eaTAAf6.W2m', false, 'Technician');


ALTER TABLE bike
    ADD COLUMN bike_owner_id INTEGER DEFAULT 17 NOT NULL
        CONSTRAINT customer_fk REFERENCES profile(id);