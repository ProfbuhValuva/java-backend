package ru.ckorovoda.models;

import lombok.Data;

@Data
public class SegmentModel {
    private int start;
    private int end;
    private String text;
}
