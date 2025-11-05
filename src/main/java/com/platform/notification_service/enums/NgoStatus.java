package com.platform.notification_service.enums;

/**
 * Enum representing the verification status of an NGO.
 */
public enum NgoStatus {
    /**
     * Status when the NGO has not yet been verified.
     */
    UNVERIFIED,
    /**
     * Status when the NGO verification is in progress.
     */
    PENDING,
    /**
     * Status when the NGO has been successfully verified.
     */
    VERIFIED,
    /**
     * Status when the NGO verification has been denied.
     */
    DENIED
}
