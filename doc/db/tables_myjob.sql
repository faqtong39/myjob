CREATE database if NOT EXISTS `myjob` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `myjob`;

SET NAMES utf8mb4;

CREATE TABLE `executor_group` (
  `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(64) NOT NULL COMMENT 'executor group code',
  `app_name` varchar(64) NOT NULL COMMENT 'AppName of executor which is used as group name',
  `title` varchar(12) NOT NULL COMMENT 'executor name',
  `registry_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'registry type：0=auto、1=manual',
  `update_time` datetime DEFAULT NULL,
  UNIQUE KEY `code_UNIQUE` (`code`),
  UNIQUE KEY `username_UNIQUE` (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `executor` (
  `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `executor_group_code` varchar(64) NOT NULL COMMENT 'executor group code',
  `address` varchar(64) NOT NULL COMMENT 'executor ip address',
  `update_time` datetime DEFAULT NULL,
  UNIQUE KEY `executor_UNIQUE` (`executor_group_code`,`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `job` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(64) NOT NULL COMMENT 'job code',
  `executor_group_code` varchar(64) NOT NULL COMMENT 'executor group code',
  `job_desc` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL,
  `alarm_email` text DEFAULT NULL COMMENT 'split by comma for multiple emails',
  `schedule_type` varchar(2) NOT NULL DEFAULT '0' COMMENT '0: fixed seconds; 1: cron',
  `schedule_conf` varchar(128) DEFAULT NULL COMMENT 'depend on schedule_type',
  `schedule_expire_strategy` varchar(2) NOT NULL DEFAULT '0' COMMENT '0: do nothing; 1: one time execution',
  `executor_route_strategy` varchar(2) DEFAULT NULL COMMENT '0: round robin; 1: random;',
  `job_handler` varchar(255) DEFAULT NULL COMMENT 'job handler key',
  `job_param` varchar(512) DEFAULT NULL COMMENT 'executor parameter',
  `job_timeout` int(11) NOT NULL DEFAULT '0' COMMENT 'execution timeout(unit: second)',
  `job_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT 'retry times',
  `source_type` varchar(2) NOT NULL COMMENT 'job source type. 0: Java; 1: Python; 2: NodeJs; 3: Rust',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: stop，1: running',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0',
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `log` (
  `id` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `executor_group_code` varchar(64) NOT NULL COMMENT 'executor group code',
  `job_code` varchar(64) NOT NULL COMMENT 'executor group code',
  `trigger_time` datetime DEFAULT NULL COMMENT 'trigger time',
  `trigger_code` varchar(64) NOT NULL COMMENT 'trigger result',
  `trigger_msg` text COMMENT 'trigger log',
  `execution_time` datetime DEFAULT NULL COMMENT 'execution time',
  `execution_code` varchar(64) NOT NULL COMMENT 'execution result',
  `execution_msg` text COMMENT 'execution log',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: success; 1: failure',
  UNIQUE KEY `log_UNIQUE` (`executor_group_code`,`job_code`,`trigger_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
