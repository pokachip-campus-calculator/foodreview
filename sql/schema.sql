-- CREATE TABLE (STEP 3에서 작성)

USE foodreview;

-- 1. 회원 테이블
CREATE TABLE member (
                        member_id INT AUTO_INCREMENT PRIMARY KEY,
                        login_id VARCHAR(50) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        nickname VARCHAR(50) NOT NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        withdrawn_at DATETIME NULL
);

-- 2. 음식점 테이블
CREATE TABLE restaurant (
                            restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
                            member_id INT NOT NULL,
                            name VARCHAR(100) NOT NULL,
                            address VARCHAR(255) NOT NULL,
                            region VARCHAR(100),
                            category VARCHAR(50),
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_restaurant_member
                                FOREIGN KEY (member_id)
                                    REFERENCES member(member_id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE
);

-- 3. 리뷰 테이블
CREATE TABLE review (
                        review_id INT AUTO_INCREMENT PRIMARY KEY,
                        restaurant_id INT NOT NULL,
                        member_id INT NOT NULL,
                        rating TINYINT NOT NULL,
                        content TEXT NOT NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT chk_review_rating
                            CHECK (rating BETWEEN 1 AND 5),

                        CONSTRAINT uq_review_member_restaurant
                            UNIQUE (member_id, restaurant_id),

                        CONSTRAINT fk_review_restaurant
                            FOREIGN KEY (restaurant_id)
                                REFERENCES restaurant(restaurant_id)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE,

                        CONSTRAINT fk_review_member
                            FOREIGN KEY (member_id)
                                REFERENCES member(member_id)
                                ON DELETE RESTRICT
                                ON UPDATE CASCADE
);
