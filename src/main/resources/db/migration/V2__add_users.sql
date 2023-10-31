INSERT INTO users (created_at, updated_at, deleted, email, password, role, name, cpf, type, phone, address)
VALUES
    (NOW(), NOW(), FALSE, 'admin@mail.com', '$2a$12$FWzkyzXdm5n5Jsihiqaa4e7yNvZy/kibrS3dK.86zJAQpiBx2TgkK', 'ADMIN', 'Admin', '44200588030', 'A', NULL, NULL),
    (NOW(), NOW(), FALSE, 'user@mail.com', '$2a$12$FWzkyzXdm5n5Jsihiqaa4e7yNvZy/kibrS3dK.86zJAQpiBx2TgkK', 'CUSTOMER', 'Roberto', '11300588030', 'C', '53981365543', 'Av. Brasil - 3332 - Rio de Janeiro/RJ')