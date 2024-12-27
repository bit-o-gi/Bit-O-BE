-- 사용자 데이터 삽입
INSERT INTO bit_o_user (id, email, nick_name, platform, provider_user_id, connected_dt, couple_id)
VALUES
    (1, 'kim.sender@example.com', '김보람', 'GOOGLE', 101, '2024-12-01 10:00:00', 1),
    (2, 'lee.receiver@example.com', '이민준', 'GOOGLE', 102, '2024-12-01 10:05:00', 1),
    (3, 'park.sender@example.com', '박지수', 'NAVER', 103, '2024-12-01 11:00:00', 2),
    (4, 'choi.receiver@example.com', '최서연', 'NAVER', 104, '2024-12-01 11:05:00', 2);

-- 커플 데이터 삽입
INSERT INTO couple (id, sender, receiver, status, created_at, updated_at)
VALUES
    (1, 'kim.sender@example.com', 'lee.receiver@example.com', 'APPROVED', '2024-12-01 09:55:00', '2024-12-01 09:55:00'),
    (2, 'park.sender@example.com', 'choi.receiver@example.com', 'APPROVED', '2024-12-01 10:55:00', '2024-12-01 10:55:00');

-- 커플 기념일 데이터 삽입
INSERT INTO day (id, couple_id, title, start_date, created_at, updated_at)
VALUES
    (1, 1, '첫 만남 기념일', '2024-01-01', '2024-12-01 11:00:00', '2024-12-01 11:00:00'),
    (2, 2, '연애 시작 기념일', '2023-12-01', '2024-12-01 11:30:00', '2024-12-01 11:30:00');

-- 기념일 데이터 삽입
INSERT INTO anniversary (id, write_time, update_time, title, content, anniversary_date, writer_id, with_people_id)
VALUES
    (1, '2024-12-01 12:00:00', NULL, '첫 데이트', '우리의 첫 데이트를 기념합니다.', '2024-01-10 00:00:00', 1, 2),
    (2, '2024-12-02 12:00:00', NULL, '첫 여행', '제주도로 떠난 첫 여행.', '2024-02-20 00:00:00', 2, 1),
    (3, '2024-12-03 12:00:00', NULL, '영화 관람', '함께 본 첫 영화.', '2024-03-15 00:00:00', 3, 4),
    (4, '2024-12-04 12:00:00', NULL, '생일 축하', '서로의 첫 생일 축하.', '2024-04-20 00:00:00', 4, 3),
    (5, '2024-12-05 12:00:00', NULL, '기념일 파티', '100일 기념 파티.', '2024-05-25 00:00:00', 1, 2);

-- 추가 커플 및 사용자를 위한 예제 데이터 삽입
INSERT INTO couple (id, sender, receiver, status, created_at, updated_at)
VALUES
    (3, 'kim.han@example.com', 'lee.soo@example.com', 'PENDING', '2024-12-01 12:30:00', '2024-12-01 12:30:00');

INSERT INTO bit_o_user (id, email, nick_name, platform, provider_user_id, connected_dt, couple_id)
VALUES
    (5, 'kim.han@example.com', '김한결', 'KAKAO', 105, '2024-12-01 13:00:00', NULL),
    (6, 'lee.soo@example.com', '이수연', 'KAKAO', 106, '2024-12-01 13:05:00', NULL);
