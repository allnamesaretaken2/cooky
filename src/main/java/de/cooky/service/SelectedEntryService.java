package de.cooky.service;

import de.cooky.data.Recipe;
import de.cooky.data.SelectedEntry;
import de.cooky.exceptions.CookyErrorMsg;
import de.cooky.repository.RecipeRepository;
import de.cooky.repository.SelectedEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SelectedEntryService {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private ShoppingItemService shoppingItemService;

	@Autowired
	private SelectedEntryRepository selectedEntryRepo;

	public void addSelectionForRecipe(Long idRecipe){
		SelectedEntry newEntry = new SelectedEntry();
		newEntry.setIdRecipe(idRecipe);

		SelectedEntry maxOrderEntry = selectedEntryRepo.findTopByOrderBySortOrderDesc();
		if(maxOrderEntry == null)
		{
			newEntry.setSortOrder(0);
		}else{
			newEntry.setSortOrder(maxOrderEntry.getSortOrder()+1);
		}

		selectedEntryRepo.save(newEntry);
	}

	public void setOrder(Long[] entryIds) {

		if (entryIds == null || entryIds.length == 0) {
			throw new CookyErrorMsg("cannot change the order as no objects were given");
		}

		List<SelectedEntry> entries = selectedEntryRepo.findAll();
		int order = 0;

		for(Long id : entryIds){

			SelectedEntry oldEntry = entries.stream().filter(entry -> entry.getId().equals(id)).findAny().orElse(null);
			if(oldEntry != null) {
				oldEntry.setSortOrder(order++);
			}
		}

		selectedEntryRepo.saveAll(entries);
	}

	public void setComment(Long id, String comment) {
		SelectedEntry entry = selectedEntryRepo.getOne(id);
		entry.setComment(comment);
	}

	public Set<SelectedEntry> getAll() {

		Set<SelectedEntry> all = selectedEntryRepo.getAllByOrderBySortOrder();

		all.forEach(entry -> {
			if(entry.getIdRecipe() != null){
				Recipe recipe = recipeRepo.findById(entry.getIdRecipe()).orElse(null);
				if(recipe != null){
					entry.setFrontendText(recipe.getName());
				}else{
					entry.setFrontendText(entry.getName());
				}
			}
		});

		return all;
	}
}
