package com.sw.note.cache;

import com.google.common.collect.Lists;
import com.sw.note.model.VoteProject;

import java.util.List;

public class VoteProjectCache {

    private static List<VoteProject> list = Lists.newArrayList();

    public static List<VoteProject> get() {
        return list;
    }

    public static void set(List<VoteProject> data) {
        list = data;
    }

    public static void empty() {
        list.clear();
    }
}
