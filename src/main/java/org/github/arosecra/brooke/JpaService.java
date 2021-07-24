package org.github.arosecra.brooke;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaService<T, ID> {
	
	public JpaRepository<T,ID> getRepository();

	public default List<T> findAll() {
		return getRepository().findAll();
	}
	
	public default Optional<T> find(ID id) {
		return getRepository().findById(id);
	}
	
	public default T save(T t) {
		return getRepository().save(t);
	}
	
	public default List<T> saveAll(Collection<T> ts) {
		return getRepository().saveAll(ts);
	}
	
	public default void flush() {
		getRepository().flush();
	}
}
