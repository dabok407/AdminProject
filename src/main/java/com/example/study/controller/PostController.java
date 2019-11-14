package com.example.study.controller;

import com.example.study.model.SearchParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // class의 RequestMapping이 동일하여도 정상 구동 됨.
public class PostController {

    // http post body -> data
    // json, xml, multipart-form, text ...
    @PostMapping(value = "/postMethod")
    public SearchParam postMethod(@RequestBody SearchParam searchParam){
        return searchParam;
    }

    /*@PutMapping
    public void put(){

    }*/

    /*@PatchMapping
    public void patch(){

    }*/

}
