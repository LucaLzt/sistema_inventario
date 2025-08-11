package com.pruebas.sistema_inventario.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pruebas.sistema_inventario.entities.Admin;
import com.pruebas.sistema_inventario.entities.Employee;
import com.pruebas.sistema_inventario.entities.Role;
import com.pruebas.sistema_inventario.repository.AdminRepository;
import com.pruebas.sistema_inventario.repository.EmployeeRepository;

@Component
public class UsersSeeder implements CommandLineRunner {
	
	private final EmployeeRepository employeeRepository;
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UsersSeeder(EmployeeRepository employeeRepository, 
			AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		this.employeeRepository = employeeRepository;
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		
		// EmployeeSeeder
		if (employeeRepository.findByEmail("employee@company.com").isEmpty()) {
			Employee employee = new Employee();
			employee.setEmail("employee@company.com");
			employee.setPassword(passwordEncoder.encode("employee1234"));
			employee.setFullName("Employee User");
			employee.setMobile(1122334455L);
			employee.setRole(Role.EMPLOYEE);
			employee.setActive(false);
			employee.setApproved(true);
			employeeRepository.save(employee);
		}
		
		// AdminSeeder
		if (adminRepository.findByEmail("admin@company.com").isEmpty()) {
			Admin admin = new Admin();
            admin.setEmail("admin@company.com");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            admin.setFullName("Admin User");
            admin.setMobile(2233445566L);
            admin.setRole(Role.ADMIN);
            admin.setActive(false);
            admin.setApproved(true);
            admin.setSuperAdmin(false);
            adminRepository.save(admin);
		}
		
		// SuperAdminSeeder
		if (adminRepository.findByEmail("superadmin@company.com").isEmpty()) {
            Admin superAdmin = new Admin();
            superAdmin.setEmail("superadmin@company.com");
            superAdmin.setPassword(passwordEncoder.encode("superadmin1234"));
            superAdmin.setFullName("Super Admin");
            superAdmin.setMobile(3344556677L);
            superAdmin.setRole(Role.ADMIN);
            superAdmin.setActive(false);
            superAdmin.setApproved(true);
            superAdmin.setSuperAdmin(true);
            adminRepository.save(superAdmin);
        }
	}
}
