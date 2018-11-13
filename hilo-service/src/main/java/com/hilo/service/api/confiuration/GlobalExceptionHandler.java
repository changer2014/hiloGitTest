package com.hilo.service.api.confiuration;

import com.scooter.common.api.JsonResponse;
import com.scooter.common.api.LogicException;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Logic exception json response.
     *
     * @param e the e
     * @return the json response
     */
    @ExceptionHandler(value = {LogicException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public JsonResponse<?> logicException(LogicException e) {

        String code = e.getCode();
        Object[] args = e.getArgs();
        String message = messageSource.getMessage(code, args, LocaleContextHolder.getLocale ());
        return JsonResponse.failed (message);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        //log.error ("Internal exception", ex);
        if (ex instanceof MethodArgumentNotValidException) {
            List<String> messages = new ArrayList<> ();
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            e.getBindingResult ().getAllErrors ().forEach (objectError ->
                    messages.add (objectError.getDefaultMessage ()));
            return new ResponseEntity<> (JsonResponse.failed (messages.toArray (new String[0])), headers,
                    status);
        }
        return new ResponseEntity<> (JsonResponse.failed (status.getReasonPhrase ()), headers, status);
    }
}
