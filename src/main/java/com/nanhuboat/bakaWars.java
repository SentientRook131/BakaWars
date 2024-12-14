package com.nanhuboat;

import com.nanhuboat.Instances.Game.Game;
import com.nanhuboat.Instances.Game.WorldCloner;
import com.nanhuboat.Templates.Map.MapBattleMode;
import com.nanhuboat.Templates.Map.MapGameplayMode;
import com.nanhuboat.Templates.Map.TemplateMap;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public final class bakaWars extends JavaPlugin {
    public static List<Game> goingGames = new ArrayList<>();
    public static List<TemplateMap> templateMaps = new ArrayList<>();
    public static Location lobby;

    @Override
    public void onEnable() {
        File file = new File("templates/map");
        file.mkdirs();
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isFile()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
                    templateMaps.add((TemplateMap) ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (TemplateMap map : templateMaps) {
            try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream("templates/map/" + map.name + ".map"))) {
                ois.writeObject(map);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("bw")) {
            if (args.length == 0) {
                if (sender.isOp()) sender.sendMessage("§c/bw <create|delete|start|stop|join|leave|setLobby|list|help>");
                else sender.sendMessage("§c/bw <join|leave|list|help>");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (sender.isOp()) {
                        sender.sendMessage("§c/bw create <name> <template-map> <battle-mode> <gameplay-mode>");
                    } else {
                        sender.sendMessage("§c你没有权限！");
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (sender.isOp()) {
                        sender.sendMessage("§c/bw delete <name>");
                    } else {
                        sender.sendMessage("§c你没有权限！");
                    }
                } else if (args[0].equalsIgnoreCase("start")) {
                    if (sender.isOp()) {
                        sender.sendMessage("§c/bw start <name>");
                    } else {
                        sender.sendMessage("§c你没有权限！");
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (sender.isOp()) {
                        sender.sendMessage("§c/bw stop <name>");
                    } else {
                        sender.sendMessage("§c你没有权限！");
                    }
                } else if (args[0].equalsIgnoreCase("join")) {
                    sender.sendMessage("§c/bw join <name>");
                } else if (args[0].equalsIgnoreCase("leave")) {
                    for (Game game : goingGames) {
                        if (sender instanceof Player && game.players.containsKey((Player) sender)) {
                            game.leave((Player) sender);
                        } else {
                            sender.sendMessage("§c你没有加入任何游戏！");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("setLobby")) {
                    if (sender instanceof Player player) {
                        if (player.isOp()) {
                            lobby = player.getLocation();
                            sender.sendMessage("§c已设置游戏大厅！");
                        } else {
                            sender.sendMessage("§c你没有权限！");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage("§c/bw list <all|going|waiting>");
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (sender.isOp()) {
                        sender.sendMessage("§c/bw <create|delete|start|stop|join|leave|list|help>");
                    } else {
                        sender.sendMessage("§c/bw <join|leave|list|help>");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join")) {
                    for (Game game : goingGames) {
                        if (game.templateMap.name.equalsIgnoreCase(args[1])) {
                            if (sender instanceof Player) {
                                if (game.isPreparing) {
                                    ((Player) sender).teleport(game.prepareLoc);
                                }
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (args[1].equalsIgnoreCase("all")) {
                        for (Game game : goingGames) {
                            sender.sendMessage("§c游戏名称：" + game.templateMap.name + "，状态：" + ((game.isPreparing) ? "准备中" : "进行中"));
                        }
                    } else if (args[1].equalsIgnoreCase("going")) {
                        for (Game game : goingGames) {
                            if (!game.isPreparing) {
                                sender.sendMessage("§c游戏名称：" + game.templateMap.name + "，状态：" + "进行中");
                            }
                        }
                    } else if (args[1].equalsIgnoreCase("waiting")) {
                        for (Game game : goingGames) {
                            if (game.isPreparing) {
                                sender.sendMessage("§c游戏名称：" + game.templateMap.name + "，状态：" + "准备中");
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (sender.isOp()) {
                        goingGames.removeIf(game -> game.templateMap.name.equalsIgnoreCase(args[1]));
                    }
                } else if (args[0].equalsIgnoreCase("start")) {
                    if (sender.isOp()) {
                        for (Game game : goingGames) {
                            if (game.templateMap.name.equalsIgnoreCase(args[1])) {
                                for (Player player : game.players.keySet()) {
                                    player.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏被强制开始！");
                                }
                                game.start();
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (sender.isOp()) {
                        for (Game game : goingGames) {
                            if (game.templateMap.name.equalsIgnoreCase(args[1])) {
                                for (Player player : game.players.keySet()) {
                                    player.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏被强制结束！");
                                }
                            }
                            game.stop();
                        }
                    }
                }
            } else if (args.length == 8) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (sender.isOp()) {
                        for (Game game : goingGames) {
                            if (game.templateMap.name.equalsIgnoreCase(args[1])) {
                                sender.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏已存在！");
                                return true;
                            }
                        }
                        TemplateMap map = getTemplateMap(args);
                        templateMaps.add(map);
                        WorldCreator worldCreator = getWorldCreator(map);
                        World emptyWorld = Bukkit.createWorld(worldCreator);
                        ((Player) sender).teleport(new Location(emptyWorld, 0, 64, 0));
                        sender.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏创建成功！");
                    }
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("mw")) {
            //多世界功能
            if (args.length == 0) {
                if (sender.isOp()) {
                    sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw <create|save|tp|list|load|unload|clone>");
                }
            } else if (args.length == 1) {
                if (sender.isOp()) {
                    if (args[0].equalsIgnoreCase("list")) {
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "当前存在的世界有：" + Bukkit.getWorlds().stream()
                                .map(World::getName)
                                .collect(Collectors.joining(", ", "[", "]")));
                    } else if (args[0].equalsIgnoreCase("load")) {
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw load <world-name>");
                    } else if (args[0].equalsIgnoreCase("unload")) {
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw unload <world-name>");
                    } else if (args[0].equalsIgnoreCase("tp")) {
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw tp <world-name>");
                    } else if (args[0].equalsIgnoreCase("create")) {
                         sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw create <world-name>");
                    } else if (args[0].equalsIgnoreCase("save")) {
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw save <world-name>");
                    }  else if (args[0].equalsIgnoreCase("clone")) {
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "/mw clone <world-name> <new-world-name>");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "你没有权限！");
                }
            } else if (args.length == 2) {
                if (sender.isOp()) {
                    if (args[0].equalsIgnoreCase("create")) {
                        WorldCreator worldCreator = getWorldCreator(args[1]);
                        World emptyWorld = Bukkit.createWorld(worldCreator);
                        sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "世界创建成功！");
                    } else if (args[0].equalsIgnoreCase("save")) {
                        if (Bukkit.getWorld(args[1]) != null) {
                            Bukkit.getWorld(args[1]).save();
                            sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "世界保存成功！");
                        } else {
                            sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "世界不存在！");
                        }
                    } else if (args[0].equalsIgnoreCase("tp")) {
                        ((Player) sender).teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
                    } else if (args[0].equalsIgnoreCase("load")) {
                        if (Bukkit.getWorld(args[1]) == null) {
                            Bukkit.createWorld(getWorldCreator(args[1]));
                        }
                    } else if (args[0].equalsIgnoreCase("unload")) {
                        if (Bukkit.getWorld(args[1]) != null) {
                            Bukkit.unloadWorld(args[1], true);
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "你没有权限！");
                }
            } else if (args.length == 3) {
                if (sender.isOp()) {
                    if (args[0].equalsIgnoreCase("tp")) {
                        Player player = Bukkit.getPlayer(args[1]);
                        World world = Bukkit.getWorld(args[2]);
                        if (player != null && world != null) {
                            player.teleport(world.getSpawnLocation());
                        } else {
                            sender.sendMessage(ChatColor.AQUA + "[多世界]" + ChatColor.RESET + "玩家或世界不存在！");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "你没有权限！");
                }
            } else if (args.length == 4) {
                if (sender.isOp()) {
                    if (args[0].equalsIgnoreCase("clone")) {
                        if (Bukkit.getWorld(args[1]) != null) {
                            WorldCloner.cloneWorld(args[1], args[2]);
                        }
                    }
                }
            }
        }
        return true;
    }

    private static WorldCreator getWorldCreator(TemplateMap map) {
        WorldCreator worldCreator = new WorldCreator(map.name);
        worldCreator.generator(new ChunkGenerator() {
            @Override
            public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }

            @Override
            public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }

            @Override
            public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }

            @Override
            public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }
        });
        return worldCreator;
    }

    private static WorldCreator getWorldCreator(String args) {
        WorldCreator worldCreator = new WorldCreator(args);
        worldCreator.generator(new ChunkGenerator() {
            @Override
            public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }

            @Override
            public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }

            @Override
            public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }

            @Override
            public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
            }
        });
        return worldCreator;
    }

    private static TemplateMap getTemplateMap(String[] args) {
        String name = args[1];
        String description = String.join("\n", args[2].split("\\|"));
        String[] author = args[3].split("\\|");
        String version = args[4];
        String date = args[5];
        MapBattleMode mapBattleMode = MapBattleMode.valueOf(args[6]);
        MapGameplayMode mapGameplayMode = MapGameplayMode.valueOf(args[7]);
        return new TemplateMap(name, description, author, version, date, mapBattleMode, mapGameplayMode, new ArrayList<>());
    }

    public static String generateRandomString(int minLength, int maxLength, boolean includeDigits, boolean includeLowercase, boolean includeUppercase, boolean includeSpecialChars) {
        StringBuilder characterPool = new StringBuilder();
        Random random = new Random();
        if (includeDigits) characterPool.append("0123456789");
        if (includeLowercase) characterPool.append("abcdefghijklmnopqrstuvwxyz");
        if (includeUppercase) characterPool.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        if (includeSpecialChars) characterPool.append("!@#$%^&*()-_=+[]{}|;:,.<>?/");
        if (characterPool.isEmpty()) {
            throw new IllegalArgumentException("At least one character set must be included.");
        }
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterPool.length());
            result.append(characterPool.charAt(index));
        }
        return result.toString();
    }
}
