package com.ced.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ced.app.model.Penalite_equipe;

@Repository
public interface Penalite_equipeRepository extends JpaRepository<Penalite_equipe, Integer>{
    // public void deleteByPk(int pk);
}