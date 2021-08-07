package com.smartpra.demo.app;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AircraftInsertEVentListener implements PostInsertEventListener {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private KafkaTemplate<String, Object> kafkaAircraftTemplae;

	@PostConstruct
	protected void init() {
		SessionFactoryImpl sF = entityManagerFactory.unwrap(SessionFactoryImpl.class);
		EventListenerRegistry registry = sF.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener((PostInsertEventListener) this);
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Async//Asynchronous process 
	public void onPostInsert(PostInsertEvent event) {

		final Object entity = event.getEntity();
		if (entity instanceof Aircraft) {

			Aircraft aircraft = (Aircraft) entity;
			
			//Set the Persistance entity to kafkaTemplate
			kafkaAircraftTemplae.send("InsertAircraft", entity);

		}

	}

}
