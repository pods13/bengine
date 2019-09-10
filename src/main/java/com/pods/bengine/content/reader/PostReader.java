package com.pods.bengine.content.reader;

import com.pods.bengine.content.post.AbstractPost;
import com.pods.bengine.content.post.matter.Matter;
import org.springframework.beans.factory.FactoryBean;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PostReader<T extends AbstractPost> {

    private static final Pattern POST_PATTERN = Pattern.compile("---(.*)---(.*)", Pattern.DOTALL);

    private final FactoryBean<? extends AbstractPost> postFactory;
    private final Class<? extends Matter> matterType;
    private final Yaml yaml;

    public PostReader(FactoryBean<? extends AbstractPost> postFactory,
                      Class<? extends Matter> matterType) {
        this.postFactory = postFactory;
        this.matterType = matterType;
        this.yaml = new Yaml(getYamlConstructor(), getYamlRepresenter());
    }

    private Constructor getYamlConstructor() {
        return new Constructor(matterType);
    }

    private Representer getYamlRepresenter() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        return representer;
    }

    public T read(Path pathToFile) {
        try {
            String fileName = pathToFile.getFileName().toString();
            String content = new String(Files.readAllBytes(pathToFile));
            return read(fileName, content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T read(Map.Entry<String, ByteBuffer> nameToContent) {
        return read(nameToContent.getKey(), new String(nameToContent.getValue().array()));
    }

    @SuppressWarnings("unchecked")
    public T read(String fileName, String content) {
        try {

            Matcher matcher = POST_PATTERN.matcher(content);
            if (!matcher.find()) {
                return (T) postFactory.getObject();
            }

            String frontMatter = matcher.group(1).trim();
            String postContent = matcher.group(2).trim();

            AbstractPost post = postFactory.getObject();
            post.setName(fileName);
            post.setContent(postContent);
            post.setMatter(yaml.load(frontMatter));
            return (T) post;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Set<T> readAll(Path pathToDir) {
        try {
            return Files.list(pathToDir)
                    .filter(Files::isRegularFile)
                    .map(this::read)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
