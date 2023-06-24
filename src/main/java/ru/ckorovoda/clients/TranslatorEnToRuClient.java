package ru.ckorovoda.clients;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Encoded;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import ru.ckorovoda.models.TranslationModelRequest;
import ru.ckorovoda.models.TranslationModelResponse;

@RegisterRestClient(baseUri = "https://ocv1bv9wdo9twjsw.eu-west-1.aws.endpoints.huggingface.cloud")
public interface TranslatorEnToRuClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Encoded
    TranslationModelResponse translateEnToRu(@HeaderParam("Authorization") String token,
                                             @NotNull @Valid TranslationModelRequest translationModelRequest);

}
