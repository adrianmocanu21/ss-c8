package adrian.mocanu.ssc6.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {
// Passing the security context from one thread to another
// o1: change the MODE to MODE_INHERITABLETHREADLOCAL of SecurityContext by adding a InitializingBean (for spring created threads)
// o2: for self created threads o1 should still work but you can wrap your created runnable intro a DelegatingSecurityContextRunnable/DelegatingSecurityContextCallable
// o3: change the exec service to DelegatingContextExecutorService -> we wrap the service

    //    o1
    @GetMapping("/hello")
    @Async
    private String hello(Authentication authentication) {
        System.out.println("Sunt pe threadul: " +  Thread.currentThread().getId());
        return  "Hello " + authentication.getName() + ". Sunt pe threadul: " +  Thread.currentThread().getId(); // we will print the token
    }

//    o2
//    @GetMapping("/hello")
//    private String hello(Authentication authentication) {
//
//        Runnable r = () ->
//        { System.out.println(authentication);
//            SecurityContextHolder.getContext().getAuthentication();
//        };
//        DelegatingSecurityContextRunnable dsr = new DelegatingSecurityContextRunnable(r);
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        service.submit(dsr);
//        service.shutdown();
//
//        return "Hello " + authentication.getName(); // we will print the token
//    }


//  o3
//    @GetMapping("/hello")
//    private String hello(Authentication authentication) {
//
//        Runnable r = () ->
//        { System.out.println(authentication);
//            SecurityContextHolder.getContext().getAuthentication();
//        };
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        DelegatingSecurityContextExecutorService dService = new DelegatingSecurityContextExecutorService(service);
//        dService.submit(r);
//        service.shutdown();
//
//        return "Hello " + authentication.getName(); // we will print the token
//    }

}
