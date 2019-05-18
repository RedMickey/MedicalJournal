-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Фев 11 2019 г., 23:24
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
  `_id_cycling_type` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `cycling_type`
--

CREATE TABLE `cycling_type` (
  `_id_cycling_type` int(11) NOT NULL,
  `cycling_type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `date_measurement_type`
--

CREATE TABLE `date_measurement_type` (
  `_id_date_measurement_type` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
(43);

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
  `having_meals_time` time DEFAULT NULL,
  `annotation` text NOT NULL,
  `is_active` int(11) NOT NULL,
  `times_a_day` int(11) NOT NULL,
  `is_one_time` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `reminder_date` date DEFAULT NULL,
  `is_one_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `pill_description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill`
--

INSERT INTO `pill` (`_id_pill`, `pill_name`, `pill_description`) VALUES
(0xe412e1da2b1b11e9b2eb708bcdaa4fda, 'таблетка1', NULL),
(0xf70a93102b1b11e9b2eb708bcdaa4fda, 'таблетка2', NULL);

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
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill_reminder`
--

INSERT INTO `pill_reminder` (`_id_pill_reminder`, `_id_pill`, `pill_count`, `_id_pill_count_type`, `start_date`, `_id_cycle`, `_id_having_meals_type`, `having_meals_time`, `annotation`, `is_active`, `times_a_day`, `is_one_time`, `user_id`) VALUES
(0x4849c7352b9811e9987c708bcdaa4fda, 0xe412e1da2b1b11e9b2eb708bcdaa4fda, 2, 1, '2019-02-04', NULL, 2, 10, 'лодлодо', 1, 2, NULL, 17),
(0xa33723622b9811e9987c708bcdaa4fda, 0xf70a93102b1b11e9b2eb708bcdaa4fda, 3, 2, '2019-02-03', NULL, NULL, NULL, 'орпорорпо', 1, 1, NULL, 17);

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
  `reminder_time` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `pill_reminder_entry`
--

INSERT INTO `pill_reminder_entry` (`_id_pill_reminder_entry`, `is_done`, `reminder_date`, `_id_pill_reminder`, `is_one_time`, `reminder_time`) VALUES
(0x8d26378b2b9611e9987c708bcdaa4fda, 1, '2019-02-08 22:40:56', 0x4849c7352b9811e9987c708bcdaa4fda, NULL, NULL),
(0x9fbd58292b9611e9987c708bcdaa4fda, 1, '2019-02-08 22:37:38', 0x4849c7352b9811e9987c708bcdaa4fda, NULL, NULL),
(0xab7d6f6b2b9611e9987c708bcdaa4fda, 0, '2019-02-08 22:41:08', 0x4849c7352b9811e9987c708bcdaa4fda, NULL, NULL),
(0xb786e6042b9611e9987c708bcdaa4fda, 1, '2019-02-08 22:37:40', 0xa33723622b9811e9987c708bcdaa4fda, NULL, NULL),
(0xdc7532652b9611e9987c708bcdaa4fda, 1, '2019-02-08 23:18:02', 0xa33723622b9811e9987c708bcdaa4fda, NULL, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `reminder_time`
--

CREATE TABLE `reminder_time` (
  `_id_reminder_time` binary(16) NOT NULL,
  `reminder_time` time NOT NULL,
  `_id_pill_reminder` binary(16) DEFAULT NULL,
  `_id_measurement_reminder` binary(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
(29, '2019-01-25 16:27:59', 'test6', 'test6ser', 'test6@m', '$2a$10$WD6l2qpP2a8td/gIZKFE1OFJhABjwk7IXeDYaKM19hlkvKbLNYXKq', 1, 2, 2007);

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
  `sun` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `week_schedule`
--

INSERT INTO `week_schedule` (`_id_week_schedule`, `mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun`) VALUES
(0x072fc094901f44d890cb557cf7fd37f1, 1, 0, 1, 0, 0, 1, 0),
(0x658476a8cc8146289c38c21d011f0a43, 1, 0, 1, 0, 0, 1, 0),
(0xd41c5a062b1711e9b2eb708bcdaa4fda, 1, 0, 1, 0, 1, 1, 0);

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
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

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
