package com.sw.note.cache;

import com.google.common.collect.Lists;
import com.sw.note.model.entity.VoteProject;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class VoteProjectCache {

    private static String locked = "";
    private static LinkedList<VoteProject> list = Lists.newLinkedList();
    private static LinkedList<VoteProject> listQ7 = Lists.newLinkedList();
    private static LinkedList<VoteProject> listTx = Lists.newLinkedList();
    private static LinkedList<VoteProject> listAq = Lists.newLinkedList();

    public static String getLocked() {
        return locked;
    }

    public static void setLocked(String locked) {
        VoteProjectCache.locked = locked;
    }

    public static LinkedList<VoteProject> get() {
        return list;
    }

    public static void merge() {
        LinkedList<VoteProject> listTmp = Lists.newLinkedList();
        listTmp.addAll(listTx);
        listTmp.addAll(listQ7);
        listTmp.addAll(listAq);
        LinkedList<String> keyList = Lists.newLinkedList();
        list = listTmp.stream().filter(voteProject -> {
            boolean inExist = !keyList.contains(voteProject.getProjectName()) && voteProject.remains();
            if (inExist) {
                keyList.add(voteProject.getProjectName());
            }
            return inExist;
        }).peek(voteProject -> {
            if (voteProject.getProjectName().equals(getLocked())) {
                voteProject.setLocked(1);
            }
        }).collect(Collectors.toCollection(LinkedList::new));
    }

    public static void setListQ7(LinkedList<VoteProject> listQ7) {
        VoteProjectCache.listQ7 = listQ7;
    }

    public static void setListTx(LinkedList<VoteProject> listTx) {
        VoteProjectCache.listTx = listTx;
    }

    public static void setListAq(LinkedList<VoteProject> listAq) {
        VoteProjectCache.listAq = listAq;
    }
}
