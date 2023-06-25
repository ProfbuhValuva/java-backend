package ru.ckorovoda.models;

import lombok.Data;

import java.util.List;

@Data
public class WhisperResponse {
    private String transcription;
    private List<SegmentModel> segments;


}
