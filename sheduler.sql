CREATE TABLE tasks
(
    Id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    Content VARCHAR(1000) NOT NULL COMMENT '내용',
    Name VARCHAR(100) NOT NULL COMMENT '관리자명',
    CreatedAt VARCHAR(100) NOT NULL COMMENT '작성일',
    UpdatedAt VARCHAR(100) NOT NULL COMMENT '수정일',
    Password VARCHAR(100) NOT NULL COMMENT '비밀번호'
);