DROP TABLE univ CASCADE CONSTRAINTS;
DROP TABLE MEMBER CASCADE CONSTRAINTS;
DROP TABLE department CASCADE CONSTRAINTS;
DROP TABLE staff CASCADE CONSTRAINTS;
DROP TABLE subject CASCADE CONSTRAINTS;
DROP TABLE professor CASCADE CONSTRAINTS;
DROP TABLE STUDENT CASCADE CONSTRAINTS;
DROP TABLE course CASCADE CONSTRAINTS;
DROP TABLE enroll CASCADE CONSTRAINTS;
DROP TABLE DAYS CASCADE CONSTRAINTS;
DROP TABLE COURSE_TIME CASCADE CONSTRAINTS;

CREATE TABLE univ (
    univ_name VARCHAR2(255) PRIMARY KEY,
    address VARCHAR2(255),
    establishment_date date,
    emblem VARCHAR2(255),
    symbol_color VARCHAR2(255)
);

CREATE TABLE MEMBER (
    univ_name VARCHAR2(255) NOT NULL,
    id VARCHAR2(50) NOT NULL,
    nickname VARCHAR2(255),
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(50) NOT NULL,
    PRIMARY KEY (univ_name, id),
    FOREIGN KEY (univ_name) REFERENCES univ(univ_name) ON DELETE CASCADE
);

CREATE TABLE department (
    dept_no VARCHAR2(50) PRIMARY KEY,
    dept_name VARCHAR2(255)
);

CREATE TABLE staff (
    univ_name VARCHAR2(255) NOT NULL,
    id VARCHAR2(50) NOT NULL,
    name VARCHAR2(255),
    dept_no VARCHAR2(50),
    phone VARCHAR2(255) NOT NULL,
    birth DATE NOT NULL,
    email VARCHAR2(255),
    PRIMARY KEY (univ_name, id),
    FOREIGN KEY (univ_name) REFERENCES univ(univ_name),
    FOREIGN KEY (dept_no) REFERENCES department(dept_no),
    CONSTRAINT staff_member_fk FOREIGN KEY (univ_name, id) REFERENCES MEMBER(univ_name, id) ON DELETE CASCADE
);

CREATE TABLE subject (
    subject_code VARCHAR2(50) PRIMARY KEY,
    subject_title VARCHAR2(255)
);

CREATE TABLE professor (
    univ_name VARCHAR2(255) NOT NULL,
    id VARCHAR2(50) NOT NULL,
    major_code VARCHAR2(50),
    name VARCHAR2(255),
    office VARCHAR2(255),
    phone VARCHAR2(255) NOT NULL,
    birth DATE NOT NULL,
    email VARCHAR2(255),
    PRIMARY KEY (univ_name, id, major_code),
    FOREIGN KEY (univ_name) REFERENCES univ(univ_name),
    FOREIGN KEY (major_code) REFERENCES subject(subject_code),
    CONSTRAINT professor_member_fk FOREIGN KEY (univ_name, id) REFERENCES MEMBER(univ_name, id) ON DELETE CASCADE
);

CREATE TABLE STUDENT (
    univ_name VARCHAR2(255) NOT NULL,
    id VARCHAR2(50) NOT NULL,
    major_code VARCHAR2(50),
    name VARCHAR2(255),
    birth DATE NOT NULL,
    phone VARCHAR2(255) NOT NULL,
    email VARCHAR2(255),
    PRIMARY KEY (univ_name, id),
    FOREIGN KEY (univ_name) REFERENCES univ(univ_name),
    FOREIGN KEY (major_code) REFERENCES subject(subject_code),
    CONSTRAINT student_member_fk FOREIGN KEY (univ_name, id) REFERENCES MEMBER(univ_name, id) ON DELETE CASCADE
);

CREATE TABLE course (
    course_code VARCHAR2(255) PRIMARY KEY,
    univ_name VARCHAR2(255) NOT NULL,
    professor_id VARCHAR2(50) NOT NULL,
    major_code VARCHAR2(50) NOT NULL,
    course_name VARCHAR2(255) NOT NULL,
    classification VARCHAR2(50),
    credit NUMBER,
    max_student NUMBER,
    rest_seat NUMBER,
    classroom VARCHAR2(255),
    FOREIGN KEY (univ_name, professor_id, major_code) REFERENCES professor(univ_name, id, major_code)
);

