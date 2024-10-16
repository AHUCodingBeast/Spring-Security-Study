package com.wsm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsm.domain.Menu;
import java.util.List;


public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);
}