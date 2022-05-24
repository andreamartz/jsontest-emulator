package com.andreamartz.jsontestemulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class JsontestController {
    private final UserAccountRepository repository;
//    private final HashMap<UUID, Long> tokenMap;

    // This is for Spring
    @Autowired
    public JsontestController(@NonNull UserAccountRepository repository) {
        this.repository = repository;
//        this.tokenMap = new HashMap<>();
    }
    // This is for Mockito
//    public JsontestController(@NonNull UserAccountRepository repository, @NonNull HashMap<UUID, Long> tokenMap) {
//        this.repository = repository;
//        this.tokenMap = tokenMap;
//    }

    public JsontestController() {

    }

    @GetMapping("/register")
    public void register(@RequestParam String username, @RequestParam String password) {
        // check whether the username already exists
        if (repository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        repository.save(new UserAccount());
        // if it does, throw a ResponseStatusException
        // if it doesn't, create the new user
//        if (repository.findByUsername(username).isPresent())
    }
    // the ip function takes in the request
    // a string containing the clients ip address is created by calling the getRemoteAddr() method
    //
    @GetMapping("/ip")
    @CrossOrigin
    public IP ip(HttpServletRequest request) {  // allows access to request metadata
        String ip = request.getRemoteAddr();
        IP ipObj = new IP(ip);  // the IP constructor takes a String argument containing the ip address
        return ipObj;
    }

    @GetMapping("/datetime")
    @CrossOrigin
    public HashMap<String, String> datetime() throws UnknownHostException {
        HashMap<String, String> dateTimeHash = new HashMap<>();

        ZonedDateTime tzInstance = ZonedDateTime.now(ZoneId.of("GMT"));

        DateTimeFormatter dateFormatter
                = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String formattedDateString = dateFormatter.format(tzInstance);

        DateTimeFormatter timeFormatter
                = DateTimeFormatter.ofPattern("hh:mm:ss");
        String formattedTimeString = timeFormatter.format(tzInstance);

//        Instant instant = Instant.now();
//        long epochValue = instant.getEpochSecond();
//        long ms = System.currentTimeMillis();

//        String seconds = String.format("%d", epochValue);
//        String milliseconds = String.format("%d", ms);

        dateTimeHash.put("date", formattedDateString);
        dateTimeHash.put("time", formattedTimeString);
//        dateTimeHash.put("milliseconds_since_epoch", milliseconds);
        return dateTimeHash;
    }

    @GetMapping("/headers")
    @CrossOrigin
    public HashMap<String, String> headers(@RequestHeader HashMap<String, String> headers) {
        return headers;
    }

    @GetMapping("/md5")
    @CrossOrigin
    protected HashMap<String, String> md5(@RequestParam String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5"); // returns a instance of MessageDigest
        // turn input string into an array of bytes
        md.update(input.getBytes());
        byte[] digest = md.digest();  // returns an array of bytes in binary that represents the hash value
//        System.out.println(digest);  // does not print an array of binary numbers for some reason
        BigInteger digestBigint = new BigInteger(1, digest);
        String digestStr = String.format("%x", digestBigint);
//        String digestStr = new String(digest);  // the hash (digest) converted to String; this did not work because the
        HashMap<String, String> md5HashMap = new HashMap<>();
        md5HashMap.put("md5", digestStr);
        md5HashMap.put("original", input);
        return md5HashMap;
    }
//    {
//        1: true
//    }
//    public void givenPassword_whenHashing_thenVerifying()
//            throws NoSuchAlgorithmException {
//        String hash = "35454B055CC325EA1AF2126E27707052";
//        String password = "ILoveJava";
//
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(password.getBytes());
//        byte[] digest = md.digest();
//        String myHash = DatatypeConverter
//                .printHexBinary(digest).toUpperCase();
//
//        assertThat(myHash.equals(hash)).isTrue();
//    }
    @GetMapping
    @CrossOrigin
    public HashMap<String, Object> validate(@RequestParam String json) {
        // determine if the json is valid
        JacksonJsonParser parser = new JacksonJsonParser();
        try {
            Map<String, Object> jsonMap = parser.parseMap(json);

            HashMap<String, Object> response = new HashMap<>();
            response.put("object_or_array", "object");
            response.put("empty", false);
//        response.put("parse_time_nanoseconds", 19608);
            response.put("validate", true);
            response.put("size", 1);
            return response;
        } catch(Exception error) {
            String message = error.getMessage();
            HashMap<String, Object> response = new HashMap<>();
            response.put("error_info", "This error came from the JacksonJsonParser");
            response.put("error", message);
            response.put("object_or_array", "object");
            response.put("validate", false);
            return response;
        }
    }
}
