package be.kdg.integration4.controller.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int errorCode = (statusCode != null) ? Integer.parseInt(statusCode.toString()) : 404;

        String errorMessage = "Oops. Looks like it's broken.";
        String errorDescription = "We encountered an error while processing your request. Please try again later.";


        if (errorCode == HttpStatus.NOT_FOUND.value()) //404
        {
            errorMessage = "Page Not Found ";
            errorDescription = "Seems like the page you're looking for doesn't exist.";
        } else if (errorCode == HttpStatus.FORBIDDEN.value())//403
        {
            errorMessage = "Access Forbidden";
            errorDescription = "You don't have permission to access this page.";
        } else if (errorCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) //500
        {
            errorMessage = "Internal Server Error";
            errorDescription = "Something went wrong with the server.";
        }

        logger.debug("Error {}: {} - {}", errorCode, errorMessage, errorDescription);

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDescription", errorDescription);

        return "error";
    }
}
