package com.fashion.backend.repository;

import com.fashion.backend.entity.Item;
import com.fashion.backend.payload.item.ItemSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
	List<Item> findAllByCategories_Id(Long categoryId);

	@Query("SELECT i FROM Item i ORDER BY i.sold DESC")
	List<Item> findTopBySold(Pageable pageable);

	@Query("SELECT i FROM Item i ORDER BY i.createdAt DESC")
	List<Item> findLatestItems(Pageable pageable);

	default List<Item> findAllNotDelete() {
		Specification<Item> spec = ItemSpecs.isNotDeleted();
		return findAll(spec);
	}

	default Page<Item> findAllNotDelete(Specification<Item> specs,
										Pageable pageable) {
		Specification<Item> spec = ItemSpecs.isNotDeleted();
		return findAll(specs.and(spec), pageable);
	}
}
