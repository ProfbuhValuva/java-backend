package ru.ckorovoda.services;

import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ckorovoda.clients.YouTubeDownloaderClient;
import ru.ckorovoda.models.CommunicationModel;
import ru.ckorovoda.models.LLMSegmentsModel;
import ru.ckorovoda.models.ScreenshotsRequestModel;
import ru.ckorovoda.models.ScreenshotsResponseModel;
import ru.ckorovoda.models.SegmentModel;
import ru.ckorovoda.models.WhisperResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class YouTubeServiceImpl implements YouTubeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeServiceImpl.class);
    @RestClient
    @Inject
    YouTubeDownloaderClient youTubeDownloaderClient;

    @Override
    public CommunicationModel getTextFromAudio(@NotNull @Valid CommunicationModel communicationModel) {
        communicationModel.setId(RandomStringUtils.randomAlphabetic(7));
        WhisperResponse textFromAudio = youTubeDownloaderClient.getPathForDownloadedFile(communicationModel);
        textFromAudio.setTranscription(textFromAudio.getTranscription().stripLeading().stripTrailing());
        for (SegmentModel s : textFromAudio.getSegments()) {
            s.setText(s.getText().stripLeading().stripTrailing());
        }
        LOGGER.info("TextFromAudio: {}", textFromAudio);
        LOGGER.info("Transcription: {}", textFromAudio.getTranscription());

        LLMSegmentsModel llmSegmentsModel = new LLMSegmentsModel();
        llmSegmentsModel.setSegments(textFromAudio.getSegments());
        if (Objects.nonNull(communicationModel.getAnnotationLength())) {
            llmSegmentsModel.setAnnotationLength(communicationModel.getAnnotationLength());
        }
        if (Objects.nonNull(communicationModel.getArticleLength())) {
            llmSegmentsModel.setArticleLength(communicationModel.getArticleLength());
        }
        // todo Optimize
        if (Objects.nonNull(communicationModel.getTime()) && (Objects.nonNull(communicationModel.getTime().getStartSeconds()))) {
            for (SegmentModel segmentModel : textFromAudio.getSegments()) {
                segmentModel.setStart(segmentModel.getStart() + communicationModel.getTime().getStartSeconds());
                segmentModel.setEnd(segmentModel.getEnd() + communicationModel.getTime().getStartSeconds());
            }
        }
        llmSegmentsModel.setModel(communicationModel.getModel());
        CommunicationModel article = youTubeDownloaderClient.getArticle(llmSegmentsModel);
        LOGGER.info("Generated article: {}", article);
        ScreenshotsRequestModel screenshotsRequestModel = new ScreenshotsRequestModel();
        List<String> timingUrls = getTimeLinkedURLs(communicationModel.getContent(), article.getContent());
        if (timingUrls.size() > 4 && !communicationModel.isAllImages()) {
            List<String> filteredUrls = new ArrayList<>();
            for (int i = 0; i < timingUrls.size(); i += timingUrls.size() / 5) {
                filteredUrls.add(timingUrls.get(i));
            }
            timingUrls = filteredUrls;
        }
        List<String> additionalUrlsWithTimecodes = new ArrayList<>();
        if (Objects.nonNull(communicationModel.getAdditionalScreenshots())) {
            LOGGER.info("Making additional Screenshots: {}", communicationModel);
            LOGGER.info("Split additionalScreenshots: {}", Arrays.toString(communicationModel.getAdditionalScreenshots().split(",\\s+")));
            List<Integer> timecodes = Arrays.stream(communicationModel.getAdditionalScreenshots().split(",\\s+"))
                    .map(Integer::parseInt)
                    .toList();
            if (timecodes.isEmpty()) {
                try {
                    int val = Integer.parseInt(communicationModel.getAdditionalScreenshots());
                    timecodes = List.of(val);
                } catch (Exception e) {
                    LOGGER.error("Can't pick an int value from additionalScreenshots");
                }
            }
            for (int i : timecodes) {
                additionalUrlsWithTimecodes.add(communicationModel.getContent() + "&t=" + i + "s");
            }
        }
        LOGGER.info("timingUrls: {}", timingUrls);
        LOGGER.info("additionalUrls: {}", additionalUrlsWithTimecodes);
        timingUrls.addAll(additionalUrlsWithTimecodes);
        screenshotsRequestModel.setTimecodes(timingUrls);
        LOGGER.info("ScreenshotsRequestModel: {}", screenshotsRequestModel);
        ScreenshotsResponseModel screenshotsResponseModel =
                youTubeDownloaderClient.makeScreenshots(screenshotsRequestModel);
        LOGGER.info("Screenshots: {}", screenshotsResponseModel);
        LLMSegmentsModel refineArticle = new LLMSegmentsModel();
        refineArticle.setModel(communicationModel.getModel());
        refineArticle.setTotalHTML(article.getContent().replace("@", "") + "\n" + String.join(" ",
                screenshotsResponseModel.getScreenshots()));
        CommunicationModel updatedArticle = youTubeDownloaderClient.getArticle(refineArticle);
        article.setContent(updatedArticle.getContent().replace("@", ""));
        return article;
    }

    public static List<String> getTimeLinkedURLs(String baseUrl, String input) {
        Pattern pattern = Pattern.compile("\\(\\d{2}:\\d{2}:\\d{2}-|(\\d{2}):(\\d{2}):(\\d{2})@@");
        Matcher matcher = pattern.matcher(input);
        List<String> urls = new ArrayList<>();

        while (matcher.find()) {
            int hours = Integer.parseInt(matcher.group(1));
            int minutes = Integer.parseInt(matcher.group(2));
            int seconds = Integer.parseInt(matcher.group(3));

            int totalSeconds = hours * 3600 + minutes * 60 + seconds;
            urls.add(baseUrl + "&t=" + totalSeconds + "s");
        }

        return urls;
    }
}
