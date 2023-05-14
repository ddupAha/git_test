package com.limbo.reggie.mapper;

import com.limbo.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
