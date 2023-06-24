package ru.ckorovoda.services;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.Valid;
import ru.ckorovoda.models.CommunicationModel;

public interface YouTubeService {

    CommunicationModel getTextFromAudio(@NotNull @Valid CommunicationModel communicationModel);
}
