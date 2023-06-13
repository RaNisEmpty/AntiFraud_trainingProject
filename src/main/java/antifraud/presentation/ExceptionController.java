package antifraud.presentation;


import antifraud.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(WrongFormatException.class)
    public ResponseEntity<HttpStatus> handleWrongFormatException(WrongFormatException e, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<HttpStatus> handleEntityExistsException(EntityExistsException e, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FeedbackAlreadySpecifiedException.class)
    public ResponseEntity<HttpStatus> handleFeedbackAlreadySpecifiedException(FeedbackAlreadySpecifiedException e, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpStatus> handleNotFoundException(NotFoundException e, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnprocessableFeedbackException.class)
    public  ResponseEntity<HttpStatus> handleUnprocessableEntityException(UnprocessableFeedbackException e, WebRequest request) {
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
