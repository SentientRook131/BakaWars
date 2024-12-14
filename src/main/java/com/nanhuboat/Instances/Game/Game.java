package com.nanhuboat.Instances.Game;

import com.nanhuboat.Instances.ResourcesSpawn.ResourcesSpawnPoint;
import com.nanhuboat.Instances.Team.RecordedPlayer;
import com.nanhuboat.Instances.Team.Team;
import com.nanhuboat.Templates.Map.TemplateMap;
import com.nanhuboat.bakaWars;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Game implements Listener {
    public TemplateMap templateMap;
    public String fileMapName;
    public World world;
    public List<Team> teams;
    public List<ResourcesSpawnPoint> resourcesSpawnPoints;
    public Map<Player, RecordedPlayer> players;
    public int leastPlayerAmount;
    public int maxPlayerAmount;
    public Location prepareLoc;
    public Location centerLoc;
    public boolean isPreparing;
    public boolean isStarting;
    public int residualTime;
    public Game(TemplateMap templateMap, List<Team> teams, int leastPlayerAmount, Location prepareLoc, Location centerLoc, List<ResourcesSpawnPoint> points) {
        this.templateMap = templateMap;
        this.teams = teams;
        this.players = new HashMap<>();
        this.resourcesSpawnPoints = points;
        this.prepareLoc = prepareLoc;
        this.centerLoc = centerLoc;
        this.leastPlayerAmount = leastPlayerAmount;
        this.isPreparing = false;
        this.isStarting = false;
        this.fileMapName = bakaWars.generateRandomString(4, 16, true, true, true, false);
        WorldCloner.cloneWorld(templateMap.name, fileMapName);
        this.world = Bukkit.createWorld(new WorldCreator(fileMapName));
        for (Team team : teams) maxPlayerAmount += team.maxPlayerAmount;
        prepare();
    }
    public void distributePlayersToTeams() {
        List<Player> unassignedPlayers = players.keySet().stream()
                .filter(player -> teams.stream().noneMatch(team -> team.players.contains(player)))
                .collect(Collectors.toList());
        Collections.shuffle(unassignedPlayers);
        int teamCount = teams.size();
        for (int i = 0; i < unassignedPlayers.size(); i++) {
            Player player = unassignedPlayers.get(i);
            Team team = teams.get(i % teamCount);
            team.players.add(player);
        }
    }
    public void prepare() {
        Bukkit.getServer().getPluginManager().registerEvents(this, JavaPlugin.getPlugin(bakaWars.class));
        this.isPreparing = true;
        residualTime = 60;
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(bakaWars.class), t -> {
            residualTime -= 1;
            if (players.size() < leastPlayerAmount) {
                if (residualTime < 59) {
                    for (Player player : players.keySet()) {
                        player.sendTitle("重置了倒计时", "起床战争", 20, 60 , 60);
                    }
                }
                residualTime = 60;
            } else {
                if (residualTime == 30) {
                    for (Player player : players.keySet()) {
                        player.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏将在30秒后开始。");
                    }
                } else if (residualTime == 15) {
                    for (Player player : players.keySet()) {
                        player.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏将在15秒后开始。");
                    }
                } else if (residualTime <= 5) {
                    for (Player player : players.keySet()) {
                        player.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + "游戏将在" + residualTime + "秒后开始。");
                    }
                }
            }
            if (residualTime == 0) {
                start();
                t.cancel();
            }
        }, 0, 20);
    }
    public void start() {
        this.isPreparing = false;
        this.isStarting = true;
        distributePlayersToTeams();
        for (Team team : teams) team.start();
        for (ResourcesSpawnPoint point : resourcesSpawnPoints) point.init();
        for (Player player : players.keySet()) {
            OpenInterfaceToChooseTeamListener listener = new OpenInterfaceToChooseTeamListener(player.getInventory(), this);
            listener.inventory.setItem(0, new ItemStack(players.get(player).team.symbolBed));
        }
    }
    public void stop() {
        this.isPreparing = false;
        this.isStarting = false;
        for (Player player : players.keySet()) {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(bakaWars.lobby);
            Bukkit.unloadWorld(this.fileMapName, false);
        }
    }
    public void playerDie(Player player) {
        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(centerLoc);
        AtomicInteger respawnTime = new AtomicInteger(5);
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(bakaWars.class), () -> {
            if (respawnTime.get() <= 0) {
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(players.get(player).team.respawnLocation);
            }
            player.sendTitle("你还剩" + respawnTime.get() + "s复活！", "", 10, 20, 10);
            respawnTime.getAndDecrement();
        }, 0, 20);
    }
    public void playerJoinTheTeam(Player player, Team team) {
        players.get(player).team.removePlayer(player);
        players.get(player).team = team;
        team.addPlayer(player);
        PlayerInventory inventory = player.getInventory();
        ItemStack stack1 = new ItemStack(Material.LEATHER_LEGGINGS), stack2 = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta1 = (LeatherArmorMeta) stack1.getItemMeta(), meta2 = (LeatherArmorMeta) stack2.getItemMeta();
        meta1.setColor(team.color);
        meta2.setColor(team.color);
        stack1.setItemMeta(meta1);
        stack2.setItemMeta(meta2);
        inventory.setLeggings(stack1);
        inventory.setBoots(stack2);
    }
    public void leave(Player player) {
        players.get(player).team.removePlayer(player);
        player.getInventory().clear();
        players.remove(player);
    }
    @EventHandler
    public void onPlayerEnterWorld(PlayerChangedWorldEvent e) {
        if (e.getPlayer().getWorld().equals(prepareLoc.getWorld())) {
            e.getPlayer().teleport(prepareLoc);
        }
    }
    @EventHandler
    public void onPlayerDied(EntityDamageByEntityEvent e) {
        Entity attacked = e.getEntity();
        Entity attacker = e.getDamager();
        if (attacked instanceof Player p1 && attacker instanceof Player p2) {
            if (p1.getHealth() <= 0) {
                playerDie(p1);
                for (Player player : players.keySet()) player.sendMessage(ChatColor.AQUA + "[起床战争]" + ChatColor.RESET + p1.getName() + "被" + p2.getName() + "击败了！");
            }
        }
    }
}
