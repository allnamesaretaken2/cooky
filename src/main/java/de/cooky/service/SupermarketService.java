package de.cooky.service;

import de.cooky.data.Supermarket;
import de.cooky.data.SupermarketArea;
import de.cooky.repository.SuperMarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SupermarketService {

    @Autowired
    SuperMarketRepository marketRepository;


    public Supermarket getOrCreate() {

        List<Supermarket> all = marketRepository.findAll();

        return all.stream().findFirst().orElseGet(() -> {
            Supermarket newMarket = new Supermarket();
            newMarket.setName("Rewe Möckernsche Straße");


            SupermarketArea area1 = new SupermarketArea();
            area1.setName("Gemüse");
            newMarket.setAreas(Set.of(area1));
            SupermarketArea area2 = new SupermarketArea();
            area2.setName("Fleisch");
            newMarket.setAreas(Set.of(area2));
            SupermarketArea area3 = new SupermarketArea();
            area3.setName("Milch und Milch-Upgrades");
            newMarket.setAreas(Set.of(area3));
            return newMarket;
        });
    }

    public Supermarket save(Supermarket supermarket) {
        return this.marketRepository.save(supermarket);
    }
}
