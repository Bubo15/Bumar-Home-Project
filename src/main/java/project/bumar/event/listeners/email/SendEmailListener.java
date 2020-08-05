package project.bumar.event.listeners.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.bumar.constant.EmailTemplateConstants;
import project.bumar.event.events.email.SendOrderDeletedEmailEvent;
import project.bumar.event.events.email.SendProductCreatedEmailEvent;
import project.bumar.event.events.email.SendProductDeletedEmailEvent;
import project.bumar.service.services.EmailService;
import project.bumar.service.services.UserService;

@Component
public class SendEmailListener {

    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public SendEmailListener(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @EventListener(SendProductDeletedEmailEvent.class)
    public void sendProductDeletedEmail(SendProductDeletedEmailEvent sendProductDeletedEmailEvent) {
        this.emailService.sendMessage(sendProductDeletedEmailEvent.getUserEmail(), EmailTemplateConstants.WELCOME_SUBJECT, String.format(EmailTemplateConstants.ORDER_PRODUCT_DELETE_TEMPLATE, sendProductDeletedEmailEvent.getProduct().getName()));
    }

    @EventListener(SendProductCreatedEmailEvent.class)
    public void sendProductCreatedEmail(SendProductCreatedEmailEvent sendProductCreatedEmailEvent) {
        this.userService.getAllUsers()
                .forEach(userServiceModel -> {
                    this.emailService.sendMessage(
                            userServiceModel.getUserProfile().getEmail(),
                            EmailTemplateConstants.WELCOME_SUBJECT,
                            String.format(EmailTemplateConstants.PRODUCT_CREATE_TEMPLATE,
                                    String.format(EmailTemplateConstants.PRODUCT_DETAILS_LINK, sendProductCreatedEmailEvent.getProduct().getId()),
                                    EmailTemplateConstants.WEBSITE_LINK));
                });

    }

    @EventListener(SendOrderDeletedEmailEvent.class)
    public void sendDeletedOrderEmail(SendOrderDeletedEmailEvent sendOrderDeletedEmailEvent){
        this.emailService.sendMessage(
                sendOrderDeletedEmailEvent.getOrder().getEmail(),
                EmailTemplateConstants.WELCOME_SUBJECT,
                String.format(EmailTemplateConstants.ORDER_DELETE_TEMPLATE, sendOrderDeletedEmailEvent.getOrder().getId()));
    }
}
