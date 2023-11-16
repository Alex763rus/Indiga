package com.example.indiga.model.jpa;

import com.example.indiga.enums.ReceptionExportStatus;
import com.example.indiga.enums.ServiceType;
import com.example.indiga.enums.TimePeriod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "reception")
public class Reception {

    @Id
    @Column(name = "reception_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receptionId;

    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "chatId")
    private User user;

    @Column(name = "service_type")
    private ServiceType serviceType;

    @Column(name = "reception_date")
    private Date receptionDate;

    @Column(name = "reception_time")
    private TimePeriod receptionTime;

    @Column(name = "reception_registration_date")
    private Timestamp receptionRegistrationDate;

    @Column(name = "export_status")
    private ReceptionExportStatus receptionExportStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reception reception = (Reception) o;
        return Objects.equals(receptionId, reception.receptionId) && Objects.equals(user, reception.user) && serviceType == reception.serviceType && Objects.equals(receptionDate, reception.receptionDate) && receptionTime == reception.receptionTime && Objects.equals(receptionRegistrationDate, reception.receptionRegistrationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receptionId, user, serviceType, receptionDate, receptionTime, receptionRegistrationDate);
    }
}
