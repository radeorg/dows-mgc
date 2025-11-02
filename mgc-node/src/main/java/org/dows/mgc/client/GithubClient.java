package org.dows.mgc.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.git.RepositoryProperties;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubClient implements RepositoryClient {
    private final RepositoryProperties repositoryProperties;

    public GitHub githubClient(String token) {
        try {
            /**
             * github = new GitHubBuilder()
             * .withOAuthToken("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")
             * .build();
             */
            RepositoryProperties.GithubSetting github1 = repositoryProperties.getGithub();
            GitHub github = new GitHubBuilder().withOAuthToken(github1.getToken()).build();
            return github;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
