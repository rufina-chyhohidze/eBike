package be.kdg.integration4.service.email;

import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.report.BikeModel;
import be.kdg.integration4.domain.report.BikeReport;
import be.kdg.integration4.repository.BikeReportRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final BikeReportRepository bikeReportRepository;

    @Value("${spring.mail.username}")
    private String originMail;

    @Value("${spring.baseurl}")
    private String url;

    @Autowired
    public EmailService(JavaMailSender mailSender, BikeReportRepository bikeReportRepository) {
        this.mailSender = mailSender;
        this.bikeReportRepository = bikeReportRepository;
    }

//    public void sendEmail(String to, String subject, String text) {
//        // This is still plain text
//        var message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        message.setFrom(originMail);
//        mailSender.send(message);
//    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = isHtml
        helper.setFrom(originMail);

        mailSender.send(message);
    }

    public void sendCustomerRegistrationConfirmationEmail(String to, String name, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Customer Registration Confirmation");
        message.setText("Dear " + name + ", one of our technicians created an account for you. You can use this email " +
                "and password: " + password + ". We recommend you to change password as soon as you login. " +
                "You can access your dashboard with this link: " + url + "/customer/dashboard");
        message.setFrom(originMail);
        mailSender.send(message);
    }

    public void sendUserApprovalEmail(String email) throws MessagingException {
        String html = """
                <html>
                    <body>
                        <h1>Your account has been approved!</h1>
                        <p>Welcome to the system. You can now log in.</p>
                    </body>
                </html>
                """;

        sendHtmlEmail(email, "User Approved!", html);
    }

    public void sendUserRejectedEmail(String email) throws MessagingException {
        String html = """
                <html>
                    <body>
                        <h1>Your account has been rejected!</h1>
                        <p>If you have any questions you can contact us at team18int4@gmail.com</p>
                    </body>
                </html>
                """;

        sendHtmlEmail(email, "User Rejected", html);
    }

    public void sendReportURLToCustomer(long reportId) {
        BikeReport report = this.bikeReportRepository.findByIdWithCustomer(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report with id " + reportId + " - NOT FOUND"));

        Bike bike = report.getBike();
        BikeModel model = bike.getBikeModel();

        StringBuilder visualRows = new StringBuilder();
        report.getVisualInspection().forEach((part, condition) ->
                visualRows.append(String.format(
                        "<tr><td style='padding:8px;'>%s</td><td style='padding:8px;'>%s</td></tr>",
                        part.name().replace("_", " "), condition.name()
                ))
        );

        StringBuilder functionalRows = new StringBuilder();
        report.getFunctionalTest().forEach((part, condition) ->
                functionalRows.append(String.format(
                        "<tr><td style='padding:8px;'>%s</td><td style='padding:8px;'>%s</td></tr>",
                        part.name().replace("_", " "), condition.name()
                ))
        );

        String html = String.format("""
                        <!DOCTYPE html>
                        <html lang="en">
                          <head>
                            <meta charset="UTF-8" />
                            <title>eBike Test Report</title>
                          </head>
                          <body style="background-color:#f3f4f6; font-family:Arial, sans-serif; padding:20px; color:#111827;">
                            <div style="max-width:800px; margin:auto; background-color:#fff; padding:24px; border-radius:12px;">
                              <h1 style="font-size:24px; font-weight:bold; color:#6b21a8;">eBike Test Report</h1>
                        
                              <h2 style="font-size:20px; font-weight:bold; color:#7e22ce; margin-top:24px;">Bike Info</h2>
                              <table style="width:100%%; border-collapse:collapse; margin-top:12px;">
                                <tr><td style="padding:8px;">Brand:</td><td style="padding:8px;">%s</td></tr>
                                <tr><td style="padding:8px;">Frame Number:</td><td style="padding:8px;">%s</td></tr>
                              </table>
                        
                              <h2 style="font-size:20px; font-weight:bold; color:#7e22ce; margin-top:24px;">Technician</h2>
                              <p style="padding:8px;">%s</p>
                        
                              <h2 style="font-size:20px; font-weight:bold; color:#7e22ce; margin-top:24px;">Visual Inspection</h2>
                              <table style="width:100%%; border-collapse:collapse; margin-top:12px; border:1px solid #ddd;">
                                <thead>
                                  <tr style="background-color:#f9fafb;">
                                    <th style="padding:8px; text-align:left;">Component</th>
                                    <th style="padding:8px; text-align:left;">Condition</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  %s
                                </tbody>
                              </table>
                        
                              <h2 style="font-size:20px; font-weight:bold; color:#7e22ce; margin-top:24px;">Functional Test</h2>
                              <table style="width:100%%; border-collapse:collapse; margin-top:12px; border:1px solid #ddd;">
                                <thead>
                                  <tr style="background-color:#f9fafb;">
                                    <th style="padding:8px; text-align:left;">Component</th>
                                    <th style="padding:8px; text-align:left;">Condition</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  %s
                                </tbody>
                              </table>
                        
                              <p style="margin-top:30px; color:#374151; font-size:14px;">Generated on %s</p>
                              <p style="margin-top:20px; font-size:14px;">View your full report: 
                              <a href="%s/report/%d" style="color:#7e22ce;">View Online</a>
                              </p>
                              <p style="font-size:14px;">Questions? Email us at <a href="mailto:team18int4@gmail.com">team18int4@gmail.com</a></p>
                            </div>
                          </body>
                        </html>
                        """,
                model.getBrand(),
                bike.getFrameNumber(),
                report.getTechnician() != null ? report.getTechnician().getName() : "N/A",
                visualRows,
                functionalRows,
                report.getReportDate(),
                url,
                reportId
        );

        try {
            sendHtmlEmail(report.getCustomer().getEmail(), "Your eBike Test Report", html);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send report email", e);
        }
    }


}
