package vn.hoidanit.jobhunter.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService){
        this.subscriberService = subscriberService;
    }

     @PostMapping("/subscribers")
    @ApiMessage("create Subscriber success")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {

        boolean isExist = this.subscriberService.isExistByEmail(subscriber.getEmail());
        if(isExist== true){
            throw new IdInvalidException("Email "+ subscriber.getEmail() + " đã tồn tại");
        }
        Subscriber currentSubscriber = this.subscriberService.create(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(currentSubscriber);
        // cach2: return ResponseEntity.ok(newUser);
    }

     @PutMapping("/subscribers")
    @ApiMessage("update subscribers success")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException
    {   
       Subscriber subsDB = this.subscriberService.findById(subscriber.getId()); 
        if(subsDB == null){
            throw new IdInvalidException(" id = "+ subsDB.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.subscriberService.update(subsDB, subscriber));
    }

     @PostMapping("/subscribers/skills")
    @ApiMessage("get Subscribers skill")
    public ResponseEntity<Subscriber> getSubscribersSkill()
            throws IdInvalidException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        
        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
}
