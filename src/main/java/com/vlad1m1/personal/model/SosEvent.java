package com.vlad1m1.personal.model;

import com.vlad1m1.personal.enums.SmsDeliveryStatus;
import com.vlad1m1.personal.enums.SosStatus;
import com.vlad1m1.personal.enums.SosTargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sos_events")
public class SosEvent {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SosTargetType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private String targetPhone;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private Double latitude;

    private Double longitude;

    private Integer accuracyMeters;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SosStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SmsDeliveryStatus smsDeliveryStatus;

    private String smsProviderMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SosTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(SosTargetType targetType) {
        this.targetType = targetType;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getTargetPhone() {
        return targetPhone;
    }

    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getAccuracyMeters() {
        return accuracyMeters;
    }

    public void setAccuracyMeters(Integer accuracyMeters) {
        this.accuracyMeters = accuracyMeters;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SosStatus getStatus() {
        return status;
    }

    public void setStatus(SosStatus status) {
        this.status = status;
    }

    public SmsDeliveryStatus getSmsDeliveryStatus() {
        return smsDeliveryStatus;
    }

    public void setSmsDeliveryStatus(SmsDeliveryStatus smsDeliveryStatus) {
        this.smsDeliveryStatus = smsDeliveryStatus;
    }

    public String getSmsProviderMessage() {
        return smsProviderMessage;
    }

    public void setSmsProviderMessage(String smsProviderMessage) {
        this.smsProviderMessage = smsProviderMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
