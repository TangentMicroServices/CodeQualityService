package za.co.tangent.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Bean;

import za.co.tangent.domain.Project;

@Repository
public interface ProjectDao extends MongoRepository<Project, String> {
	Project findByName(@Param("name") String name);
}