CREATE TABLE DAYS (
	day_name varchar2(10),
	day_order number
);

CREATE TABLE course_time (
	univ_name varchar2(255),
	course_code VARCHAR2(255),
	course_day VARCHAR2(255),
    start_time VARCHAR2(100),
    end_time VARCHAR2(100)
);

CREATE TABLE enroll (
    univ_name VARCHAR2(255) NOT NULL,
    id VARCHAR2(50) NOT NULL,
    major_code VARCHAR2(50) NOT NULL,
    course_code VARCHAR2(50) NOT NULL,
    PRIMARY KEY (univ_name, id, course_code),
    FOREIGN KEY (univ_name, id) REFERENCES STUDENT(univ_name, id),
    FOREIGN KEY (major_code) REFERENCES subject(subject_code),
    FOREIGN KEY (course_code) REFERENCES course(course_code)
);

-- 학생 등록 시 트리거
CREATE OR REPLACE TRIGGER insert_student_member
AFTER INSERT ON Student
FOR EACH ROW
BEGIN
    INSERT INTO Member (univ_name, id, nickname, password, role)
    VALUES (:NEW.univ_name, :NEW.id, :NEW.name, 'WELCOME' || to_char(:NEW.birth, 'YYMMdd') || '!', 'Student');
END;

-- 교수 등록 시 트리거
CREATE OR REPLACE TRIGGER insert_professor_member
AFTER INSERT ON Professor
FOR EACH ROW
BEGIN
    INSERT INTO Member (univ_name, id, nickname, password, role)
    VALUES (:NEW.univ_name, :NEW.id, :NEW.name, 'WELCOME' || to_char(:NEW.birth, 'YYMMdd') || '!', 'Professor');
END;

-- 교직원 등록 시 트리거
CREATE OR REPLACE TRIGGER insert_staff_member
AFTER INSERT ON Staff
FOR EACH ROW
BEGIN
    INSERT INTO Member (univ_name, id, nickname, password, role)
    VALUES (:NEW.univ_name, :NEW.id, :NEW.name, 'WELCOME' || to_char(:NEW.birth, 'YYMMdd') || '!', 'Staff');
END;

INSERT INTO univ VALUES ('고려대학교', '서울특별시 성북구 안암로 145', '1905-05-05', 'images/crimson2negative.gif', '#8b0029');
INSERT INTO univ VALUES ('동양미래대학교', '서울시 구로구 경인로 445', '1939-04-21', 'images/세로조합국영문로고.jpg', '#036b3f');
INSERT INTO univ VALUES ('숙명여자대학교', '서울특별시 용산구 청파로47길 100', '1906-05-22', 'images/emblem-1_White.png', '#0d2d84');

SELECT * FROM UNIV;

INSERT INTO DEPARTMENT VALUES ('1000', '교무처'); --교원 인사(채용, 승진, 징계) 관리, 수업/학적 관리
INSERT INTO DEPARTMENT VALUES ('3000', '기획처'); --정책 결정, 예산, 감사
INSERT INTO DEPARTMENT VALUES ('4000', '총무처'); --교직원 인사 / 회계 / 시설물 유지 보수
INSERT INTO DEPARTMENT VALUES ('5000', '입학처'); --입학관리, 입시홍보 -> 학생 insert
INSERT INTO DEPARTMENT VALUES ('7000', '연구처'); --내부적 지원
INSERT INTO DEPARTMENT VALUES ('9000', '단과대학 행정실'); --학생 자료 관리 / 수강계획, 강의실 배치

SELECT * FROM DEPARTMENT ORDER BY DEPT_NO;

INSERT INTO STAFF VALUES ('고려대학교', '1001', '강호랑', '1000', '010-1111-1111' , '1989-02-02', 'strongtiger@naver.com');
INSERT INTO STAFF VALUES ('고려대학교', '1002', '고구려', '1000', '010-1111-1122', '1989-02-02', 'goguryeo@gmail.com');
INSERT INTO STAFF VALUES ('고려대학교', '1003', '김한국', '3000', '010-1111-1133', '1989-02-02', 'koreakim@gmail.com');
INSERT INTO STAFF VALUES ('고려대학교', '1004', '코레', '4000', '010-1111-1144', '1989-02-02', 'heykore@gmail.com');
INSERT INTO STAFF VALUES ('고려대학교', '1005', '고대한', '5000', '010-1111-1155', '1989-02-02', 'daehan@naver.com');
INSERT INTO STAFF VALUES ('고려대학교', '1006', '정민국', '7000', '010-1111-1166', '1989-02-02', 'minkuk@naver.com');
INSERT INTO STAFF VALUES ('고려대학교', '1007', '이나라', '9000', '010-1111-1177', '1989-02-02', 'naralee@gmail.com');
INSERT INTO STAFF VALUES ('고려대학교', '1008', '권민주', '9000', '010-1111-1188', '1989-02-02', 'minju@naver.com');

