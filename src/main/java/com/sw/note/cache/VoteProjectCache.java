package com.sw.note.cache;

import com.google.common.collect.Lists;
import com.sw.note.model.VoteProject;

import java.util.List;
import java.util.stream.Collectors;

public class VoteProjectCache {

    private static String lock = "";
    private static List<VoteProject> list = Lists.newArrayList();
    private static List<VoteProject> listQ7 = Lists.newArrayList();
    private static List<VoteProject> listTx = Lists.newArrayList();
    private static List<VoteProject> listAq = Lists.newArrayList();

    public static String getLock() {
        return lock;
    }

    public static void setLock(String lock) {
        VoteProjectCache.lock = lock;
    }

    public static List<VoteProject> get() {
        return list;
    }

    public static void merge() {
        List<VoteProject> listTmp = Lists.newArrayList();
        listTmp.addAll(listTx);
        listTmp.addAll(listQ7);
        listTmp.addAll(listAq);
        List<String> keyList = Lists.newArrayList();
        list = listTmp.stream().filter(voteProject -> {
            boolean inExist = !keyList.contains(voteProject.getProjectName());
            if (inExist) {
                keyList.add(voteProject.getProjectName());
            }
            return inExist;
        }).peek(voteProject -> {
            if (voteProject.getProjectName().equals(lock)) {
                voteProject.setLock(1);
            }
        }).collect(Collectors.toList());
    }

    public static void setListQ7(List<VoteProject> listQ7) {
        VoteProjectCache.listQ7 = listQ7;
    }

    public static void setListTx(List<VoteProject> listTx) {
        VoteProjectCache.listTx = listTx;
    }

    public static void setListAq(List<VoteProject> listAq) {
        VoteProjectCache.listAq = listAq;
    }
}
