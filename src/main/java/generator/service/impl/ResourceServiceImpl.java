package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Resource;
import generator.service.ResourceService;
import generator.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

/**
* @author Jinhong Jiang
* @description 针对表【resource(资源表)】的数据库操作Service实现
* @createDate 2025-04-28 23:45:46
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{

}




