package com.sw.note.mapper;

import com.sw.note.model.entity.VmReset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface VmResetMapper extends tk.mybatis.mapper.common.Mapper<VmReset> {
    @Select("<script> \n" +
            "SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\tvm_reset\n" +
            "WHERE\n" +
            "\tUSER = #{user}\n" +
            "AND sort_no = #{sortNo}\n" +
            "ORDER BY\n" +
            "\tdate DESC\n" +
            "limit 1\n" +
            "</script>")
    VmReset getLastReset(@Param("user") String user, @Param("sortNo") String sortNo);
}
