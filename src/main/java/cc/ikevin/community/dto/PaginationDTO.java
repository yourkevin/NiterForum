package cc.ikevin.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage = totalPage;
        this.page = page;

        if(totalPage!=0) pages.add(page);
        for (int i = 1; i <= 3; i++) {//显示前后各三页
            if (page - i > 0) {
                pages.add(0, page - i);//一直往前加
            }

            if (page + i <= totalPage) {
                pages.add(page + i);//一直往后加
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage||totalPage==0) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)||totalPage==0) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)||totalPage==0) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
