package win.simple.util.execption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import win.simple.entity.StateEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常转换Json
 * @author Ning
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public StateEntity runtimeException(Exception e) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(500);
        stateEntity.setMsg(e.getMessage());
        return stateEntity;
    }

    @ExceptionHandler(value = BusinessException.class)
    public StateEntity businessExceptionHandler(BusinessException e) {
        StateEntity stateEntity = new StateEntity();
        stateEntity.setState(e.getCode());
        stateEntity.setMsg(e.getMessage());
        return stateEntity;
    }

}
