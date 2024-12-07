USE test;

DELIMITER //

DROP PROCEDURE IF EXISTS getSessionsToday //

CREATE PROCEDURE 
  getSessionsToday(user_id INT)
BEGIN  
	SELECT S.*
	FROM Managing AS M
		INNER JOIN `Group` AS G
		ON M.group_id = G.id
		INNER JOIN Session AS S
		ON S.group_id = G.id
	WHERE M.user_id=user_id AND date(S.start_at) = CURDATE();
END 
//

DROP PROCEDURE IF EXISTS getAttendanceOfSession //

CREATE PROCEDURE 
  getAttendanceOfSession(session_id INT)
BEGIN
	SELECT code, 
       name, 
       last_name, 
       email, 
       phone_number, 
       MAX(attendance) AS attendance
    FROM (
		SELECT P.*, true AS attendance
		FROM Attendance AS Att
			INNER JOIN Assignation AS Ass
			ON Att.assignation_id = Ass.id
			INNER JOIN Person AS P
			ON Ass.person_code = P.code
		WHERE Att.session_id = session_id
		UNION
		SELECT P.*, false AS attendance
		FROM Session AS S
			INNER JOIN Assignation AS Ass
			ON Ass.group_id = S.group_id
			INNER JOIN Person AS P
			ON P.code = Ass.person_code
		WHERE S.id = session_id
    ) AS T
    GROUP BY code, name, last_name, email, phone_number;
END 
//

DROP PROCEDURE IF EXISTS getUser //

CREATE PROCEDURE 
  getUser(username VARCHAR(32))
BEGIN  
	SELECT U.username,  U.password, P.code, U.role, P.name, P.last_name, P.email, P.phone_number
	FROM User AS U
		INNER JOIN Person AS P
        ON U.code_person = P.code
	WHERE U.username = username;
END 
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `CreateGroup`(
    IN g_name VARCHAR(64),
    IN g_description VARCHAR(512),
    IN g_amount_persons INT,
    IN g_amount_sessions INT
)
BEGIN
INSERT INTO `Group` (`name`, description, amount_persons, amount_sessions)
VALUES (g_name, g_description, g_amount_persons, g_amount_sessions);
SELECT *
FROM `Group`
WHERE id = LAST_INSERT_ID();
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `AddPersonToGroup`(
    IN p_code CHAR(32),
    IN p_name VARCHAR(64),
    IN p_last_name VARCHAR(64),
    IN p_email VARCHAR(64),
    IN p_phone_number VARCHAR(20),
    IN g_group_id INT
)
BEGIN
    DECLARE p_status ENUM('UNKNOWN', 'VERIFIED');
    IF NOT EXISTS (SELECT 1 FROM Person WHERE code = p_code) THEN
        SET p_status = 'UNKNOWN';

        -- Insertar la nueva persona
INSERT INTO Person (code, name, last_name, email, phone_number, status)
VALUES (p_code, p_name, p_last_name, p_email, p_phone_number, p_status);
ELSE
        SET p_status = 'VERIFIED';
END IF;

INSERT INTO Assignation (person_code, group_id)
VALUES (p_code, g_group_id);

IF p_status = 'VERIFIED' THEN
UPDATE Person
SET status = p_status
WHERE code = p_code;
END IF;
SELECT code, name, last_name, email, phone_number, status
FROM Person
WHERE code = p_code;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `AddManagerToGroup`(
    IN u_username VARCHAR(32),
    IN u_password VARCHAR(72),
    IN u_htop_seed VARCHAR(36),
    IN u_code_person CHAR(32),
    IN u_name VARCHAR(64),
    IN u_last_name VARCHAR(64),
    IN u_email VARCHAR(64),
    IN u_phone_number VARCHAR(20),
    IN g_group_id INT
)
BEGIN
DECLARE user_id INT;
    DECLARE p_status ENUM('UNKNOWN', 'VERIFIED');

    IF NOT EXISTS (SELECT 1 FROM Person WHERE code = u_code_person) THEN
        INSERT INTO Person (code, name, last_name, email, phone_number, status)
        VALUES (u_code_person, u_name, u_last_name, u_email, u_phone_number, 'UNKNOWN');
ELSE
        SET p_status = 'VERIFIED';
UPDATE Person
SET status = p_status
WHERE code = u_code_person;
END IF;

    IF NOT EXISTS (SELECT 1 FROM User WHERE username = u_username) THEN
        INSERT INTO User (username, password, htop_seed, code_person, role)
        VALUES (u_username, u_password, u_htop_seed, u_code_person, 'MANAGER');
END IF;

SELECT id INTO user_id
FROM User
WHERE username = u_username;

IF NOT EXISTS (SELECT 1 FROM Managing WHERE user_id = user_id AND group_id = g_group_id) THEN
        INSERT INTO Managing (user_id, group_id)
        VALUES (user_id, g_group_id);
END IF;

SELECT U.id AS user_id, U.username, U.code_person, U.role, P.name, P.last_name, P.email
FROM User U
         INNER JOIN Person P ON U.code_person = P.code
