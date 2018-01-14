package fr.sharingcraftsman.user;

import fr.sharingcraftsman.user.api.file.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {
  private StorageService storageService;

  @Value("${status.toinitialize}")
  private boolean toInitialize;

  @Autowired
  public ApplicationInitializer(StorageService storageService) {
    this.storageService = storageService;
  }

  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    if (this.toInitialize) {
      this.storageService.init();
    }
  }
}

