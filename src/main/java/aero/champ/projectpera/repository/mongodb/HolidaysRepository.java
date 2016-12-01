package aero.champ.projectpera.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import aero.champ.projectpera.BO.Holidays;

public interface HolidaysRepository extends MongoRepository<Holidays, String> {

}
