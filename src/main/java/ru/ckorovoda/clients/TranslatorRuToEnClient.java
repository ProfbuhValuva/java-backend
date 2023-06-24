package ru.ckorovoda.clients;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import ru.ckorovoda.models.TranslationModelRequest;
import ru.ckorovoda.models.TranslationModelResponse;

import java.util.List;

@RegisterRestClient(baseUri = "https://lhppbhysljszv2yl.eu-west-1.aws.endpoints.huggingface.cloud")
public interface TranslatorRuToEnClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<TranslationModelResponse> translateRuToEn(@HeaderParam("Authorization") String token,
                                                   @NotNull @Valid TranslationModelRequest translationModelRequest);

}
