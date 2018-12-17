package com.mdc.servicelayer.services;

import com.google.gson.Gson;
import com.mdc.servicelayer.JWTValidation;
import com.mdc.servicelayer.Utilities;
import com.mdc.servicelayer.model.Book;
import com.mdc.servicelayer.repository.BookRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


@RestController
@EnableAutoConfiguration
public class Services {

    //protected final Log logger = LogFactory.getLog(getClass());
    Logger logger = java.util.logging.Logger.getLogger(Services.class.getName());

    @Autowired
    JWTValidation jwtValidation;

    @Autowired
    Utilities utilities;

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(
            value = "/api/books",
            method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody String processBooksRequest(HttpServletResponse response) {

        String responseBody = "";

        try {

            List<Book> books = bookRepository.findAll();

            Gson gson = new Gson();

            responseBody = gson.toJson(books);

        }
        catch (Exception e) {
            try {
                response.sendError(403, utilities.getInvalidLoginErrorMsg());
                return responseBody;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return responseBody;
    }

    private String extractJSONString(String jsonData, String field) {

        JSONObject json = new JSONObject(jsonData);

        return (String)json.get(field);
    }

    private Integer extractJSONInteger(String jsonData, String field) {

        JSONObject json = new JSONObject(jsonData);

        return (Integer)json.get(field);
    }


}
