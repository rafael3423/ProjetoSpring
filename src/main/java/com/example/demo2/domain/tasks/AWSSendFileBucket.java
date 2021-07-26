/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.domain.tasks;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author victor
 */
public class AWSSendFileBucket {

    @Autowired
    private AmazonS3 amazonS3;

    private static final String BUCKET = "projetospring-bucket";

    public String saveFile(MultipartFile file)  {
        try {
            amazonS3.putObject(new PutObjectRequest(BUCKET,
                    file.getOriginalFilename(), file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return "http://s3.amazonaws.com/" + BUCKET + "/" + file.getOriginalFilename();

        }catch(IllegalStateException | IOException e){
            throw new RuntimeException(e);
        }
    }

}
