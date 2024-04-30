drop database board;

-- 한글 저장을 위해 UTF-8를 캐릭터 셋으로 하는 데이터베이스 생성
CREATE DATABASE board DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

use board;

select * from member;
select * from board;
select * from comment;

use shop;

select * from member;
select * from item;
select * from item_img;
select * from orders;
select * from order_item;

UPDATE item SET reg_time = '2024-03-24' WHERE item_id = 1;
UPDATE item SET reg_time = '2024-04-23' WHERE item_id = 2;
UPDATE item SET reg_time = '2024-04-22' WHERE item_id = 3;
UPDATE item SET reg_time = '2024-04-21' WHERE item_id = 4;
UPDATE item SET reg_time = '2024-04-01' WHERE item_id = 5;
UPDATE item SET reg_time = '2024-04-02' WHERE item_id = 6;
UPDATE item SET reg_time = '2024-02-02' WHERE item_id = 7;
UPDATE item SET reg_time = '2024-02-02' WHERE item_id = 8;
UPDATE item SET reg_time = '2024-02-02' WHERE item_id = 9;
UPDATE item SET reg_time = '2024-02-02' WHERE item_id = 10;

UPDATE item SET create_by = 'person@naver.com' WHERE item_id = 1;
UPDATE item SET create_by = 'person@naver.com' WHERE item_id = 2;
UPDATE item SET create_by = 'person@naver.com' WHERE item_id = 3;