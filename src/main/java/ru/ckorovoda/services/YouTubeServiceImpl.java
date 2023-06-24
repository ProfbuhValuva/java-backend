package ru.ckorovoda.services;

import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ckorovoda.clients.TranslatorRuToEnClient;
import ru.ckorovoda.clients.YouTubeDownloaderClient;
import ru.ckorovoda.models.CommunicationModel;
import ru.ckorovoda.models.LLMSegmentsModel;
import ru.ckorovoda.models.WhisperResponse;

@ApplicationScoped
public class YouTubeServiceImpl implements YouTubeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeServiceImpl.class);
    @RestClient
    @Inject
    YouTubeDownloaderClient youTubeDownloaderClient;

    @RestClient
    @Inject
    TranslatorRuToEnClient translatorRuToEnClient;

    private static final String TOKEN = "Bearer ";

    @Override
    public CommunicationModel getTextFromAudio(@NotNull @Valid CommunicationModel communicationModel) {
        communicationModel.setId(RandomStringUtils.randomAlphabetic(7));
        WhisperResponse textFromAudio = youTubeDownloaderClient.getPathForDownloadedFile(communicationModel);
        textFromAudio.setTranscription(textFromAudio.getTranscription().stripLeading().stripTrailing());
        for (WhisperResponse.Segment s : textFromAudio.getSegments()) {
            s.setText(s.getText().stripLeading().stripTrailing());
        }
        LOGGER.info("TextFromAudio: {}", textFromAudio);
        LOGGER.info("Transcription: {}", textFromAudio.getTranscription());

        // Translates batch of text using HuggingFace (OpenNMT)
        /*
        StringBuilder totalTranslatedText = new StringBuilder();
        int maxLength = 510;
        String textToTranslate = textFromAudio.getTranscription();

        while (textToTranslate.length() > 0) {
            String batchToTranslate = textToTranslate.substring(0, Math.min(textToTranslate.length(), maxLength));
            totalTranslatedText.append(translatorRuToEnClient.translateRuToEn(TOKEN,
                    new TranslationModelRequest(batchToTranslate)).get(0).getTranslationText());
            textToTranslate = textToTranslate.substring(Math.min(textToTranslate.length(), maxLength));
        }
        TranslationModelResponse translationModelResponse = new TranslationModelResponse();
        translationModelResponse.setTranslationText(totalTranslatedText.toString());
        LOGGER.info("TranslationModelResponse: {}", translationModelResponse);
         */
        CommunicationModel article = youTubeDownloaderClient.getArticle(new LLMSegmentsModel(textFromAudio.getSegments()));
        return article;
    }
}
