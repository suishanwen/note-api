package com.sw.note.service;

import com.sw.note.beans.BusinessException;
import com.sw.note.beans.Enquiry;
import com.sw.note.mapper.NoteMapper;
import com.sw.note.model.Note;
import com.sw.note.util.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NoteSerivce {

    @Autowired
    NoteMapper noteMapper;

    public List<Map> getAllPost() {
        return noteMapper.getAll();
    }

    public List<Note> getRecommend() {
        return noteMapper.getRecommend();
    }

    public Note get(Integer id) {
        return noteMapper.selectByPrimaryKey(id);
    }

    public Note getByTitle(String title) {
        return noteMapper.getByTitle(title);
    }

    public Note add(Note note) {
        note.setPostTime(new Date());
        int count = noteMapper.insertSelective(note);
        if (count >= 1) {
            return getByTitle(note.getTitle());
        }
        throw new BusinessException("添加失败!");
    }

    public Note edit(Note note) {
        note.setEditTime(new Date());
        int count = noteMapper.updateByPrimaryKeySelective(note);
        if (count >= 1) {
            return get(note.getId());
        }
        throw new BusinessException("修改失败!");
    }

    public void delete(Integer id) {
        int count = noteMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new BusinessException("删除失败!");
        }
    }

    public void sendEnquiry(Enquiry enquiry) {
        MailUtils cn = new MailUtils();
        cn.setAddress("controlservice@sina.com", "suishanwen@icloud.com", enquiry.getName() + ":" + enquiry.getEmail() + ":" + enquiry.getSubject());
        cn.send("smtp.sina.com", "controlservice@sina.com", "a123456", enquiry.getMessage());
    }


}
