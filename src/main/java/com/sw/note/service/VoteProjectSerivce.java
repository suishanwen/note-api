package com.sw.note.service;

import com.sw.note.mapper.VoteProjectMapper;
import com.sw.note.model.VoteProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteProjectSerivce {

    @Autowired
    VoteProjectMapper voteProjectMapper;


    public List<VoteProject> query() {
        return voteProjectMapper.query();
    }

    public void empty() {
        voteProjectMapper.empty();
    }

    public void save(List<VoteProject> voteProjectList) {
        voteProjectList.forEach(voteProject -> {
            voteProjectMapper.insert(voteProject);
        });
    }


}
