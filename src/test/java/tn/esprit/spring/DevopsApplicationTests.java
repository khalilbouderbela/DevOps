package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.services.IEmployeService;

@SpringBootTest
class DevopsApplicationTests {

	@Autowired
	IEmployeService serv;
	
	@Test
	void contextLoads() {
	
		Employe emp =new Employe(
				"bouderbela", "khalil", "khalil.bouderbela@esprit.tn",true
				);
        //ajouterEmploye
		serv.ajouterEmploye(emp);

		
		
		//mettreAjourEmailByEmployeId 
		
		String email="khalil.bouderbela@esprit.tn";
		int employeId=emp.getId();
		
		 
		serv.mettreAjourEmailByEmployeId(email, employeId);
		
		//deleteEmployeById
		serv.deleteEmployeById(emp.getId());
		
	}

}
