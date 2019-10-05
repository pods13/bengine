package com.pods.bengine.image;

import com.pods.bengine.github.GithubService;
import com.pods.bengine.github.RepoData;
import com.pods.bengine.image.convert.ImageConverter;
import com.tinify.Options;
import com.tinify.Tinify;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pods.bengine.core.exception.wrapper.LambdaExceptionWrapper.wrap;
import static java.util.stream.Collectors.toMap;


@RestController
@RequestMapping("/images")
public class ImageController {

    private final GithubService githubService;
    private final ImageConverter imageConverter;

    public ImageController(GithubService githubService, ImageConverter imageConverter) {
        this.githubService = githubService;
        this.imageConverter = imageConverter;
    }

    @PostMapping
    public ResponseEntity<?> uploadImages(@RequestParam String owner, @RequestParam(name = "repo") String repoName,
                                          @RequestParam(defaultValue = "") String path,
                                          @RequestParam(defaultValue = "CONTENT") ResizingMode resizingMode,
                                          @RequestParam MultipartFile[] images) {
        //TODO add validation that all images have either jpeg or png media type

        final var repoData = new RepoData(repoName, owner, "master", getPathPrefix(resizingMode, path));
        Map<String, ByteBuffer> resizedImagesByNames = Stream.of(images)
                .map(wrap(img -> new AbstractMap.SimpleEntry<>(getBasename(img), convertImage(img))))
                .map(nameToBuf -> resize(resizingMode, nameToBuf))
                .flatMap(imgs -> imgs.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        Set<String> storedImages = githubService.store(repoData, resizedImagesByNames);
        return ResponseEntity.ok(storedImages);
    }

    private String getPathPrefix(ResizingMode resizingMode, String path) {
        if (ResizingMode.CONTENT.equals(resizingMode)) {
            int monthValue = LocalDate.now().getMonthValue();
            var datePrefix = monthValue < 10 ? "0" + monthValue : String.valueOf(monthValue);
            return datePrefix + "/" + path;
        }
        return path;
    }

    private String getBasename(MultipartFile image) {
        return Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[0];
    }

    private ByteBuffer convertImage(MultipartFile img) throws IOException {
        if (!MediaType.IMAGE_JPEG_VALUE.equals(img.getContentType())) {
            return imageConverter.convertToJpg(img.getBytes());
        }

        return ByteBuffer.wrap(img.getBytes());
    }

    private Map<String, ByteBuffer> resize(ResizingMode resizingMode,
                                           AbstractMap.SimpleEntry<String, ByteBuffer> basenameToBuffer) {
        return Stream.of(resizingMode.getSizes())
                .map(wrap(size -> new AbstractMap.SimpleEntry<>(toName(basenameToBuffer.getKey(), size),
                        toResizedImage(basenameToBuffer.getValue(), size))))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String toName(String basename, ImageSize size) {
        final String name = basename.toLowerCase().trim();
        if (size == ImageSize.LARGE) {
            return name + ImageFormats.JPG.asFileExtension();
        }

        String heightRef = size.getHeight() != null ? "x" + size.getHeight() : "";
        return name + "-" + size.getWidth() + heightRef + ImageFormats.JPG.asFileExtension();
    }

    private ByteBuffer toResizedImage(ByteBuffer imageBuffer, ImageSize size) throws IOException {
        byte[] resizedImageBuffer = Tinify.fromBuffer(imageBuffer.array())
                .resize(toTinifyOptions(size))
                .toBuffer();
        return ByteBuffer.wrap(resizedImageBuffer);
    }

    private Options toTinifyOptions(ImageSize imageSize) {
        return new Options()
                .with("method", imageSize.getMethod())
                .with("width", imageSize.getWidth())
                .with("height", imageSize.getHeight());
    }
}