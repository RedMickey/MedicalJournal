package com.example.michel.mycalendar2.utils.utilModels;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataForDeletion {
    private UUID reminderId;
    private String curDateStr;
}
