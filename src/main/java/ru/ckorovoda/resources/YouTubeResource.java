package ru.ckorovoda.resources;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import ru.ckorovoda.models.CommunicationModel;
import ru.ckorovoda.services.YouTubeService;

@Path("/url")
public class YouTubeResource {

    @Inject
    YouTubeService youTubeService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CommunicationModel processURL(@NotNull @Valid CommunicationModel communicationModel) {
        return youTubeService.getTextFromAudio(communicationModel);
    }
}
