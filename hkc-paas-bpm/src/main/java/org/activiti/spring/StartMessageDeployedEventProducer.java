package org.activiti.spring;

import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.events.StartMessageDeployedEvent;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.runtime.api.event.impl.StartMessageSubscriptionConverter;
import org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

@Slf4j
public class StartMessageDeployedEventProducer extends AbstractActivitiSmartLifeCycle {

    private RepositoryService repositoryService;
    private ManagementService managementService;
    private APIProcessDefinitionConverter converter;
    private StartMessageSubscriptionConverter subscriptionConverter;
    private List<ProcessRuntimeEventListener<StartMessageDeployedEvent>> listeners;
    private ApplicationEventPublisher eventPublisher;

    public StartMessageDeployedEventProducer(RepositoryService repositoryService,
                                             ManagementService managementService,
                                             StartMessageSubscriptionConverter subscriptionConverter,
                                             APIProcessDefinitionConverter converter,
                                             List<ProcessRuntimeEventListener<StartMessageDeployedEvent>> listeners,
                                             ApplicationEventPublisher eventPublisher) {
        this.repositoryService = repositoryService;
        this.managementService = managementService;
        this.subscriptionConverter = subscriptionConverter;
        this.converter = converter;
        this.listeners = listeners;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void doStart() {
        log.debug("StartMessageDeployedEventProducer");
    }

    @Override
    public void doStop() {

    }
}
