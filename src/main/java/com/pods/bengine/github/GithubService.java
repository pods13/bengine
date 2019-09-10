package com.pods.bengine.github;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTreeBuilder;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.pods.bengine.core.exception.wrapper.LambdaExceptionWrapper.wrap;

@Service
public class GithubService {

    private final GitHub githubClient;

    public GithubService(GitHub githubClient) {
        this.githubClient = githubClient;
    }

    public Set<String> store(RepoData repoData, Map<String, ByteBuffer> nameToBuffer) throws IOException {
        GHRepository repo = githubClient.getOrganization(repoData.getOwner()).getRepository(repoData.getName());
        String masterTreeSha = repo
                .getTreeRecursive(repoData.getBranch(), 1)
                .getSha();
        GHTreeBuilder ghTreeBuilder = repo.createTree()
                .baseTree(masterTreeSha);
        for (Map.Entry<String, ByteBuffer> entry : nameToBuffer.entrySet()) {
            String pathToEntry = "";
            if (repoData.getPathPrefix().isEmpty()) {
                pathToEntry = entry.getKey();
            } else {
                pathToEntry = repoData.getPathPrefix() + "/" + entry.getKey();
            }
            ByteBuffer buffer = entry.getValue();
            String sha = repo.createBlob()
                    .binaryContent(buffer.array())
                    .create()
                    .getSha();
            ghTreeBuilder
                    .shaEntry(pathToEntry, sha, false);
        }
        String treeSha = ghTreeBuilder.create().getSha();
        GHRef masterRef = repo.getRef("heads/" + repoData.getBranch());
        String commitSha = repo.createCommit()
                .message("Added new files")
                .tree(treeSha)
                .parent(masterRef.getObject().getSha())
                .create()
                .getSHA1();

        masterRef.updateTo(commitSha);
        return nameToBuffer.keySet();
    }

    public Map<String, ByteBuffer> getContent(String owner, String repoName, String path) throws IOException {
        GHRepository repository = githubClient.getOrganization(owner).getRepository(repoName);
        return repository.getDirectoryContent(path).stream()
                .collect(Collectors.toMap(GHContent::getName, wrap(this::toByteBuffer)));
    }

    private ByteBuffer toByteBuffer(GHContent content) throws IOException {
        return ByteBuffer.wrap(content.read().readAllBytes());
    }
}