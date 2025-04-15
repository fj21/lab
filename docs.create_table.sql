create database lab;
use lab;

-- 用户表user
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `phone` char(11) COLLATE utf8mb4_unicode_ci NOT NULL UNIQUE COMMENT '用户名',
                        `username` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                        `password` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码,md5加密',
                        `salt` char(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码盐',
                        `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
                        `signature` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '个性签名',
                        `status` tinyint unsigned DEFAULT 0 COMMENT '0正常\r\n   1锁定',
                        `attention` int DEFAULT  0 comment '关注数',
                        `followers` int DEFAULT 0 comment '粉丝数',
                        `works` int DEFAULT 0 comment '创作数',
                        `likes` int DEFAULT 0 comment '获赞数',
                        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- 帖子表post
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
                        `user_id` BIGINT NOT NULL COMMENT '作者ID',
                        `content` TEXT NOT NULL COMMENT '文字内容',
                        `type` TINYINT NOT NULL COMMENT '内容类型',
                        `visibility` TINYINT DEFAULT 0 COMMENT '可见性 0 - public  1- private  2- friends-only',
                        `like_count` INT DEFAULT 0 COMMENT '点赞数',
                        `collect_count` INT DEFAULT 0 COMMENT '收藏数',
                        `comment_count` INT DEFAULT 0 COMMENT '评论数',
                        `view_count` INT DEFAULT 0 COMMENT '浏览数',
                        `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '是否删除',
                        `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                        `updated_at` DATETIME ON UPDATE CURRENT_TIMESTAMP,

                        INDEX `idx_user` (`user_id`),
                        INDEX `idx_created` (`created_at`),
                        FULLTEXT INDEX `ft_content` (`content`) WITH PARSER ngram
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='帖子表';

-- 帖子资源表post_media
DROP TABLE IF EXISTS `post_media`;
CREATE TABLE `post_media` (
                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `post_id` BIGINT NOT NULL COMMENT '所属帖子',
                              `media_type` TINYINT NOT NULL COMMENT '媒体类型  0-图片  1-视频',
                              `media_url` VARCHAR(512) NOT NULL COMMENT '资源地址',
                              `cover_url` VARCHAR(512) COMMENT '视频封面图地址',
                              `sort_order` INT DEFAULT 0 COMMENT '排序序号',
                              INDEX `idx_post` (`post_id`),
                              INDEX `idx_type` (`media_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='帖子资源表';

-- 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
                           `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `post_id` bigint unsigned NOT NULL COMMENT '被评论的视频',
                           `user_id` int unsigned NOT NULL COMMENT '发表评论的用户',
                           `parent_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联的1级评论id，如果是一级评论，则值为0',
                           `content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论的内容',
                           `liked_num` bigint UNSIGNED NULL DEFAULT NULL COMMENT '点赞数',
                           `status` tinyint unsigned DEFAULT 0 COMMENT '0正常\r\n   1删除不可见',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='评论表';

-- 私信信息表
DROP TABLE IF EXISTS `private_message`;
CREATE TABLE `private_message` (
                                      `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键Id',
                                      `sender_id` bigint unsigned NOT NULL COMMENT '发送者id',
                                      `receiver_id` bigint unsigned NOT NULL COMMENT '接受者Id',
                                      `message_type` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '消息类型,0：普通消息 1：朋友分享 2：系统消息',
                                      `message_content` varchar(128) NOT NULL COMMENT '消息内容',
                                      `status` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '消息状态：0正常    1删除',
                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                                      PRIMARY KEY  (`id`)
)  ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='私信信息表';

-- 用户点赞表
DROP TABLE IF EXISTS `post_like`;
CREATE TABLE `post_like` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `author_id` int unsigned NOT NULL COMMENT '发布帖子的用户',
                             `video_id` bigint unsigned NOT NULL COMMENT '被点赞的帖子',
                             `user_id` int unsigned NOT NULL COMMENT '点赞的用户',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户点赞表';

-- 用户收藏表
DROP TABLE IF EXISTS `post_collect`;
CREATE TABLE `post_collect` (
                                `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `author_id` int unsigned NOT NULL COMMENT '发布视频的用户',
                                `video_id` bigint unsigned NOT NULL COMMENT '被收藏的视频',
                                `user_id` int unsigned NOT NULL COMMENT '收藏的用户',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户收藏表';

-- 用户关注表
DROP TABLE IF EXISTS `follow `;
CREATE TABLE `tb_follow ` (
                              `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `follow_id` int unsigned NOT NULL COMMENT '被关注的用户',
                              `user_id` int unsigned NOT NULL COMMENT '操作关注用户',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户关注表';

