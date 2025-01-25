package com.moquawel.tenderInvitation.schedule;

import com.moquawel.tenderInvitation.service.OfferService;
import com.moquawel.tenderInvitation.service.TenderInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyScheduledTasks {

    private final TenderInvitationService tenderInvitationService;
    private final OfferService offerService;

////    @Scheduled(cron = "0 0 8-20 * * 1-6")
//    @Scheduled(cron = "0/15 * * * * * ")
//    public void tenderInvitationTask() {
//        try {
//            log.info("Tender Invitation");
//            tenderInvitationService.getOffersFromTuneps();
//        }catch (Exception e) {
//            log.error("this err occurred while loading the offers from tuneps: {}", e.getMessage());
//        }
//    }
//
//    @Scheduled(cron = "0 59 23 * * ?")
////    @Scheduled(cron = "0/5 * * * * ?")
//    public void dbCleaningTask() {
//        try {
//            offerService.removeExpiredOffers();
//        }catch (Exception e) {
//            log.error("this err occurred while deleting expired offers: {}",e.getMessage());
//        }
//    }

}