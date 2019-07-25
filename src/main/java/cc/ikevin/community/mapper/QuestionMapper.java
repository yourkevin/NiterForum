package cc.ikevin.community.mapper;

import cc.ikevin.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag)values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void creat(Question question);

    @Select("select * from question order by gmt_modified DESC limit #{offset},#{size}")
    List<Question> list(@Param(value="offset") Integer offset,@Param(value="size") Integer size);

    @Select("select count(1) from question")
    Integer count();
    @Select("select * from question where creator = #{userId} order by gmt_modified DESC limit #{offset},#{size}")
    List<Question> listByUser(@Param(value="userId") Long userId, @Param(value="offset") Integer offset,@Param(value="size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param(value="userId") Long userId);

    @Select("select * from question where id = #{id}")
    Question selectByPrimaryKey(@Param(value="id") Long id);
}