INSERT INTO STAFF VALUES ('동양미래대학교', '1001', '강미래', '1000', '010-2222-1111', '1989-02-02', 'future@gmail.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1002', '양미연', '1000', '010-2222-1122', '1989-02-02', 'miyeon@naver.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1003', '김경환', '3000', '010-2222-1133', '1989-02-02', 'kyeonghwan@gmail.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1004', '감동현', '4000', '010-2222-1144', '1989-02-02', 'donghyun@naver.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1005', '박현석', '5000', '010-2222-1155', '1989-02-02', 'hyeonseok@gmail.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1006', '한수현', '7000', '010-2222-1166', '1989-02-02', 'suhyeon@naver.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1007', '오연지', '9000', '010-2222-1177', '1989-02-02', 'yeonji@gmail.com');
INSERT INTO STAFF VALUES ('동양미래대학교', '1008', '백상혁', '9000', '010-2222-1188', '1989-02-02', 'sanghyeok@naver.com');

INSERT INTO STAFF VALUES ('숙명여자대학교', '1001', '오숙명', '1000', '010-3333-1111', '1989-02-02', 'ohmydestiny@naver.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1002', '나경자', '1000', '010-3333-1122', '1989-02-02', 'kyeongja@naver.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1003', '이현우', '3000', '010-3333-1133', '1989-02-02', 'hynu@gmail.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1004', '김호연', '4000', '010-3333-1144', '1989-02-02', 'sayho@gmail.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1005', '한솔', '5000', '010-3333-1155', '1989-02-02', 'hansol@gmail.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1006', '홍화연', '7000', '010-3333-1166', '1989-02-02', 'redflower@naver.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1007', '김대범', '9000', '010-3333-1177', '1989-02-02', 'bravekim@gmail.com');
INSERT INTO STAFF VALUES ('숙명여자대학교', '1008', '김동원', '9000', '010-3333-1188', '1989-02-02', 'tunakim1008@naver.com');

SELECT * FROM STAFF;
SELECT * FROM MEMBER;

INSERT INTO SUBJECT VALUES ('001', '국어국문학과');
INSERT INTO SUBJECT VALUES ('002', '사회복지학과');
INSERT INTO SUBJECT VALUES ('003', '경영학과');
INSERT INTO SUBJECT VALUES ('004', '법학과');
INSERT INTO SUBJECT VALUES ('005', '건축학과');
INSERT INTO SUBJECT VALUES ('006', '컴퓨터공학과');

SELECT * FROM SUBJECT;

INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0010001', '001', '고현서', 'SK미래관101', '010-1111-1234', '1989-02-02', 'hynsu@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0010002', '001', '한국어', 'SK미래관102', '010-1111-1285', '1989-02-02', 'korean@naver.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0020001', '002', '김사랑', 'SK미래관201', '010-1111-2564', '1989-02-02', 'lovekim@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0020002', '002', '한봉사', 'SK미래관204', '010-1111-7741', '1989-02-02', 'volunteerhan@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0030001', '003', '허경영', 'SK미래관302', '010-1111-5642', '1989-02-02', 'lookatmyeyes@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0030002', '003', '곽부자', 'SK미래관303', '010-1111-3851', '1989-02-02', 'richman@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0040001', '004', '선아현', 'SK미래관104', '010-1111-7851', '1989-02-02', 'ahyeon@naver.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0040002', '004', '김초롱', 'SK미래관106', '010-1111-3241', '1989-02-02', 'twinkle@naver.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0050001', '005', '나무현', 'SK미래관206', '010-1111-8964', '1989-02-02', 'treehyeon@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0050002', '005', '마리오', 'SK미래관208', '010-1111-1265', '1989-02-02', 'mario@naver.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0060001', '006', '안경남', 'SK미래관501', '010-1111-7563', '1989-02-02', 'glassesman@gmail.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0060002', '006', '고미란', 'SK미래관502', '010-1111-2121', '1989-02-02', 'gogomiran@naver.com');
INSERT INTO PROFESSOR VALUES ('고려대학교', 'P0060003', '006', '강현욱', 'SK미래관503', '010-1111-2453', '1989-02-02', 'hwkang@gmail.com');

INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0010001', '001', '이은정', '2호관101', '010-2222-1234', '1989-02-02', 'eunjeong2@naver.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0010002', '001', '오명숙', '2호관102', '010-2222-1285', '1989-02-02', 'myeongsuk5@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0020001', '002', '오지리', '2호관201', '010-2222-2564', '1989-02-02', 'geo5@naver.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0020002', '002', '한경태', '2호관204', '010-2222-7741', '1989-02-02', 'han1234@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0030001', '003', '신용우', '2호관602', '010-2222-5642', '1989-02-02', 'godyongwoo@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0030002', '003', '한영철', '2호관603', '010-2222-3851', '1989-02-02', 'ychan@naver.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0040001', '004', '백한우', '2호관704', '010-2222-7851', '1989-02-02', 'whitekoreancow@naver.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0040002', '004', '이혜련', '2호관702', '010-2222-3241', '1989-02-02', 'hrlee@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0050001', '005', '강경아', '2호관206', '010-2222-8964', '1989-02-02', 'kka206@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0050002', '005', '우도연', '2호관208', '010-2222-1265', '1989-02-02', 'doyeonwoo@naver.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0060001', '006', '임유라', '2호관501', '010-2222-7563', '1989-02-02', 'yura@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0060002', '006', '하평화', '2호관502', '010-2222-2121', '1989-02-02', 'peace@gmail.com');
INSERT INTO PROFESSOR VALUES ('동양미래대학교', 'P0060003', '006', '나백합', '2호관503', '010-2222-2453', '1989-02-02', 'imlily@gmail.com');

INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0010001', '001', '전민아', '순헌관506', '010-3333-1234', '1989-02-02', 'iammina@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0010002', '001', '홍범기', '순헌관507', '010-3333-1285', '1989-02-02', 'bumki@gmail.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0010003', '001', '이홍식', '순헌관508', '010-3333-8787', '1989-02-02', 'hongsik@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0020001', '002', '강수진', '순헌관501', '010-3333-2564', '1989-02-02', 'sujin@gmail.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0020002', '002', '이지은', '순헌관502', '010-3333-7741', '1989-02-02', 'iloveu@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0030001', '003', '진유정', '수련교수회관302', '010-3333-5642', '1989-02-02', 'yujeong@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0030002', '003', '유현화', '수련교수회관303', '010-3333-3851', '1989-02-02', 'hyeonhwa@gmail.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0040001', '004', '원장우', '행파교수회관104', '010-3333-7851', '1989-02-02', 'jjang@gmail.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0040002', '004', '제갈태민', '행파교수회관106', '010-3333-3241', '1989-02-02', 'jegal@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0050001', '005', '이명자', '수련교수회관206', '010-3333-8964', '1989-02-02', 'myeongja@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0050002', '005', '김건우', '수련교수회관208', '010-3333-1265', '1989-02-02', 'gunu@gmail.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0060001', '006', '박이선', '명신관711', '010-3333-7563', '1989-02-02', 'python@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0060002', '006', '임용욱', '명신관712', '010-3333-2121', '1989-02-02', 'circle3@naver.com');
INSERT INTO PROFESSOR VALUES ('숙명여자대학교', 'P0060003', '006', '김자바', '명신관713', '010-3333-2453', '1989-02-02', 'javakim@gmail.com');

SELECT * FROM PROFESSOR;
SELECT * FROM MEMBER;

