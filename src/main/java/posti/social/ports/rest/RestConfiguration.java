package posti.social.ports.rest;

import java.util.List;
import javax.validation.Validator;

import posti.social.application.domain.FindMessageQuery;
import posti.social.application.domain.InboxQuery;
import posti.social.application.domain.MessageRepository;
import posti.social.application.domain.UserRepository;
import posti.social.adapters.FindMessageCommandAdapter;
import posti.social.adapters.FollowUserServiceAdapter;
import posti.social.adapters.GetInboxCommandAdapter;
import posti.social.adapters.GetMessageByIdServiceAdapter;
import posti.social.adapters.PublishMessageServiceAdapter;
import posti.social.adapters.ReplyMessageServiceAdapter;
import posti.social.application.api.CreateUserService;
import posti.social.adapters.CreateUserServiceAdapter;
import posti.social.application.api.FindMessageCommand;
import posti.social.application.api.FollowUserService;
import posti.social.application.api.GetInboxCommand;
import posti.social.application.api.GetMessageByIdService;
import posti.social.application.api.PublishMessageService;
import posti.social.application.api.ReplyMessageService;
import posti.social.ports.querydsl.FindMessageQueryDslPredicate;
import posti.social.ports.querydsl.InboxQueryDslPredicate;
import com.querydsl.core.QueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
public class RestConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Validator validator;

    @Bean
    public CreateUserService createUserService(UserRepository repository) {
        return new CreateUserService(repository);
    }

    @Bean
    public PublishMessageService publishMessageService(UserRepository repository) {
        return new PublishMessageService(repository);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(autowiredArgumentResolver());
    }

    @Bean
    public FollowUserService followUserService(UserRepository userRepository) {
        return new FollowUserService(userRepository);
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public GetInboxCommand getInboxCommand(InboxQuery inboxQuery) {
       return new GetInboxCommand(inboxQuery);
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public InboxQuery inboxQuery(MessageRepository messageRepository) {
        return new InboxQueryDslPredicate(messageRepository);
    }

    @Bean
    public GetMessageByIdService getMessageByIdService(MessageRepository messageRepository) {
        return new GetMessageByIdService(messageRepository);
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public FindMessageQuery findMessageQuery(MessageRepository messageRepository) {
        return new FindMessageQueryDslPredicate(messageRepository);
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public FindMessageCommand findMessageCommand(FindMessageQuery query) {
        return new FindMessageCommand(query);
    }

    @Bean
    public ReplyMessageService replyMessageService(MessageRepository messageRepository, UserRepository userRepository) {
        return new ReplyMessageService(userRepository, messageRepository);
    }

    public HandlerMethodArgumentResolver autowiredArgumentResolver() {
        return new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterAnnotation(Autowired.class) != null
                    && applicationContext.getBean(parameter.getParameterType()) != null;
            }

            @Override
            public Object resolveArgument(
                    MethodParameter parameter,
                    ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest,
                    WebDataBinderFactory binderFactory) {

                return applicationContext.getBean(parameter.getParameterType());
            }
        };
    }

    @Bean
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(QueryFactory.class);
        return bean;
    }

    @Bean
    public QueryFactory queryFactory() {
        return (QueryFactory) serviceLocatorFactoryBean().getObject();
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public FindMessageCommandAdapter FindMessageCommandAdapter(Validator validator, FindMessageCommand command) {
        return new FindMessageCommandAdapter(command, validator);
    }

    @Bean
    public GetMessageByIdServiceAdapter getMessageByIdServiceAdapter(Validator validator, GetMessageByIdService service) {
        return new GetMessageByIdServiceAdapter(service, validator);
    }

    @Bean
    public ReplyMessageServiceAdapter replyMessageServiceAdapter(Validator validator, ReplyMessageService service) {
        return new ReplyMessageServiceAdapter(service, validator);
    }

    @Bean
    public PublishMessageServiceAdapter publishMessageServiceAdapter(Validator validator, PublishMessageService service) {
        return new PublishMessageServiceAdapter(service, validator);
    }

    @Bean
    public CreateUserServiceAdapter createUserServiceAdapter(Validator validator, CreateUserService service) {
        return new CreateUserServiceAdapter(service, validator);
    }

    @Bean
    public FollowUserServiceAdapter followUserServiceAdapter(Validator validator, FollowUserService service) {
        return new FollowUserServiceAdapter(service, validator);
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public GetInboxCommandAdapter getInboxCommandAdapter(Validator validator, GetInboxCommand command) {
        return new GetInboxCommandAdapter(command, validator);
    }
}
