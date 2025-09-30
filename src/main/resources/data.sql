INSERT INTO BUILDING (BUILDING_NAME, BUILDING_CODE) VALUES
    ('Жилой дом №1', 'BLDG001'),
    ('Жилой дом №2', 'BLDG002'),
    ('Жилой дом №3', 'BLDG003'),
    ('Офисное здание "Центр"', 'OFFICE001'),
    ('ЖК "Солнечный"', 'RESID001');

INSERT INTO APARTMENT (APARTMENT_NUMBER, TOTAL_SQUARE, LIVING_SQUARE, ROOMS_AMOUNT, FLOOR, BUILDING_ID) VALUES
    ('1А', 65.5, 45.2, 3, 1, 1),
    ('2А', 72.0, 50.5, 3, 1, 1),
    ('3А', 58.3, 38.7, 2, 1, 1),
    ('11А', 65.5, 45.2, 3, 2, 1),
    ('12А', 72.0, 50.5, 3, 2, 1),
    ('13А', 58.3, 38.7, 2, 2, 1),
    ('21А', 80.2, 60.1, 4, 3, 1),
    ('22А', 65.5, 45.2, 3, 3, 1);

INSERT INTO APARTMENT (APARTMENT_NUMBER, TOTAL_SQUARE, LIVING_SQUARE, ROOMS_AMOUNT, FLOOR, BUILDING_ID) VALUES
    ('101', 55.8, 35.4, 2, 1, 2),
    ('102', 68.9, 48.3, 3, 1, 2),
    ('103', 75.2, 52.7, 3, 1, 2),
    ('201', 55.8, 35.4, 2, 2, 2),
    ('202', 68.9, 48.3, 3, 2, 2),
    ('203', 75.2, 52.7, 3, 2, 2),
    ('301', 90.5, 65.8, 4, 3, 2),
    ('302', 68.9, 48.3, 3, 3, 2);

INSERT INTO APARTMENT (APARTMENT_NUMBER, TOTAL_SQUARE, LIVING_SQUARE, ROOMS_AMOUNT, FLOOR, BUILDING_ID) VALUES
    ('1', 45.2, 28.5, 1, 1, 3),
    ('2', 62.7, 42.1, 2, 1, 3),
    ('3', 78.4, 55.9, 3, 1, 3),
    ('11', 45.2, 28.5, 1, 2, 3),
    ('12', 62.7, 42.1, 2, 2, 3),
    ('13', 78.4, 55.9, 3, 2, 3);

INSERT INTO APARTMENT (APARTMENT_NUMBER, TOTAL_SQUARE, LIVING_SQUARE, ROOMS_AMOUNT, FLOOR, BUILDING_ID) VALUES
    ('Офис 101', 120.5, 95.2, 5, 1, 4),
    ('Офис 102', 85.3, 68.7, 3, 1, 4),
    ('Офис 201', 150.8, 125.4, 6, 2, 4),
    ('Офис 202', 95.7, 78.2, 4, 2, 4);

INSERT INTO APARTMENT (APARTMENT_NUMBER, TOTAL_SQUARE, LIVING_SQUARE, ROOMS_AMOUNT, FLOOR, BUILDING_ID) VALUES
    ('А1', 82.6, 58.9, 4, 1, 5),
    ('А2', 67.4, 46.8, 3, 1, 5),
    ('Б1', 82.6, 58.9, 4, 2, 5),
    ('Б2', 67.4, 46.8, 3, 2, 5);

INSERT INTO ELECTRICITY (APARTMENT_ID, DAY_CONSUMPTION, NIGHT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (1, 1250, 680, 0.0, true),
     (2, 1450, 820, 150.50, true),
     (3, 980, 520, 0.0, true),
     (4, 1320, 750, 0.0, true),
     (5, 1580, 890, 75.25, true),
     (6, 1100, 600, 0.0, true),
     (7, 1850, 1020, 0.0, true),
     (8, 1380, 780, 200.00, true);

INSERT INTO ELECTRICITY (APARTMENT_ID, DAY_CONSUMPTION, NIGHT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (9, 1150, 620, 0.0, true),
     (10, 1420, 810, 0.0, true),
     (11, 1650, 920, 125.75, true),
     (12, 1180, 640, 0.0, true),
     (13, 1480, 840, 0.0, true),
     (14, 1680, 950, 0.0, true),
     (15, 2100, 1150, 300.00, true),
     (16, 1520, 860, 0.0, true);

INSERT INTO ELECTRICITY (APARTMENT_ID, DAY_CONSUMPTION, NIGHT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (17, 850, 450, 0.0, true),
     (18, 1250, 680, 50.00, true),
     (19, 1580, 890, 0.0, true),
     (20, 880, 470, 0.0, true),
     (21, 1280, 700, 0.0, true),
     (22, 1620, 910, 175.25, true);

INSERT INTO ELECTRICITY (APARTMENT_ID, DAY_CONSUMPTION, NIGHT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (23, 3500, 1200, 0.0, true),
     (24, 2800, 950, 0.0, true),
     (25, 4200, 1500, 500.00, true),
     (26, 3100, 1100, 0.0, true);

INSERT INTO ELECTRICITY (APARTMENT_ID, DAY_CONSUMPTION, NIGHT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (27, 1750, 980, 0.0, true),
     (28, 1420, 810, 100.50, true),
     (29, 1780, 1000, 0.0, true),
     (30, 1450, 830, 0.0, true);

INSERT INTO WATER_SUPPLY (APARTMENT_ID, COLD_CONSUMPTION, HOT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (1, 25, 15, 0.0, true),
     (2, 32, 18, 45.20, true),
     (3, 20, 12, 0.0, true),
     (4, 28, 16, 0.0, true),
     (5, 35, 20, 25.75, true),
     (6, 22, 13, 0.0, true),
     (7, 42, 25, 0.0, true),
     (8, 30, 17, 60.00, true);

INSERT INTO WATER_SUPPLY (APARTMENT_ID, COLD_CONSUMPTION, HOT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (9, 23, 14, 0.0, true),
     (10, 31, 18, 0.0, true),
     (11, 38, 22, 35.50, true),
     (12, 24, 15, 0.0, true),
     (13, 33, 19, 0.0, true),
     (14, 39, 23, 0.0, true),
     (15, 48, 28, 85.25, true),
     (16, 34, 20, 0.0, true);

INSERT INTO WATER_SUPPLY (APARTMENT_ID, COLD_CONSUMPTION, HOT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (17, 18, 10, 0.0, true),
     (18, 26, 15, 15.00, true),
     (19, 36, 21, 0.0, true),
     (20, 19, 11, 0.0, true),
     (21, 27, 16, 0.0, true),
     (22, 37, 22, 42.75, true);

INSERT INTO WATER_SUPPLY (APARTMENT_ID, COLD_CONSUMPTION, HOT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (23, 85, 35, 0.0, true),
     (24, 65, 28, 0.0, true),
     (25, 120, 45, 150.00, true),
     (26, 75, 32, 0.0, true);

INSERT INTO WATER_SUPPLY (APARTMENT_ID, COLD_CONSUMPTION, HOT_CONSUMPTION, DEBT, ACTIVE) VALUES
     (27, 40, 24, 0.0, true),
     (28, 33, 19, 28.50, true),
     (29, 41, 25, 0.0, true),
     (30, 34, 20, 0.0, true);
