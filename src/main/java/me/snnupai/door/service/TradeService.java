package me.snnupai.door.service;

import lombok.extern.log4j.Log4j;
import me.snnupai.door.mapper.TradeMapper;
import me.snnupai.door.pojo.Trade;
import me.snnupai.door.pojo.TradeExample;
import me.snnupai.door.status.DelStatus;
import me.snnupai.door.status.TradeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static me.snnupai.door.util.Utils.limit;

@Service
@Log4j
public class TradeService {

    @Autowired
    TradeMapper tradeMapper;

    public List<Trade> getTradeList(int offset, int limit) {
        TradeExample example = new TradeExample();
        example.setOffset(offset);
        example.setLimit(limit);
        example.setOrderByClause(" created_date desc ");

        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(DelStatus.non_del);
        criteria.andStatusEqualTo(TradeStatus.willSell);

        return tradeMapper.selectByExampleWithBLOBs(example);
    }

    public Trade getTradeById(String id) {
        return tradeMapper.selectByPrimaryKey(id);
    }

    public int addTrade(Trade trade) {
        return tradeMapper.insertSelective(trade);
    }

    public long totalPages() {
        TradeExample example = new TradeExample();
        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(TradeStatus.willSell)
                .andDelFlagEqualTo(DelStatus.non_del);
        long count = tradeMapper.countByExample(example);
        log.info("count =" + count);
        if (count % limit == 0) {
            return count / limit;
        } else {
            return count / limit + 1;
        }
    }

    /**
     * 获取正在出售的商品信息count
     *
     * @return
     */
    public Long count() {
        TradeExample example = new TradeExample();
        TradeExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(TradeStatus.willSell)
                .andDelFlagEqualTo(DelStatus.non_del);
        long count = tradeMapper.countByExample(example);
        return count;
    }

    public void updateTrade(Trade trade) {
        tradeMapper.updateByPrimaryKey(trade);
    }
}
