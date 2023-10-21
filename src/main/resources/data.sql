INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (100, '샘 스미스 내한공연', '샘 스미스', '일산 킨텍스 제1전시장 4,5홀', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (101, '포스트 말론 내한공연', '포스트 말론', '서울', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (102, '찰리 푸스', '찰리 푸스', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (103, '아리아나그란데', '아리아나그란데', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (104, '찰리 푸스 2', '찰리 푸스', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (105, '샘 스미스 2', '샘 스미스', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_name, location, runtime, photo, description)
VALUES (106, '포스트 말론 2', '포스트 말론', '대전', 90, 'photo.url', 'description');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (100, 100, '2023-10-20 18:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (101, 100, '2023-10-19 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (102, 101, '2023-10-30 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (103, 101, '2023-10-29 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (104, 102, '2023-10-18 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (105, 102, '2023-10-17 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (106, 103, '2023-11-01 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (107, 103, '2023-11-02 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (114, 103, '2023-11-03 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (108, 104, '2023-10-25 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (109, 104, '2023-10-26 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (110, 105, '2023-11-02 18:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (111, 105, '2023-11-03 19:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (112, 106, '2023-11-13 17:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (113, 106, '2023-11-14 18:00');


INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (100, 100, '2023-09-01 20:30:00', '', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (101, 100, '2023-09-02 20:30:00', '', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (102, 100, '2023-08-28 20:30:00', '얼리버드 아티스트 선예매', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'EARLY_BIRD');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (103, 100, '2023-08-30 19:00:00', '얼리버드 라네코 선예매', 'https://tickets.interpark.com/', '인터파크24', 'EARLY_BIRD');

INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (104, 101, '2023-08-30 20:30:00', '', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (105, 101, '2023-08-29 20:30:00', '', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');

INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (106, 102, '2023-09-02 20:30:00', '', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (107, 102, '2023-08-26 20:30:00', '얼리버드 아티스트 선예매', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'EARLY_BIRD');

INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (108, 103, '2023-09-03 20:30:00', '', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (109, 103, '2023-08-27 19:00:00', '얼리버드 라네코 선예매', 'https://tickets.interpark.com/', '인터파크24', 'EARLY_BIRD');

INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (110, 104, '2023-10-11 20:30:00', '', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (111, 105, '2023-10-07 20:30:00', '', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, description, link, platform, ticket_type)
VALUES (112, 106, '2023-10-21 20:30:00', '', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');

