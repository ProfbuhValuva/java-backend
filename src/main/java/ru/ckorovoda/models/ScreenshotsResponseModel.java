package ru.ckorovoda.models;

import lombok.Data;

import java.util.List;

@Data
public class ScreenshotsResponseModel {
    private List<String> screenshots;
}
