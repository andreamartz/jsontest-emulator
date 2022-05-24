package com.andreamartz.jsontestemulator;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IP {
    @JsonProperty   // I need EITHER @JsonProperty OR the getter method to make sure that JSON is returned. I don't need both.
    final String ip;

    public IP(String ip) {
        this.ip = ip;
    }  // this constructor returns an IP object with an ip property which is a String representing the client's ip address.

//    public String getIp() {
//        return ip;
//    }
}
