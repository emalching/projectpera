package aero.champ.projectpera.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import aero.champ.projectpera.BO.BatchRun;

public interface BatchRunRepository extends MongoRepository<BatchRun, String> {

}
