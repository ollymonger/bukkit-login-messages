package com.ollymonger.loginmessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;
import java.util.Random;

public final class LoginMessage extends JavaPlugin implements Listener {


    public String prefix = ChatColor.translateAlternateColorCodes('&', "&6[&7LMSG&6]&r ");
    public void createConfig() {
        this.getConfig().createSection("loginmessages");
        List<String> loginmessage = this.getConfig().getStringList("loginmessages");
            loginmessage.add("Welcome to the server, %player%.");
            this.getConfig().set("loginmessages", loginmessage);

        this.getConfig().createSection("prefix");
            List<String> prefix = this.getConfig().getStringList("prefix");
            prefix.add("SERVER");
            this.getConfig().set("prefix", prefix);

        getLogger().info("Config successfully set up!");
        saveConfig();
    }

    public void getData() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs(); //makes directory if none exists
            }
            File file = new File(getDataFolder(), "config.yml"); //sets file name and file folder
            if (!file.exists()) { //makes file if none exists
                getLogger().info("CONFIG NOT FOUND, CREATING A NEW CONFIG!");
                createConfig();
            } else {
                getLogger().info("CONFIG FOUND! Loading!");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                    this.getConfig().load(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin: LoginMessage is now enabled");
        getLogger().info("Plugin Version: " + getDescription().getVersion());
        getLogger().info("Last Updated: (25/07/19)");
        getLogger().info("Most Recent Update: Messages and prefixes can now use colour codes, prefixes can now have spaces (replaces _'s)");
        getData();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) { //when a player joins run this
        String messageRandom = this.getConfig().getStringList("loginmessages").get(new Random().nextInt(this.getConfig().getStringList("loginmessages").size())); //random selector of config.yml
        String joinPrefix = this.getConfig().get("prefix").toString();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String messageExport = messageRandom.replaceAll("%player%", player.getName()); //An export of the random that looks for %player% and replaces with the player's name.
            String prefixExport = joinPrefix.replace("_", " ");
            String messageColor = ChatColor.translateAlternateColorCodes('&', messageExport); //Takes export, so if any chat colors they get translated
            String prefixColor = ChatColor.translateAlternateColorCodes('&', prefixExport); //Takes export, so if any chat colors they get translated
            event.setJoinMessage(prefixColor + " " + messageColor); // sets the join message to the exported random.
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (cmd.getName().equalsIgnoreCase("loginmessage")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "This plugin allows you to set custom (randomised) login messages.");
                    sender.sendMessage(prefix + "Instead of spaces, use _ to create spaces between words, corrected on join.");
                    sender.sendMessage(prefix + "/loginmessage <message>, /loginmessage prefix <prefix>, /lm <message>, /lm prefix <prefix>");
                    sender.sendMessage(prefix + "... /lm <message>, /lm prefix <prefix>");
                }

                if (args.length == 2 && args[0].equalsIgnoreCase("prefix")){
                    sender.sendMessage(prefix + "Returning the LoginMessage: " + args[1]);
                    List<String> joinPrefix = this.getConfig().getStringList("prefix");
                    joinPrefix.clear();
                    joinPrefix.add(args[1]);
                    this.getConfig().set("prefix", joinPrefix);
                    saveConfig();
                }
                if (args.length == 1 && args[0].equalsIgnoreCase(args[0])){
                    sender.sendMessage(prefix + "Returning the LoginMessage: " + args[0]);
                    List<String> list = this.getConfig().getStringList("loginmessages");
                    list.add(args[0].replace("_", " "));
                    this.getConfig().set("loginmessages", list);
                    saveConfig();
                    getLogger().info("Saved.");
                }
            }
            if (cmd.getName().equalsIgnoreCase("lm")) {
                if (args.length == 0) {
                    sender.sendMessage(prefix + "This plugin allows you to set custom (randomised) login messages.");
                    sender.sendMessage(prefix + "Instead of spaces, use _ to create spaces between words, corrected on join.");
                    sender.sendMessage(prefix + "/loginmessage <message>, /loginmessage prefix <prefix>, /lm <message>, /lm prefix <prefix>");
                    sender.sendMessage(prefix + "... /lm <message>, /lm prefix <prefix>");
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("prefix")){
                    sender.sendMessage(prefix + "Returning the LoginMessage: " + args[1]);
                    List<String> joinPrefix = this.getConfig().getStringList("prefix");
                    joinPrefix.clear();
                    joinPrefix.add(args[1]);
                    this.getConfig().set("prefix", joinPrefix);
                    saveConfig();
                }
                if (args.length == 1 && args[0].equalsIgnoreCase(args[0])){
                    sender.sendMessage(prefix + "Returning the LoginMessage: " + args[0]);
                    List<String> list = this.getConfig().getStringList("loginmessages");
                    list.add(args[0].replace("_", " "));
                    this.getConfig().set("loginmessages", list);
                    saveConfig();
                    getLogger().info("Saved.");
                }
            }
        }
        return true;
    }

}