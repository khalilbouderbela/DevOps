package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {
	private static final Logger l = Logger.getLogger(EmployeServiceImpl.class);

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	public int ajouterEmploye(Employe employe) {
		try {
			l.info("in ajouterEmploye( )" + employe.toString());
			l.debug("Je viens de lancer la Save. " + employeRepository.save(employe));

		} catch (Exception e) {
			l.error("Erreur dans getAllPrducts() : " + e);

		}

		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		Optional<Employe> optionalemploye = employeRepository.findById(employeId);
		if (optionalemploye.isPresent()) {
			try {
			Employe employe = optionalemploye.get();
			l.info("in mettreAjourEmailByEmployeId( ) avant modifier le mail " + employe.toString());

			employe.setEmail(email);
			l.info("in mettreAjourEmailByEmployeId( ) apres modifier le mail " + employe.toString());

			l.debug("Je viens de lancer la Save. " + employeRepository.save(employe));

			} catch (Exception e) {
				l.error("Erreur dans mettreAjourEmailByEmployeId() : " + e);

			}
		}
	}

	@Transactional
	public void affecterEmployeADepartement(int employeId, int depId) {
		
		Optional<Departement> optionaldep = deptRepoistory.findById(depId);
		Optional<Employe> optionalemploye = employeRepository.findById(employeId);
		
		
		try {
		if (optionaldep.isPresent() && optionalemploye.isPresent()) {
		
			Departement depManagedEntity = optionaldep.get();
			l.info("in affecterEmployeADepartement( ) depManagedEntity:  " + depManagedEntity.toString());

			Employe employeManagedEntity = optionalemploye.get();
			l.debug("in affecterEmployeADepartement( ) employeManagedEntity:  " + depManagedEntity.toString());

			if (depManagedEntity.getEmployes() == null) {
		
				List<Employe> employes = new ArrayList<>();
				l.info("in  if depManagedEntity.getEmployes() == null "+employes.add(employeManagedEntity));
		
				depManagedEntity.setEmployes(employes);
				
			} else {
				l.info("in affecterEmployeADepartement( ) :  " + 	depManagedEntity.getEmployes().add(employeManagedEntity));

		

			}
		}
		} catch (Exception e) {
			l.error("Erreur dans affecterEmployeADepartement() : " + e);

		}
	}

	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId) {
		Optional<Departement> optionaldep = deptRepoistory.findById(depId);
		if (optionaldep.isPresent()) {
			Departement dep = optionaldep.get();

			int employeNb = dep.getEmployes().size();
			for (int index = 0; index < employeNb; index++) {
				if (dep.getEmployes().get(index).getId() == employeId) {
					dep.getEmployes().remove(index);
					break;// a revoir
				}
			}

		}
	}

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		Optional<Contrat> optionalcontrat = contratRepoistory.findById(contratId);
		Employe employeManagedEntity = employeRepository.findById(employeId).orElse(null);
		if (optionalcontrat.isPresent()) {
			Contrat contratManagedEntity = optionalcontrat.get();

			contratManagedEntity.setEmploye(employeManagedEntity);
			contratRepoistory.save(contratManagedEntity);
		}
	}

	public String getEmployePrenomById(int employeId) {
		String nom = "";
		Optional<Employe> optionalemploye = employeRepository.findById(employeId);
		if (optionalemploye.isPresent()) {
			Employe employeManagedEntity = optionalemploye.get();

			nom = employeManagedEntity.getPrenom();

		}
		return nom;

	}

	public void deleteEmployeById(int employeId) {
		Optional<Employe> optionalemploye = employeRepository.findById(employeId);

		// Desaffecter l'employe de tous les departements
		// c'est le bout master qui permet de mettre a jour
		// la table d'association

		if (optionalemploye.isPresent()) {
			Employe employe = optionalemploye.get();
			for (Departement dep : employe.getDepartements()) {
				dep.getEmployes().remove(employe);
			}

			employeRepository.delete(employe);
		}
	}

	public void deleteContratById(int contratId) {
		Optional<Contrat> optionalcontrat = contratRepoistory.findById(contratId);
		if (optionalcontrat.isPresent()) {
			Contrat contratManagedEntity = optionalcontrat.get();

			contratRepoistory.delete(contratManagedEntity);
		}
	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}

	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
	}

}
