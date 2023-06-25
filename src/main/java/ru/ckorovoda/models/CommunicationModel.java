package ru.ckorovoda.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@Schema(description = "Base communication model with Java Backend")
public class CommunicationModel {
    @NotBlank
    @Schema(description = "URL for YouTube video", required = true, example = "https://www.youtube.com/watch?v=U43zZ5RsXGA")
    private String content;
    @Schema(hidden = true)
    private String id;
    @Schema(description = "Optional. Time range for the YouTube video")
    private TimeModel time;
    @Positive
    @Schema(description = "Optional. Limit in characters for introduction before the article", example = "400")
    private Integer annotationLength;
    @Positive
    @Schema(description = "Optional. Limit in characters for the article itself", example = "6000")
    private Integer articleLength;
    @Schema(description = "Marks if all images for each timecode must be shown", defaultValue = "false", example = "false")
    private boolean allImages;
    @Schema(description = "Additional Screenshots with seconds separated by comma", example = "12, 32, 45")
    private String additionalScreenshots;
    @Schema(description = "Selected model", required = true, example = "gpt-3.5-turbo-16k-0613")
    private String model;
}
