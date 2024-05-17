package org.dgp.eventmanager.repositories;

import org.dgp.eventmanager.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
