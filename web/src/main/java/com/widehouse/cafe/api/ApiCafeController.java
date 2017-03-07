package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.service.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 18..
 */
@RestController
public class ApiCafeController {
    @Autowired
    private CafeService cafeService;

//    @GetMapping(name = "/api/cafes/", params = "id")
//    public Cafe getCafeById(@RequestParam Long id) {
//        Cafe cafe = cafeService.getCafe(id);
//
//        return cafe;
//    }

    @GetMapping("/api/cafes/{cafeUrl}")
    public Cafe getCafeByUrl(@PathVariable String cafeUrl) {
        Cafe cafe = cafeService.getCafe(cafeUrl);

        return cafe;
    }
}
