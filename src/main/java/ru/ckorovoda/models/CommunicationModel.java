package ru.ckorovoda.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommunicationModel {
    @NotBlank
    private String content;
    private String id;

    public CommunicationModel(String content) {
        this.content = content;
    }
}
