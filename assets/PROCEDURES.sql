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





-- Test --

-- CALL getSessionsToday(1);
-- CALL getAttendanceOfSession(1);
CALL getUser('luisb');