INSERT INTO COURSE VALUES ('21050296-001', '숙명여자대학교', 'P0010001', '001', '미디어와문학', '전공', 3, 6, 6, '순헌관416');
INSERT INTO COURSE VALUES ('21000008-001', '숙명여자대학교', 'P0010001', '001', '현대비평의이해', '전공', 3, 4, 4, '순헌관416');
INSERT INTO COURSE VALUES ('21000001-001', '숙명여자대학교', 'P0010002', '001', '고전문학사', '전공', 3, 4, 4, '명신관201');
INSERT INTO COURSE VALUES ('21001952-001', '숙명여자대학교', 'P0010003', '001', '국어학의이해', '전공', 3, 4, 4, '순헌관416');
INSERT INTO COURSE VALUES ('21002313-001', '숙명여자대학교', 'P0010003', '001', '커뮤니케이션과미디어', '교양', 3, 2, 2, '명신관525');
INSERT INTO COURSE VALUES ('21002103-001', '숙명여자대학교', 'P0060001', '006', '객체지향프로그래밍', '전공', 3, 10, 10, '명신관701');
INSERT INTO COURSE VALUES ('21003066-001', '숙명여자대학교', 'P0060002', '006', '데이터구조', '전공', 3, 15, 15, '명신관701');
INSERT INTO COURSE VALUES ('21003066-002', '숙명여자대학교', 'P0060002', '006', '데이터구조', '전공', 3, 15, 15, '명신관702');
INSERT INTO COURSE VALUES ('21102524-001', '숙명여자대학교', 'P0060002', '006', '프로그래밍입문', '전공', 3, 15, 15, '명신관505');
INSERT INTO COURSE VALUES ('21102521-001', '숙명여자대학교', 'P0040002', '004', '복지와제도', '교양', 3, 6, 6, '진리관B102');
INSERT INTO COURSE VALUES ('21000912-001', '숙명여자대학교', 'P0040002', '004', '사랑과헌법', '교양', 3, 6, 6, '진리관B102');

SELECT * FROM COURSE ORDER BY COURSE_CODE;

INSERT INTO DAYS VALUES ('Mon', 1);
INSERT INTO DAYS VALUES ('Tue', 2);
INSERT INTO DAYS VALUES ('Wed', 3);
INSERT INTO DAYS VALUES ('Thu', 4);
INSERT INTO DAYS VALUES ('Fri', 5);
INSERT INTO DAYS VALUES ('Sat', 6);

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21050296-001', 'Mon', '15:00', '16:15');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21050296-001', 'Wed', '15:00', '16:15');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21000008-001', 'Tue', '12:00', '13:15');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21000008-001', 'Thu', '12:00', '13:15');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21000001-001', 'Mon', '10:30', '11:45');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21000001-001', 'Wed', '10:30', '11:45');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21001952-001', 'Tue', '10:30', '11:45');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21001952-001', 'Thu', '10:30', '11:45');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21002313-001', 'Mon', '12:00', '13:15');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21002313-001', 'Wed', '12:00', '13:15');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21002103-001', 'Mon', '10:30', '11:45');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21002103-001', 'Wed', '10:30', '11:45');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21003066-001', 'Mon', '12:00', '13:15');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21003066-001', 'Wed', '12:00', '13:15');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21003066-002', 'Mon', '15:00', '16:15');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21003066-002', 'Wed', '15:00', '16:15');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21102524-001', 'Tue', '13:30', '14:45');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21102524-001', 'Thu', '13:30', '14:45');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21102521-001', 'Tue', '12:00', '13:15');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21102521-001', 'Thu', '12:00', '13:15');

INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21000912-001', 'Tue', '13:15', '14:45');
INSERT INTO COURSE_TIME VALUES ('숙명여자대학교', '21000912-001', 'Thu', '13:15', '14:45');

SELECT * FROM COURSE_TIME;

INSERT INTO student VALUES ('숙명여자대학교', '1910776', '001', '이지영', '2001-02-02', '010-4765-6540', 'lilydodo11@gmail.com');
INSERT INTO student VALUES ('숙명여자대학교', '2332114', '001', '김눈송', '2005-12-22', '010-1234-5678', 'noonsong@gmail.com');
INSERT INTO student VALUES ('숙명여자대학교', '1825771', '001', '박구름', '2000-08-06', '010-5678-8520', 'cloudpark@gmail.com');
INSERT INTO student VALUES ('숙명여자대학교', '2018334', '006', '선우미호', '2001-10-23', '010-7979-4646', 'seonu1234@gmail.com');
INSERT INTO student VALUES ('숙명여자대학교', '2212345', '006', '공유', '2004-04-16', '010-5657-4521', 'share@gmail.com');
INSERT INTO student VALUES ('숙명여자대학교', '1911226', '006', '권선경', '2000-11-27', '010-7657-4321', 'sunny@gmail.com');

