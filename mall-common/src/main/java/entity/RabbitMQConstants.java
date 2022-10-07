package entity;

public class RabbitMQConstants {
    public static final String DELAY_TIME= Integer.toString(1000 * 60 * 35); // 35分钟, 检查订单付款状态的延时队列+5分钟再检查,防止用户出现卡点支付


    /*** 普通订单 延时队列 ***/
    public static final String QUEUE_ORDER_DELAY = "queue.orderdelay";

    /*** 普通订单 支付延时消费队列 ***/
    public static final String QUEUE_ORDER_CHECK = "queue.ordercheck";

    /*** 普通订单 支付队列 ***/
    public static final String QUEUE_ORDER_PAY = "queue.orderpay";

    /*** 普通订单 延时队列交换机 ***/
    public static final String EXCHANGE_ORDER_DELAY = "exchange.orderdelay";

    /*** 普通订单 延时队列交换机 ***/
    public static final String EXCHANGE_ORDER_PAY = "exchange.orderpay";



    /*** 秒杀订单 延时队列 ***/
    public static final String QUEUE_SEC_KILL_ORDER_DELAY = "queue.seckilldelay";

    /*** 秒杀订单 支付延时消费队列 ***/
    public static final String QUEUE_SEC_KILL_ORDER_CHECK = "queue.seckillordercheck";

    /*** 秒杀订单 支付队列 ***/
    public static final String QUEUE_SEC_KILL_ORDER_PAY = "queue.seckillorderpay";

    /*** 秒杀订单 延时队列交换机 ***/
    public static final String EXCHANGE_SEC_KILL_ORDER_DELAY = "exchange.seckilldelay";

    /*** 秒杀订单 延时队列交换机 ***/
    public static final String EXCHANGE_SEC_KILL_ORDER_PAY = "exchange.seckillorderpay";
}
