package vn.hoidanit.jobhunter.service.subscriber;

import vn.hoidanit.jobhunter.domain.Subscriber;

public interface SubscriberService {
    boolean isEmail(String email);
    Subscriber handleCreateSubscriber(Subscriber subscriber);
    Subscriber handleUpdateSubscriber(Subscriber subscriberDb,Subscriber subscriber);

    Subscriber fetchSubscriberById(long id);

    void sendSubscriberEmailJobs();

    Subscriber getSubscriberByEmail(String email);


}
