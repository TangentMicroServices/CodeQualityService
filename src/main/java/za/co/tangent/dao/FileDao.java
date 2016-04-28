package za.co.tangent.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import za.co.tangent.domain.File;

@Repository
public interface FileDao extends MongoRepository<File, Long> {
	List<File> findByProjectId(@Param("id") long id);
}
