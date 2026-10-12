package com.example.jpa.service;

import com.example.jpa.dao.SalleDao;
import com.example.jpa.dao.UtilisateurDao;
import com.example.jpa.entities.Salle;
import com.example.jpa.entities.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ServiceGestion {

    private EntityManagerFactory emf;
    private EntityManager em;
    private SalleDao salleDao;
    private UtilisateurDao utilisateurDao;

    public ServiceGestion() {
        // L'usine (coûteuse) puis le surveillant, partagé par les deux DAO
        emf = Persistence.createEntityManagerFactory("gestionPU");
        em = emf.createEntityManager();
        salleDao = new SalleDao(em);
        utilisateurDao = new UtilisateurDao(em);
    }

    // À appeler à la fin pour libérer les ressources
    public void fermer() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    public void ajouterSalle(Salle salle) {
        salleDao.create(salle);
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDao.create(utilisateur);
    }

    public List<Salle> listerSalles() {
        return salleDao.findAll();
    }

    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDao.findAll();
    }

    public List<Utilisateur> utilisateursParSalle(String nomSalle) {
        return utilisateurDao.findBySalleNom(nomSalle);
    }
}