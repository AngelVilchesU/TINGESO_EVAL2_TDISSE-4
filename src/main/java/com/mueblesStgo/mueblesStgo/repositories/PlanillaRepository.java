package com.mueblesStgo.mueblesStgo.repositories;

import com.mueblesStgo.mueblesStgo.entities.PlanillaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanillaRepository extends CrudRepository<PlanillaEntity, Long> {

}
