package com.fengbiaoedu.owbean;

import lombok.Data;

import java.util.Map;

@Data
public class OWQuestBean {
    String type;
    String command;
    Map argument;
    String session;
    int sequence;
}
