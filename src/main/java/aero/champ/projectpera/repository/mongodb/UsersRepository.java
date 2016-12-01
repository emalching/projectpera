package aero.champ.projectpera.repository.mongodb;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import aero.champ.projectpera.BO.Users;

public interface UsersRepository extends MongoRepository<Users, String> {

	public List<Users> findByDisplayName(String displayName);
	
}
