package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.HaoVSort;
import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.utils.SortPlayer;
import com.hao.haovsort.utils.PlayerSelector;
import com.hao.haovsort.utils.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SortCustom implements CommandExecutor {

    private String breaker;

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        // /sortcustom <player> <delay> <length> random 1 ; radix 4 ; finish #00AA00
        try {
            List<Player> targets = PlayerSelector.getInstance().getPlayers(cs, args[0]);
            Long delay = Long.parseLong(args[1]);
            Integer length = Util.getLength(args[2]);
            if (targets == null || targets.isEmpty()) {
                throw new NullPointerException("Player not found.");
            }
            targets.forEach((p) -> invoke(p, delay, length, Arrays.copyOfRange(args, 3, args.length)));
        } catch (NullPointerException | InvalidArgsException | NumberFormatException e) {
            Util.alert(cs, e.toString());
        } catch (IndexOutOfBoundsException e) {
            Util.alert(cs, "Syntax error : /sortcustom <player> <delay> <length> [ <type> [<args>]... ]...");
        } catch (Exception e) {
            Util.alert(cs, "Something wrong : " + e.toString());
            e.printStackTrace();
        }
        return true;
    }

    private AlgorithmCommand[] getAlgorithmCommands(String[] args, List<Player> players, Long delay) {
        String str = Util.argsToString(args);
        return Arrays.asList(str.split(breaker)).stream().map((t) -> t.trim())
                .map((t) -> new AlgorithmCommand(t.split(" ")[0], delay, players,
                        Arrays.copyOfRange(t.split(" "), 1, t.split(" ").length)))
                .collect(Collectors.toList())
                .toArray(new AlgorithmCommand[str.split(breaker).length]);
    }

    private void invoke(Player target, Long delay, Integer length, String[] args)
            throws NullPointerException, InvalidArgsException {
        HaoVSort.getInstance().getAlgorithmManager().cleanPlayer(target);
        SortPlayer player = new SortPlayer();
        List<Player> targets = Arrays.asList(target);
        if (delay < 1)
            throw new InvalidArgsException("Delay cannot lower than 1");
        if (length < 1)
            throw new InvalidArgsException("length must be greater than 0");
        if (length > 760
                || (HaoVSort.getInstance().getConfiguration().getLimitLength()
                && length > HaoVSort.getInstance().getConfiguration().getMaxActionBarArrayLength()
                ))
            throw new InvalidArgsException("Data too big");
        player.setPlayers(targets);
        player.setOwner(target);
        player.setCommands(new AlgorithmCommandCollector(Util.createArray(length),
                getAlgorithmCommands(args, targets, delay)));
        player.start();
        HaoVSort.getInstance().getAlgorithmManager().addPlayer(target, player);
    }

    public SortCustom(String breaker) {
        this.breaker = breaker;
    }
}