SELECT * FROM STUDENT;

SELECT * FROM MEMBER;

SELECT m.ID, m.NICKNAME, m.ROLE, u.UNIV_NAME, u.EMBLEM, u.SYMBOL_COLOR FROM MEMBER m, UNIV u 
WHERE m.UNIV_NAME = u.UNIV_NAME 
AND m.UNIV_NAME = '숙명여자대학교' AND m.ID = '1910776' AND m.PASSWORD = 'WELCOME010202!';
        
SELECT * FROM
	(SELECT ROWNUM rnum, DATA.* FROM
		(SELECT c.course_name, c.course_code, p.name,
			s.subject_title, c.classification, c.classroom, d.start_time, d.end_time,
			LISTAGG(d.course_day, ', ') WITHIN GROUP (ORDER BY dd.day_order) AS course_day_str,
			c.rest_seat, c.max_student, c.major_code
			FROM COURSE_TIME d
			JOIN COURSE c ON c.course_code = d.course_code
			JOIN (SELECT * FROM PROFESSOR WHERE univ_name = '숙명여자대학교') p ON p.id = c.professor_id
			JOIN SUBJECT s ON c.major_code = s.subject_code
			JOIN DAYS dd ON d.course_day = dd.day_name
			WHERE c.univ_name = '숙명여자대학교'
			AND c.major_code LIKE '%' || '' || '%'
			AND c.classification LIKE '%' || '' || '%'
			GROUP BY c.course_name, c.course_code, p.name, s.subject_title,
			c.classification, c.rest_seat, c.max_student, c.classroom, d.start_time, d.end_time, c.major_code
		) DATA
	)
WHERE rnum BETWEEN 1 AND 100;

SELECT NVL(COUNT(*), 0) FROM COURSE
        WHERE UNIV_NAME = '숙명여자대학교'
        AND major_code LIKE '%' || '' || '%'
        AND classification LIKE '%' || '' || '%';
       
SELECT p.UNIV_NAME, p.ID, s.SUBJECT_TITLE, p.NAME, TO_CHAR(p.BIRTH, 'YYYY-MM-dd') AS BIRTH, p.EMAIL, p.PHONE
FROM STUDENT p, SUBJECT s
WHERE p.MAJOR_CODE = s.SUBJECT_CODE 
AND UNIV_NAME = '숙명여자대학교' AND ID = '1910776';

SELECT p.UNIV_NAME, p.ID, s.SUBJECT_TITLE, p.NAME, TO_CHAR(p.BIRTH, 'YYYY-MM-dd') AS BIRTH, p.EMAIL, p.PHONE
FROM PROFESSOR p, SUBJECT s
WHERE p.MAJOR_CODE = s.SUBJECT_CODE
AND UNIV_NAME = '숙명여자대학교' AND ID = '0010003';

SELECT s.UNIV_NAME, s.ID , d.DEPT_NAME , s.NAME , TO_CHAR(s.BIRTH, 'YYYY-MM-dd') AS BIRTH, s.EMAIL, s.PHONE
FROM STAFF s, DEPARTMENT d 
WHERE s.DEPT_NO = d.DEPT_NO
AND UNIV_NAME = '숙명여자대학교' AND ID = '1001';

--UPDATE STUDENT SET NAME = '이지인', PHONE = '010-4157-6540', EMAIL = 'rileydodo123@naver.com'
--WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776';

--UPDATE MEMBER SET NICKNAME = '닉네임' WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776';

--INSERT INTO ENROLL VALUES ('숙명여자대학교', '1910776', '001', '21000001-001');

--UPDATE COURSE SET REST_SEAT = REST_SEAT - 1 WHERE COURSE_CODE = '21000001-001';

