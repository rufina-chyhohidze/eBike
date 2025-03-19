INSERT INTO workshop(workshop_location, workshop_name)
VALUES ('BRUSSELS', 'WORKSHOP1'),
       ('MECHELEN', 'WORKSHOP2'),
       ('ANTWERP', 'WORKSHOP3');

ALTER TABLE bike
    ADD COLUMN bike_owner_id INTEGER DEFAULT 17 NOT NULL
        CONSTRAINT customer_fk REFERENCES profile(id);