package com.cqu.lab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqu.lab.dao.domain.PrivateMessage;
import com.cqu.lab.service.PrivateMessageService;
import com.cqu.lab.mapper.PrivateMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【private_message(私信信息表)】的数据库操作Service实现
* @createDate 2025-04-12 15:54:10
*/
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage>
    implements PrivateMessageService{

}




