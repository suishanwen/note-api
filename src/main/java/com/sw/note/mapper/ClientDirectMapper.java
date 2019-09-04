package com.sw.note.mapper;

import com.sw.note.model.ClientDirect;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ClientDirectMapper extends tk.mybatis.mapper.common.Mapper<ClientDirect> {


    @Select("<script> \n" +
            "SELECT id\n" +
            "FROM client_direct\n" +
            "WHERE \n" +
            "    user_id = #{userId}\n" +
            "AND sort_no = #{sortNo}\n" +
            "</script>")
    String selectIdByUser(@Param("userId") String userId, @Param("sortNo") int sortNo);


    @Select("<script> \n" +
            "SELECT direct\n" +
            "FROM client_direct\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    String selectDirectById(@Param("id") String id);

    @Select("SELECT\n" +
            "\tcount(DISTINCT version)\n" +
            "FROM\n" +
            "\tclient_direct")
    int checkVersion();

    @Select("SELECT\n" +
            "\tmax(version) version\n" +
            "FROM\n" +
            "\tclient_direct")
    String selectLatestVersion();

    @Update("UPDATE client_direct\n" +
            "SET direct = 'TASK_SYS_UPDATE'\n" +
            "WHERE\n" +
            "\tversion <> #{version}")
    void updateLatestVersion(@Param("version") String version);

    @Insert("<script> \n" +
            "INSERT INTO\n" +
            " client_direct(id,user_id,sort_no)\n" +
            "values (#{id},#{userId},#{sortNo}) \n" +
            "</script>")
    void insertClient(@Param("id") String id, @Param("userId") String userId, @Param("sortNo") int sortNo);


    @Update("<script> \n" +
            "UPDATE \n" +
            "    client_direct\n" +
            "SET  version= #{version}\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    void setVersion(@Param("id") String id, @Param("version") String version);

    @Update("<script> \n" +
            "UPDATE \n" +
            "    client_direct\n" +
            "SET  instance= #{instance}\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    void setInstance(@Param("id") String id, @Param("instance") String instance);

    @Update("<script> \n" +
            "UPDATE \n" +
            "    client_direct\n" +
            "SET  direct=''\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "AND direct = #{direct}\n" +
            "</script>")
    void confirmDirect(@Param("id") String id, @Param("direct") String direct);


    @Select("<script> \n" +
            "SELECT\n" +
            "   id,\n" +
            "   sort_no,\n" +
            "   instance,\n" +
            "   project_name,\n" +
            "   success,\n" +
            "   reward,\n" +
            "   update_time,\n" +
            "   direct,\n" +
            "   version\n" +
            "FROM\n" +
            "    client_direct\n" +
            "WHERE \n" +
            "    user_id = #{userId}\n" +
            "ORDER BY\n" +
            "    sort_no\n" +
            "</script>")
    List<ClientDirect> selectByUserId(@Param("userId") String userId);

    @Select("<script> \n" +
            "SELECT\n" +
            "   id,\n" +
            "   sort_no,\n" +
            "   instance,\n" +
            "   project_name,\n" +
            "   success,\n" +
            "   reward,\n" +
            "   update_time,\n" +
            "   direct,\n" +
            "   version\n" +
            "FROM\n" +
            "    client_direct\n" +
            "ORDER BY\n" +
            "    user_id,sort_no\n" +
            "</script>")
    List<ClientDirect> selectAllCient();


    @Update("<script> \n" +
            "UPDATE\n" +
            "    client_direct\n" +
            "SET direct = #{direct}\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    void updateDirect(@Param("id") String id, @Param("direct") String direct);


    @Update("<script> \n" +
            "UPDATE\n" +
            "    client_direct\n" +
            "SET direct = #{direct}\n" +
            "</script>")
    void updateDirectAll( @Param("direct") String direct);

    @Delete("<script> \n" +
            "DELETE FROM\n" +
            "    client_direct\n" +
            "WHERE \n" +
            "    id = #{id}\n" +
            "</script>")
    void deleteById(@Param("id") String id);

    @Select("SELECT\n" +
            "\tcount( id ) \n" +
            "FROM\n" +
            "\tclient_direct \n" +
            "WHERE\n" +
            "\tupdate_time >= CURRENT_TIMESTAMP - INTERVAL 15 MINUTE;")
    int selectActive();
}

