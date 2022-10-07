package com.mall.seckill.timer;

import com.mall.seckill.dao.SeckillGoodsMapper;
import com.mall.seckill.pojo.SeckillGoods;
import entity.DateUtil;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 定时将秒杀商品存入到Redis缓存
 *
 * 定时任务配置步骤
 * 1.在定时任务类的指定方法上加上@Scheduled开启定时任务
 * 2.定时任务表达式：使用cron属性来配置定时任务执行时间
 *
 *
 * 定时任务 spring task(多线程)
 * 1.开始spring task
 * 2.在执行的方法上修饰一个注解  注解中指定何时执行即可
 *
 */
@Component
public class SeckillGoodsPushTask {

    private SeckillGoodsMapper seckillGoodsMapper;

    private RedisTemplate redisTemplate;

    @Autowired
    public void setSeckillGoodsMapper(SeckillGoodsMapper seckillGoodsMapper) {
        this.seckillGoodsMapper = seckillGoodsMapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * CronTrigger配置完整格式为： [秒][分] [小时][日] [月][周] [年]
     * 序号	说明	是否必填	允许填写的值	        允许的通配符
     * 1	秒	是	    0-59	            , - * /
     * 2	分	是	    0-59	            , - * /
     * 3	小时	是	    0-23	            , - * /
     * 4	日	是	    1-31	            , - * ? / L W
     * 5	月	是	    1-12或JAN-DEC	    , - * /
     * 6	周	是	    1-7或SUN-SAT	    , - * ? / L W
     * 7	年	否	    empty 或1970-2099	, - * /
     *
     * 通配符说明:
     * * 表示所有值. 例如:在分的字段上设置 "*",表示每一分钟都会触发。
     *
     * ? 表示不指定值。使用的场景为不需要关心当前设置这个字段的值。
     *
     * 例如:要在每月的10号触发一个操作，但不关心是周几，所以需要周位置的那个字段设置为"?" 具体设置为 0 0 0 10 * ?
     *
     * - 表示区间。例如 在小时上设置 "10-12",表示 10,11,12点都会触发。
     *
     * , 表示指定多个值，例如在周字段上设置 "MON,WED,FRI" 表示周一，周三和周五触发  12,14,19
     *
     * / 用于递增触发。如在秒上面设置"5/15" 表示从5秒开始，每增15秒触发(5,20,35,50)。 在月字段上设置'1/3'所示每月1号开始，每隔三天触发一次。
     *
     * L 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会依据是否是润年[leap]), 在周字段上表示星期六，相当于"7"或"SAT"。如果在"L"前加上数字，则表示该数据的最后一个。例如在周字段上设置"6L"这样的格式,则表示“本月最后一个星期五"
     *
     * W 表示离指定日期的最近那个工作日(周一至周五). 例如在日字段上设置"15W"，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发, 如果15号是周未，则找最近的下周一(16号)触发.如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为 "1W",它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，"W"前只能设置具体的数字,不允许区间"-").
     *
     * # 序号(表示每月的第几个周几)，例如在周字段上设置"6#3"表示在每月的第三个周六.注意如果指定"#5",正好第五周没有周六，则不会触发该配置(用在母亲节和父亲节再合适不过了) ；
     *
     *  常用表达式
     * "0 0 10,14,16 * * ?" 每天上午10点，下午2点，4点
     * "0 0/30 9-17 * * ?" 朝九晚五工作时间内每半小时
     * "0 0 12 ? * WED" 表示每个星期三中午12点
     * "0 0 12 * * ?" 每天中午12点触发
     * "0 15 10 ? * *" 每天上午10:15触发
     * "0 15 10 * * ?" 每天上午10:15触发
     * "0 15 10 * * ? *" 每天上午10:15触发
     * "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
     * "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
     * "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
     * "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
     * "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
     * "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
     * "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
     * "0 15 10 15 * ?" 每月15日上午10:15触发
     * "0 15 10 L * ?" 每月最后一日的上午10:15触发
     * "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
     * "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
     * "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
     */
    //反复被执行的方法 隔5秒钟执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis() {
        //1.获取当前的时间对应的5个时间段
        List<Date> dateMenus = DateUtil.getDateMenus();
        //2. 循环查询每个时间区间的秒杀商品
        for (Date startTime : dateMenus) {

            //2022082616
            String extName =  DateUtil.data2str(startTime,DateUtil.PATTERN_YYYYMMDDHH);
            //3.将循环到的时间段 作为条件 从数据库中执行查询 得出数据集

            /**
             * select * from tb_seckill_goods where
             stock_count>0
             and `status`='1'
             and start_time > 开始时间段
             and end_time < 开始时间段+2hour  and id  not in (redis中已有的id)
             */

            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //审核状态通过
            criteria.andEqualTo("status","1");
            //商品库存stock_count>0
            criteria.andGreaterThan("stockCount",0);

            criteria.andGreaterThanOrEqualTo("startTime",startTime);
            criteria.andLessThan("endTime",DateUtil.addDateHour(startTime, 2));

            //排除掉redis已有的商品
            Set keys = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).keys();
            if(keys!=null && keys.size()>0) {
                criteria.andNotIn("id", keys);
            }

            //查询商品
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);

            //4.将数据集存储到redis中(key field value的数据格式 )
            /**
             *  HashMap
                key(时间段:2019090516)   field (id:1)   value(商品的数据pojo)
                                        field (id:2)   value(商品的数据pojo)

                key(时间段:2019090518)   field (id:3)   value(商品的数据pojo)
                                        field (id:4)   value(商品的数据pojo)
             */
            for (SeckillGoods seckillGood : seckillGoods) {
                Long goodsId = seckillGood.getId();
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + extName).put(goodsId,seckillGood);
                //设置有效期
                redisTemplate.expireAt(SystemConstants.SEC_KILL_GOODS_PREFIX + extName,DateUtil.addDateHour(startTime, 2));

                //添加一个计数器 (key:商品的ID  value : 库存数) 超卖控制
                redisTemplate.boundHashOps(SystemConstants.SECK_KILL_GOODS_COUNT_KEY).increment(goodsId,seckillGood.getStockCount());
            }
        }
    }

    /*public static void main(String[] args) {
        //获取所有的时间段(根据当前的时间获取5个)
        List<Date> dateMenus = DateUtil.getDateMenus();

        for (Date dateMenu : dateMenus) {
            System.out.println(dateMenu);
        }
    }*/
}
