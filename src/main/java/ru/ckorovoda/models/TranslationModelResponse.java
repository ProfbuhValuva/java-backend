package ru.ckorovoda.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TranslationModelResponse {
    @JsonProperty("translation_text")
    private String translationText;
}
