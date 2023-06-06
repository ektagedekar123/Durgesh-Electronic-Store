package com.lcwd.electronicstore.helper;

import com.lcwd.electronicstore.entities.User;
import com.lcwd.electronicstore.payloads.PageableResponse;
import com.lcwd.electronicstore.payloads.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageHelper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){

        List<U> entity = page.getContent();

        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());

        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNo(page.getNumber()+1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

         return response;
    }
}
