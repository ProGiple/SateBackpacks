package org.satellite.progiple.satebackpacks.other.configs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ShapedRecipe;
import org.novasparkle.lunaspring.Configuration.Configuration;
import org.satellite.progiple.satebackpacks.SateBackpacks;
import org.satellite.progiple.satebackpacks.backpacks.BackPack;

import java.io.File;
import java.util.*;

public class BackPackConfig {
    private final Configuration config;
    private final NamespacedKey key;
    private final String id;
    public BackPackConfig(String id) {
        this.config = new Configuration(new File(SateBackpacks.getPlugin().getDataFolder(), String.format("backpacks/%s.yml", id)));
        this.key = new NamespacedKey(SateBackpacks.getPlugin(), String.format("craft-%s", id));
        this.id = id;
        SateBackpacks.getBPCfgs().put(id, this);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getSection(path);
    }

    public void loadRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(this.key, new BackPack(this.id).getItem());
        ConfigurationSection recipeSection = this.getSection("recipe");

        List<String> shapes = new ArrayList<>(recipeSection.getStringList("pattern"));
        if (shapes.isEmpty()) {
            shapes.add(" N ");
            recipe.setIngredient('N', Material.STONE);
        }

        if (shapes.size() <= 3) {
            switch (shapes.size()) {
                case 1 -> recipe.shape(shapes.get(0));
                case 2 -> recipe.shape(shapes.get(0), shapes.get(1));
                case 3 -> recipe.shape(shapes.get(0), shapes.get(1), shapes.get(2));
            }
        }

        Set<Character> usedLetters = new HashSet<>();
        for (String str : shapes) {
            for (char letter : str.toCharArray()) {
                if (usedLetters.size() >= 9) break;

                if (usedLetters.contains(letter)) continue;
                Material material = Material.AIR;
                String stringMaterial = recipeSection.getString(String.format("materials.%s", letter));
                if (stringMaterial != null && !stringMaterial.isEmpty()) material = Material.getMaterial(stringMaterial);

                if (material != null && material != Material.AIR) recipe.setIngredient(letter, material);
                usedLetters.add(letter);
            }
        }
        Bukkit.getServer().addRecipe(recipe);
    }

    public void unloadRecipe() {
        Bukkit.getServer().removeRecipe(this.key);
    }
}
