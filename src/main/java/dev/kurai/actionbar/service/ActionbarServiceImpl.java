package dev.kurai.actionbar.service;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.Actionbar;
import dev.kurai.actionbar.task.ActionbarUpdaterTask;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

final class ActionbarServiceImpl implements ActionbarService {

  private final Map<UUID, Actionbar> actionbars = Maps.newHashMap();

  public ActionbarServiceImpl(
      final Plugin plugin, final Function<Player, Audience> audienceProvider) {
    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            plugin, new ActionbarUpdaterTask(this, audienceProvider), 0L, 1L);
  }

  @Override
  public Actionbar actionbar(final UUID holder) {
    return this.actionbars.computeIfAbsent(holder, _ -> Actionbar.create());
  }
}
