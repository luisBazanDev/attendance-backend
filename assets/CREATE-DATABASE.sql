USE test;

CREATE TABLE IF NOT EXISTS Person (
	code CHAR(32) PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    status ENUM('UNKNOWN', 'VERIFIED')
);

CREATE TABLE IF NOT EXISTS User (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(32) NOT NULL,
    password VARCHAR(72) NOT NULL,
    htop_seed VARCHAR(36) NOT NULL,
    code_person CHAR(32) NOT NULL,
    role ENUM('ADMIN', 'MANAGER') NOT NULL DEFAULT 'MANAGER',
    FOREIGN KEY(code_person) REFERENCES Person(code)
);

CREATE TABLE IF NOT EXISTS `Group` (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(512) NULL,
    amount_persons INT DEFAULT 0,
    amount_sessions INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Managing (
	user_id INT NOT NULL,
    group_id INT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES User(id),
    FOREIGN KEY(group_id) REFERENCES `Group`(id)
);

CREATE TABLE IF NOT EXISTS Assignation (
	id INT AUTO_INCREMENT PRIMARY KEY,
    person_code CHAR(32) NOT NULL,
    group_id INT NOT NULL,
    FOREIGN KEY(person_code) REFERENCES Person(code),
    FOREIGN KEY(group_id) REFERENCES `Group`(id)
);

CREATE TABLE IF NOT EXISTS Session (
	id INT AUTO_INCREMENT PRIMARY KEY,
	group_id INT,
    session_number TINYINT UNSIGNED NOT NULL,
    start_at TIMESTAMP NOT NULL,
    duration_in_minutes INT NOT NULL DEFAULT 90,
    description VARCHAR(512) NULL,
    FOREIGN KEY(group_id) REFERENCES `Group`(id),
    UNIQUE KEY `session_id` (group_id, session_number)
);

CREATE TABLE IF NOT EXISTS Attendance (
	assignation_id INT,
    session_id INT,
    recorded_at  TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY(assignation_id) REFERENCES Assignation(id),
    FOREIGN KEY(session_id) REFERENCES Session(id)
);

-- SEED --

INSERT INTO Person (code, name, last_name, email, phone_number, status)
VALUES
('U22203189', 'Luis', 'Bazán', 'luis@bazan.pe', '51123456789', 'VERIFIED'),
('U25001013', 'Juan', 'Quispe', 'a@gmail.com', '51331203122', 'UNKNOWN'),
('U25001033', 'Diana', 'Perez', 'aa2@gmail.com', '51241203122', 'UNKNOWN'),
('U26001023', 'Pepe', 'Ramos', 'a@gmail.com', '51131203122', 'VERIFIED');

-- Passwords: 123456
INSERT INTO User (username, password, htop_seed, code_person, role)
VALUES
('luisb', '$2a$12$9K.FExxuDKwEEvF0P.FwMeV9z/pnadCMdNK/kXq1DJksqXmfpYhvG', '', 'U22203189', 'ADMIN'),
('peper', '$2a$12$9K.FExxuDKwEEvF0P.FwMeV9z/pnadCMdNK/kXq1DJksqXmfpYhvG', '', 'U26001023', 'MANAGER');

INSERT INTO `Group` (name, description, amount_persons, amount_sessions)
VALUES
('Grupo de estudio de prueba', 'Una descripcción bien chevere, para probar que esto soporta la cantidad de caracteres acordada', 4, 3);

INSERT INTO Managing (user_id, group_id)
VALUES
(1, 1);

INSERT INTO Assignation (person_code, group_id)
VALUES
('U22203189', 1),
('U25001013', 1),
('U25001033', 1),
('U26001023', 1);

INSERT INTO Session (group_id, session_number, start_at, description)
VALUES
(1, 1, NOW(), 'Sesion 1'),
(1, 2, adddate(NOW(), INTERVAL 1 DAY), 'Sesion 2'),
(1, 3, adddate(NOW(), INTERVAL 2 DAY), 'Sesion 3');

INSERT INTO Attendance (session_id, assignation_id)
VALUES
(1, 1);

