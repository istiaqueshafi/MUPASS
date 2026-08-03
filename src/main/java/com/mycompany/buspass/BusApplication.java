package com.mycompany.buspass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BusApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd MMM, hh:mm a");

    private String applicationId;
    private String passNumber;
    private String shift;          
    private String pickupPoint;
    private String destination;
    private String route;
    private String busNumber;
    private String status;         

    private LocalDateTime submittedAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime expiryDate;

    public BusApplication(String applicationId, String shift, String pickupPoint,
                           String destination, String route, String busNumber) {
        this.applicationId = applicationId;
        this.shift = shift;
        this.pickupPoint = pickupPoint;
        this.destination = destination;
        this.route = route;
        this.busNumber = busNumber;
        this.submittedAt = LocalDateTime.now();
    }

    public String getApplicationId() { return applicationId; }
    public String getPassNumber() { return passNumber; }
    public void setPassNumber(String passNumber) { this.passNumber = passNumber; }
    public String getShift() { return shift; }
    public String getPickupPoint() { return pickupPoint; }
    public String getDestination() { return destination; }
    public String getRoute() { return route; }
    public String getBusNumber() { return busNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime v) { this.verifiedAt = v; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime a) { this.approvedAt = a; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime e) { this.expiryDate = e; }

    public String fmtSubmitted() { return submittedAt == null ? "-" : submittedAt.format(FMT); }
    public String fmtVerified() { return verifiedAt == null ? "-" : verifiedAt.format(FMT); }
    public String fmtApproved() { return approvedAt == null ? "-" : approvedAt.format(FMT); }
    public String fmtExpiry() { return expiryDate == null ? "-" : expiryDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")); }
}