SELECT *
        FROM (
        SELECT ROWNUM rnum, DATA.*
        FROM (
        SELECT c.course_name, c.course_code, p.name,
        s.subject_title, c.classification, c.classroom, d.start_time, d.end_time,
        LISTAGG(d.course_day, ', ') WITHIN GROUP (ORDER BY dd.day_order) AS course_day_str,
        c.rest_seat, c.max_student, c.major_code
        FROM COURSE_TIME d
        JOIN COURSE c ON c.course_code = d.course_code
        JOIN (SELECT * FROM PROFESSOR WHERE univ_name = '숙명여자대학교') p ON p.id = c.professor_id
        JOIN SUBJECT s ON c.major_code = s.subject_code
        JOIN (SELECT * FROM ENROLL WHERE univ_name = '숙명여자대학교' AND id = '1910776') e ON c.course_code = e.course_code
        JOIN DAYS dd ON d.course_day = dd.day_name
        WHERE c.univ_name = '숙명여자대학교'
        AND c.major_code LIKE '%' || '' || '%'
        AND c.classification LIKE '%' || '' || '%'
        GROUP BY c.course_name, c.course_code, p.name, s.subject_title,
        c.classification, c.rest_seat, c.max_student, c.classroom, d.start_time, d.end_time, c.major_code
        ) DATA
        )
        WHERE rnum BETWEEN 1 AND 100;
        
SELECT NVL(COUNT(*), 0) FROM ENROLL
WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776';

SELECT * FROM ENROLL;

--DELETE FROM ENROLL
--        WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776'
--        AND COURSE_CODE = '21000001-001';

--UPDATE COURSE SET REST_SEAT = REST_SEAT + 1 WHERE COURSE_CODE = '21003066-001';

SELECT COUNT(*)
        FROM ENROLL e
        JOIN COURSE c ON e.course_code = c.course_code
        JOIN COURSE_TIME ct ON c.course_code = ct.course_code
        WHERE e.univ_name = '숙명여자대학교'
        AND e.id = '1910776'
        AND ct.course_day IN ('Mon', 'Wed')
        AND (
            (TO_DATE('12:00', 'HH24:MI') BETWEEN TO_DATE(ct.start_time, 'HH24:MI') AND TO_DATE(ct.end_time, 'HH24:MI'))
            OR
            (TO_DATE('13:15', 'HH24:MI') BETWEEN TO_DATE(ct.start_time, 'HH24:MI') AND TO_DATE(ct.end_time, 'HH24:MI'))
            OR
            (TO_DATE(ct.start_time, 'HH24:MI') BETWEEN TO_DATE('12:00', 'HH24:MI') AND TO_DATE('13:15', 'HH24:MI'))
        );
        
SELECT * FROM (
	SELECT c.univ_name, d.start_time, d.end_time, d.course_day
        FROM COURSE c
        JOIN COURSE_TIME d ON c.course_code = d.course_code AND c.UNIV_NAME = d.UNIV_NAME
        JOIN DAYS dd ON d.course_day = dd.day_name
        WHERE c.UNIV_NAME = '숙명여자대학교' AND c.COURSE_CODE = '21000001-001'
);

SELECT c.course_code, c.univ_name, d.start_time, d.end_time, d.course_day
        FROM COURSE c
        JOIN COURSE_TIME d ON c.course_code = d.course_code AND c.UNIV_NAME = d.UNIV_NAME
        JOIN DAYS dd ON d.course_day = dd.day_name
        WHERE c.UNIV_NAME = '숙명여자대학교' AND c.COURSE_CODE = '21000001-001';
        
--UPDATE MEMBER SET PASSWORD = '123'
--WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776';
       
--UPDATE MEMBER SET PASSWORD = 'WELCOME' || to_char(
--	(SELECT birth FROM STUDENT 
--		WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776')
--, 'YYMMdd') || '!'
--WHERE UNIV_NAME = '숙명여자대학교' AND ID = '1910776';

SELECT * FROM MEMBER;

DROP TABLE verification_codes;
DROP SEQUENCE verification_codes_seq;

CREATE TABLE verification_codes (
    id INT PRIMARY KEY,
    user_id VARCHAR2(255),
    code VARCHAR(10),
    valid VARCHAR(1) DEFAULT 'N'
);

CREATE SEQUENCE verification_codes_seq
    START WITH 1
    INCREMENT BY 1
    NOMAXVALUE;
    
SELECT * FROM (
        SELECT * FROM verification_codes WHERE user_id = '1910776' ORDER BY id DESC
        ) WHERE ROWNUM = 1;

SELECT * FROM MEMBER;

SELECT m.ID, m.NICKNAME, m.ROLE, u.UNIV_NAME, u.EMBLEM, u.SYMBOL_COLOR
        FROM MEMBER m, UNIV u
        WHERE m.UNIV_NAME = u.UNIV_NAME
        AND m.UNIV_NAME = '숙명여자대학교' AND m.ID = '1910776' AND m.PASSWORD = 'WELCOME010202!';