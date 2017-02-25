/** Cafe Category **/
INSERT INTO category (name, create_date_time, list_order) VALUES ('게임',now(),1),('만화/애니메이션',now(),2),('방송/연예',now(),3),('문화/예술',now(),4),('영화',now(),5),('음악',now(),6),('팬카페',now(),7),('여행',now(),8),('스포츠/레저',now(),9),('애완동물',now(),10),('취미',now(),11),('생활',now(),12);

/** Cafe **/
INSERT INTO cafe(id, category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1, 1,'cafetest','테스트 카페','테스트용 카페입니다','PUBLIC', 12345, 1234566, 160540, 1233, '2003-12-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe1','서머너즈워:공식카페','서머너즈워 공식카페입니다','PUBLIC', 843143, 1231312, 160540, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe2','큐라레: 마법도서관 공식 카페','큐라레 공식카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe3','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe4','하스스톤 대표 카페','하스스톤 대표 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe5','PS Vita 카페','PS Vita 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe6','워프레임 한국팬카페','워프레임 한국팬카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe7','드래곤 플라이트 사랑 모임','드래곤 플라이트 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe8','그로우토피아 코리아 카페','그로우토피아 코리아 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe9','Don''t starve. 굶지마 카페','굶지마 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe10','클래시오브클랜,클래시로얄,붐비치 대표까페 KOREA xx기','CoC 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe11','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe12','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');
INSERT INTO cafe(category_id, url, name, description, visibility, cafe_article_count, cafe_comment_count, cafe_member_count, cafe_visit_count, create_date_time) values (1,'gamecafe13','샌드박스 팬 카페','샌드박스 팬 카페입니다','PUBLIC', 898790, 1231312, 58582, 123123, '2014-03-21 01:02:03');

/** Member **/
INSERT INTO member(id, nickname) VALUES (1, 'kiel');

/** CafeMember **/
INSERT INTO cafe_member(cafe_id, member_id, role, join_date) VALUES (1, 1, 'MANAGER', now());

/** Board **/
INSERT INTO board(cafe_id, name, list_order) VALUES (1, 'test board1', 1);
INSERT INTO board(cafe_id, name, list_order) VALUES (1, 'test board2', 3);
INSERT INTO board(cafe_id, name, list_order) VALUES (1, 'test board3', 2);
INSERT INTO board(cafe_id, name, list_order) VALUES (1, 'test board4', 4);