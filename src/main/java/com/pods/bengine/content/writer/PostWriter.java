package com.pods.bengine.content.writer;

import com.pods.bengine.content.post.AbstractPost;
import com.pods.bengine.content.post.matter.Matter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class PostWriter<T extends AbstractPost> {

    private final Class<? extends Matter> matterType;
    private final Yaml yaml;

    public PostWriter(Class<? extends Matter> matterType) {
        this.matterType = matterType;
        this.yaml = new Yaml(getRepresenter(), getDumperOptions());
    }

    public void write(Path dir, T post) {
        Path pathToFile = dir.resolve(post.getName());

        String dump = yaml.dump(post.getMatter());
        String fileContent = dump + "---\n" + post.getContent();
        try {
            Files.write(pathToFile, fileContent.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Cannot write to file by path: " + pathToFile);
        }
    }

    public ByteBuffer serialize(T post) {
        String dump = yaml.dump(post.getMatter());
        String fileContent = dump + "---\n" + post.getContent();
        return ByteBuffer.wrap(fileContent.getBytes());
    }

    private Representer getRepresenter() {
        Representer representer = new Representer();
        representer.addClassTag(matterType, Tag.MAP);
        return representer;
    }

    private DumperOptions getDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setExplicitStart(true);
        return options;
    }
}
