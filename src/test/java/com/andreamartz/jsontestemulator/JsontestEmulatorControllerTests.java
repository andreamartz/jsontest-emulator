package com.andreamartz.jsontestemulator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class JsontestEmulatorControllerTests {
    @InjectMocks
    JsontestController controller;

    @Mock
    HttpServletRequest request;

    @Mock
    UserAccountRepository repository;

    @Test
    // test register method
    void itShouldReturnInvalidOnRegisteringIfUsernameExistsAlready() {
        final String username = "some username";
        // Q: do I need to use @InjectMocks or can I just create a new instance
        //     of my controller here?
        // A: I need @InjectMocks because I need to inject a FIELD
        //     (the repository field) on the controller

        // mock the call to repository.findByUsername(username)
        when(repository.findByUsername(username)).thenReturn(Optional.of(new UserAccount()));
        // Assert that execution of the supplied executable throws a ResponseStatusException and return the exception.
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            // Q: I THINK this is calling the actual register method on the controller and not a mocked one:
            controller.register(username, "");
        });
    }

    @Test
    // test register method
    void itShouldSaveANewUserAccountWhenUserIsUniqe() {

    }

    @Test
    void itShouldReturnIpAddress() throws Exception {
        // the controller should return an ip address
            // when the user hits endpoint /ip from localhost:8080
        // I expect that if I pass in "1.2.3.4" as the ip address
        // that the instantiated object will have ip property equal to "1.2.3.4"

        // We need to test that the controller
        AutoCloseable someVar = MockitoAnnotations.openMocks(this);  // would have gotten this for free if used @InjectMocks; always need to do this when mocking but not injecting with @InjectMocks
        JsontestController controller = new JsontestController();
//        String ipAddr = request.getRemoteAddr();
        when(request.getRemoteAddr()).thenReturn("1.2.3.4");  // mocking (the method call request.getRemoteAddr(), NOT injecting request
        IP ip = controller.ip(request);   // manual injection of request into controller
        Assertions.assertEquals("1.2.3.4", ip.ip);
        someVar.close();   // this closes the AutoCloseable from above
    }

    @Test
    void itShouldReturnCurrentDateAndTimeAndMilliseconds() throws UnknownHostException {
        JsontestController controller = new JsontestController();
        // result will be the result of calling the datatime method on the controller
        // hard code formatted date, time, and milliseconds
        HashMap<String, String> expected = new HashMap<>();
        expected.put("date", "01-01-2022");
        expected.put("time", "01:01:01");

        ZonedDateTime datetime = ZonedDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneId.of("GMT"));

//        expected.put("milliseconds_since_epoch", )
        // return object of type ZonedDateTime
        Mockito.mockStatic(ZonedDateTime.class).when(() -> ZonedDateTime.now(ZoneId.of("GMT"))).thenReturn(datetime);
        HashMap<String, String> result = controller.datetime();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void itShouldReturnTheHeadersThatWerePassedIn () {
        JsontestController controller = new JsontestController();  // a dependency?, but not a side effect
        // expected
        HashMap<String, String> expected = new HashMap<>();
        expected.put("key", "value");
        // actual
        HashMap<String, String> headers = new HashMap<>();
        headers.put("key", "value");
        HashMap<String, String> result = controller.headers(headers);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void itShouldReturnTheMd5HashOfInputString() throws NoSuchAlgorithmException {
        JsontestController controller = new JsontestController();
        // excepted
        HashMap<String, String> expected = new HashMap<>();
        String originalText = "example_text";
        expected.put("md5", "fa4c6baa0812e5b5c80ed8885e55a8a6");
        expected.put("original", originalText);
        // actual
        String input = originalText;
        HashMap<String, String> result = controller.md5(input);
        Assertions.assertEquals(expected, result);
    }
// want to do one that uses Mockito.mockStatic

    @Test
    void itShouldReturnValidJsonResponseWhenJsonIsValid() {
        JsontestController controller = new JsontestController();
        HashMap<String, Object> expected = new HashMap<>();
        expected.put("object_or_array", "object");
        expected.put("empty", false);
//        expected.put("parse_time_nanoseconds", 19608);
        expected.put("validate", true);
        expected.put("size", 1);

        HashMap <String, String> jsonInput = new HashMap<>();
        jsonInput.put("key", "value");
//        HashMap<String, String> result = controller.validate(jsonInput);
        HashMap<String, Object> result = controller.validate("{\"key\":\"value\"}");

        Assertions.assertEquals(expected, result);
//        {
//            "error_info": "This error came from the org.json reference parser.",
//                "error": "Expected a ':' after a key at 3 [character 4 line 1]",
//                "object_or_array": "object",
//                "validate": false
//        }
    }

    @Test
    void itShouldReturnErrorResponseWhenJsonIsInvalid() {
        JsontestController controller = new JsontestController();
        HashMap<String, Object> expected = new HashMap<>();

        expected.put("error_info", "This error came from the JacksonJsonParser");
        expected.put("error", "Cannot parse JSON");
        expected.put("object_or_array", "object");
        expected.put("validate", false);

        HashMap<String, Object> result = controller.validate("{1[:\"value\"}");

        Assertions.assertEquals(expected, result);
    }
}


