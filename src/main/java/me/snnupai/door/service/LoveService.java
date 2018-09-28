package me.snnupai.door.service;

import lombok.extern.slf4j.Slf4j;
import me.snnupai.door.mapper.LoveMapper;
import me.snnupai.door.pojo.Love;
import me.snnupai.door.pojo.LoveExample;
import me.snnupai.door.status.DelStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LoveService {

    @Autowired
    LoveMapper loveMapper;
    @Autowired
    SensitiveService sensitiveService;

    public List<Love> getLoveList(int offset, int limit) {
        LoveExample example = new LoveExample();
        example.setOffset((offset - 1) * limit);
        example.setLimit(limit);
        example.setOrderByClause(" `id`  desc ");

        return loveMapper.selectByExampleWithBLOBs(example);
    }

    public void addLove(Love love) {
        String filterContent = sensitiveService.filter(love.getContent());
        love.setContent(filterContent);
        loveMapper.insertSelective(love);
    }

    public Long queryLoveCounts() {
        LoveExample example = new LoveExample();
        LoveExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(DelStatus.non_del);

        return loveMapper.countByExample(example);
    }
}
