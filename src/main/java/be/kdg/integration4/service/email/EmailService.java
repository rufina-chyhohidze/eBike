package be.kdg.integration4.service.email;

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

    @Value("${MAIL_USERNAME}")
    private String originMail;

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
                "and password: " + password + ". We recommend you to change password as soon as you login.");
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

    public void sendReportURLToCustomer(long reportId) throws MessagingException {
        BikeReport report = this.bikeReportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report with id " + reportId + " - NOT FOUND"));
        String html = String.format("""
                <html>
                    <body>
                        <h1>Bike Report</h1>
                        <h5>Take a look at your bike report: <a href="/report/%d">Bike Report</a></h5>
                        <p>If you have any questions you can contact us at team18int4@gmail.com</p>
                    </body>
                </html>
                """, reportId);

        sendHtmlEmail(report.getCustomer().getEmail(), "Bke Report", html);
    }
}
