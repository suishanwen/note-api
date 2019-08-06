package com.sw.note.mapper;

import com.sw.note.model.BugReport;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface BugReportMapper extends tk.mybatis.mapper.common.Mapper<BugReport> {


}
