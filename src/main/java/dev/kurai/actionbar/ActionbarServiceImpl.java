package dev.kurai.actionbar;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.task.ActionbarUpdaterTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * Default {@link ActionbarService} implementation that stores per-holder {@link Actionbar}
 * instances in a concurrent map and schedules an {@link ActionbarUpdaterTask} asynchronously with a
 * one-tick period.
 */
@ApiStatus.Internal
final class ActionbarServiceImpl implements ActionbarService {

  /**
   * Thanks to <a href="https://github.com/SuperCrafting/">Super_Crafting</a> for the suggestion.
   */
  @Getter private final JoinConfiguration joinConfiguration;

  private final ConcurrentMap<UUID, Actionbar> actionbars;

  /**
   * Constructs a service and immediately schedules its asynchronous recurring update task.
   *
   * @param plugin the plugin that owns the scheduled task
   * @param audienceProvider a function that maps each {@link Player} to its target {@link Audience}
   * @param joinConfiguration the join configuration used to compose action-bar entries
   */
  ActionbarServiceImpl(
      final Plugin plugin,
      final Function<Player, Audience> audienceProvider,
      final JoinConfiguration joinConfiguration) {
    requireNonNull(plugin, "Plugin cannot be null");
    requireNonNull(audienceProvider, "Audience provider cannot be null");

    this.joinConfiguration = requireNonNull(joinConfiguration, "Join configuration cannot be null");
    this.actionbars = Maps.newConcurrentMap();

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            plugin, new ActionbarUpdaterTask(this, audienceProvider), 0L, 1L);
  }

  /** {@inheritDoc} */
  @Override
  public Actionbar actionbar(final UUID holderId) {
    return this.actionbars.computeIfAbsent(
        requireNonNull(holderId, "Holder ID cannot be null"), _ -> Actionbar.actionbar());
  }
}
