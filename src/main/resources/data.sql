INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (100, '샘 스미스 내한공연', '샘 스미스', '일산 킨텍스 제1전시장 4,5홀', 90, '', '');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (101, '포스트 말론 내한공연', '포스트 말론', '서울', 90, '', '');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (102, '찰리 푸스', '찰리 푸스', '대전', 90, '', '');

INSERT INTO performance_date (id, performance_pk, start_date)
VALUES (100, 100, '2023-10-21 18:00');
INSERT INTO performance_date (id, performance_pk, start_date)
VALUES (101, 100, '2023-10-20 20:00');

INSERT INTO performance_date (id, performance_pk, start_date)
VALUES (102, 101, '2023-10-30 20:00');
INSERT INTO performance_date (id, performance_pk, start_date)
VALUES (103, 101, '2023-10-29 20:00');

INSERT INTO performance_date (id, performance_pk, start_date)
VALUES (104, 102, '2023-10-18 20:00');
INSERT INTO performance_date (id, performance_pk, start_date)
VALUES (105, 102, '2023-10-17 20:00');

