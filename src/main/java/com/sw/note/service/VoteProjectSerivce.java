package com.sw.note.service;

import com.sw.note.cache.VoteProjectCache;
import com.sw.note.model.VoteProject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteProjectSerivce {

    public List<VoteProject> query() {
        return VoteProjectCache.get();
    }
}
