package com.sw.note.mapper;

import com.sw.note.model.entity.BugReport;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface BugReportMapper extends tk.mybatis.mapper.common.Mapper<BugReport> {


}
