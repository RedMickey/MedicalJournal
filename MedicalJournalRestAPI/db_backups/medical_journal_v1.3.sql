-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Мар 08 2019 г., 23:25
-- Версия сервера: 5.7.23
-- Версия PHP: 7.2.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `medical_journal`
--

-- --------------------------------------------------------

--
-- Структура таблицы `cycle`
--

CREATE TABLE `cycle` (
  `_id_cycle` binary(16) NOT NULL,
  `period` int(11) NOT NULL,
  `period_DM_type` int(11) NOT NULL,
  `once_a_period` int(11) DEFAULT NULL,
  `once_a_period_DM_type` int(11) DEFAULT NULL,
  `_id_week_schedule` binary(16) DEFAULT NULL,
  `_id_cycling_type` int(11) NOT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `cycle`
--

INSERT INTO `cycle` (`_id_cycle`, `period`, `period_DM_type`, `once_a_period`, `once_a_period_DM_type`, `_id_week_schedule`, `_id_cycling_type`, `synch_time`, `change_type`) VALUES
(0x0cc6a8fff7fc4deb8b925fc5e4b728b4, 10, 1, NULL, NULL, NULL, 1, '0000-00-00 00:00:00', 0),
(0x4ff2f2f4c15640a386b326e0a39223a2, 10, 1, NULL, NULL, NULL, 1, '2019-03-08 16:44:01', 2),
(0xa445de85ef99492eada7bbb28583697a, 10, 1, NULL, NULL, 0x341f1d473219461badfdf822efbfbbe7, 2, '2019-03-08 15:56:09', 1),
(0xc15836ffd96a4a68bdc29a17baeb5c8c, 10, 1, NULL, NULL, 0xdf1804061be5490e8b0385d8d73ca7be, 2, '2019-03-08 15:53:48', 1),
(0xdc2dcff56c304229986c8f9e73e74154, 13, 1, NULL, NULL, NULL, 1, '0000-00-00 00:00:00', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `cycling_type`
--

CREATE TABLE `cycling_type` (
  `_id_cycling_type` int(11) NOT NULL,
  `cycling_type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `cycling_type`
--

INSERT INTO `cycling_type` (`_id_cycling_type`, `cycling_type_name`) VALUES
(1, 'every_day'),
(2, 'specific_days'),
(3, 'day_interval');

-- --------------------------------------------------------

--
-- Структура таблицы `date_measurement_type`
--

CREATE TABLE `date_measurement_type` (
  `_id_date_measurement_type` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `date_measurement_type`
--

INSERT INTO `date_measurement_type` (`_id_date_measurement_type`, `type_name`) VALUES
(1, 'дн.'),
(2, 'нед.'),
(3, 'мес.');

-- --------------------------------------------------------

--
-- Структура таблицы `having_meals_type`
--

CREATE TABLE `having_meals_type` (
  `_id_having_meals_type` int(11) NOT NULL,
  `type_name` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `having_meals_type`
--

INSERT INTO `having_meals_type` (`_id_having_meals_type`, `type_name`) VALUES
(1, 'before'),
(2, 'with'),
(3, 'after');

-- --------------------------------------------------------

--
-- Структура таблицы `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(44);

-- --------------------------------------------------------

--
-- Структура таблицы `measurement_reminder`
--

CREATE TABLE `measurement_reminder` (
  `_id_measurement_reminder` binary(16) NOT NULL,
  `_id_measurement_type` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `_id_cycle` binary(16) DEFAULT NULL,
  `_id_having_meals_type` int(11) DEFAULT NULL,
  `having_meals_time` int(11) DEFAULT NULL,
  `annotation` text NOT NULL,
  `is_active` int(11) NOT NULL,
  `times_a_day` int(11) NOT NULL,
  `is_one_time` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `measurement_reminder`
--

INSERT INTO `measurement_reminder` (`_id_measurement_reminder`, `_id_measurement_type`, `start_date`, `_id_cycle`, `_id_having_meals_type`, `having_meals_time`, `annotation`, `is_active`, `times_a_day`, `is_one_time`, `user_id`, `synch_time`, `change_type`) VALUES
(0x0b639c91f3b540d894680ae14e9c4c8e, 1, '2019-02-13', 0x0cc6a8fff7fc4deb8b925fc5e4b728b4, 1, 313800, 'ghghfgh', 1, 2, NULL, 17, '0000-00-00 00:00:00', 0),
(0xa676b053e1cd4986b6513af7daccd2b1, 1, '2019-03-08', 0xa445de85ef99492eada7bbb28583697a, NULL, 0, '', 1, 2, NULL, 43, '2019-03-08 15:56:09', 1),
(0xd168094429b046b1a57787c3ce25bfd6, 1, '2019-03-08', 0xc15836ffd96a4a68bdc29a17baeb5c8c, NULL, 0, '', 1, 2, NULL, 43, '2019-03-08 15:53:48', 1),
(0xea4897423ac1432798a358013d771cf5, 2, '2019-02-13', 0xdc2dcff56c304229986c8f9e73e74154, NULL, NULL, '', 0, 1, NULL, 17, '0000-00-00 00:00:00', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `measurement_reminder_entry`
--

CREATE TABLE `measurement_reminder_entry` (
  `_id_measur_remind_entry` binary(16) NOT NULL,
  `value1` double DEFAULT NULL,
  `value2` double DEFAULT NULL,
  `_id_measurement_reminder` binary(16) NOT NULL,
  `is_done` int(11) NOT NULL,
  `reminder_time` time DEFAULT NULL,
  `reminder_date` datetime DEFAULT NULL,
  `is_one_time` int(11) DEFAULT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `measurement_reminder_entry`
--

INSERT INTO `measurement_reminder_entry` (`_id_measur_remind_entry`, `value1`, `value2`, `_id_measurement_reminder`, `is_done`, `reminder_time`, `reminder_date`, `is_one_time`, `synch_time`, `change_type`) VALUES
(0x15cc9c9d61cf4edbb2262e8ad6e19f43, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:00:00', '2019-03-12 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0x421b6b0a729f4d508ddaccb2e7d3a3aa, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:00:00', '2019-03-13 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0x48c2e9e31c904d50a04947baf61fa5c3, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:56:00', '2019-03-14 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0x5919480c582a4b6284ba88db57d94ed6, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:56:00', '2019-03-11 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0x6d3732867e86450cb2b523f8e44d5def, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:00:00', '2019-03-11 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0x996ee8cce7ff478e8d7dec5cb16d8629, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:56:00', '2019-03-12 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0xa379947834b8450486800ad75f0dac96, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:56:00', '2019-03-13 00:00:00', NULL, '2019-03-08 15:56:09', 1),
(0xcbbd2b40eca34526be06005d608df842, -10000, -10000, 0xa676b053e1cd4986b6513af7daccd2b1, 0, '18:00:00', '2019-03-14 00:00:00', NULL, '2019-03-08 15:56:09', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `measurement_type`
--

CREATE TABLE `measurement_type` (
  `_id_measurement_type` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL,
  `_id_measur_value_type` int(11) NOT NULL,
  `standard_min_value` double NOT NULL,
  `standard_max_value` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `measurement_type`
--

INSERT INTO `measurement_type` (`_id_measurement_type`, `type_name`, `_id_measur_value_type`, `standard_min_value`, `standard_max_value`) VALUES
(1, 'Температура', 1, 36.3, 36.9),
(2, 'Давление', 2, 80, 120);

-- --------------------------------------------------------

--
-- Структура таблицы `measurement_value_type`
--

CREATE TABLE `measurement_value_type` (
  `_id_measur_value_type` int(11) NOT NULL,
  `type_value_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `measurement_value_type`
--

INSERT INTO `measurement_value_type` (`_id_measur_value_type`, `type_value_name`) VALUES
(1, '°C'),
(2, 'мм рт. ст.');

-- --------------------------------------------------------

--
-- Структура таблицы `pill`
--

CREATE TABLE `pill` (
  `_id_pill` binary(16) NOT NULL,
  `pill_name` varchar(50) NOT NULL,
  `pill_description` text,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill`
--

INSERT INTO `pill` (`_id_pill`, `pill_name`, `pill_description`, `synch_time`, `change_type`) VALUES
(0x4a9f912c538342cb92e44796e151ff2e, 'tttt', '', '2019-03-08 16:43:31', 1),
(0x972901d0390c47c28ebb1513c6b93e27, 'yyyy', '', '2019-03-08 17:27:24', 1),
(0xbc7b120c87d244cb92aa3f54967b72be, 'tttt2', '', '2019-03-08 16:44:01', 1),
(0xe412e1da2b1b11e9b2eb708bcdaa4fda, 'таблетка1', NULL, '0000-00-00 00:00:00', 0),
(0xf70a93102b1b11e9b2eb708bcdaa4fda, 'таблетка2', NULL, '0000-00-00 00:00:00', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `pill_count_type`
--

CREATE TABLE `pill_count_type` (
  `_id_pill_count_type` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill_count_type`
--

INSERT INTO `pill_count_type` (`_id_pill_count_type`, `type_name`) VALUES
(1, 'шт'),
(2, 'капли'),
(3, 'мг'),
(4, 'гр'),
(5, 'мл');

-- --------------------------------------------------------

--
-- Структура таблицы `pill_reminder`
--

CREATE TABLE `pill_reminder` (
  `_id_pill_reminder` binary(16) NOT NULL,
  `_id_pill` binary(16) NOT NULL,
  `pill_count` int(11) NOT NULL,
  `_id_pill_count_type` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `_id_cycle` binary(16) DEFAULT NULL,
  `_id_having_meals_type` int(11) DEFAULT NULL,
  `having_meals_time` int(11) DEFAULT NULL,
  `annotation` text,
  `is_active` int(11) NOT NULL,
  `times_a_day` int(11) NOT NULL,
  `is_one_time` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill_reminder`
--

INSERT INTO `pill_reminder` (`_id_pill_reminder`, `_id_pill`, `pill_count`, `_id_pill_count_type`, `start_date`, `_id_cycle`, `_id_having_meals_type`, `having_meals_time`, `annotation`, `is_active`, `times_a_day`, `is_one_time`, `user_id`, `synch_time`, `change_type`) VALUES
(0x4849c7352b9811e9987c708bcdaa4fda, 0xe412e1da2b1b11e9b2eb708bcdaa4fda, 2, 1, '2019-02-04', 0x0cc6a8fff7fc4deb8b925fc5e4b728b4, 2, 10, 'лодлодо', 1, 2, NULL, 17, '0000-00-00 00:00:00', 0),
(0xa33723622b9811e9987c708bcdaa4fda, 0xf70a93102b1b11e9b2eb708bcdaa4fda, 3, 2, '2019-02-03', 0xdc2dcff56c304229986c8f9e73e74154, NULL, NULL, 'орпорорпо', 0, 1, NULL, 17, '0000-00-00 00:00:00', 0),
(0xc948750850be41ed95d705a29be4a8ed, 0xbc7b120c87d244cb92aa3f54967b72be, 3, 1, '2019-03-08', 0x4ff2f2f4c15640a386b326e0a39223a2, NULL, 0, '', 1, 1, NULL, 43, '2019-03-08 16:44:01', 2);

-- --------------------------------------------------------

--
-- Структура таблицы `pill_reminder_entry`
--

CREATE TABLE `pill_reminder_entry` (
  `_id_pill_reminder_entry` binary(16) NOT NULL,
  `is_done` int(11) NOT NULL,
  `reminder_date` datetime NOT NULL,
  `_id_pill_reminder` binary(16) NOT NULL,
  `is_one_time` int(11) DEFAULT NULL,
  `reminder_time` time DEFAULT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill_reminder_entry`
--

INSERT INTO `pill_reminder_entry` (`_id_pill_reminder_entry`, `is_done`, `reminder_date`, `_id_pill_reminder`, `is_one_time`, `reminder_time`, `synch_time`, `change_type`) VALUES
(0x08bfe268d0c542e7a7c3aec5c9a7c79c, 0, '2019-03-16 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x0ef7db68c9b3482dbfb2fdeeb020a8f4, 0, '2019-03-09 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x133fc7699dc943478fbdcfcf041403e6, 0, '2019-03-12 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x210d512454364714b6f75b4e3d70d1d5, 0, '2019-03-14 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x375f4df7bdae452aaea02d0a9e4855db, 0, '2019-03-13 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x38aa6fe47b3e4e29a4cd16e806d8b5c1, 0, '2019-03-12 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0x395ee141393b4246aa3701f3a7c0d73f, 0, '2019-03-08 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0x48216c590060408d876f9d0841838540, 0, '2019-03-17 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x49161a92cfa9419694fda6381940d262, 0, '2019-03-16 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0x54f374ecb98b4c089ea196935da2dc01, 0, '2019-03-08 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x72c32bba12ea445b9d43cf70cce9b502, 0, '2019-03-15 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0x773d1b26f6724ea4bd5e81df62333f34, 0, '2019-03-11 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0x80a47180ddbe4ad69c9aa87993a32349, 0, '2019-03-11 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0x8d26378b2b9611e9987c708bcdaa4fda, 1, '2019-02-20 00:02:09', 0x4849c7352b9811e9987c708bcdaa4fda, NULL, NULL, '0000-00-00 00:00:00', 0),
(0x9fbd58292b9611e9987c708bcdaa4fda, 0, '2019-02-20 09:43:54', 0x4849c7352b9811e9987c708bcdaa4fda, NULL, NULL, '0000-00-00 00:00:00', 0),
(0xab7d6f6b2b9611e9987c708bcdaa4fda, 0, '2019-02-20 00:02:08', 0x4849c7352b9811e9987c708bcdaa4fda, NULL, NULL, '0000-00-00 00:00:00', 0),
(0xb786e6042b9611e9987c708bcdaa4fda, 1, '2019-02-20 00:02:06', 0xa33723622b9811e9987c708bcdaa4fda, NULL, NULL, '0000-00-00 00:00:00', 0),
(0xbb1a48d075d54bb9a5a31bd009c2d2ca, 0, '2019-03-10 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0xd300dfa1dc4347f9ad41f33eb1a05a03, 0, '2019-03-15 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0xd5a4815e1ad44608846004299b25e708, 0, '2019-03-14 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0xdc7532652b9611e9987c708bcdaa4fda, 0, '2019-02-20 10:02:41', 0xa33723622b9811e9987c708bcdaa4fda, NULL, NULL, '0000-00-00 00:00:00', 0),
(0xde824bfc52234b74ade3a62faa4eb990, 0, '2019-03-10 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:43:31', 1),
(0xe0bf214f72754a768b9a7f9b9c4a61da, 0, '2019-03-13 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0xe69e7dc5fe3343a8b03c657a893e7b34, 0, '2019-03-17 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1),
(0xfd1abc331f274247bad9951d31a5039c, 0, '2019-03-09 00:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '19:00:00', '2019-03-08 16:44:02', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `reminder_time`
--

CREATE TABLE `reminder_time` (
  `_id_reminder_time` binary(16) NOT NULL,
  `reminder_time` time NOT NULL,
  `_id_pill_reminder` binary(16) DEFAULT NULL,
  `_id_measurement_reminder` binary(16) DEFAULT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `reminder_time`
--

INSERT INTO `reminder_time` (`_id_reminder_time`, `reminder_time`, `_id_pill_reminder`, `_id_measurement_reminder`, `synch_time`, `change_type`) VALUES
(0x0004d8a20a5044f5ab5be5ab2aad4083, '18:00:00', NULL, 0xa676b053e1cd4986b6513af7daccd2b1, '2019-03-08 15:56:09', 1),
(0x76b46cba2c384d0ba75b46f60f1078f7, '19:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '2019-03-08 16:44:02', 1),
(0x7987416822474793b0187aad69e6ec3e, '18:56:00', NULL, 0xa676b053e1cd4986b6513af7daccd2b1, '2019-03-08 15:56:09', 1),
(0x7e07e8f803d74389835b7972c7caa8eb, '19:00:00', 0xc948750850be41ed95d705a29be4a8ed, NULL, '2019-03-08 16:43:31', 1),
(0xab43415e2bba4a5b993112fca473d401, '18:53:00', NULL, 0xd168094429b046b1a57787c3ce25bfd6, '2019-03-08 15:53:48', 1),
(0xe84214a798aa4c29868fa4a66adf7022, '18:00:00', NULL, 0xd168094429b046b1a57787c3ce25bfd6, '2019-03-08 15:53:48', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `synchronization_time` timestamp NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `role_id` int(11) NOT NULL,
  `gender_id` int(11) NOT NULL,
  `birthday_year` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user`
--

INSERT INTO `user` (`user_id`, `synchronization_time`, `name`, `surname`, `email`, `password`, `role_id`, `gender_id`, `birthday_year`) VALUES
(15, '1970-01-18 22:04:30', 'testUser1', 'kek1', 'dsfsdf@kek3', '$2a$10$5AKAHbkUlxpq8i1jFoRGBOulKnrr6fsH.OkP99kX7fUHOThtWG5GG', 2, 1, 2009),
(16, '1970-01-18 22:04:30', 'testUser1', 'kek1', 'dsfsdf@kek4', '$2a$10$lUm.XOgv8UXeoXJ/Lz1MJOxslplW/6/EOJIXSDiFyvhxRExSV66t2', 2, 1, 2009),
(17, '1970-01-18 22:04:30', 'testUser1', 'kek1', 'dsfsdf@kek5', '$2a$10$UroBGiAvrBc0c0J74Cndr.WSyxwwg.GQZQY0wZT/.7N6eOICza216', 2, 1, 2009),
(18, '2019-02-06 20:43:13', 'testUser12', 'kek123', 'dsfsdf@kek6', '$2a$10$saCBCHkR8J2JSFBfFHfzXO9unnKHoqOW1O6nRu1aLQegQ0ENN7To2', 1, 3, 1999),
(19, '2019-01-23 19:06:16', 'testUser1', 'kek1', 'dsfsdf@kek7', '$2a$10$7aqzwoDh5iMYx.jKbpax3u6NZb45R3VBLvWLfeP8I7tR1j50nxLZ2', 2, 1, 2009),
(21, '2019-01-23 19:47:03', 'kek123', 'kek456', 'test@1', '$2a$10$cgnSZ9kFYW7D15gdkoQ43efhVdHuWi3vwuwRE6fSCHCF0ciXDz0nu', 1, 1, 2009),
(23, '2019-01-23 20:08:09', 'test2', 'surTest2', 'test@2', '$2a$10$zEmwqGos8bIDO3VgYXilcOSHvalUwG9qfsbfnGxP556YgwRDm2Sa.', 1, 2, 2009),
(24, '2019-01-23 20:17:22', 'hgjgh', 'hgjghj', 'fdgdf@gfd', '$2a$10$C2j7wxe1hGPqWlxbSceDn.wcL.rdfbWVFeSHasF7BlD.jVWIgfu96', 1, 1, 2009),
(27, '2019-01-23 20:18:25', 'hfgh', 'fghfgh', 'test@3', '$2a$10$renqcgJokAnrxCodDl1UE.JLTxBnEwc2jHfE3Li8Gm.3Q1yrZfYfO', 1, 1, 2009),
(28, '2019-01-25 15:13:38', 'test5', 'test5ser', 'test5@m', '$2a$10$0w/jDvIa7dgvgpuGMaH/2ONIoKutW1VXpjDBUX1EHmW5jRJlQP8xS', 1, 1, 2009),
(29, '2019-01-25 16:27:59', 'test6', 'test6ser', 'test6@m', '$2a$10$WD6l2qpP2a8td/gIZKFE1OFJhABjwk7IXeDYaKM19hlkvKbLNYXKq', 1, 2, 2007),
(43, '2019-02-23 12:15:42', 'kekNe3', 'dddd', 'new2@kek8', '$2a$10$B6oxUgmCWf9Kmd25PVAo/umVQIey/m9LGfj9z.D/q0m4H5TOK6al2', 1, 1, 2009);

-- --------------------------------------------------------

--
-- Структура таблицы `user_gender`
--

CREATE TABLE `user_gender` (
  `gender_id` int(11) NOT NULL,
  `gender_name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user_gender`
--

INSERT INTO `user_gender` (`gender_id`, `gender_name`) VALUES
(1, 'Пол'),
(2, 'Мужской'),
(3, 'Женский');

-- --------------------------------------------------------

--
-- Структура таблицы `user_role`
--

CREATE TABLE `user_role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user_role`
--

INSERT INTO `user_role` (`role_id`, `role_name`) VALUES
(1, 'admin'),
(2, 'user');

-- --------------------------------------------------------

--
-- Структура таблицы `week_schedule`
--

CREATE TABLE `week_schedule` (
  `_id_week_schedule` binary(16) NOT NULL,
  `mon` int(11) DEFAULT NULL,
  `tue` int(11) DEFAULT NULL,
  `wed` int(11) DEFAULT NULL,
  `thu` int(11) DEFAULT NULL,
  `fri` int(11) DEFAULT NULL,
  `sat` int(11) DEFAULT NULL,
  `sun` int(11) DEFAULT NULL,
  `synch_time` timestamp NOT NULL,
  `change_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `week_schedule`
--

INSERT INTO `week_schedule` (`_id_week_schedule`, `mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun`, `synch_time`, `change_type`) VALUES
(0x069530da9e674253a6ca3cb14936e001, 1, 0, 0, 0, 0, 0, 0, '0000-00-00 00:00:00', 0),
(0x341f1d473219461badfdf822efbfbbe7, 1, 1, 1, 1, 0, 0, 0, '2019-03-08 15:56:09', 1),
(0xdf1804061be5490e8b0385d8d73ca7be, 1, 1, 1, 1, 0, 0, 0, '2019-03-08 15:53:48', 1);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `cycle`
--
ALTER TABLE `cycle`
  ADD PRIMARY KEY (`_id_cycle`),
  ADD KEY `period_DM_type` (`period_DM_type`),
  ADD KEY `once_a_period_DM_type` (`once_a_period_DM_type`),
  ADD KEY `_id_cycling_type` (`_id_cycling_type`),
  ADD KEY `_id_week_schedule` (`_id_week_schedule`);

--
-- Индексы таблицы `cycling_type`
--
ALTER TABLE `cycling_type`
  ADD PRIMARY KEY (`_id_cycling_type`);

--
-- Индексы таблицы `date_measurement_type`
--
ALTER TABLE `date_measurement_type`
  ADD PRIMARY KEY (`_id_date_measurement_type`);

--
-- Индексы таблицы `having_meals_type`
--
ALTER TABLE `having_meals_type`
  ADD PRIMARY KEY (`_id_having_meals_type`);

--
-- Индексы таблицы `measurement_reminder`
--
ALTER TABLE `measurement_reminder`
  ADD PRIMARY KEY (`_id_measurement_reminder`),
  ADD KEY `_id_measurement_type` (`_id_measurement_type`),
  ADD KEY `_id_having_meals_type` (`_id_having_meals_type`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `_id_cycle` (`_id_cycle`);

--
-- Индексы таблицы `measurement_reminder_entry`
--
ALTER TABLE `measurement_reminder_entry`
  ADD PRIMARY KEY (`_id_measur_remind_entry`),
  ADD KEY `_id_measurement_reminder` (`_id_measurement_reminder`);

--
-- Индексы таблицы `measurement_type`
--
ALTER TABLE `measurement_type`
  ADD PRIMARY KEY (`_id_measurement_type`),
  ADD KEY `_id_measur_value_type` (`_id_measur_value_type`);

--
-- Индексы таблицы `measurement_value_type`
--
ALTER TABLE `measurement_value_type`
  ADD PRIMARY KEY (`_id_measur_value_type`);

--
-- Индексы таблицы `pill`
--
ALTER TABLE `pill`
  ADD PRIMARY KEY (`_id_pill`);

--
-- Индексы таблицы `pill_count_type`
--
ALTER TABLE `pill_count_type`
  ADD PRIMARY KEY (`_id_pill_count_type`);

--
-- Индексы таблицы `pill_reminder`
--
ALTER TABLE `pill_reminder`
  ADD PRIMARY KEY (`_id_pill_reminder`),
  ADD KEY `_id_pill_count_type` (`_id_pill_count_type`),
  ADD KEY `_id_having_meals_type` (`_id_having_meals_type`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `_id_pill` (`_id_pill`),
  ADD KEY `_id_cycle` (`_id_cycle`);

--
-- Индексы таблицы `pill_reminder_entry`
--
ALTER TABLE `pill_reminder_entry`
  ADD PRIMARY KEY (`_id_pill_reminder_entry`),
  ADD KEY `_id_pill_reminder` (`_id_pill_reminder`);

--
-- Индексы таблицы `reminder_time`
--
ALTER TABLE `reminder_time`
  ADD PRIMARY KEY (`_id_reminder_time`),
  ADD KEY `_id_pill_reminder` (`_id_pill_reminder`),
  ADD KEY `_id_measurement_reminder` (`_id_measurement_reminder`);

--
-- Индексы таблицы `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `role_id` (`role_id`),
  ADD KEY `gender_id` (`gender_id`);

--
-- Индексы таблицы `user_gender`
--
ALTER TABLE `user_gender`
  ADD PRIMARY KEY (`gender_id`);

--
-- Индексы таблицы `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`role_id`);

--
-- Индексы таблицы `week_schedule`
--
ALTER TABLE `week_schedule`
  ADD PRIMARY KEY (`_id_week_schedule`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT для таблицы `user_role`
--
ALTER TABLE `user_role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `cycle`
--
ALTER TABLE `cycle`
  ADD CONSTRAINT `cycle_ibfk_1` FOREIGN KEY (`period_DM_type`) REFERENCES `date_measurement_type` (`_id_date_measurement_type`),
  ADD CONSTRAINT `cycle_ibfk_2` FOREIGN KEY (`once_a_period_DM_type`) REFERENCES `date_measurement_type` (`_id_date_measurement_type`),
  ADD CONSTRAINT `cycle_ibfk_4` FOREIGN KEY (`_id_cycling_type`) REFERENCES `cycling_type` (`_id_cycling_type`),
  ADD CONSTRAINT `cycle_ibfk_5` FOREIGN KEY (`_id_week_schedule`) REFERENCES `week_schedule` (`_id_week_schedule`);

--
-- Ограничения внешнего ключа таблицы `measurement_reminder`
--
ALTER TABLE `measurement_reminder`
  ADD CONSTRAINT `measurement_reminder_ibfk_1` FOREIGN KEY (`_id_measurement_type`) REFERENCES `measurement_type` (`_id_measurement_type`),
  ADD CONSTRAINT `measurement_reminder_ibfk_3` FOREIGN KEY (`_id_having_meals_type`) REFERENCES `having_meals_type` (`_id_having_meals_type`),
  ADD CONSTRAINT `measurement_reminder_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `measurement_reminder_ibfk_5` FOREIGN KEY (`_id_cycle`) REFERENCES `cycle` (`_id_cycle`);

--
-- Ограничения внешнего ключа таблицы `measurement_reminder_entry`
--
ALTER TABLE `measurement_reminder_entry`
  ADD CONSTRAINT `measurement_reminder_entry_ibfk_1` FOREIGN KEY (`_id_measurement_reminder`) REFERENCES `measurement_reminder` (`_id_measurement_reminder`);

--
-- Ограничения внешнего ключа таблицы `measurement_type`
--
ALTER TABLE `measurement_type`
  ADD CONSTRAINT `measurement_type_ibfk_1` FOREIGN KEY (`_id_measur_value_type`) REFERENCES `measurement_value_type` (`_id_measur_value_type`);

--
-- Ограничения внешнего ключа таблицы `pill_reminder`
--
ALTER TABLE `pill_reminder`
  ADD CONSTRAINT `pill_reminder_ibfk_2` FOREIGN KEY (`_id_pill_count_type`) REFERENCES `pill_count_type` (`_id_pill_count_type`),
  ADD CONSTRAINT `pill_reminder_ibfk_4` FOREIGN KEY (`_id_having_meals_type`) REFERENCES `having_meals_type` (`_id_having_meals_type`),
  ADD CONSTRAINT `pill_reminder_ibfk_5` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `pill_reminder_ibfk_6` FOREIGN KEY (`_id_pill`) REFERENCES `pill` (`_id_pill`),
  ADD CONSTRAINT `pill_reminder_ibfk_7` FOREIGN KEY (`_id_cycle`) REFERENCES `cycle` (`_id_cycle`);

--
-- Ограничения внешнего ключа таблицы `pill_reminder_entry`
--
ALTER TABLE `pill_reminder_entry`
  ADD CONSTRAINT `pill_reminder_entry_ibfk_1` FOREIGN KEY (`_id_pill_reminder`) REFERENCES `pill_reminder` (`_id_pill_reminder`);

--
-- Ограничения внешнего ключа таблицы `reminder_time`
--
ALTER TABLE `reminder_time`
  ADD CONSTRAINT `reminder_time_ibfk_1` FOREIGN KEY (`_id_pill_reminder`) REFERENCES `pill_reminder` (`_id_pill_reminder`),
  ADD CONSTRAINT `reminder_time_ibfk_2` FOREIGN KEY (`_id_measurement_reminder`) REFERENCES `measurement_reminder` (`_id_measurement_reminder`);

--
-- Ограничения внешнего ключа таблицы `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`role_id`),
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`gender_id`) REFERENCES `user_gender` (`gender_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
