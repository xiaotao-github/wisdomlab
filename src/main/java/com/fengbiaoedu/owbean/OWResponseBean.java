package com.fengbiaoedu.owbean;

import lombok.Data;

import java.util.Map;

@Data
public class OWResponseBean {
    boolean result;
    String type;
    String session;
    String description;
    Map argument;
    String command;
    int sequence;
}
