package edu.hust.soict.cbls.common.exception;

import edu.hust.soict.cbls.common.utils.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommonException extends RuntimeException{

    public static String getMessage(Throwable e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String messages = e.getMessage();
        return e.getClass().getCanonicalName() + (!StringUtils.isNullOrEmpty(messages) ? ": " + messages : "") + "\n\tat " +
                Arrays.stream(stackTraceElements)
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n\tat "));
    }
}
