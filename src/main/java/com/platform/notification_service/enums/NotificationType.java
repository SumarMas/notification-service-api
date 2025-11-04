package com.platform.notification_service.enums;
/**
 * Enumeration of different types of notifications.
 */
public enum NotificationType {
    /** Notification type for user creation events. */
    USER_CREATED,
    /** Notification type for Receiving NGO documents. */
    NGO_DOCUMENTS_RECEIVED,
    /** Notification type for approved NGO documents. */
    NGO_DOCUMENTS_APPROVED,
    /** Notification type for rejected NGO documents. */
    NGO_DOCUMENTS_REJECTED,
    /** Notification type for successful donations. */
    DONATION_SUCCESS,
    /** Notification type for Finalized Campaigns. */
    CAMPAIGN_FINALIZED,
    /** Notification type when Ngo
     * Published Message in Campaigns. */
    NGO_PUBLISHED_MESSAGE,
    /** Notification type for Payout Requests. */
    PAYOUT_REQUESTED,
    /** Notification type for Approved Payouts. */
    PAYOUT_APPROVED
}
