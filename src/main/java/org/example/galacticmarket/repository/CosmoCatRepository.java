package org.example.galacticmarket.repository;

import org.example.galacticmarket.repository.entity.CosmoCatEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CosmoCatRepository extends CrudRepository<CosmoCatEntity, Long> {}
