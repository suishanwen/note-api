package com.sw.note.config;

import com.sw.note.model.VoteSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoteConfig {

    @Bean
    public VoteSystem voteSystem() {
        return new VoteSystem()
                .pollingRatio(1);
    }

}
