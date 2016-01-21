package com.soagrowers.product;



import com.soagrowers.product.aggregates.ProductAggregate;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.unitofwork.DefaultUnitOfWorkFactory;
import org.axonframework.unitofwork.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by ben on 21/09/15.
 */
public class ProductCommandApi {


    private final static Logger LOG = LoggerFactory.getLogger(ProductCommandApi.class);
    private final static MongoEventStore eventStore;
    private final static CommandGateway commandGateway;
    private final static ApplicationContext applicationContext;
    private final static EventSourcingRepository<ProductAggregate> repository;
    private final static String CONTEXT_FILE_NAME = "commandContext.xml";

    static {
        LOG.info("Starting the ProductCommandApi with context file: {}", CONTEXT_FILE_NAME);
        applicationContext = new ClassPathXmlApplicationContext(CONTEXT_FILE_NAME);
        commandGateway = applicationContext.getBean(CommandGateway.class);
        repository = (EventSourcingRepository<ProductAggregate>) applicationContext.getBean("productRepository");
        eventStore = (MongoEventStore)applicationContext.getBean("mongoEventStore");
    }

    private ProductCommandApi() {
    }

    public static MongoEventStore getEventStore() {
        return eventStore;
    }

    public static CommandGateway getGateway() {
        return commandGateway;
    }


    public static EventSourcingRepository<ProductAggregate> getRepository() {
        return repository;
    }


    public static final ProductAggregate loadAggregate(String productId) {
        LOG.debug("Loading: 'ProductAggregate' {}", productId);
        UnitOfWork unitOfWork = new DefaultUnitOfWorkFactory().createUnitOfWork();
        ProductAggregate item = repository.load(productId);
        unitOfWork.commit();
        LOG.debug("Loaded: 'ProductAggregate' {}, '{}', complete={}", item.getId(), item.getName(), item.isSaleable());
        return item;
    }
}
