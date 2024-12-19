package in.tejas.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import in.tejas.entity.UserEntity;
import java.util.List;


@Repository
public interface UserRepo extends CrudRepository<UserEntity, Integer>{
	
	
	public UserEntity findByUname(String uname);

}
