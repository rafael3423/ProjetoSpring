///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.example.demo2.api.utils;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//
///**
// *
// * @author note
// */
//public class SenhaUtils {
//
//    public static String gerarBCrypt(String senha) {
//
//        if (senha == null) {
//            return senha;
//        }
//
//        return new BCryptPasswordEncoder().encode(senha);
//    }
//
//    public static boolean senhaValida(String senha, String senhaEncoded) {
//
//        return new BCryptPasswordEncoder().matches(senha, senhaEncoded);
//    }
//
//}
//
