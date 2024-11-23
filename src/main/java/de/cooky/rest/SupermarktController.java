package de.cooky.rest;

import de.cooky.data.Supermarket;
import de.cooky.service.SupermarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/supermarket")
public class SupermarktController {

    @Autowired
    SupermarketService marketService;

    @GetMapping("/getorcreate")
    public Supermarket getOrCreate() {
        return marketService.getOrCreate();
    }

    @PutMapping
    public ResponseEntity<Supermarket> update(@RequestBody Supermarket supermarket) {

        Supermarket updated = marketService.save(supermarket);

        return ResponseEntity.accepted().body(updated);
    }
}
