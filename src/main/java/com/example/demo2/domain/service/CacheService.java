/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo2.domain.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 *
 * @author note
 */
@Service
public class CacheService {
    
    @Cacheable("cache")
    public String exemploCache(){
        
        return "cache";
    }
    
}


