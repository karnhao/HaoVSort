
package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.SortPlayer;
import com.hao.haovsort.utils.Configuration;
import com.hao.haovsort.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /sort command
 * <p>
 * syntax :
 * {@code /sort <player> <type> <delay> <length> ...(แล้วแต่ละ Algorithms จะกำหนด)[algorithm's args]}
 * <p>
 * example : {@code /sort karnhao selection 10 100 args...}
 */
public class Sort implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        try {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                throw new NullPointerException("Player not found.");
            }
            AlgorithmsManager.cleanPlayer(target);
            SortPlayer player = new SortPlayer();
            List<Player> targets = Arrays.asList(target);
            Long delay = Long.parseLong(args[2]);
            Integer length = Util.getLength(args[3]);
            if (delay < 1)
                throw new Exception("Delay cannot lower than 1");
            if (length > 1024
                    || (length > Configuration.getMaxActionBarArrayLength() && Configuration.getLimitLength()))
                throw new Exception("Data too large");
            player.setPlayers(targets);
            player.setOwner(target);
            player.setCommands(new AlgorithmCommandCollector(Util.createArray(length),
                    new AlgorithmCommand("random", 10l, targets, "1"),
                    new AlgorithmCommand(args[1], delay, targets,
                            Arrays.copyOfRange(args, 4, args.length)),
                    new AlgorithmCommand("finish", 10l, targets)));
            player.start();
            AlgorithmsManager.addPlayer(target, player);
        } catch (Exception e) {
            Util.alert(cs, e.toString());
        }
        return true;
    }
}
