/** Cafe Category **/
INSERT INTO category (name, list_order, created_at) VALUES
('게임',1,now()),('만화/애니메이션',2,now()),('방송/연예',3,now()),('문화/예술',4,now()),('영화',5,now()),('음악',6,now()),
('팬카페',7,now()),('여행',8,now()),('스포츠/레저',9,now()),('애완동물',10,now()),('취미',11,now()),('생활',12,now()),
('패션,미용',13,now()),('건강,다이어트',14,now()),('가족,육아',15,now()),('컴퓨터,통신',16,now()),('교육',17,now()),('외국어',18,now());

/** Cafe **/
INSERT INTO cafe(id, category_id, url, name, description, visibility, article_count, comment_count, member_count, visit_count, created_at) values (1, 1,'cafetest','테스트 카페','테스트용 카페입니다','PUBLIC', 12345, 1234566, 160540, 1233, '2003-12-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, article_count, comment_count, member_count, visit_count, created_at) VALUES
    (1,'gamecafe1','서머너즈워:공식카페','서머너즈워 공식카페입니다','PUBLIC', 843143, 1231312, 160540, 123123, '2014-03-21 01:02:03'),
    (1,'gamecafe2','큐라레: 마법도서관 공식 카페','큐라레 공식카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2015-03-21 01:02:03'),
    (1,'gamecafe3','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2016-03-21 01:02:03'),
    (1,'gamecafe4','하스스톤 대표 카페','하스스톤 대표 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2016-04-21 01:02:03'),
    (1,'gamecafe5','PS Vita 카페','PS Vita 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-02-21 01:02:03'),
    (1,'gamecafe6','워프레임 한국팬카페','워프레임 한국팬카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2016-01-21 01:02:03'),
    (1,'gamecafe7','드래곤 플라이트 사랑 모임','드래곤 플라이트 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2016-11-21 01:02:03'),
    (1,'gamecafe8','그로우토피아 코리아 카페','그로우토피아 코리아 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03'),
    (1,'gamecafe9','Don''t starve. 굶지마 카페','굶지마 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03'),
    (1,'gamecafe10','클래시오브클랜,클래시로얄,붐비치 대표까페 KOREA xx기','CoC 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03'),
    (1,'gamecafe11','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03'),
    (1,'gamecafe12','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03'),
    (1,'gamecafe13','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');

/** Member **/
INSERT INTO user(id, username, password) VALUES
(1, 'kiel', '$2a$10$KwimrCmfFII4tsOh1R5WSet.OY2SmDdueOMiUjCS5z4xSpwHPfEZi'), -- 1234
(2, 'user', '$2a$10$8nPB6Czz76wxVBuBAvoRveNW5eUIl5Y9doMh8rDeI7y4Wy5SdGBLi'); -- password


/** CafeMember **/
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (1, 1, 'MANAGER', now());
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (2, 1, 'MANAGER', now());
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (3, 1, 'MANAGER', now());
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (4, 1, 'MANAGER', now());
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (5, 1, 'MANAGER', now());
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (6, 1, 'MANAGER', now());


/** Board **/
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, '카페태그보기', 'TAG', 1);
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, '베스트게시물', 'BEST', 2);
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, '카페 캘린더', 'CALENDAR', 3);
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, '카페북 책꽂이', 'BOOK', 4);

INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, 'test board1', 'LIST', 11);
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, 'test board2', 'LIST', 13);
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, 'test board3', 'LIST', 12);
INSERT INTO board(cafe_id, name, type, list_order) VALUES (1, 'test board4', 'LIST', 14);

/** Article **/
INSERT INTO article(board_id, writer_id, title, content, comment_count, read_count, created_at, updated_at) VALUES
    (1, 1, '테스트 첫번째 글', '테스트 글 내용 1', 0, 0, now(), now()),
    (1, 1, '테스트 두번째 글', '테스트 글 내용 2', 0, 0, now(), now()),
    (1, 1, '테스트 3번째 글', '테스트 글 내용 3', 0, 0, now(), now()),
    (1, 1, '테스트 44번째 글', '테스트 글 내용 4', 0, 0, now(), now()),
    (1, 1, '테스트 555번째 글', '테스트 글 내용 5', 0, 0, now(), now()),
    (1, 1, '테스트 6666번째 글', '테스트 글 내용 6', 0, 0, now(), now()),
    (1, 1, '테스트 7*7번째 글', '테스트 글 내용 7', 0, 0, now(), now()),
    (1, 1, '테스트 888번째 글', '테스트 글 내용 8', 0, 0, now(), now()),
    (2, 1, '테스트 99번째 보드2 글', '테스트 글 내용 9', 0, 0, now(), now()),
    (2, 1, '테스트 1010번째 보드 2글', '테스트 글 내용 10', 0, 0, now(), now());

/** Comment **/
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, updated_at) VALUES (1, 1, '덧글입니다1', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다2', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다3', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다4', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다5', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다6', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다7', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다8', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다9', now(), now());
--INSERT INTO comment(article_id, commenter_id, comment, create_date_time, update_date_time) VALUES (1, 1, '덧글입니다10', now(), now());

/** Tag **/
INSERT INTO tag(id, name, create_date_time) VALUES (1, '태그', now());
INSERT INTO tag(id, name, create_date_time) VALUES (2, '스타워즈', now());
INSERT INTO tag(id, name, create_date_time) VALUES (3, '반지의제왕', now());
INSERT INTO tag(id, name, create_date_time) VALUES (4, '어벤져스', now());
INSERT INTO tag(id, name, create_date_time) VALUES (5, '아이언맨', now());

/*** Article-Tag **/
INSERT INTO article_tag(article_id, tag_id) VALUES (1, 1);
INSERT INTO article_tag(article_id, tag_id) VALUES (1, 2);
INSERT INTO article_tag(article_id, tag_id) VALUES (1, 3);
INSERT INTO article_tag(article_id, tag_id) VALUES (2, 3);
INSERT INTO article_tag(article_id, tag_id) VALUES (2, 4);
INSERT INTO article_tag(article_id, tag_id) VALUES (2, 5);