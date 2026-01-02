package dev.aira.notificacao.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaStartupDiagnostic {

    private final ApplicationContext applicationContext;
    private final KafkaListenerEndpointRegistry registry;

    @PostConstruct
    public void checkBeans() {
        log.info("=== VERIFICANDO BEANS KAFKA ===");

        String[] allBeans = applicationContext.getBeanDefinitionNames();
        log.info("Total de beans na aplicação: {}", allBeans.length);

        for (String beanName : allBeans) {
            if (beanName.toLowerCase().contains("kafka") ||
                    beanName.toLowerCase().contains("consumer")) {
                log.info("Bean encontrado: {}", beanName);
            }
        }

        try {
            Object consumer = applicationContext.getBean("consultationConsumer");
            log.info("✅ ConsultationConsumer encontrado: {}", consumer.getClass().getName());
        } catch (NoSuchBeanDefinitionException e) {
            log.error("❌ ConsultationConsumer NÃO encontrado!");
            log.error("Verifique se a classe está anotada com @Component");
            log.error("Verifique se o pacote está sendo escaneado");
        }

        try {
            Object factory = applicationContext.getBean("protobufKafkaListenerContainerFactory");
            log.info("✅ protobufKafkaListenerContainerFactory encontrado {}", factory.getClass().getName());
        } catch (NoSuchBeanDefinitionException e) {
            log.error("❌ protobufKafkaListenerContainerFactory NÃO encontrado!");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("=== APLICAÇÃO PRONTA - VERIFICANDO LISTENERS ===");

        Collection<MessageListenerContainer> containers = registry.getListenerContainers();
        log.info("Total de listener containers registrados: {}", containers.size());

        if (containers.isEmpty()) {
            log.error("❌❌❌ NENHUM LISTENER REGISTRADO! ❌❌❌");
            log.error("Possíveis causas:");
            log.error("1. @KafkaListener não está sendo processado");
            log.error("2. Componente não está sendo escaneado pelo Spring");
            log.error("3. Erro na criação do bean");
        } else {
            containers.forEach(container -> {
                log.info("--- Listener Container ---");
                log.info("ID: {}", container.getListenerId());
                log.info("Running: {}", container.isRunning());
                log.info("Paused: {}", container.isPauseRequested());
                log.info("Auto-startup: {}", container.isAutoStartup());
                log.info("Assigned Partitions: {}", container.getAssignedPartitions());

                if (!container.isRunning()) {
                    log.error("❌ Container NÃO está rodando!");
                    try {
                        log.info("Tentando iniciar manualmente...");
                        container.start();
                        log.info("✅ Container iniciado com sucesso");
                    } catch (Exception e) {
                        log.error("❌ Erro ao iniciar container", e);
                    }
                } else {
                    log.info("✅ Container está rodando");
                }
                log.info("-------------------------");
            });
        }
    }
}
