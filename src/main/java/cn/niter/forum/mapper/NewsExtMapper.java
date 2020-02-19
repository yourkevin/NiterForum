package cn.niter.forum.mapper;

import cn.niter.forum.dto.NewsQueryDTO;
import cn.niter.forum.model.News;

import java.util.List;

public interface NewsExtMapper {
    int incView(News record);
    Integer countBySearch(NewsQueryDTO newsQueryDTO);
    List<News> selectBySearch(NewsQueryDTO newsQueryDTO);

}
