package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.constants.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/14
 * @description 描述信息
 */

@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Double queryAllBidMoney() {

        Double allBIdMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);

        if(allBIdMoney == null){
            synchronized (this){
                allBIdMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);

                if(allBIdMoney == null){

                    allBIdMoney = bidInfoMapper.selectAllBidMoney();

                    redisTemplate.opsForValue().set(Constants.ALL_BID_MONEY, allBIdMoney, 15, TimeUnit.MINUTES);
                }

            }
        }

        return allBIdMoney;
    }


    /**
     * 查询用户投标信息表，并分页
     * @param paramMap 用户id 掠过的记录数  每页展示的记录数
     * @return bidInfoList 用户投标信息表
     */
    @Override
    public List<BidInfo> queryRecentlyBidInfoList(Map<String, Object> paramMap) {

        return bidInfoMapper.queryRecentlyBidInfoList(paramMap);
    }

    /**
     * 通过投标产品的id来查询所对应的用户投资信息表
     * @param paramMap 数组 包含掠过的记录数 每页展示的记录数 投标产品的id
     * @return 投标产品列表
     */
    @Override
    public List<BidInfo> queryRecentlyBidInfoListByLoanId(Map<String, Object> paramMap) {

        return bidInfoMapper.selectRecentlyBidInfoListByLoanId(paramMap);

    }
}
