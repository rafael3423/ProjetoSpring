package com.example.demo2.api.exceptionHandler;

import com.example.demo2.domain.exception.NegocioException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

//    @Autowired
//    private MessageSource messageSource;  //>> caso quiser usar com frases criadas no message.properties.
    
    @org.springframework.web.bind.annotation.ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request){
        
        var status = HttpStatus.BAD_REQUEST;
        var erros = new Erros(ex.getMessage(),status.value(),OffsetDateTime.now(),null);
        
        return handleExceptionInternal(ex, erros, new HttpHeaders(), status, request);
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        var campos = new ArrayList<CamposDoErro>();
       
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            campos.add(new CamposDoErro(error.getDefaultMessage(), ((FieldError)error).getField()));
//            campos.add(new CamposDoErro(messageSource.getMessage(error, LocaleContextHolder.getLocale()), ((FieldError)error).getField())); >> caso quiser usar com frases criadas no message.properties.
    
        });

        Erros erros = new Erros("Um ou mais campos preenchidos incorretamente!.",
                status.value(),
                OffsetDateTime.now(),
                campos);

        return super.handleExceptionInternal(ex, erros, headers, status, request);
    }

}

