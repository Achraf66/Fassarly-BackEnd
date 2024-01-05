package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
}