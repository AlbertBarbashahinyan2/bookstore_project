package com.example.demospring1.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class AuthorSearchCriteria extends SearchCriteria {
    private String name;
    private String bookTitle;
    private Long minBooks;
    private Long maxBooks;
    private String award;

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();

        return pageRequest.withSort(
                Sort.by("name").descending()
        );
    }
}