WHERE U.id = user_id;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `CheckGroupLimit`(
    IN g_group_id INT
)
BEGIN
DECLARE current_persons INT;
    DECLARE max_persons INT;
SELECT COUNT(*) INTO current_persons
FROM Assignation
WHERE group_id = g_group_id;
SELECT amount_persons INTO max_persons
FROM `Group`
WHERE id = g_group_id;
IF current_persons >= max_persons THEN
SELECT 'FULL_LIMIT' AS group_status;
ELSE
SELECT 'AVAILABLE' AS group_status;
END IF;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `CreateOrAssignSession`(
    IN g_group_id INT,
    IN s_session_number TINYINT,
    IN s_start_at TIMESTAMP,
    IN s_duration INT,
    IN s_description VARCHAR(512)
)
BEGIN
    DECLARE session_exists INT;

SELECT COUNT(*) INTO session_exists
FROM Session
WHERE group_id = g_group_id AND session_number = s_session_number;

IF session_exists > 0 THEN
UPDATE Session
SET start_at = s_start_at,
    duration_in_minutes = s_duration,
    description = s_description
WHERE group_id = g_group_id AND session_number = s_session_number;
ELSE
        INSERT INTO Session (group_id, session_number, start_at, duration_in_minutes, description)
        VALUES (g_group_id, s_session_number, s_start_at, s_duration, s_description);
END IF;

SELECT id AS session_id, group_id, session_number, start_at, duration_in_minutes, description
FROM Session
WHERE group_id = g_group_id AND session_number = s_session_number;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `GetGroupStatistics`(
    IN p_group_id INT
)
BEGIN
    DECLARE v_amount_persons INT;
    DECLARE v_amount_sessions INT;
    DECLARE v_amount_attendance INT;

SELECT COUNT(*) INTO v_amount_persons
FROM Assignation
WHERE group_id = p_group_id;

SELECT COUNT(*) INTO v_amount_sessions
FROM Session
WHERE group_id = p_group_id;

SELECT COUNT(*) INTO v_amount_attendance
FROM Attendance a
         JOIN Session s ON a.session_id = s.id
WHERE s.group_id = p_group_id;

SELECT
    v_amount_persons AS TotalPersons,
    v_amount_sessions AS TotalSessions,
    v_amount_attendance AS TotalAttendance;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `SearchGroupByName`(
    IN p_group_name VARCHAR(64)
)
BEGIN
SELECT
    id AS GroupID,
    name AS GroupName,
    description AS GroupDescription,
    amount_persons AS TotalPersons,
    amount_sessions AS TotalSessions
FROM `Group`
WHERE name LIKE CONCAT('%', p_group_name, '%');
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `SearchSessionsByGroupID`(
    IN p_group_id INT
)
BEGIN
SELECT
    id AS SessionID,
    group_id AS GroupID,
    session_number AS SessionNumber,
    start_at AS StartAt,
    duration_in_minutes AS DurationInMinutes,
    description AS SessionDescription
FROM Session
WHERE group_id = p_group_id;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `ShowAllGroups`()
BEGIN
SELECT
    id AS GroupID,
    name AS GroupName,
    description AS GroupDescription,
    amount_persons AS TotalPersons,
    amount_sessions AS TotalSessions
FROM `Group`;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `ShowAssignationByGroupID`(
    IN p_group_id INT
)
BEGIN
SELECT
    a.id AS AssignationID,
    p.code AS PersonCode,
    p.name AS PersonName,
    p.last_name AS PersonLastName,
    p.email AS PersonEmail,
    p.phone_number AS PersonPhoneNumber,
    p.status AS PersonStatus
FROM Assignation a
         JOIN Person p ON a.person_code = p.code
WHERE a.group_id = p_group_id;
END
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE `TakeAttendance`(
    IN p_person_code CHAR(32),
    IN p_group_id INT,
    IN p_session_number TINYINT UNSIGNED
)
BEGIN
    DECLARE v_assignation_id INT;
    DECLARE v_session_id INT;
	DECLARE v_result_message VARCHAR(255);

SELECT id INTO v_assignation_id
FROM Assignation
WHERE person_code = p_person_code AND group_id = p_group_id
    LIMIT 1;

IF v_assignation_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Asignación no encontrada para la persona y el grupo proporcionados.';
END IF;

SELECT id INTO v_session_id
FROM Session
WHERE group_id = p_group_id AND session_number = p_session_number
    LIMIT 1;

IF v_session_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Sesión no encontrada para el grupo y número de sesión proporcionados.';
END IF;

INSERT INTO Attendance (assignation_id, session_id, recorded_at)
VALUES (v_assignation_id, v_session_id, NOW())
    ON DUPLICATE KEY UPDATE
                         recorded_at = NOW();

SET v_result_message = 'Asistencia registrada correctamente.';

SELECT v_result_message AS ResultMessage;
END
//

DELIMITER ;

-- Test --

-- CALL getSessionsToday(1);
-- CALL getAttendanceOfSession(1);
CALL getUser('luisb');