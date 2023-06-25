package ru.ckorovoda.clients;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import ru.ckorovoda.models.CommunicationModel;
import ru.ckorovoda.models.LLMSegmentsModel;
import ru.ckorovoda.models.ScreenshotsRequestModel;
import ru.ckorovoda.models.ScreenshotsResponseModel;
import ru.ckorovoda.models.WhisperResponse;

@RegisterRestClient(baseUri = "http://localhost:5000")
public interface YouTubeDownloaderClient {

    @Path("/youtube")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    WhisperResponse getPathForDownloadedFile(@NotNull @Valid CommunicationModel communicationModel);

    @Path("/llm")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    CommunicationModel getArticle(@NotNull @Valid LLMSegmentsModel llmSegmentsModel);

    @Path("/screenshots")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Blocking
    ScreenshotsResponseModel makeScreenshots(ScreenshotsRequestModel screenshotsRequestModel);
}
