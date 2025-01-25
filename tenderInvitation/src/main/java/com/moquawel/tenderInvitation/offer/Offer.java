package com.moquawel.tenderInvitation.offer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "offer")
public class Offer {

    @Id
    private String offerId;

    @Transient
    private String bdRecvEndDt;

    private LocalDateTime myBdRecvEndDt;

    private String bidInstNm;

    private String bidNmEn;

    @Transient
    private String publicDt;

    private LocalDateTime myPublicDt;

    private String bidModSeq;

    private String bidNo;

    private String bidNmAr;

    private String bidNmFr;

    private int epBidMasterId;

    private String companyType;

    private String category;


}
