package consulting.segieth.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@Component
public class RoleInspector implements CommandLineRunner {

    private final ApplicationContext applicationContext;
    
    public static Set<String> allRolesInCode;

    public RoleInspector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) {
        // Holt alle Views, die eine Route haben
        discoverUsedRoles("consulting.segieth");
        
    }
    
    public Set<String> discoverUsedRoles(String basePackage) {
        allRolesInCode = new HashSet<>();

        // 1. Scanner erstellen (false = keine Standardfilter wie @Component)
        ClassPathScanningCandidateComponentProvider scanner = 
            new ClassPathScanningCandidateComponentProvider(false);

        // 2. Nur nach der RolesAllowed Annotation suchen
        scanner.addIncludeFilter(new AnnotationTypeFilter(RolesAllowed.class));

        // 3. Scan im Package starten
        scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                RolesAllowed annotation = clazz.getAnnotation(RolesAllowed.class);
                if (annotation != null) {
                    allRolesInCode.addAll(Arrays.asList(annotation.value()));
                    System.out.println("Gefunden in " + clazz.getSimpleName() + ": " + Arrays.toString(annotation.value()));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        
        return allRolesInCode;
    }
}