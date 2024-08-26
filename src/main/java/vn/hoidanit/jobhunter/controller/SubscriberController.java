package vn.hoidanit.jobhunter.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.subscriber.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("FETCH CREATE SUBSCRIBER")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {
        // check email
        if(subscriberService.isEmail(subscriber.getEmail())){
            throw  new IdInvalidException("Email" + subscriber.getEmail()+" da ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.handleCreateSubscriber(subscriber));
    }



    @GetMapping("/subscribers/skills")
    @ApiMessage("FETCH SUBSCRIBER SKILL")
    public ResponseEntity<Subscriber> getSubscribersKill()  {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()? SecurityUtil.getCurrentUserLogin().get() : "";

        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.getSubscriberByEmail(email));
    }
}
