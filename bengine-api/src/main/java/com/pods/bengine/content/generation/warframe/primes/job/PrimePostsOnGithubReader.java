package com.pods.bengine.content.generation.warframe.primes.job;

import com.pods.bengine.content.generation.warframe.primes.PrimePostData;
import com.pods.bengine.content.post.warframe.primes.PrimePost;
import com.pods.bengine.content.post.warframe.primes.PrimePostMatter;
import com.pods.bengine.content.reader.PostReader;
import com.pods.bengine.github.GithubService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
public class PrimePostsOnGithubReader implements ItemReader<PrimePostData> {

    private final GithubService githubService;
    private final PostReader<PrimePost> postReader;

    private List<PrimePostData> primePostData;

    public PrimePostsOnGithubReader(GithubService githubService, @Qualifier("primePostReader") PostReader<PrimePost> postReader) {
        this.githubService = githubService;
        this.postReader = postReader;
    }

    @BeforeStep
    public void readPostsOnGithub(final StepExecution stepExecution) throws IOException {
        String owner = "warframeblog";
        String repoName = "warframeblog";
        String path = "content/primes";
        primePostData = githubService.getContent(owner, repoName, path).entrySet().stream()
                .map(postReader::read)
                .map(this::toPrimePostData)
                .collect(Collectors.toList());
    }

    private PrimePostData toPrimePostData(PrimePost primePost) {
        PrimePostData primePostData = new PrimePostData();
        PrimePostMatter matter = primePost.getMatter();
        primePostData.setItemName(matter.getItemName());
        primePostData.setStatus(matter.getStatus());
        primePostData.setAlongWithPrimeItems(matter.getAlongWithPrimeItems());
        return primePostData;
    }

    @Override
    public PrimePostData read() {
        if (!primePostData.isEmpty()) {
            return primePostData.remove(0);
        }
        return null;
    }
}
