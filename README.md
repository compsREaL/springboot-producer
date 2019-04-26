# springboot-producer
RabbitMQ入门demo

AMQP（Advanced Message Queuing Protocol）核心概念：

    Server：又称Broker，接受客户端的连接，实现AMQP实体服务
    Connection：连接，应用程序与Broker的网络连接
    Channel：网络信道，channel是进行消息读写的通道。客户端可以建立多个channel，每个channel代表一个会话
    Message：消息，服务器和应用程序之间传送的数据，由Properties和Body两部分组成。Properties可以对消息进行修饰，比如消息的优先级、延迟等高级特性；  Body为消息实体内容。
    Virtual Host：虚拟地址，用于进行逻辑隔离，最上层的路由。一个Virtual Host里面可以有若干个Exchange和Queue，同一个Virtual Host里面不能有相同名称的Exchange或Queue
    Exchange：交换机，接收消息，根据路由键转发消息到绑定的队列。
    Binding：Exchange和Queue之间的虚拟连接，binding中可以包含routing key
    Rooting Key：一个路由规则，虚拟机可以用它来确定如何路由一个特定消息
    Queue：也称Message Queue，消息队列，保存消息并将它们转发给消费者
  

RabbitMQ消息流转：

    1.生产者发送一个消息，该消息指定一个Exchange Name 和一个Rooting Key
    2.根据Exchange Name找到指定的Exchange
    3.Exchange根据Rooting Key将消息路由到对应的Queue上
    4.消费者如果监听该Queue，就会获得Message消息
  
  
RabbitMQ可靠性投递方案：

    1.发消息前，先将业务消息数据库存入数据库中，Message Log也记录进对应的表中，并将status设置为0。[Status：0，消息发送中；1，消息发送成功；2，消息发送失败]
    2.以confirm模式异步发送消息给MQ Broker
    3.MQ Broker收到消息后会返回确认，将确认消息回传给producer的Confirm Listener
    4.producer监听到MQ Broker确认的消息后会更新Message Log记录表，将status设置为1。
    5.如果producer未监听到MQ Broker的确认消息，会定时从表中选出status状态为0的数据
    6.将status为0的数据进行重新投递。
    7.设置最大重投次数，如果重投次数大于最大重投次数，则将status设置为2。人工解决。
  


