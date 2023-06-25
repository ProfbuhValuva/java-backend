package ru.ckorovoda.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class TimeModel {
    @JsonProperty("start_seconds")
    @Positive
    @Schema(description = "The start time in seconds", example = "34")
    private Integer startSeconds;
    @JsonProperty("end_seconds")
    @Positive
    @Schema(description = "The end time in seconds", example = "72")
    private Integer endSeconds;
}
