INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (100, 'Conan Gray 내한공연', '4Uc8Dsxct0oMqx0P6i60ea', '일산 킨텍스 제1전시장 4,5홀', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (101, 'The Vamps 내한공연', '7gAppWoH7pcYmphCVTXkzs', '서울', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (102, 'Peder Elias 내한공연', '56zJ6PZ3mNPBiBqglW2KxL', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (103, 'Harry Styles 내한 공연', '6KImCVD70vtIoJWnq6nGn3', '서울', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (104, '찰리 푸스 내한 공연', '6VuMaDnrHyPL1p4EHjYLi7', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (105, '샘 스미스 내한 공연', '2wY79sveU1sp5g7SokKOiI', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (106, '포스트 말론 내한 공연', '246dkjvS1zLTtiykXe5h60', '대전', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (107, 'Harry Styles 이전 내한 공연 1', '6KImCVD70vtIoJWnq6nGn3', '부산', 90, 'photo.url', 'description');
INSERT INTO Performance (id, name, artist_id, location, runtime, photo, description)
VALUES (108, 'Harry Styles 이전 내한 공연 2', '6KImCVD70vtIoJWnq6nGn3', '대전', 90, 'photo.url', 'description');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (100, 100, '2023-11-20 18:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (101, 100, '2023-11-19 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (102, 101, '2023-11-30 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (103, 101, '2023-11-29 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (104, 102, '2023-11-18 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (105, 102, '2023-11-17 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (106, 103, '2023-11-01 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (107, 103, '2023-11-02 20:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (114, 103, '2023-11-03 20:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (108, 104, '2023-11-25 20:00');
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

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (115, 107, '2022-10-13 17:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (116, 107, '2022-10-13 17:00');

INSERT INTO performance_date (id, performance_id, start_date)
VALUES (117, 108, '2023-04-11 18:00');
INSERT INTO performance_date (id, performance_id, start_date)
VALUES (118, 108, '2023-04-12 17:00');


INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (100, 100, '2023-10-01 20:30:00', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (101, 100, '2023-10-02 20:30:00', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (102, 100, '2023-10-28 20:30:00', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'EARLY_BIRD');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (103, 100, '2023-10-30 19:00:00', 'https://tickets.interpark.com/', '인터파크24', 'EARLY_BIRD');

INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (104, 101, '2023-11-30 20:30:00', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (105, 101, '2023-11-29 20:30:00', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');

INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (106, 102, '2023-11-02 20:30:00', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (107, 102, '2023-11-26 20:30:00', 'http://m.ticket.yes24.com/Perf/46461', '예스24', 'EARLY_BIRD');

INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (108, 103, '2023-11-03 20:30:00', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (109, 103, '2023-11-27 19:00:00', 'https://tickets.interpark.com/', '인터파크24', 'EARLY_BIRD');

INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (110, 104, '2023-11-11 20:30:00', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (111, 105, '2023-11-07 20:30:00', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');
INSERT INTO ticketing (id, performance_id, start_date, link, platform, ticket_type)
VALUES (112, 106, '2023-11-21 20:30:00', 'https://tickets.interpark.com/', '인터파크24', 'REGULAR');

INSERT INTO USERS(user_id, nickname, oauth2id, profile_image, provider, refresh_token, role)
VALUES (100, '', '', '', '', '', 'USER');
INSERT INTO USERS(user_id, nickname, oauth2id, profile_image, provider, refresh_token, role)
VALUES (101, '', '', '', '', '', 'USER');
INSERT INTO USERS(user_id, nickname, oauth2id, profile_image, provider, refresh_token, role)
VALUES (102, '', '', '', '', '', 'USER');
INSERT INTO USERS(user_id, nickname, oauth2id, profile_image, provider, refresh_token, role)
VALUES (103, '', '', '', '', '', 'USER');
INSERT INTO USERS(user_id, nickname, oauth2id, profile_image, provider, refresh_token, role)
VALUES (104, '', '', '', '', '', 'USER');

INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (100, 100, 101);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (101, 101, 101);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (102, 102, 101);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (103, 103, 101);

INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (104, 100, 102);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (105, 101, 102);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (106, 102, 102);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (107, 103, 102);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (108, 104, 102);

INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (109, 100, 103);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (111, 101, 103);

INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (110, 100, 104);

INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (112, 100, 105);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (113, 101, 105);
INSERT INTO performance_heart (id, user_id, performance_id)
VALUES (114, 102, 105);