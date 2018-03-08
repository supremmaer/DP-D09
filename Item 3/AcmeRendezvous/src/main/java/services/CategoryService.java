
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Actor;
import domain.Administrator;
import domain.Category;

@Service
@Transactional
public class CategoryService {

	@Autowired
	private CategoryRepository	categoryRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private ServiceService		serviceService;


	//Constructors
	public CategoryService() {
		super();
	}

	public Category create() {
		Category result;

		result = new Category();
		result.setCategories(new HashSet<Category>());

		return result;
	}

	public Collection<Category> findAll() {
		Collection<Category> result;

		result = this.categoryRepository.findAll();

		return result;
	}
	//TODO: Incluir Assert
	public void delete(final int categoryId) {
		Category category;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);
		Assert.isTrue(this.serviceService.findByCategoryId(categoryId).isEmpty());
		category = this.findOne(categoryId);
		Assert.notNull(category);
		Assert.isTrue(category.getCategories().isEmpty());

		this.categoryRepository.delete(category);

	}
	//TODO: Incluir Assert
	public Category save(final Category category) {
		Category result;
		Actor actor;

		actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);
		//		if (category.getId() != 0 && category.getParent() != null)
		//			Assert.isTrue(!this.findBranch(category).contains(category.getParent()));
		result = this.categoryRepository.save(category);

		return result;
	}

	public Category findOne(final int categoryId) {
		Category result;

		result = this.categoryRepository.findOne(categoryId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Category> findRoots() {
		Collection<Category> result;

		result = this.categoryRepository.findRoots();

		return result;
	}

	public Collection<Category> findChilds(final int categoryId) {
		Collection<Category> result;

		result = this.categoryRepository.findChilds(categoryId);

		return result;
	}

	public Collection<Category> findBranch(final Category category) {
		Collection<Category> all;
		Collection<Category> nextToEvaluate;
		Collection<Category> aux;
		Category category2;

		category2 = this.findOne(category.getId());
		all = new HashSet<Category>();
		all.add(category2);
		nextToEvaluate = category2.getCategories();
		aux = new HashSet<Category>();
		while (!nextToEvaluate.isEmpty()) {
			all.addAll(nextToEvaluate);
			aux = new HashSet<Category>();
			for (final Category e : nextToEvaluate)
				aux.addAll(e.getCategories());
			nextToEvaluate = new HashSet<Category>();
			nextToEvaluate.addAll(aux);

		}

		return all;
	}

	public Collection<Category> findParentable(final Category category) {
		Collection<Category> result;

		result = this.findAll();
		if (category.getId() != 0)
			result.removeAll(this.findBranch(category));

		return result;
	}
	public Category reconstruct(final Category category, final BindingResult binding) {
		Category result;

		if (category.getId() == 0) {
			result = category;
			result.setCategories(new HashSet<Category>());
		} else {

			result = this.findOne(category.getId());
			if (category.getParent() != null)
				Assert.isTrue(!this.findBranch(result).contains(category.getParent()));
			result.setDescription(category.getDescription());
			result.setName(category.getName());
			result.setParent(category.getParent());
		}
		this.validator.validate(result, binding);
		return result;
	}
}
