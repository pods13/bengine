package com.pods.bengine.content.generation.warframe.primes.job;

import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.content.writer.PostWriter;
import com.pods.bengine.github.GithubService;
import com.pods.bengine.github.RepoData;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PrimePostsWriter implements ItemWriter<PrimePost> {

    private final GithubService githubService;
    private final PostWriter<PrimePost> postWriter;

    public PrimePostsWriter(GithubService githubService, PostWriter<PrimePost> postWriter) {
        this.githubService = githubService;
        this.postWriter = postWriter;
    }

    @Override
    public void write(List<? extends PrimePost> posts) throws Exception {
        String owner = "warframeblog";
        String repoName = "warframeblog";
        String path = "content/primes";
        RepoData repoData = new RepoData(repoName, owner, "develop", path);

        Map<String, ByteBuffer> nameToBuffer = posts.stream()
                .collect(Collectors.toMap(PrimePost::getName, postWriter::serialize));
        githubService.store(repoData, nameToBuffer);
    }
}