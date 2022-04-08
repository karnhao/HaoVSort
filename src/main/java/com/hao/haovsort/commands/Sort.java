
package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.SortPlayer;
import com.hao.haovsort.utils.Configuration;
import com.hao.haovsort.utils.PlayerSelector;
import com.hao.haovsort.utils.Util;

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
            List<Player> targets = PlayerSelector.getPlayers(cs, args[0]);
            String type = args[1];
            Long delay = Long.parseLong(args[2]);
            Integer length = Util.getLength(args[3]);
            if (targets == null || targets.isEmpty()) {
                throw new NullPointerException("Player not found.");
            }
            targets.forEach((p) -> Sort.invoke(p, type, delay, length, Arrays.copyOfRange(args, 4, args.length)));
        } catch (NullPointerException | InvalidArgsException e) {
            Util.alert(cs, e.toString());
        } catch (IndexOutOfBoundsException e) {
            Util.alert(cs, "Syntax error : /sort <player> <type> <delay> <length> [<args>]...");
        } catch (Exception e) {
            Util.alert(cs, "Something wrong : " + e.toString());
            e.printStackTrace();
        }
        return true;
    }

    private static void invoke(Player target, String type, Long delay, Integer length, String[] args)
            throws NullPointerException, InvalidArgsException {
        AlgorithmsManager.cleanPlayer(target);
        SortPlayer player = new SortPlayer();
        List<Player> targets = Arrays.asList(target);
        if (delay < 1)
            throw new InvalidArgsException("Delay cannot lower than 1");
        if (length > 1024
                || (length > Configuration.getMaxActionBarArrayLength() && Configuration.getLimitLength()))
            throw new InvalidArgsException("Data too large");
        player.setPlayers(targets);
        player.setOwner(target);
        player.setCommands(new AlgorithmCommandCollector(Util.createArray(length),
                new AlgorithmCommand("random", 10l, targets, "1"),
                new AlgorithmCommand(type, delay, targets, args),
                new AlgorithmCommand("finish", 10l, targets)));
        player.start();
        AlgorithmsManager.addPlayer(target, player);
    }
}
