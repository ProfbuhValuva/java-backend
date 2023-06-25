package ru.ckorovoda.models;

import lombok.Data;

import java.util.List;

@Data
public class ScreenshotsRequestModel {
    private List<String> timecodes;
}
