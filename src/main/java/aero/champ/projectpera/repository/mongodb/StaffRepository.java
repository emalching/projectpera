package aero.champ.projectpera.repository.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import aero.champ.projectpera.BO.Staff;

public interface StaffRepository extends MongoRepository<Staff, String> {

	public Staff findByCardNumber(int cardNumber);
	
	@Query(value="{'timeInOutList.workDate' : {$gte: ?0, $lte: ?1}}")
	public List<Staff> findByDate(String startDate, String endDate);
	
	@Query(value="{'timeInOutList.workDate' : {$ne: ?0}}")
	public List<Staff> findByMissingDate(String missingDate);
	
	public List<Staff> findByTeamLeadName(String teamLeadName);
	
}
