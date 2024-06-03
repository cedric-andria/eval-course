package com.ced.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ced.app.model.Etape;
import com.ced.app.repository.EtapeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class EtapeService {
    @Autowired
    private EtapeRepository etapeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Etape> findAll()
    {
        return etapeRepository.findAll();
    }

    @SuppressWarnings("unchecked")
    public List<Etape> findAllOrderByRang()
    {
        List<Etape> tabetapes = new ArrayList<>();
        String nativeQuery = "SELECT * FROM etape order by rang asc";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Etape.class);

        tabetapes = query.getResultList();
        return tabetapes;
    }

    public Page<Etape> findPagedEtape(int pageNumber, int elementsPerPage, String orderBy, String order)
    {
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, elementsPerPage, sort);
        return etapeRepository.findAll(pageable);
    }

    @SuppressWarnings("unchecked")
    public Etape getByPk(int pk) throws Exception
    {
        List<Etape> tabetapes = new ArrayList<>();
        Etape matchedEtape = null;
        String nativeQuery = "SELECT * FROM etape where pk = :pk";
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Etape.class);
        query.setParameter("pk", pk);
        tabetapes = query.getResultList();
        try {
            matchedEtape = tabetapes.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("no etaped matched by pk");
        }
        return matchedEtape;
    }

    @SuppressWarnings("unchecked")
    public List<Etape> getPreviousEtapeOf(int idetape)
    {
        List<Etape> tabetapes = new ArrayList<>();
        String nativeQuery = "Select * from etape where rang < :rang order by rang desc";
        Etape etapeReference = null;
        try {
            etapeReference = getByPk(idetape);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Etape.class);
        query.setParameter("rang", etapeReference.getRang());
        tabetapes = query.getResultList();
        return tabetapes;
    }

    @SuppressWarnings("unchecked")
    public List<Etape> getNextEtapeOf(int idetape)
    {
        List<Etape> tabetapes = new ArrayList<>();
        String nativeQuery = "Select * from etape where rang > :rang order by rang asc";
        Etape etapeReference = null;
        try {
            etapeReference = getByPk(idetape);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jakarta.persistence.Query query = entityManager.createNativeQuery(nativeQuery, Etape.class);
        query.setParameter("rang", etapeReference.getRang());
        tabetapes = query.getResultList();
        return tabetapes;
    }
}
