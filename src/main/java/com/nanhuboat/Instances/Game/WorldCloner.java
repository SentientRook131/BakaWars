package com.nanhuboat.Instances.Game;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;

import java.io.File;

public class WorldCloner {

    public static void cloneWorld(String originalWorldName, String newWorldName) {
        World originalWorld = Bukkit.getWorld(originalWorldName);
        if (originalWorld == null) {
            throw new IllegalArgumentException("Original world not found!");
        }
        File originalWorldFolder = originalWorld.getWorldFolder();
        File newWorldFolder = new File(originalWorldFolder.getParent(), newWorldName);
        if (newWorldFolder.exists()) {
            deleteWorldFolder(newWorldFolder);
        }
        try {
            copyWorldFolder(originalWorldFolder, newWorldFolder);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        WorldCreator worldCreator = new WorldCreator(newWorldName);
        World newWorld = Bukkit.createWorld(worldCreator);
        copyWorldEntitiesAndBlocks(originalWorld, newWorld);
    }
    private static void copyWorldFolder(File source, File destination) throws Exception {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    copyWorldFolder(new File(source, file), new File(destination, file));
                }
            }
        } else {
            java.nio.file.Files.copy(source.toPath(), destination.toPath());
        }
    }
    private static void deleteWorldFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteWorldFolder(file);
                }
            }
            folder.delete();
        }
    }
    private static void copyWorldEntitiesAndBlocks(World originalWorld, World newWorld) {
        for (int x = 0; x < originalWorld.getMaxHeight(); x++) {
            for (int y = 0; y < originalWorld.getMaxHeight(); y++) {
                for (int z = 0; z < originalWorld.getMaxHeight(); z++) {
                    originalWorld.getBlockAt(x, y, z).setType(newWorld.getBlockAt(x, y, z).getType());
                }
            }
        }
        for (Entity entity : originalWorld.getEntities()) {
            newWorld.spawnEntity(entity.getLocation(), entity.getType());
        }
    }
}
