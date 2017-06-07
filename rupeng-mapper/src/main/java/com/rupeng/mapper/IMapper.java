package com.rupeng.mapper;

import java.util.List;

public interface IMapper<T> {

    int insert(T pojo);

    int update(T pojo); //根据id更新

    int delete(Long id); //根据id删除

    List<T> select(T pojo); //以非空字段值作为查询条件进行查询

}
