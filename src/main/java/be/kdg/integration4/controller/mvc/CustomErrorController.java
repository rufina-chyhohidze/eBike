package be.kdg.integration4.controller.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomErrorController {


    @ExceptionHandler(Exception.class)
    public Object handleError(Exception e, HttpServletRequest request, Model model) {
        final boolean isApi = request.getRequestURI().startsWith("/api");
        if (isApi) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
        }
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

        log.debug("Error {}: {} - {}", errorCode, errorMessage, errorDescription);

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDescription", errorDescription);

        return "error";
    }

    private record ErrorDto(String message) {
    }
}
