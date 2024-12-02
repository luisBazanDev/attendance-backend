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

DELIMITER ;



-- Test --

-- CALL getSessionsToday(1);
CALL getAttendanceOfSession(1);
