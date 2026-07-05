package dev.kurai.actionbar;

import java.util.UUID;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;

/**
 * Manages per-player {@link Actionbar} instances and drives the recurring update cycle that renders
 * and sends action-bar messages to online players.
 *
 * <p>Get an instance via {@link #actionbarService(Plugin, Function)}. The returned service
 * immediately schedules a repeating task with a one-tick period that refreshes action bars for
 * online players with active entries.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ActionbarService actionbarService =
 *     ActionbarService.actionbarService(
 *         plugin,
 *         player -> BukkitAudiences.of(plugin).player(player));
 *
 * actionbarService
 *     .actionbar(player.getUniqueId())
 *     .registerActionbarEntry(actionbarEntry);
 * }</pre>
 */
public sealed interface ActionbarService permits ActionbarServiceImpl {

  /**
   * Creates a new {@code ActionbarService} and starts its recurring update task.
   *
   * @param plugin the plugin that owns the scheduled task
   * @param audienceProvider a function that maps a {@link Player} to the {@link Audience} that
   *     receives action-bar messages
   * @return a new running {@code ActionbarService}; never {@code null}
   */
  @Contract("_, _ -> new")
  static ActionbarService actionbarService(
      final Plugin plugin, final Function<Player, Audience> audienceProvider) {
    return actionbarService(plugin, audienceProvider, JoinConfiguration.commas(true));
  }

  /**
   * Creates a new {@code ActionbarService} with the specified {@link JoinConfiguration} and starts
   * its recurring update task.
   *
   * @param plugin the plugin that owns the scheduled task
   * @param audienceProvider a function that maps a {@link Player} to the {@link Audience} that
   *     receives action-bar messages
   * @param joinConfiguration the join configuration used to compose action-bar entries
   * @return a new running {@code ActionbarService}; never {@code null}
   */
  @Contract("_, _, _ -> new")
  static ActionbarService actionbarService(
      final Plugin plugin,
      final Function<Player, Audience> audienceProvider,
      final JoinConfiguration joinConfiguration) {
    return new ActionbarServiceImpl(plugin, audienceProvider, joinConfiguration);
  }

  /**
   * Returns the {@link JoinConfiguration} used to compose action-bar entries.
   *
   * @return the active join configuration; never {@code null}
   */
  JoinConfiguration joinConfiguration();

  /**
   * Returns the {@link Actionbar} associated with the specified holder ID, creating one lazily if
   * none exists.
   *
   * @param holderId the holder's unique ID
   * @return the associated action bar; never {@code null}
   */
  Actionbar actionbar(final UUID holderId);
}
