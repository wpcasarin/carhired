INSERT INTO automakers (created_at, deleted, name, updated_at, deleted_at, country)
VALUES (NOW(), FALSE, 'Subaru', NOW(), NULL, 'Japan');
INSERT INTO models (created_at, deleted, name, automaker_id, updated_at, deleted_at, released_at)
VALUES (NOW(), FALSE, 'Impreza SW WRX', 1, NOW(), NULL, '2006-08-03');
INSERT INTO vehicles (created_at, deleted, license_plaque, color, num_doors, mileage, renavam, chassis, rent_price, available, model_id, updated_at, deleted_at)
VALUES (NOW(), FALSE, 'HO06NZQ', 'White', 5, 3123, '06946174660', 'JH4DA9360LS010859', 100, TRUE, 1, NOW(), NULL)