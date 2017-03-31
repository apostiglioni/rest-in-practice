package posti.social.application.domain;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Message.class)
public abstract class Message_ extends posti.social.application.domain.PersistentObject_ {

	public static volatile SingularAttribute<Message, LocalDateTime> publishTime;
	public static volatile ListAttribute<Message, Message> replies;
	public static volatile SingularAttribute<Message, User> author;
	public static volatile SingularAttribute<Message, String> body;

}

