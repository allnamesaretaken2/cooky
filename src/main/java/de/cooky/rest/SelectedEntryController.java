package de.cooky.rest;

import de.cooky.data.SelectedEntry;
import de.cooky.repository.SelectedEntryRepository;
import de.cooky.service.SelectedEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Set;

@RestController
@RequestMapping("/rest/selectedentry")
public class SelectedEntryController {

    @Autowired
    private SelectedEntryRepository selectedEntryRepo;

    @Autowired
    private SelectedEntryService selectedEntryService;

    @GetMapping
    public Set<SelectedEntry> getAll(){

        return selectedEntryRepo.getAllByOrderBySortOrder();
    }

    @DeleteMapping
    public void  deleteAll(){
        selectedEntryRepo.deleteAll();
    }

    @PutMapping("/select")
    public void select(@RequestBody LinkedHashMap<String, Boolean> selectionSettings) {

        selectedEntryService.setSelection(selectionSettings);
    }

    @PutMapping("/setComment/{id}")
    public void setComment(@RequestBody( required = false) String comment, @PathVariable Long id){

        selectedEntryService.setComment(id, comment);
    }
}
