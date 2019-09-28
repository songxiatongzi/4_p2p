package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.constants.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 董怀宾_bjpowernode
 * @date 2019/8/13
 * @description 描述信息
 * 这里使用的是spring的service
 */
@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    /**
     * spring提供的，操作redis对象
     * 加载jedis数据连接池
     */

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * @param
     * @return java.lang.Double
     * @author Administrator
     * @description
     * @date 2019/8/13
     * @method queryHistoryAverageRate 查询年华收益率
     **/
    @Override
    public Double queryHistoryAverageRate() {

        //设置redis模板对象的序列化方式 (需要接口的实现类) 可以让key的值不是二进制数
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //首先去缓存中获取，
        //缓存中没有的话，去数据库中查询
        //将查询的数据存放到redis数据库中
        //将常用的字符串改成常量值，便于使用 Constants.HISTORY_AVERAGE_RATE
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);

        if (historyAverageRate == null) {

            synchronized (this) {
                historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);

                if (historyAverageRate == null) {
                    //去数据库中查询
                    historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
                    //将查询好的数据存到redis缓存中
                    //return ValueOperations<K, V>
                    //设置失效时间
                    //Long

                    //将查询结果放进redis中
                    //Constants  常量的类名  ctrl+shift +u
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 15, TimeUnit.MINUTES);
                    System.out.println("从mysql数据库中查询");
                } else {
                    System.out.println("从redis中获取");
                }
            }
        } else {
            System.out.println("从redis中获取");
        }


        return historyAverageRate;
    }

    /**
     * @param paramMap 页码 产品类型
     * @return java.util.List<com.bjpowernode.p2p.model.loan.LoanInfo>
     * @author Administrator
     * @description 通过产品类型查询茶品信息列表
     * @date 2019/8/15
     * @method queryLoadInfoListByProductType
     **/
    @Override
    public List<LoanInfo> queryLoadInfoListByProductType(Map<String, Object> paramMap) {

        return loanInfoMapper.selectLoadInfoListByProductType(paramMap);

    }

    /**
     * @param paramMap
     * @return com.bjpowernode.p2p.model.vo.PaginationVo<com.bjpowernode.p2p.model.loan.LoanInfo>
     * @author Administrator
     * @description
     * @date 2019/8/16
     * @method queryLoanInfoListByPage
     **/
    @Override
    public PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap) {

        PaginationVo<LoanInfo> paginationVo = new PaginationVo<>();

        //快速获取总记录数
        Long total = loanInfoMapper.selectTotal(paramMap);

        paginationVo.setTotal(total);

        //获取列表记录数
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoadInfoListByProductType(paramMap);
        paginationVo.setDataList(loanInfoList);
        return paginationVo;
    }

    /**
     * 通过投标产品的id来查询投标信息列表
     *
     * @param id 投标产品的信息表的id
     * @return 投标信息列表
     */
    @Override
    public LoanInfo queryLoanInfoById(Integer id) {

        return loanInfoMapper.selectByPrimaryKey(id);
    }


}
