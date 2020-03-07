-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: 2020-03-06 22:58:20
-- 服务器版本： 5.5.62-log
-- PHP Version: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `community2`
--

-- --------------------------------------------------------

--
-- 表的结构 `ad`
--

CREATE TABLE `ad` (
  `id` int(11) NOT NULL,
  `title` varchar(256) DEFAULT NULL,
  `url` varchar(512) DEFAULT NULL,
  `image` varchar(256) DEFAULT NULL,
  `gmt_start` bigint(20) NOT NULL,
  `gmt_end` bigint(20) NOT NULL,
  `gmt_create` bigint(20) NOT NULL,
  `gmt_modified` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `pos` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL COMMENT '父类id',
  `type` int(11) NOT NULL COMMENT '父类类型',
  `commentator` bigint(20) NOT NULL COMMENT '评论人id',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '更新时间',
  `like_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `content` varchar(1024) NOT NULL,
  `comment_count` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 表的结构 `news`
--

CREATE TABLE `news` (
  `news_id` bigint(20) NOT NULL COMMENT '主键',
  `id` varchar(40) DEFAULT NULL,
  `title` varchar(128) CHARACTER SET utf8mb4 NOT NULL,
  `description` text CHARACTER SET utf8mb4 NOT NULL,
  `html` text CHARACTER SET utf8mb4 NOT NULL,
  `source` varchar(16) NOT NULL,
  `link` varchar(64) NOT NULL,
  `pub_date` varchar(24) NOT NULL,
  `imageurl1` varchar(128) DEFAULT NULL,
  `imageurl2` varchar(128) DEFAULT NULL,
  `imageurl3` varchar(128) DEFAULT NULL,
  `tag` varchar(128) DEFAULT NULL,
  `view_count` int(11) NOT NULL DEFAULT '0',
  `comment_count` int(11) NOT NULL DEFAULT '0',
  `like_count` int(11) NOT NULL DEFAULT '0',
  `gmt_latest_comment` bigint(20) NOT NULL,
  `status` int(2) NOT NULL DEFAULT '1',
  `column2` int(2) NOT NULL DEFAULT '0',
  `gmt_create` bigint(20) NOT NULL,
  `gmt_modified` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `notification`
--

CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL,
  `notifier` bigint(20) NOT NULL,
  `receiver` bigint(20) NOT NULL,
  `outerid` bigint(20) NOT NULL,
  `type` int(1) NOT NULL COMMENT '1回问，2回评，3收藏，4点赞',
  `gmt_create` bigint(20) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '0未读，1已读',
  `notifier_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `outer_title` varchar(1024) CHARACTER SET utf8mb4 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `question`
--

CREATE TABLE `question` (
  `id` bigint(20) NOT NULL,
  `title` varchar(120) NOT NULL,
  `description` text CHARACTER SET utf8mb4,
  `gmt_create` bigint(20) NOT NULL,
  `gmt_modified` bigint(20) NOT NULL,
  `creator` bigint(20) NOT NULL,
  `comment_count` int(11) NOT NULL DEFAULT '0',
  `view_count` int(11) NOT NULL DEFAULT '0',
  `like_count` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(256) DEFAULT NULL,
  `gmt_latest_comment` bigint(20) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '1加精，2置顶，3精+顶',
  `column2` int(3) DEFAULT '2' COMMENT '专栏号',
  `permission` int(3) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `question`
--

INSERT INTO `question` (`id`, `title`, `description`, `gmt_create`, `gmt_modified`, `creator`, `comment_count`, `view_count`, `like_count`, `tag`, `gmt_latest_comment`, `status`, `column2`, `permission`) VALUES
(1, '欢迎访问，又一个NiterForum社区', '<p id=\"descriptionP\"><p><img src=\"/images/smile/douyin/bly.png\" alt=\"undefined\" data-w-e=\"1\" style=\"height: 20px;\">感谢您选用<a href=\"https://niter.cn/\" target=\"_blank\">NiterForum</a>社区</p><p><img src=\"/images/smile/douyin/bn8.png\" alt=\"undefined\" data-w-e=\"1\" style=\"height: 20px;\">关于站点的<span style=\"\\&quot;color:\" rgb(0,=\"\" 255,=\"\" 0);\\\"=\"\">配置</span>，您可以参考此帖：<a href=\"\\&quot;https://niter.cn/p/135\\&quot;\">https://niter.cn/p/135</a>&nbsp;&nbsp;</p><p><img src=\"/images/smile/douyin/bmp.png\" alt=\"undefined\" data-w-e=\"1\" style=\"height: 20px;\">关于<span style=\"\\&quot;color:\" rgb(249,=\"\" 150,=\"\" 59);\\\"=\"\">更新</span>日志，您可以关注此帖：<a href=\"\\&quot;https://niter.cn/p/26\\&quot;\">https://niter.cn/p/26</a>&nbsp;&nbsp;</p><p><img src=\"/images/smile/douyin/bp6.png\" alt=\"undefined\" data-w-e=\"1\" style=\"height: 20px;\">我们已经将<span style=\"\\&quot;color:\" rgb(255,=\"\" 0,=\"\" 0);\\\"=\"\">最新</span>版本的代码上传到了<a href=\"https://github.com/yourkevin/NiterForum/\" target=\"_blank\"><img style=\"height: 12px;\" src=\"https://api.d5.nz/api/favicon/?url=https://github.com/yourkevin/NiterForum/\">Github</a>与<a href=\"https://gitee.com/yourkevin/NiterForum/\" target=\"_blank\"><img style=\"height: 12px;\" src=\"https://api.d5.nz/api/favicon/?url=https://gitee.com/yourkevin/NiterForum/\">码云</a>，欢迎下载体验。</p><p><img src=\"/images/smile/douyin/blx.png\" alt=\"undefined\" data-w-e=\"1\" style=\"height: 20px;\">您可以编辑或者删除掉此帖。</p><p><br></p></p><p><br></p>', 1583505927481, 1583506092096, 1, 0, 6, 0, '教程,公告', 1583505927481, 1, 4, 0);

-- --------------------------------------------------------

--
-- 表的结构 `thumb`
--

CREATE TABLE `thumb` (
  `id` bigint(20) NOT NULL,
  `target_id` bigint(20) NOT NULL COMMENT '点赞目标问题/回复',
  `type` int(11) NOT NULL COMMENT '目标类型',
  `liker` bigint(20) NOT NULL COMMENT '点赞者',
  `gmt_create` bigint(20) NOT NULL,
  `gmt_modified` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `account_id` varchar(100) DEFAULT NULL,
  `qq_account_id` varchar(64) DEFAULT NULL,
  `baidu_account_id` varchar(100) DEFAULT NULL,
  `weibo_account_id` varchar(100) DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `token` char(36) DEFAULT NULL,
  `gmt_create` bigint(20) DEFAULT NULL,
  `gmt_modified` bigint(20) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT '/images/default-avatar.png',
  `email` varchar(32) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `account_id`, `qq_account_id`, `baidu_account_id`, `weibo_account_id`, `name`, `token`, `gmt_create`, `gmt_modified`, `avatar_url`, `email`, `phone`, `password`) VALUES
(1, NULL, NULL, NULL, NULL, 'NiterBot', 'cccfcdb1-17d6-4183-a4e2-5ca911ba7199', 1583505749783, 1583506631145, '/images/avatar/default.png', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `user_account`
--

CREATE TABLE `user_account` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `group_id` int(3) DEFAULT '1',
  `vip_rank` int(1) DEFAULT '0',
  `score` int(11) DEFAULT '0',
  `score1` int(11) DEFAULT '0',
  `score2` int(11) DEFAULT '0',
  `score3` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `user_account`
--

INSERT INTO `user_account` (`id`, `user_id`, `group_id`, `vip_rank`, `score`, `score1`, `score2`, `score3`) VALUES
(1, 1, 1, 0, 20, 10, 2, 1);

-- --------------------------------------------------------

--
-- 表的结构 `user_info`
--

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `realname` varchar(50) DEFAULT NULL,
  `userdetail` varchar(1024) DEFAULT NULL,
  `birthday` varchar(20) DEFAULT NULL,
  `marriage` varchar(5) DEFAULT NULL,
  `sex` varchar(5) DEFAULT '男',
  `blood` varchar(5) DEFAULT NULL,
  `figure` varchar(5) DEFAULT NULL,
  `constellation` varchar(20) DEFAULT NULL,
  `education` varchar(20) DEFAULT NULL,
  `trade` varchar(50) DEFAULT NULL,
  `job` varchar(50) DEFAULT NULL,
  `location` varchar(30) DEFAULT '北京-北京-东城区'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `user_info`
--

INSERT INTO `user_info` (`id`, `user_id`, `realname`, `userdetail`, `birthday`, `marriage`, `sex`, `blood`, `figure`, `constellation`, `education`, `trade`, `job`, `location`) VALUES
(1, 1, NULL, '大家好，这里是NiterForum官方机器人', '1987-04-03', NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ad`
--
ALTER TABLE `ad`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `news`
--
ALTER TABLE `news`
  ADD PRIMARY KEY (`news_id`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `thumb`
--
ALTER TABLE `thumb`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_account`
--
ALTER TABLE `user_account`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `user_info`
--
ALTER TABLE `user_info`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `ad`
--
ALTER TABLE `ad`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `comment`
--
ALTER TABLE `comment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `news`
--
ALTER TABLE `news`
  MODIFY `news_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键';

--
-- 使用表AUTO_INCREMENT `notification`
--
ALTER TABLE `notification`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `question`
--
ALTER TABLE `question`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `thumb`
--
ALTER TABLE `thumb`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `user_account`
--
ALTER TABLE `user_account`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `user_info`
--
ALTER TABLE `user_info`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
