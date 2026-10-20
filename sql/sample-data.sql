-- INSERT 샘플 데이터 (STEP 3에서 작성)

USE foodreview;

-- 1. 회원 데이터
INSERT INTO member (login_id, password_hash, nickname) VALUES
                                                           ('user01', 'test_hash_01', '먹보'),
                                                           ('user02', 'test_hash_02', '맛집탐험가'),
                                                           ('user03', 'test_hash_03', '밥심'),
                                                           ('user04', 'test_hash_04', '미식가'),
                                                           ('user05', 'test_hash_05', '테스터');

-- 2. 음식점 데이터
INSERT INTO restaurant (member_id, name, address, category) VALUES
                                                                (1, '할매국밥', '경기도 성남시 수정구 예시로 1', '한식'),
                                                                (2, '스시하루', '경기도 성남시 분당구 예시로 2', '일식'),
                                                                (3, '불맛곱창', '경기도 성남시 중원구 예시로 3', '한식');

-- 3. 리뷰 데이터
INSERT INTO review (restaurant_id, member_id, rating, content) VALUES
                                                                   (1, 2, 5, '국물이 정말 맛있어요.'),
                                                                   (1, 3, 5, '든든하게 먹기 좋아요.'),
                                                                   (1, 4, 4, '맛있지만 조금 짰어요.'),
                                                                   (2, 1, 5, '신선하고 맛있어요.'),
                                                                   (2, 3, 4, '구성이 괜찮아요.'),
                                                                   (2, 4, 4, '무난하게 맛있어요.');
