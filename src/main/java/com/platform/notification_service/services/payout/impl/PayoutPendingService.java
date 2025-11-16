package com.platform.notification_service.services.payout.impl;

import com.platform.notification_service.dtos.Ngo.NgoDto;
import com.platform.notification_service.dtos.payout.PayoutMessageDto;
import com.platform.notification_service.dtos.user.UserDto;
import com.platform.notification_service.entities.NotificationEntity;
import com.platform.notification_service.enums.NotificationType;
import com.platform.notification_service.services.Ngo.INgoGetService;
import com.platform.notification_service.services.notification.INotificationService;
import com.platform.notification_service.services.payout.IPayoutPendingService;
import com.platform.notification_service.services.user.IUserGetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the IPayoutPendingService interface
 * for handling payout pending notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayoutPendingService implements IPayoutPendingService {
    /** Service for retrieving NGO details. */
    private final INgoGetService ngoGetService;
    /** Service for retrieving user details. */
    private final IUserGetService userGetService;
    /** Notification service for sending notifications. */
    private final INotificationService notificationService;
    /**
     * Sends a notification when a payout is pending.
     *
     * @param payoutMessageDto the payout message data transfer object
     */
    @Override
    @Transactional
    public void sendPayoutPendingNotification(PayoutMessageDto payoutMessageDto) {
        log.trace("sendPayoutPendingNotification");
        NgoDto ngoDto = getNgoById(payoutMessageDto.getNgoId());
        appNotifyAdminsPayoutPending(ngoDto, payoutMessageDto);
        appNotifyNgoPayoutPending(ngoDto, payoutMessageDto);
    }

    private void appNotifyAdminsPayoutPending(NgoDto ngoDto, PayoutMessageDto payoutMessageDto) {
        String title = "Payout Pending Approval";
        String message = String.format("NGO %s has requested a payout of $%s pending your approval.",
                ngoDto.getName(), payoutMessageDto.getTotalAmount());
        List<UserDto> adminUsers = getUsersAdmin();
        for (UserDto admin : adminUsers) {
            sendInAppNotification(admin, title, message, NotificationType.PAYOUT_REQUESTED);
        }
    }
    private void appNotifyNgoPayoutPending(NgoDto ngoDto, PayoutMessageDto payoutMessageDto) {
        String title = "Payout Request Submitted";
        String message = String.format("Your payout request of $%s is pending approval.",
                payoutMessageDto.getTotalAmount());
        sendInAppNotification(ngoDto.getUserCreator(), title, message, NotificationType.PAYOUT_REQUESTED);
    }
    private NgoDto getNgoById(UUID id) {
        return ngoGetService.getById(id);
    }
    private List<UserDto> getUsersAdmin() {
        return userGetService.getAdminUsers();
    }
    private void sendInAppNotification(UserDto userDto,
                                       String title, String message,
                                       NotificationType notificationType) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId(UUID.randomUUID())
                .userId(UUID.fromString(userDto.getId()))
                .message(message)
                .title(title)
                .type(notificationType)
                .build();
        notificationService.createNotification(notificationEntity);
    }
}
