-- 创建资源表
CREATE TABLE IF NOT EXISTS `resource` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
  `user_id` BIGINT NOT NULL COMMENT '上传者ID',
  `title` VARCHAR(255) NOT NULL COMMENT '资源标题',
  `description` TEXT COMMENT '资源描述',
  `content` TEXT COMMENT '资源内容',
  `type` TINYINT NOT NULL COMMENT '资源类型 1-书籍 2-实验指南 3-视频教程 4-软件工具',
  `cover_url` VARCHAR(255) COMMENT '封面图URL',
  `download_url` VARCHAR(255) COMMENT '资源文件URL',
  `download_count` INT DEFAULT 0 COMMENT '下载次数',
  `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX `idx_user` (`user_id`),
  INDEX `idx_type` (`type`),
  INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';
