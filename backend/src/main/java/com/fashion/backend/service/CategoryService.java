package com.fashion.backend.service;

import com.fashion.backend.constant.Message;
import com.fashion.backend.entity.Category;
import com.fashion.backend.entity.Item;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.SimpleListResponse;
import com.fashion.backend.payload.SimpleResponse;
import com.fashion.backend.payload.category.CategoryResponse;
import com.fashion.backend.payload.category.CreateCategoryRequest;
import com.fashion.backend.payload.category.UpdateCategoryRequest;
import com.fashion.backend.repository.CategoryRepository;
import com.fashion.backend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public SimpleListResponse<CategoryResponse> getCategories() {
		List<Category> categories = categoryRepository.findAll();

		return SimpleListResponse.<CategoryResponse>builder()
								 .data(categories.stream().map(this::mapToDTO).toList())
								 .build();
	}

	@Transactional
	public CategoryResponse createCategory(CreateCategoryRequest request) {
		Category category = Category.builder()
									.name(request.getName())
									.build();

		return mapToDTO(categoryRepository.save(category));
	}

	@Transactional
	public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
		Category category = Common.findCategoryById(id, categoryRepository);

		category.setName(request.getName());

		return mapToDTO(categoryRepository.save(category));
	}

	@Transactional
	public SimpleResponse deleteCategory(Long id) {
		Category category = Common.findCategoryById(id, categoryRepository);

		List<Item> items = itemRepository.findAllByCategories_Id(id);

		for (Item item : items) {
			if (!item.isDeleted() && item.getCategories().size() == 1) {
				throw new AppException(HttpStatus.BAD_REQUEST,
									   Message.Category.CAN_NOT_DELETE_CATEGORY_THAT_EXIST_ITEM_CONTAIN_ONLY_IT);
			}
			item.getCategories().remove(category);
		}
		itemRepository.saveAll(items);

		categoryRepository.delete(category);

		return new SimpleResponse();
	}

	private CategoryResponse mapToDTO(Category category) {
		return CategoryResponse.builder()
							   .id(category.getId())
							   .name(category.getName())
							   .build();
	}
}
