package org.DAD.application.model.CommonModels;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Dictionary;

@NoArgsConstructor
@Getter
public class PaginationModel {

    @NotNull
    @Min(1)
    private Integer page;

    @NotNull
    @Min(1)
    private Integer pageSize;

    @NotNull
    private Integer pagesCount;

    public PaginationModel(Integer elementsCount, Integer pageSize, Integer currentPage){
        pagesCount = (elementsCount / pageSize) +
                (elementsCount % pageSize >= 1 ? 1 : 0);
        page = Math.min(Math.max(1,currentPage), pagesCount);
        this.pageSize = pageSize;
    }

}