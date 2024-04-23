package de.cooky.rest;

import de.cooky.data.SelectedEntry;
import de.cooky.repository.SelectedEntryRepository;
import de.cooky.service.SelectedEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

        return selectedEntryService.getAll();
    }

    @DeleteMapping
    public void  deleteAll(){
        selectedEntryRepo.deleteAll();
    }

    @PostMapping("/{idRecipe}")
    public void create(@PathVariable("idRecipe") Long idRecipe) {
        selectedEntryService.addSelectionForRecipe(idRecipe);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        selectedEntryRepo.deleteById(id);
    }

    @PutMapping("/setorder")
    public void setorder(@RequestBody Long[] entryIds) {

        selectedEntryService.setOrder(entryIds);
    }

    @PutMapping("/setComment/{id}")
    public void setComment(@RequestBody( required = false) String comment, @PathVariable Long id){

        selectedEntryService.setComment(id, comment);
    }
}
