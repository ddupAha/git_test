package com.limbo.reggie.service.impl;

import com.limbo.reggie.entity.AddressBook;
import com.limbo.reggie.mapper.AddressBookMapper;
import com.limbo.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2023-05-11
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
