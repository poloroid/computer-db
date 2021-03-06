package test.excilys.computerdatabase.service;

import java.util.List;

import org.junit.runner.RunWith;

import test.excilys.computerdatabase.dao.CompanyDAOMock;

import com.excilys.computerdatabase.model.Company;
import com.excilys.computerdatabase.service.CompanyDBServiceInterface;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class CompanyDBServiceMock implements CompanyDBServiceInterface {
	private CompanyDAOMock companyDAO = new CompanyDAOMock();
	
	public CompanyDBServiceMock(CompanyDAOMock companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Override
	public List<Company> getPage(int pageNumber) {
		return companyDAO.getPage(pageNumber);
	}

	@Override
	public Company get(long id) {
		return companyDAO.get(id);
	}

	@Override
	public void save(String name) {
		companyDAO.save(name);
	}

	@Override
	public void update(long id, String name) {
		companyDAO.update(id, name);
	}

}
