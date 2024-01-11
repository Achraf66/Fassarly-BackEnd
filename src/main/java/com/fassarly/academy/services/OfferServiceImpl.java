package com.fassarly.academy.services;

import com.fassarly.academy.entities.Offer;
import com.fassarly.academy.repositories.OfferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OfferServiceImpl {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public Offer updateOffer(Long id, Offer updatedOffer) {
        if (offerRepository.existsById(id)) {
            updatedOffer.setId(id);
            return offerRepository.save(updatedOffer);
        } else {
            // Handle not found exception
            return null;
        }
    }

    public Offer createOrUpdateOffer(Offer offer) {
        if (offer.getId() != null) {
            return offerRepository.findById(offer.getId())
                    .map(existingOffer -> {
                        // If it exists, update the existing offer
                        // Update fields you want to change
                        existingOffer.setMensuelle(offer.getMensuelle());
                        existingOffer.setTrimestrielle(offer.getTrimestrielle());
                        existingOffer.setAnuelle(offer.getAnuelle());
                        return offerRepository.save(existingOffer);
                    })
                    .orElseGet(() -> {
                        // If it doesn't exist, create a new offer
                        return offerRepository.save(offer);
                    });
        } else {
            // If the ID is null, it's a new offer
            return offerRepository.save(offer);
        }
    }



    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }
}
