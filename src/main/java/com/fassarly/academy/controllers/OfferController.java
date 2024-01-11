package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Offer;
import com.fassarly.academy.services.OfferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin("http://localhost:4200/")
public class OfferController {

    @Autowired
    private OfferServiceImpl offerService;

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable Long id) {
        Optional<Offer> offer = offerService.getOfferById(id);
        return offer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Offer> createOffer(@RequestBody Offer offer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.createOffer(offer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer updatedOffer) {
        Offer offer = offerService.updateOffer(id, updatedOffer);
        return offer != null ? ResponseEntity.ok(offer) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/createOrUpdateOffer")
    public ResponseEntity<Offer> createOrUpdateOffer(@RequestBody Offer offer) {
        Offer result = offerService.createOrUpdateOffer(offer);

        // Adjust the response based on whether the offer was created or updated
        HttpStatus responseStatus = (result.getId() != null) ? HttpStatus.OK : HttpStatus.CREATED;

        return ResponseEntity.status(responseStatus).body(result);
    }

}
