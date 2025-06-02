//package be.kdg.integration4.service.email;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EmailSenderRunner implements CommandLineRunner {
//
//    @Autowired
//    private EmailService emailService;
//
//    // TODO: TO TEST ENTER YOUR REAL EMAIL HERE ADN RUN THE APP - U SHOULD RECEIVE AN EMAIL SHORTLY AFTER
//    private final String yourRealEmail = "anir@saddik.dev";
//
//    @Override
//    public void run(String... args) throws Exception {
//        emailService.sendUserRejectedEmail("anirtraning@gmail.com");
//        emailService.sendUserRejectedEmail(yourRealEmail);
//    }
//}
//
