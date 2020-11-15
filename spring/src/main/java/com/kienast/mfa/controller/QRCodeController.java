package com.kienast.mfa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import com.google.gson.Gson;
import static dev.samstevens.totp.util.Utils.getDataUriForImage;

import java.util.HashMap;
import java.util.Map;

@Controller
public class QRCodeController {

    @Autowired
    private SecretGenerator secretGenerator;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @RequestMapping("/")
    public ModelAndView welcome() {
    	ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/mfa/setup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setupDevice() throws QrGenerationException {
        // Generate and store the secret
        String secret = secretGenerator.generate();

        QrData data = qrDataFactory.newBuilder()
            .label("example@example.com")
            .secret(secret)
            .issuer("Spring")
            .build();

        // Generate the QR code image data as a base64 string which
        // can be used in an <img>
        System.out.println(secret);
        
        
        String qrCodeImage =  getDataUriForImage(
          qrGenerator.generate(data), 
          qrGenerator.getImageMimeType()
        );
        System.out.println(qrCodeImage);
        //return qrCodeImage;
        Map<String, Object> map = new HashMap<>();
        map.put("qrCode", qrCodeImage);
        
        Gson gson = new Gson();
		return ResponseEntity.ok(gson .toJson(map));

    }
}