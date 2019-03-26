package account_manager;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public enum  AppContext {
    INSTANCE;

    private final ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);

    public ApplicationContext getContext() {
        return context;
    }
}
