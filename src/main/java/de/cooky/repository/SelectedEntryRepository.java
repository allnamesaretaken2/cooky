package de.cooky.repository;

import de.cooky.data.SelectedEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SelectedEntryRepository extends JpaRepository<SelectedEntry, Long> {

	Set<SelectedEntry> getAllByOrderBySortOrder();

    SelectedEntry findTopByOrderBySortOrderDesc();

    /**
     * Only select the selection entries that are linked to a recipe. The other ones are normally free entries with plain
     * text
     */
    Set<SelectedEntry> getAllByIdRecipeIsNotNullOrderBySortOrder();
}
