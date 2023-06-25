package ru.ckorovoda.resources;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ckorovoda.models.CommunicationModel;
import ru.ckorovoda.services.YouTubeService;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Path("/url")
public class YouTubeResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeResource.class);

    private static final String UPLOAD_DIRECTORY = "C:\\Users\\liwgfr\\PycharmProjects\\profhub-hackathon-url-service" +
            "\\";
    @Inject
    YouTubeService youTubeService;

    AtomicReference<CommunicationModel> atomicReference = new AtomicReference<>(new CommunicationModel());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CommunicationModel processURL(@NotNull @Valid CommunicationModel communicationModel) {
        LOGGER.info("Incoming request is: {}", communicationModel);
        if (Objects.nonNull(communicationModel.getTime())) {
            var time = communicationModel.getTime();
            if (time.getStartSeconds() != null && time.getEndSeconds() != null) {
                if (time.getEndSeconds() <= time.getStartSeconds()) {
                    throw new BadRequestException("End seconds must be higher than start seconds!");
                }
            }
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> atomicReference.set(youTubeService.getTextFromAudio(communicationModel)));
        executorService.shutdown();
        return communicationModel;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response streamData() {
        LOGGER.info("Accessing streamData. AtomicReference is: {}", atomicReference.get());
        if (atomicReference.get().getContent() == null) {
            return Response.accepted().build();
        }
        CommunicationModel communicationModel = atomicReference.get();
        atomicReference.set(new CommunicationModel());
        return Response.ok(communicationModel).build();
    }

    @GET
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Path("/{imageName}")
    public Response getImage(@PathParam("imageName") String imageName) {
        File file = new File(UPLOAD_DIRECTORY + imageName);
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(file).build();
    }
}
