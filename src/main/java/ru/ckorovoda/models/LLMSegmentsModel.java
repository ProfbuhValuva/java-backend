package ru.ckorovoda.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LLMSegmentsModel {
    private List<SegmentModel> segments;
    @JsonProperty("annotation_length")
    @Positive
    private Integer annotationLength;
    @JsonProperty("article_length")
    @Positive
    private Integer articleLength;
    @JsonProperty("total_html")
    private String totalHTML;
    private String model;
}
