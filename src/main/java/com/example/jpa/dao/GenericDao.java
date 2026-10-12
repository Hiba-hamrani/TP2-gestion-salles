package com.example.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class GenericDao<T> {

    private final Class<T> entityClass;
    protected EntityManager em;

    public GenericDao(Class<T> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    // CREATE : transient -> managed, puis INSERT au commit
    public void create(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // READ : recherche par clé primaire (renvoie null si rien n'est trouvé)
    public T find(Object id) {
        return em.find(entityClass, id);
    }

    // UPDATE : merge renvoie une copie managed ; c'est elle qu'il faut utiliser ensuite
    public T update(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // DELETE : remove() n'accepte qu'une entité managed, d'où le merge si elle est détachée
    public void delete(T entity) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // READ ALL : "SELECT e FROM Salle e" ou "SELECT e FROM Utilisateur e"
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }
}