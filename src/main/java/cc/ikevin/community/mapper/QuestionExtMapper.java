package cc.ikevin.community.mapper;

import cc.ikevin.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

   // Integer countBySearch(QuestionQueryDTO questionQueryDTO);

   // List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
