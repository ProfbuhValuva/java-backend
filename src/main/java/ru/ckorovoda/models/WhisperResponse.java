package ru.ckorovoda.models;

import lombok.Data;

import java.util.List;

@Data
public class WhisperResponse {
    private String transcription;
    private List<Segment> segments;

    @Data
    public static class Segment {
        private int start;
        private int end;
        private String text;
    }
}
