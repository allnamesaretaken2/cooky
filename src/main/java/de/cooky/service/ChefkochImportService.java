package de.cooky.service;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import de.cooky.data.RecipePart;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cooky.data.Ingredient;
import de.cooky.data.IngredientToRecipePart;
import de.cooky.data.Recipe;
import de.cooky.exceptions.CookyErrorMsg;

@Service
@Transactional
public class ChefkochImportService {

	private static final Logger LOG = LoggerFactory.getLogger(ChefkochImportService.class);
	
	@Autowired
	private IngredientService ingredientService;

	@Autowired
	private RecipeService recipeService;

	/**
	 * Convert a chefkoch-recipe into a cooky-recipe. It does not save the recipe
	 */
	public Recipe importRecipe(String url) {

		LOG.info("start import of recipe from url " + url);
		
		try {
			new URL(url);//implicit validation of the url

			Document doc = Jsoup.connect(url).get();

			Recipe recipe = createTemporaryRecipeWithOneRecipePart(doc, url);

			Set<IngredientToRecipePart> itrSet = createTemporaryIngredients(doc);

			recipe.getRecipeParts().iterator().next().setIngredients(itrSet);

			LOG.info("finished import of recipe from url " + url);

			recipeService.create(recipe);
			
			return recipe;

		} catch (IOException e) {

			LOG.error("import from url " + url + " failed. Reason:");
			
			throw new CookyErrorMsg(e);
		}

	}

	private Set<IngredientToRecipePart> createTemporaryIngredients(Document doc) {
		Elements ingredientTable = doc.getElementsByAttributeValue("class", "ingredients table-header");

		if (ingredientTable.isEmpty()) {
			throw new CookyErrorMsg("expected at least one element with 'ingredients'");
		}

		if (ingredientTable.size() > 1) {
			throw new CookyErrorMsg(
					"expected only one element with 'ingredients'. But there are " + ingredientTable.size());
		}

		Element table = ingredientTable.first();

		Elements rowList = table.getElementsByTag("tr");

		Set<IngredientToRecipePart> itrSet = new LinkedHashSet<>();

		rowList.forEach(row -> {

			IngredientToRecipePart itr = new IngredientToRecipePart();

			Element amount = row.getElementsByTag("td").first();
			String amountText = amount.text();
			amountText = amountText.replace(((char) 160) + "", " ");
			
			try {
			
			String[] amountSplitted = amountText.trim().split(" ");

			if (amountSplitted.length == 1) {

				String value = amountSplitted[0];

				boolean isNumber = NumberUtils.isCreatable(value);

				if (isNumber) {
					itr.setAmount(Float.parseFloat(value));
				} else {
					itr.setUnit(value);
				}
			} else if (amountSplitted.length == 2) {

				itr.setAmount(Float.parseFloat(amountSplitted[0]));
				itr.setUnit(amountSplitted[1]);

			} else {
				// TODO append warning: kann zutat nicht auslesen
			}
			}catch (RuntimeException e) {

				//TODO: use logger
				itr.setAmount(null);
				itr.setUnit(amountText);
			}

			// TODO Zutats-Bezeichnung noch einbasteln
			Element name = row.getElementsByTag("td").last();
			String ingredientName;
			if (name.text().isEmpty()) {
				Element linkContent = row.getElementsByTag("td").last().getAllElements().first();

				ingredientName = linkContent.text();
			} else {
				ingredientName = name.text();
			}

			if (StringUtils.isNotEmpty(ingredientName)) {

				Ingredient ingredient = ingredientService.getOrCreateTemporaryIngredient(ingredientName);
				itr.setIngredient(ingredient);

				itrSet.add(itr);
			}

		});

		return itrSet;
	}

	private Recipe createTemporaryRecipeWithOneRecipePart(Document doc, String url) {

		Elements recipeTitles = doc.getElementsByTag("h1");

		if (recipeTitles.isEmpty()) {
			throw new CookyErrorMsg("expected at least one element with tag-type 'h1'");
		}

		if (recipeTitles.size() > 1) {
			throw new CookyErrorMsg("expected only one element with tag-type 'h1'. But there are " + recipeTitles.size());
		}


		Elements textElements = doc.getElementsByTag("article");

		if (textElements.isEmpty()) {
			throw new CookyErrorMsg("expected at least one element with 'article'");
		}

		Element textElement = textElements.stream().filter(element -> {
			Elements elementsByTag = element.getElementsByTag("h2");
			if(!elementsByTag.isEmpty()) {
				return elementsByTag.first().text().compareTo("Zubereitung") == 0;	
			}else {
				return false;
			}
		}).findFirst().orElse(null);

		if(textElement == null) {
			throw new CookyErrorMsg("Could not find text element containing the recipe text");
		}
		
		Elements portions = doc.getElementsByAttributeValue("name", "portionen");
		
		if (portions.isEmpty()) {
			throw new CookyErrorMsg("expected at least one element with 'portionen'");
		}

		if (portions.size() > 1) {
			throw new CookyErrorMsg(
					"expected only one element with 'portionen'. but there are " + portions.size());
		}

		Recipe recipe = new Recipe();

		// TODO check if there already is a receipe with this name
		StringBuilder builder = new StringBuilder();
		
		textElement = textElement.getElementsByAttributeValue("class", "ds-box").first();
		textElement.childNodes().forEach(child -> {
			if(child.nodeName().equals("br"))
			{
				//linebreaks are, obviously, created with a HTML br-tag
				builder.append("\n");
			}else {
				builder.append(child);
			}
		});

		recipe.setName(recipeTitles.first().text());
		recipe.setSource(url);
		recipe.setDescription(builder.toString());
		recipe.setPersons(Integer.parseInt(portions.first().attr("value")));

		RecipePart part = new RecipePart();
		recipe.getRecipeParts().add(part);

		return recipe;
	}

}
