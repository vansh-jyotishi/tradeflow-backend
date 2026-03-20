package com.tradeflow.backend.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class InquiryStatusConverter implements AttributeConverter<Inquiry.InquiryStatus, String> {

    @Override
    public String convertToDatabaseColumn(Inquiry.InquiryStatus status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public Inquiry.InquiryStatus convertToEntityAttribute(String dbValue) {
        if (dbValue == null) return null;
        for (Inquiry.InquiryStatus s : Inquiry.InquiryStatus.values()) {
            if (s.getDbValue().equals(dbValue)) return s;
        }
        throw new IllegalArgumentException("Unknown inquiry status: " + dbValue);
    }
}
