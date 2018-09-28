package me.snnupai.door.mapper;

import java.util.List;
import me.snnupai.door.pojo.Trade;
import me.snnupai.door.pojo.TradeExample;
import org.apache.ibatis.annotations.Param;

public interface TradeMapper {
    long countByExample(TradeExample example);

    int deleteByExample(TradeExample example);

    int deleteByPrimaryKey(String id);

    int insert(Trade record);

    int insertSelective(Trade record);

    List<Trade> selectByExampleWithBLOBs(TradeExample example);

    List<Trade> selectByExample(TradeExample example);

    Trade selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Trade record, @Param("example") TradeExample example);

    int updateByExampleWithBLOBs(@Param("record") Trade record, @Param("example") TradeExample example);

    int updateByExample(@Param("record") Trade record, @Param("example") TradeExample example);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKeyWithBLOBs(Trade record);

    int updateByPrimaryKey(Trade record);
}