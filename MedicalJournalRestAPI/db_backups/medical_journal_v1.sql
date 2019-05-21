-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Время создания: Янв 26 2019 г., 22:12
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
-- Структура таблицы `blood_pressure_measurement`
--

CREATE TABLE `blood_pressure_measurement` (
  `_id_blood_press_measur` varchar(10) NOT NULL,
  `systolic_BP` int(11) DEFAULT NULL,
  `diastolic_BP` int(11) DEFAULT NULL,
  `reminder_date` date DEFAULT NULL,
  `_id_measurement_reminder` varchar(10) DEFAULT NULL,
  `is_done` int(11) NOT NULL,
  `reminder_time` time NOT NULL,
  `insert_timestamp` timestamp NULL DEFAULT NULL,
  `update_timestamp` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `cycle`
--

CREATE TABLE `cycle` (
  `_id_cycle` varchar(10) NOT NULL,
  `period` int(11) NOT NULL,
  `period_DM_type` int(11) NOT NULL,
  `once_a_period` int(11) DEFAULT NULL,
  `once_a_period_DM_type` int(11) DEFAULT NULL,
  `_id_week_schedule` varchar(10) DEFAULT NULL,
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
(30);

-- --------------------------------------------------------

--
-- Структура таблицы `measurement_reminder`
--

CREATE TABLE `measurement_reminder` (
  `_id_measurement_reminder` varchar(10) NOT NULL,
  `_id_measurement_type` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `_id_cycle` varchar(10) DEFAULT NULL,
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
  `_id_measur_remind_entry` varchar(10) NOT NULL,
  `value1` double DEFAULT NULL,
  `value2` double DEFAULT NULL,
  `_id_measurement_reminder` varchar(10) NOT NULL,
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

-- --------------------------------------------------------

--
-- Структура таблицы `measurement_value_type`
--

CREATE TABLE `measurement_value_type` (
  `_id_measur_value_type` int(11) NOT NULL,
  `type_value_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `pill`
--

CREATE TABLE `pill` (
  `_id_pill` varchar(10) NOT NULL,
  `pill_name` varchar(50) NOT NULL,
  `pill_description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `pill_count_type`
--

CREATE TABLE `pill_count_type` (
  `_id_pill_count_type` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `pill_reminder`
--

CREATE TABLE `pill_reminder` (
  `_id_pill_reminder` varchar(10) NOT NULL,
  `_id_pill` varchar(10) NOT NULL,
  `pill_count` int(11) NOT NULL,
  `_id_pill_count_type` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `_id_cycle` varchar(10) DEFAULT NULL,
  `_id_having_meals_type` int(11) DEFAULT NULL,
  `having_meals_time` int(11) DEFAULT NULL,
  `annotation` text,
  `is_active` int(11) NOT NULL,
  `times_a_day` int(11) NOT NULL,
  `is_one_time` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `pill_reminder_entry`
--

CREATE TABLE `pill_reminder_entry` (
  `_id_pill_reminder_entry` varchar(10) NOT NULL,
  `is_done` int(11) NOT NULL,
  `reminder_date` date NOT NULL,
  `_id_pill_reminder` varchar(10) NOT NULL,
  `reminder_time` time NOT NULL,
  `is_one_time` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `reminder_time`
--

CREATE TABLE `reminder_time` (
  `_id_reminder_time` varchar(10) NOT NULL,
  `reminder_time` time NOT NULL,
  `_id_pill_reminder` varchar(10) DEFAULT NULL,
  `_id_measurement_reminder` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `temperature_measurement`
--

CREATE TABLE `temperature_measurement` (
  `_id_temp_measur` varchar(10) NOT NULL,
  `temperature` double DEFAULT NULL,
  `reminder_date` date DEFAULT NULL,
  `_id_measurement_reminder` varchar(10) NOT NULL,
  `is_done` int(11) NOT NULL,
  `reminder_time` time DEFAULT NULL
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
(18, '2019-01-23 19:06:16', 'testUser1', 'kek1', 'dsfsdf@kek6', '$2a$10$riL.wJqJdJJxRcz3Kygz/uyiB.RO3QhgiF7msf/WpwGA.mC3Nwyba', 2, 2, 1999),
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
  `_id_week_schedule` varchar(10) NOT NULL,
  `mon` int(11) DEFAULT NULL,
  `tue` int(11) DEFAULT NULL,
  `wed` int(11) DEFAULT NULL,
  `thu` int(11) DEFAULT NULL,
  `fri` int(11) DEFAULT NULL,
  `sat` int(11) DEFAULT NULL,
  `sun` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `blood_pressure_measurement`
--
ALTER TABLE `blood_pressure_measurement`
  ADD PRIMARY KEY (`_id_blood_press_measur`),
  ADD KEY `_id_measurement_reminder` (`_id_measurement_reminder`);

--
-- Индексы таблицы `cycle`
--
ALTER TABLE `cycle`
  ADD PRIMARY KEY (`_id_cycle`),
  ADD KEY `period_DM_type` (`period_DM_type`),
  ADD KEY `once_a_period_DM_type` (`once_a_period_DM_type`),
  ADD KEY `_id_week_schedule` (`_id_week_schedule`),
  ADD KEY `_id_cycling_type` (`_id_cycling_type`);

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
  ADD KEY `_id_cycle` (`_id_cycle`),
  ADD KEY `_id_having_meals_type` (`_id_having_meals_type`),
  ADD KEY `user_id` (`user_id`);

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
  ADD KEY `_id_pill` (`_id_pill`),
  ADD KEY `_id_pill_count_type` (`_id_pill_count_type`),
  ADD KEY `_id_cycle` (`_id_cycle`),
  ADD KEY `_id_having_meals_type` (`_id_having_meals_type`),
  ADD KEY `user_id` (`user_id`);

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
-- Индексы таблицы `temperature_measurement`
--
ALTER TABLE `temperature_measurement`
  ADD PRIMARY KEY (`_id_temp_measur`),
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
-- Ограничения внешнего ключа таблицы `blood_pressure_measurement`
--
ALTER TABLE `blood_pressure_measurement`
  ADD CONSTRAINT `blood_pressure_measurement_ibfk_1` FOREIGN KEY (`_id_measurement_reminder`) REFERENCES `measurement_reminder` (`_id_measurement_reminder`);

--
-- Ограничения внешнего ключа таблицы `cycle`
--
ALTER TABLE `cycle`
  ADD CONSTRAINT `cycle_ibfk_1` FOREIGN KEY (`period_DM_type`) REFERENCES `date_measurement_type` (`_id_date_measurement_type`),
  ADD CONSTRAINT `cycle_ibfk_2` FOREIGN KEY (`once_a_period_DM_type`) REFERENCES `date_measurement_type` (`_id_date_measurement_type`),
  ADD CONSTRAINT `cycle_ibfk_3` FOREIGN KEY (`_id_week_schedule`) REFERENCES `week_schedule` (`_id_week_schedule`),
  ADD CONSTRAINT `cycle_ibfk_4` FOREIGN KEY (`_id_cycling_type`) REFERENCES `cycling_type` (`_id_cycling_type`);

--
-- Ограничения внешнего ключа таблицы `measurement_reminder`
--
ALTER TABLE `measurement_reminder`
  ADD CONSTRAINT `measurement_reminder_ibfk_1` FOREIGN KEY (`_id_measurement_type`) REFERENCES `measurement_type` (`_id_measurement_type`),
  ADD CONSTRAINT `measurement_reminder_ibfk_2` FOREIGN KEY (`_id_cycle`) REFERENCES `cycle` (`_id_cycle`),
  ADD CONSTRAINT `measurement_reminder_ibfk_3` FOREIGN KEY (`_id_having_meals_type`) REFERENCES `having_meals_type` (`_id_having_meals_type`),
  ADD CONSTRAINT `measurement_reminder_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

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
  ADD CONSTRAINT `pill_reminder_ibfk_1` FOREIGN KEY (`_id_pill`) REFERENCES `pill` (`_id_pill`),
  ADD CONSTRAINT `pill_reminder_ibfk_2` FOREIGN KEY (`_id_pill_count_type`) REFERENCES `pill_count_type` (`_id_pill_count_type`),
  ADD CONSTRAINT `pill_reminder_ibfk_3` FOREIGN KEY (`_id_cycle`) REFERENCES `cycle` (`_id_cycle`),
  ADD CONSTRAINT `pill_reminder_ibfk_4` FOREIGN KEY (`_id_having_meals_type`) REFERENCES `having_meals_type` (`_id_having_meals_type`),
  ADD CONSTRAINT `pill_reminder_ibfk_5` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

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
-- Ограничения внешнего ключа таблицы `temperature_measurement`
--
ALTER TABLE `temperature_measurement`
  ADD CONSTRAINT `temperature_measurement_ibfk_1` FOREIGN KEY (`_id_measurement_reminder`) REFERENCES `measurement_reminder` (`_id_measurement_reminder`);

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
